package it.unipd.dei.webapp.wacar.servlet;

import it.unipd.dei.webapp.wacar.dao.*;
import it.unipd.dei.webapp.wacar.resource.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import it.unipd.dei.webapp.wacar.utils.CarOrCircuitType;
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
        User user = (User) (req.getSession()).getAttribute("account");
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(op);
        LogContext.setUser(user.getEmail());

        try {
            if (op.lastIndexOf("admin") == op.length() - 5) { // Math url wacar/admin
                op = "";
            } else { // Math url wacar/**/admin**
                op = op.substring(op.lastIndexOf("admin/") + 6);
            }
            LOGGER.info("op: " + op);

            switch (op) {
                case "insertCar/":
                    LogContext.setAction(Actions.GET_INSERT_CAR_PAGE);
                    insertCarPage(req, res);
                    break;
                case "insertCircuit/":
                    LogContext.setAction(Actions.GET_INSERT_CIRCUIT_PAGE);
                    insertCircuitPage(req, res);
                    break;
                case "insertMapping/":
                    LogContext.setAction(Actions.GET_MAPPING_PAGE);
                    carCircuitSuitabilityPage(req, res);
                    break;
                case "editCar/":
                    LogContext.setAction(Actions.GET_EDIT_CAR_PAGE);
                    editCarPage(req, res);
                    break;
                case "editCircuit/":
                    LogContext.setAction(Actions.GET_EDIT_CAR_PAGE);
                    editCircuitPage(req, res);
                    break;
                case "insertCarType/":
                    LogContext.setAction(Actions.GET_INSERT_CAR_TYPE_PAGE);
                    insertCarTypePage(req, res);
                    break;
                case "insertCircuitType/":
                    LogContext.setAction(Actions.GET_INSERT_CIRCUIT_TYPE_PAGE);
                    insertCircuitTypePage(req, res);
                    break;
                case "admin-info/": // URL /wacar/admin/admin-info
                    LogContext.setAction(Actions.ADMIN_INFO);
                    if (user != null) {
                        Message m = new Message("Login success");
                        req.getRequestDispatcher("/jsp/userPage.jsp").forward(req, res);
                    }
                    else {
                        Message m = new Message("Login FAILED");
                        LOGGER.error("Login Failed. Stacktrace {}:", m.getMessage());
                    }
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
     * Access the database to load the mapping page.
     * In particular, only the circuit types, car types and carCircuitSuitability present in the database will be shown.
     *
     * @param req the {@code HttpServletRequest} incoming request
     * @param res the {@code HttpServletResponse} response object
     * @throws IOException      if any error happens during the response writing operation
     * @throws ServletException if any problem occurs while executing the servlet.
     */
    private void carCircuitSuitabilityPage(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            List<CarCircuitSuitability> cCSuitList = new ListCarCircuitSuitabilityDAO(getConnection()).access().getOutputParam();
            List<Type> carTypeList = new GetCarTypesDAO(getConnection()).access().getOutputParam();
            List<Type> circuitTypeList = new GetCircuitTypesDAO(getConnection()).access().getOutputParam();

            // Pre-processing of the cCSuitList for the jsp page
            HashMap<String, List<String>> ccSuitMap = new HashMap<>();
            for (CarCircuitSuitability el : cCSuitList) {
                List<String> carList = ccSuitMap.computeIfAbsent(el.getCarType(), k -> new ArrayList<>());
                carList.add(el.getCircuitType());
            }
            req.setAttribute("cCSuitMap", ccSuitMap);
            req.setAttribute("carTypeList", carTypeList);
            req.setAttribute("circuitTypeList", circuitTypeList);
            req.getRequestDispatcher("/jsp/car-circuit-suitability.jsp").forward(req, res);
        } catch (SQLException e) {
            LOGGER.error("Cannot read car-suitability types: unexpected error while accessing the database.", e);
        }
    }

    /**
     * Access the database to load the requested car.
     * The car is identified by two parameters: model and brand.
     * Send the page to the client.
     *
     * @param req the {@code HttpServletRequest} incoming request
     * @param res the {@code HttpServletResponse} response object
     * @throws IOException      if any error happens during the response writing operation
     * @throws ServletException if any problem occurs while executing the servlet.
     */
    private void editCarPage(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String brand = req.getParameter("brand");
        String model = req.getParameter("model");

        try {
            Car car = new GetCarDAO(getConnection(), brand, model).access().getOutputParam();
            List<Type> carTypeList = new GetCarTypesDAO(getConnection()).access().getOutputParam();
            req.setAttribute("car", car);
            req.setAttribute("carTypeList", carTypeList);
            req.getRequestDispatcher("/jsp/edit-car.jsp").forward(req, res);

        } catch (SQLException e) {
            LOGGER.error("Cannot read car: unexpected error while accessing the database.", e);
        }
    }

    /**
     * Access the database to load the requested circuit.
     * The car is identified by the GET parameter name.
     * Send the page to the client.
     *
     * @param req the {@code HttpServletRequest} incoming request
     * @param res the {@code HttpServletResponse} response object
     * @throws IOException      if any error happens during the response writing operation
     * @throws ServletException if any problem occurs while executing the servlet.
     */
    private void editCircuitPage(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String name = req.getParameter("name");
        try {
            Circuit circuit = new GetCircuitDAO(getConnection(), name).access().getOutputParam();
            List<Type> circuiTypeList = new GetCircuitTypesDAO(getConnection()).access().getOutputParam();
            req.setAttribute("circuit", circuit);
            req.setAttribute("circuitTypeList", circuiTypeList);
            req.getRequestDispatcher("/jsp/edit-circuit.jsp").forward(req, res);

        } catch (SQLException e) {
            LOGGER.error("Cannot read circuit: unexpected error while accessing the database.", e);
        }
    }

    /**
     * Access the database to load the car types.
     *
     * @param req the {@code HttpServletRequest} incoming request
     * @param res the {@code HttpServletResponse} response object
     * @throws IOException      if any error happens during the response writing operation
     * @throws ServletException if any problem occurs while executing the servlet.
     */
    private void insertCarTypePage(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            List<Type> carTypeList = new GetCarTypesDAO(getConnection()).access().getOutputParam();
            req.setAttribute("carTypesList", carTypeList);
            req.getRequestDispatcher("/jsp/insert-car-type.jsp").forward(req, res);
        } catch (SQLException e) {
            LOGGER.error("Cannot read car types: unexpected error while accessing the database.", e);
        }
    }

    /**
     * Access the database to load the circuit types.
     *
     * @param req the {@code HttpServletRequest} incoming request
     * @param res the {@code HttpServletResponse} response object
     * @throws IOException      if any error happens during the response writing operation
     * @throws ServletException if any problem occurs while executing the servlet.
     */
    private void insertCircuitTypePage(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        try {
            List<Type> circuitTypeList = new GetCircuitTypesDAO(getConnection()).access().getOutputParam();
            req.setAttribute("circuitTypesList", circuitTypeList);
            req.getRequestDispatcher("/jsp/insert-circuit-type.jsp").forward(req, res);
        } catch (SQLException e) {
            LOGGER.error("Cannot read circuit types: unexpected error while accessing the database.", e);
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
        LogContext.setUser(((User) (req.getSession()).getAttribute("account")).getEmail());

        String op = req.getRequestURI();

        try {
            op = op.substring(op.lastIndexOf("admin") + 6);
            switch (op) {
                case "insertCar/":
                    LogContext.setAction(Actions.INSERT_CAR);
                    insertCarOperations(req, res, false);
                    break;

                case "insertCircuit/":
                    LogContext.setAction(Actions.INSERT_CIRCUIT);
                    insertCircuitOperations(req, res, false);
                    break;

                case "insertMapping/":
                    LogContext.setAction(Actions.INSERT_MAPPING);
                    insertCarCircuitSuitabilityOperations(req, res);
                    break;

                case "deleteMapping/":
                    LogContext.setAction(Actions.DELETE_MAPPING);
                    deleteMappingOperations(req, res);
                    break;

                case "editCar/":
                    LogContext.setAction(Actions.EDIT_CAR);
                    insertCarOperations(req, res, true);
                    break;

                case "editCircuit/":
                    LogContext.setAction(Actions.EDIT_CAR);
                    insertCircuitOperations(req, res, true);
                    break;

                case "insertCarType/":
                    LogContext.setAction(Actions.INSERT_CAR_TYPE);
                    insertTypeOperations(req, res, CarOrCircuitType.CAR_TYPE);
                    break;

                case "insertCircuitType/":
                    LogContext.setAction(Actions.INSERT_CIRCUIT_TYPE);
                    insertTypeOperations(req, res, CarOrCircuitType.CIRCUIT_TYPE);
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
     * All the operations needed to insert or edit a car in the database.
     *
     * @param req    the {@code HttpServletRequest} incoming request
     * @param res    the {@code HttpServletResponse} response object
     * @param update Set it to true if the operation is an update, false otherwise
     * @throws IOException      if any error happens during the response writing operation
     * @throws ServletException if any error occurs while executing the servlet.
     */
    private void insertCarOperations(HttpServletRequest req, HttpServletResponse res, final boolean update) throws IOException, ServletException {

        // request parameters
        String model = null;
        String brand = null;
        String description = null;
        int maxSpeed = -1;
        int horsepower = -1;
        float acceleration = -1;
        boolean availability = false;
        String type = null;
        byte[] image = null;
        String imageMediaType = null;

        // model
        Car car = null;
        Message m;

        try {

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
                            acceleration = Float.parseFloat(new String(is.readAllBytes(), StandardCharsets.UTF_8).trim());
                        }
                        break;

                    case "availability":
                        try (InputStream is = p.getInputStream()) {
                            availability = Boolean.parseBoolean(new String(is.readAllBytes(), StandardCharsets.UTF_8).trim());
                        }
                        break;

                    case "image":

                        // parse the image if it is a car insert or a car update with a new image
                        if (!update || Objects.equals(p.getContentType(), "image/png") || Objects.equals(p.getContentType(), "image/jpeg") || Objects.equals(p.getContentType(), "image/jpg")) {
                            imageMediaType = p.getContentType();
                            parseImageMediaType(imageMediaType);
                            try (InputStream is = p.getInputStream()) {
                                image = is.readAllBytes();
                            }
                        }

                        break;
                }
            }

            // Create a new car object
            car = new Car(brand, model, description, maxSpeed, horsepower, acceleration, availability, type, image, imageMediaType);

            // insert the car in the database
            if (update) {
                new UpdateCarDAO(getConnection(), car).access();
                m = new Message(String.format("Car object %s %s successfully updated.", brand, model));
                LOGGER.info(new StringFormattedMessage("Car object %s %s successfully updated.", brand, model));
            } else {
                new InsertCarDAO(getConnection(), car).access();
                m = new Message(String.format("Car object %s %s successfully created.", brand, model));
                LOGGER.info(new StringFormattedMessage("Car object %s %s successfully created.", brand, model));
            }

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

            // stores the car and the message as a request attribute
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
    private void insertCircuitOperations(HttpServletRequest req, HttpServletResponse res, final boolean update) throws IOException, ServletException {

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

                        // parse the image if it is a car insert or a car update with a new image
                        if (!update || Objects.equals(p.getContentType(), "image/png") || Objects.equals(p.getContentType(), "image/jpeg") || Objects.equals(p.getContentType(), "image/jpg")) {
                            imageMediaType = p.getContentType();
                            parseImageMediaType(imageMediaType);
                            try (InputStream is = p.getInputStream()) {
                                image = is.readAllBytes();
                            }
                        }

                        break;
                }
            }

            // Create a new circuit object
            circuit = new Circuit(name, type, length, cornersNumber, address, description, lapPrice, availability, image, imageMediaType);

            if (update) {
                //update the circuit in the database
                new UpdateCircuitDAO(getConnection(), circuit).access();
                m = new Message(String.format("Circuit object %s successfully updated.", name));
                LOGGER.info(new StringFormattedMessage("Circuit object %s successfully updated.", name));
            } else {
                // insert the circuit in the database
                new InsertCircuitDAO(getConnection(), circuit).access();
                m = new Message(String.format("Circuit object %s successfully created.", name));
                LOGGER.info(new StringFormattedMessage("Circuit object %s successfully created.", name));
            }


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
            // stores the car and the message as a request attribute
            req.setAttribute("circuit", circuit);
            req.setAttribute("message", m);

            // forwards the control to the JSP
            req.getRequestDispatcher("/jsp/create-circuit-result.jsp").forward(req, res);
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
     * All the operations needed to insert a mapping between a car and a circuit in the database.
     *
     * @param req the {@code HttpServletRequest} incoming request
     * @param res the {@code HttpServletResponse} response object
     * @throws IOException      if any error happens during the response writing operation
     * @throws ServletException if any error occurs while executing the servlet.
     */
    private void insertCarCircuitSuitabilityOperations(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {

        // request parameters
        String carType = null;
        String circuitType = null;

        // model
        CarCircuitSuitability cCSuit;
        Message m;

        try {
            carType = req.getParameter("carType");
            circuitType = req.getParameter("circuitType");

            // Create a new car object
            cCSuit = new CarCircuitSuitability(carType, circuitType);

            // insert the car in the database
            new InsertCarCircuitSuitabilityDAO(getConnection(), cCSuit).access();

            m = new Message(String.format("Mapping between car type %s and circuit type %s successfully created.", carType, circuitType));

            LOGGER.info(String.format("Mapping between car type %s and circuit type %s successfully created.", carType, circuitType));
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                m = new Message(String.format("Cannot create the mapping: mapping %s %s already exists.", carType, circuitType), "E300",
                        e.getMessage());
                LOGGER.error(String.format("Cannot create the mapping: mapping %s %s already exists.", carType, circuitType));
            } else if ("23503".equals(e.getSQLState())) {
                m = new Message(String.format("Cannot create the mapping: car type %s or circuit type %s does not exist.", carType, circuitType), "E400",
                        e.getMessage());
                LOGGER.error(
                        String.format("Cannot create the mapping: car type %s or circuit type %s does not exist.", carType, circuitType),
                        e);
            } else if ("23502".equals(e.getSQLState())) {
                m = new Message("Cannot create the mapping: the circuit type or the car type are null.", "E500",
                        e.getMessage());
                LOGGER.error(
                        "Cannot create the mapping: the circuit type or the car type are null.",
                        e);
            } else {
                m = new Message("Cannot create the mapping: unexpected error while accessing the database.", "E200",
                        e.getMessage());

                LOGGER.error("Cannot create the mapping: unexpected error while accessing the database.", e);
            }
        }
        try {
            // forwards the control to the JSP
            req.setAttribute("message", m);
            carCircuitSuitabilityPage(req, res);
        } catch (IOException e) {
            LOGGER.error(new StringFormattedMessage("Unable to send response when creating the mapping (%s, %s).", carType, circuitType), e);
            throw e;
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    /**
     * All the operations needed to delete a mapping between a car and a circuit in the database.
     *
     * @param req the {@code HttpServletRequest} incoming request
     * @param res the {@code HttpServletResponse} response object
     * @throws IOException if any error happens during the response writing operation
     */
    private void deleteMappingOperations(HttpServletRequest req, HttpServletResponse res) throws IOException {
        // request parameters
        String carType = null;
        String circuitType = null;

        // model
        CarCircuitSuitability cCSuit;
        Message m;

        try {
            carType = req.getParameter("carType");
            circuitType = req.getParameter("circuitType");

            // Create a new car object
            cCSuit = new CarCircuitSuitability(carType, circuitType);

            // insert the car in the database
            new DeleteCarCircuitSuitabilityDAO(getConnection(), cCSuit).access();

            m = new Message(String.format("Mapping between car type %s and circuit type %s successfully deleted.", carType, circuitType));

            LOGGER.info(String.format("Mapping between car type %s and circuit type %s successfully deleted.", carType, circuitType));
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                m = new Message(String.format("Cannot delete the mapping: mapping %s %s already exists.", carType, circuitType), "E300",
                        e.getMessage());
                LOGGER.error(String.format("Cannot delete the mapping: mapping %s %s already exists.", carType, circuitType));
            } else if ("23503".equals(e.getSQLState())) {
                m = new Message(String.format("Cannot delete the mapping: car type %s or circuit type %s does not exist.", carType, circuitType), "E400",
                        e.getMessage());
                LOGGER.error(
                        String.format("Cannot delete the mapping: car type %s or circuit type %s does not exist.", carType, circuitType),
                        e);
            } else if ("23502".equals(e.getSQLState())) {
                m = new Message("Cannot delete the mapping: the circuit type or the car type are null.", "E500",
                        e.getMessage());
                LOGGER.error(
                        "Cannot delete the mapping: the circuit type or the car type are null.",
                        e);
            } else {
                m = new Message("Cannot delete the mapping: unexpected error while accessing the database.", "E200",
                        e.getMessage());

                LOGGER.error("Cannot delete the mapping: unexpected error while accessing the database.", e);
            }
        }
        try {
            // forwards the control to the JSP
            req.setAttribute("message", m);
            res.sendRedirect(req.getContextPath() + "/admin/insertMapping/");
        } catch (IOException e) {
            LOGGER.error(new StringFormattedMessage("Unable to send response when deleting the mapping (%s, %s).", carType, circuitType), e);
            throw e;
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    /**
     * All the operations needed to insert a new type in the database.
     *
     * @param req the {@code HttpServletRequest} incoming request.
     * @param res the {@code HttpServletResponse} response object.
     * @param carOrCircuit whether it is a car type or a circuit type.
     * @throws IOException      if any error happens during the response writing operation.
     * @throws ServletException if any error occurs while executing the servlet.
     */
    private void insertTypeOperations(HttpServletRequest req, HttpServletResponse res, final CarOrCircuitType carOrCircuit) throws IOException, ServletException {

        // request parameters
        String name = null;

        // model
        Type type;
        Message m;

        try {
            name = req.getParameter("name");

            // Create a new type object
            type = new Type(name);

            // create the type in the database
            switch (carOrCircuit){
                case CAR_TYPE:
                    new InsertCarTypeDAO(getConnection(), type).access();
                    break;
                case CIRCUIT_TYPE:
                    new InsertCircuitTypeDAO(getConnection(), type).access();
                    break;
                default:
                    throw new IllegalArgumentException(String.format("Type %s not suited for the insert type operations.", carOrCircuit.getName()));
            }

            m = new Message(String.format("%s %s successfully inserted.", carOrCircuit.getName(), name));
            LOGGER.info(new StringFormattedMessage("%s %s successfully inserted.", carOrCircuit.getName(), name));

        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState())) {
                m = new Message(String.format("Cannot create the %s object: %s %s already exists.", carOrCircuit.getName(), carOrCircuit.getName(), name), "E300",
                        e.getMessage());
                LOGGER.error(
                        new StringFormattedMessage("Cannot create the %s object: %s %s already exists.", carOrCircuit.getName(), carOrCircuit.getName(), name),
                        e);
            } else if ("23502".equals(e.getSQLState())) {
                m = new Message("Cannot create the type object: the type name cannot be null.", "E500",
                        e.getMessage());
                LOGGER.error(
                        "Cannot create the type object: the type name cannot be null.",
                        e);
            } else {
                m = new Message("Cannot create the type object: unexpected error while accessing the database.", "E200",
                        e.getMessage());

                LOGGER.error("Cannot create the type object: unexpected error while accessing the database.", e);
            }
        }

        try {
            // stores the message as a request attribute
            req.setAttribute("message", m);
            if(carOrCircuit == CarOrCircuitType.CAR_TYPE) insertCarTypePage(req,res);
            else insertCircuitTypePage(req, res);
        } catch (IOException e) {
            LOGGER.error(new StringFormattedMessage("Unable to send response when creating the %s object %s.", carOrCircuit.getName(), name), e);
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
    private void parseImageMediaType(final String imageMediaType) throws MimeTypeParseException {
        switch (imageMediaType.toLowerCase().trim()) {

            case "image/png":
            case "image/jpeg":
            case "image/jpg":
                // nothing to do
                break;

            default:
                LOGGER.error(String.format("Unsupported MIME media type %s for the image. ", imageMediaType));
                throw new MimeTypeParseException(
                        String.format("Unsupported MIME media type %s for the image. ",
                                imageMediaType));
        }
    }
}
