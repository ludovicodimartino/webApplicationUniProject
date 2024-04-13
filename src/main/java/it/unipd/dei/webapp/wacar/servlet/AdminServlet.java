package it.unipd.dei.webapp.wacar.servlet;

import it.unipd.dei.webapp.wacar.dao.GetCarTypesDAO;
import it.unipd.dei.webapp.wacar.dao.GetCircuitTypesDAO;
import it.unipd.dei.webapp.wacar.dao.InsertCarDAO;
import it.unipd.dei.webapp.wacar.dao.InsertCircuitDAO;
import it.unipd.dei.webapp.wacar.resource.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

import jakarta.activation.MimeTypeParseException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Part;
import org.apache.logging.log4j.message.StringFormattedMessage;

/**
 * This servlet handles the operations of the admin.
 *
 * @author Ludovico Di Martino (ludovico.dimartino@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
@WebServlet(name = "AdminServlet", value = "/admin/*")
@MultipartConfig
public class AdminServlet extends AbstractDatabaseServlet {
    /**
     * Handles the HTTP GET request of the admin, that are:
     * <pre>
     *  - insertCar page
     *  - insertCircuit page
     *  </pre>
     *
     * @param req the {@code HttpServletRequest} incoming request from the client
     * @param res the {@code HttpServletResponse} response object from the server
     * @throws IOException      if any error occurs in the client/server communication.
     * @throws ServletException if any problem occurs while executing the servlet.
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String op = req.getRequestURI();
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(op);
        LogContext.setUser(((User) (req.getSession()).getAttribute("account")).getEmail());

        try {
            if (op.lastIndexOf("admin") == op.length() - 5) { // Math url wacar/admin
                op = "";
            } else { // Math url wacar/**/admin**
                op = op.substring(op.lastIndexOf("admin") + 6);
            }

            switch (op) {
                case "insertCar/":
                    LogContext.setAction(Actions.GET_INSERT_CAR_PAGE);
                    insertCarPage(req, res);
                    break;
                case "insertCircuit/":
                    LogContext.setAction(Actions.GET_INSERT_CIRCUIT_PAGE);
                    insertCircuitPage(req, res);
                    break;
                case "": // URL /wacar/admin
                    //redirect to admin page
                    break;
                default:
                    Message m = new Message("An error occurred default", "E200", "Operation Unknown");
                    LOGGER.info("stacktrace {}:", m.getMessage());
            }
        } catch (Exception e) {
            LOGGER.error("Unable to serve request.", e);
            throw e;
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    /**
     * Access the database to load the insert car page.
     * In particular, only the car types present in the database will be shown.
     *
     * @param req the {@code HttpServletRequest} incoming request
     * @param res the {@code HttpServletResponse} response object
     * @throws IOException      if any error happens during the response writing operation
     * @throws ServletException if any problem occurs while executing the servlet.
     */
    public void insertCarPage(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            List<Type> carTypeList = new GetCarTypesDAO(getConnection()).access().getOutputParam();
            req.setAttribute("carList", carTypeList);
            req.getRequestDispatcher("/jsp/insert-car.jsp").forward(req, res);
        } catch (SQLException e) {
            LOGGER.error("Cannot read car types: unexpected error while accessing the database.", e);
        }
    }

    /**
     * Access the database to load the insert circuit page.
     * In particular, only the circuit types present in the database will be shown.
     *
     * @param req the {@code HttpServletRequest} incoming request
     * @param res the {@code HttpServletResponse} response object
     * @throws IOException      if any error happens during the response writing operation
     * @throws ServletException if any problem occurs while executing the servlet.
     */
    public void insertCircuitPage(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            List<Type> circuitTypeList = new GetCircuitTypesDAO(getConnection()).access().getOutputParam();
            req.setAttribute("circuitList", circuitTypeList);
            req.getRequestDispatcher("/jsp/insert-circuit.jsp").forward(req, res);
        } catch (SQLException e) {
            LOGGER.error("Cannot read car types: unexpected error while accessing the database.", e);
        }
    }

    /**
     * Handles the HTTP POST request of the admin, that are:
     * <pre>
     *  - insertCar
     *  - insertCircuit
     *  - addCircuitType
     *  - addCarType
     *  - modifyCar
     *  - modifyCircuit
     *  - makeCarUnavailable
     *  - makeCircuitUnavailable
     *  </pre>
     *
     * @param req the {@code HttpServletRequest} incoming request from the client
     * @param res the {@code HttpServletResponse} response object from the server
     * @throws IOException      if any error occurs in the client/server communication.
     * @throws ServletException if any error occurs while executing the servlet.
     */
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        //take the request uri
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());

        String op = req.getRequestURI();

        try {
            op = op.substring(op.lastIndexOf("admin") + 6);
            switch (op) {
                case "insertCar/":
                    LogContext.setAction(Actions.INSERT_CAR);
                    insertCarOperations(req, res);
                    break;

                case "insertCircuit/":
                    LogContext.setAction(Actions.INSERT_CIRCUIT);
                    insertCircuitOperations(req, res);
                    break;

                default:
                    Message m = new Message("An error occurred default", "E200", "Operation Unknown");
                    LOGGER.info("stacktrace {}:", m.getMessage());
            }
        } catch (Exception e) {
            LOGGER.error("Unable to serve request.", e);
            throw e;
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    /**
     * All the operations needed to insert a car in the database.
     *
     * @param req the {@code HttpServletRequest} incoming request
     * @param res the {@code HttpServletResponse} response object
     * @throws IOException      if any error happens during the response writing operation
     * @throws ServletException if any error occurs while executing the servlet.
     */
    private void insertCarOperations(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

        // request parameters
        String model = null;
        String brand = null;
        String description;
        int maxSpeed;
        int horsepower;
        int acceleration;
        boolean availability;
        String type = null;
        byte[] image = null;
        String imageMediaType = null;

        // model
        Car car = null;
        Message m;

        try {
            // retrieves the request parameters
            model = req.getParameter("model");
            brand = req.getParameter("brand");
            description = req.getParameter("description");
            maxSpeed = Integer.parseInt(req.getParameter("maxSpeed"));
            horsepower = Integer.parseInt(req.getParameter("horsepower"));
            acceleration = Integer.parseInt(req.getParameter("acceleration"));
            availability = Boolean.parseBoolean(req.getParameter("availability"));
            type = req.getParameter("type");

            // retrieves the request parameters
            for (Part p : req.getParts()) {

                switch (p.getName()) {
                    case "model":
                        try (InputStream is = p.getInputStream()) {
                            model = new String(is.readAllBytes(), StandardCharsets.UTF_8).trim();
                        }
                        break;

                    case "brand":
                        try (InputStream is = p.getInputStream()) {
                            brand = new String(is.readAllBytes(), StandardCharsets.UTF_8).trim();
                        }
                        break;

                    case "description":
                        try (InputStream is = p.getInputStream()) {
                            description = new String(is.readAllBytes(), StandardCharsets.UTF_8).trim();
                        }
                        break;

                    case "type":
                        try (InputStream is = p.getInputStream()) {
                            type = new String(is.readAllBytes(), StandardCharsets.UTF_8).trim();
                        }
                        break;

                    case "maxSpeed":
                        try (InputStream is = p.getInputStream()) {
                            maxSpeed = Integer.parseInt(new String(is.readAllBytes(), StandardCharsets.UTF_8).trim());
                        }
                        break;

                    case "horsepower":
                        try (InputStream is = p.getInputStream()) {
                            horsepower = Integer.parseInt(new String(is.readAllBytes(), StandardCharsets.UTF_8).trim());
                        }
                        break;

                    case "acceleration":
                        try (InputStream is = p.getInputStream()) {
                            acceleration = Integer.parseInt(new String(is.readAllBytes(), StandardCharsets.UTF_8).trim());
                        }
                        break;

                    case "availability":
                        try (InputStream is = p.getInputStream()) {
                            availability = Boolean.parseBoolean(new String(is.readAllBytes(), StandardCharsets.UTF_8).trim());
                        }
                        break;

                    case "image":
                        imageMediaType = p.getContentType();
                        parseImageMediaType(imageMediaType);
                        try (InputStream is = p.getInputStream()) {
                            image = is.readAllBytes();
                        }

                        break;
                }
            }

            // Create a new car object
            car = new Car(brand, model, description, maxSpeed, horsepower, acceleration, availability, type, image, imageMediaType);

            // insert the car in the database
            new InsertCarDAO(getConnection(), car).access().getOutputParam();

            m = new Message(String.format("Car object %s %s successfully created.", brand, model));

            LOGGER.info(new StringFormattedMessage("Car object %s %s successfully created.", brand, model));

        } catch (NumberFormatException e) {
            m = new Message(
                    "Cannot create the car object. Invalid input parameters: maxSpeed, horsepower and acceleration must be integer.",
                    "E100", e.getMessage());
        } catch (MimeTypeParseException e) {
            m = new Message(
                    "Cannot create the car object. Unsupported MIME media type for car image. Expected: image/png or image/jpeg.",
                    "E600", e.getMessage());
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                m = new Message(String.format("Cannot create the car object: car %s %s already exists.", brand, model), "E300",
                        e.getMessage());
                LOGGER.error(
                        new StringFormattedMessage("Cannot create the car object: car %s %s already exists.", brand, model),
                        e);
            } else if ("23503".equals(e.getSQLState())) {
                m = new Message(String.format("Cannot create the car object: car type %s does not exist.", type), "E400",
                        e.getMessage());
                LOGGER.error(
                        new StringFormattedMessage("Cannot create the car object: car type %s does not exist.", type),
                        e);
            } else if ("23502".equals(e.getSQLState())) {
                m = new Message("Cannot create the car object: one or more car attributes are null.", "E500",
                        e.getMessage());
                LOGGER.error(
                        "Cannot create the car object: one or more car attributes are null.",
                        e);
            } else {
                m = new Message("Cannot create the car object: unexpected error while accessing the database.", "E200",
                        e.getMessage());

                LOGGER.error("Cannot create the car object: unexpected error while accessing the database.", e);
            }
        }

        try {

            // stores the employee and the message as a request attribute
            req.setAttribute("car", car);
            req.setAttribute("message", m);

            // forwards the control to the create-employee-result JSP
            req.getRequestDispatcher("/jsp/create-car-result.jsp").forward(req, res);
        } catch (IOException e) {
            LOGGER.error(new StringFormattedMessage("Unable to send response when creating the car object %s %s.", brand, model), e);
            throw e;
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
            LogContext.removeResource();
        }

    }

    /**
     * All the operations needed to insert a circuit in the database.
     *
     * @param req the {@code HttpServletRequest} incoming request
     * @param res the {@code HttpServletResponse} response object
     * @throws IOException      if any error happens during the response writing operation
     * @throws ServletException if any error occurs while executing the servlet.
     */
    private void insertCircuitOperations(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

        // request parameters
        String name = null;
        String address = null;
        String description = null;
        int length = -1;
        int cornersNumber = -1;
        int lapPrice = -1;
        boolean availability = false;
        String type = null;
        byte[] image = null;
        String imageMediaType = null;

        // model
        Circuit circuit = null;
        Message m;

        try {

            // retrieves the request parameters
            for (Part p : req.getParts()) {

                switch (p.getName()) {
                    case "name":
                        try (InputStream is = p.getInputStream()) {
                            name = new String(is.readAllBytes(), StandardCharsets.UTF_8).trim();
                        }
                        break;

                    case "address":
                        try (InputStream is = p.getInputStream()) {
                            address = new String(is.readAllBytes(), StandardCharsets.UTF_8).trim();
                        }
                        break;

                    case "description":
                        try (InputStream is = p.getInputStream()) {
                            description = new String(is.readAllBytes(), StandardCharsets.UTF_8).trim();
                        }
                        break;

                    case "type":
                        try (InputStream is = p.getInputStream()) {
                            type = new String(is.readAllBytes(), StandardCharsets.UTF_8).trim();
                        }
                        break;

                    case "length":
                        try (InputStream is = p.getInputStream()) {
                            length = Integer.parseInt(new String(is.readAllBytes(), StandardCharsets.UTF_8).trim());
                        }
                        break;

                    case "lapPrice":
                        try (InputStream is = p.getInputStream()) {
                            lapPrice = Integer.parseInt(new String(is.readAllBytes(), StandardCharsets.UTF_8).trim());
                        }
                        break;

                    case "cornersNumber":
                        try (InputStream is = p.getInputStream()) {
                            cornersNumber = Integer.parseInt(new String(is.readAllBytes(), StandardCharsets.UTF_8).trim());
                        }
                        break;

                    case "availability":
                        try (InputStream is = p.getInputStream()) {
                            availability = Boolean.parseBoolean(new String(is.readAllBytes(), StandardCharsets.UTF_8).trim());
                        }
                        break;

                    case "image":
                        imageMediaType = p.getContentType();
                        parseImageMediaType(imageMediaType);
                        try (InputStream is = p.getInputStream()) {
                            image = is.readAllBytes();
                        }
                        break;
                }
            }

            // Create a new car object
            circuit = new Circuit(name, type, length, cornersNumber, address, description, lapPrice, availability, image, imageMediaType);

            // insert the car in the database
            new InsertCircuitDAO(getConnection(), circuit).access().getOutputParam();

            m = new Message(String.format("Circuit object %s successfully created.", name));

            LOGGER.info(new StringFormattedMessage("Circuit object %s successfully created.", name));

        } catch (NumberFormatException e) {
        m = new Message(
                "Cannot create the circuit object. Invalid input parameters: maxSpeed, horsepower and acceleration must be integer.",
                "E100", e.getMessage());
        } catch (MimeTypeParseException e) {
            m = new Message(
                    "Cannot create the circuit object. Unsupported MIME media type for circuit image. Expected: image/png or image/jpeg.",
                    "E600", e.getMessage());
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                m = new Message(String.format("Cannot create the circuit object: circuit %s already exists.", name), "E300",
                        e.getMessage());
                LOGGER.error(
                        new StringFormattedMessage("Cannot create the circuit object: circuit %s already exists.", name),
                        e);
            } else if ("23503".equals(e.getSQLState())) {
                m = new Message(String.format("Cannot create the circuit object: circuit type %s does not exist.", type), "E400",
                        e.getMessage());
                LOGGER.error(
                        new StringFormattedMessage("Cannot create the circuit object: circuit type %s does not exist.", type),
                        e);
            } else if ("23502".equals(e.getSQLState())) {
                m = new Message("Cannot create the circuit object: one or more car attributes are null.", "E500",
                        e.getMessage());
                LOGGER.error(
                        "Cannot create the circuit object: one or more car attributes are null.",
                        e);
            } else {
                m = new Message("Cannot create the circuit object: unexpected error while accessing the database.", "E200",
                        e.getMessage());

                LOGGER.error("Cannot create the circuit object: unexpected error while accessing the database.", e);
            }
        }

        try {
            // set the MIME media type of the response
            res.setContentType("text/html; charset=utf-8");

            // get a stream to write the response
            PrintWriter out = res.getWriter();

            // write the HTML page
            out.printf("<!DOCTYPE html>%n");

            out.printf("<html lang=\"en\">%n");
            out.printf("<head>%n");
            out.printf("<meta charset=\"utf-8\">%n");
            out.printf("<title>Create Circuit</title>%n");
            out.printf("</head>%n");

            out.printf("<body>%n");
            out.printf("<h1>Create Circuit</h1>%n");
            out.printf("<hr/>%n");

            if (m.isError()) {
                out.printf("<ul>%n");
                out.printf("<li>error code: %s</li>%n", m.getErrorCode());
                out.printf("<li>message: %s</li>%n", m.getMessage());
                out.printf("<li>details: %s</li>%n", m.getErrorDetails());
                out.printf("</ul>%n");
            } else {
                out.printf("<p>%s</p>%n", m.getMessage());
                out.printf("<ul>%n");
                out.printf("<li>name: %s</li>%n", circuit.getName());
                out.printf("<li>address: %s</li>%n", circuit.getAddress());
                out.printf("<li>description: %s</li>%n", circuit.getDescription());
                out.printf("<li>length: %s</li>%n", circuit.getLength());
                out.printf("<li>corners number: %s</li>%n", circuit.getCornersNumber());
                out.printf("<li>lap price: %s</li>%n", circuit.getLapPrice());
                out.printf("<li>type: %s</li>%n", circuit.getType());
                out.printf("<li>available: %s</li>%n", circuit.getAvailable());
                out.printf("</ul>%n");
            }

            out.printf("</body>%n");

            out.printf("</html>%n");

            // flush the output stream buffer
            out.flush();

            // close the output stream
            out.close();
        } catch (IOException e) {
            LOGGER.error(new StringFormattedMessage("Unable to send response when creating the circuit object %s.", name), e);
            throw e;
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    /**
     * Parse the media type of the image.
     *
     * @param imageMediaType the media type of the image.
     */
    private void parseImageMediaType(final String imageMediaType) throws MimeTypeParseException{
        switch (imageMediaType.toLowerCase().trim()) {

            case "image/png":
            case "image/jpeg":
            case "image/jpg":
                // nothing to do
                break;

            default:
                LOGGER.error(String.format("Unsupported MIME media type %s for circuit image. ", imageMediaType));

                throw new MimeTypeParseException(
                        String.format("Unsupported MIME media type %s for circuit image. ",
                                imageMediaType));
        }
    }
}
