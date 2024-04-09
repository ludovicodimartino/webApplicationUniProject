package it.unipd.dei.webapp.wacar.servlet;

import it.unipd.dei.webapp.wacar.dao.InsertCarDAO;
import it.unipd.dei.webapp.wacar.resource.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;
import org.apache.logging.log4j.message.StringFormattedMessage;

/**
 * This servlet handles the operations of the admin.
 *
 * @author Ludovico Di Martino (ludovico.dimartino@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
@WebServlet(name = "AdminServlet", value = "/admin/*")
public class AdminServlet extends AbstractDatabaseServlet {
    /**
     * Handles the HTTP GET request of the admin, that are:
     *  <pre>
     *  - insertCar page
     *  - insertCircuit page
     *  </pre>
     *
     * @param req the {@code HttpServletResponse} incoming request from the client
     * @param res the {@code HttpServletResponse} response object from the server
     *
     * @throws IOException if any error occurs in the client/server communication.
     * @throws ServletException if any problem occurs while executing the servlet.
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String op = req.getRequestURI();
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(op);
        LOGGER.info("op GET {}", op);

        try{
            if(op.lastIndexOf("admin") == op.length() - 5){ // Math url wacar/admin
                op = "";
            } else { // Math url wacar/**/admin**
                op = op.substring(op.lastIndexOf("admin") + 6);
            }

            switch (op){
                case "insertCar/":
                    LogContext.setAction(Actions.GET_INSERT_CAR_PAGE);
                    ArrayList<String> carTypeList = new ArrayList<>(Arrays.asList("Micro", "SUV", "Supercar"));
                    req.setAttribute("carList", carTypeList);
                    req.getRequestDispatcher("/jsp/insertCar.jsp").forward(req, res);
                    break;
                case "": // URL /wacar/admin
                    //redirect to admin page
                    break;
                default:
                    Message m = new Message("An error occurred default","E200","Operation Unknown");
                    LOGGER.info("stacktrace {}:", m.getMessage());
            }
        } catch (Exception e){
            LOGGER.error("Unable to serve request.", e);
            throw e;
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    /**
     * Handles the HTTP POST request of the admin, that are:
     *  <pre>
     *  - insertCar
     *  - insertCircuit
     *  - addCircuitType
     *  - addCarType
     *  - modifyCar
     *  - modifyCircuit
     *  </pre>
     *
     * @param req the {@code HttpServletResponse} incoming request from the client
     * @param res the {@code HttpServletResponse} response object from the server
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        //take the request uri
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());

        String op = req.getRequestURI();

        try{
            op = op.substring(op.lastIndexOf("admin") + 6);
            switch (op){
                case "insertCar/":
                    LogContext.setAction(Actions.INSERT_CAR);
                    insertCarOperations(req, res);
                    break;

                case "insertCircuit/":
                    LogContext.setAction(Actions.INSERT_CIRCUIT);
                    insertCircuitOperations(req, res);
                    break;

                default:
                    Message m = new Message("An error occurred default","E200","Operation Unknown");
                    LOGGER.info("stacktrace {}:", m.getMessage());
            }
        } catch (Exception e){
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
     * @param req the {@code HttpServletResponse} incoming request
     * @param res the {@code HttpServletResponse} response object
     *
     * @throws IOException if any error happens during the response writing operation
     */
    public void insertCarOperations(HttpServletRequest req, HttpServletResponse res) throws IOException{

        // request parameters
        String model = null;
        String brand = null;
        String description;
        int maxSpeed;
        int horsepower;
        int acceleration;
        boolean availability;
        String type = null;

        // model
        Car car = null;
        Message m;

        try{
            // retrieves the request parameters
            model = req.getParameter("model");
            brand = req.getParameter("brand");
            description = req.getParameter("description");
            maxSpeed = Integer.parseInt(req.getParameter("maxSpeed"));
            horsepower = Integer.parseInt(req.getParameter("horsepower"));
            acceleration = Integer.parseInt(req.getParameter("acceleration"));
            availability = Boolean.parseBoolean(req.getParameter("availability"));
            type = req.getParameter("type");

            // Create a new car object
            car = new Car(brand, model, description, maxSpeed, horsepower, acceleration, availability, type, "SEGNAPOSTO");

            // insert the car in the database
            new InsertCarDAO(getConnection(), car).access().getOutputParam();

            m = new Message(String.format("Car object %s %s successfully created.", brand, model));

            LOGGER.info(new StringFormattedMessage("Car object %s %s successfully created.", brand, model));

        } catch(NumberFormatException e) {
            m = new Message(
                    "Cannot create the car object. Invalid input parameters: maxSpeed, horsepower and acceleration must be integer.",
                    "E100", e.getMessage());
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
            } else {
                m = new Message("Cannot create the car object: unexpected error while accessing the database.", "E200",
                        e.getMessage());

                LOGGER.error("Cannot create the car object: unexpected error while accessing the database.", e);
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
            out.printf("<title>Create Car</title>%n");
            out.printf("</head>%n");

            out.printf("<body>%n");
            out.printf("<h1>Create Car</h1>%n");
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
                out.printf("<li>brand: %s</li>%n", car.getBrand());
                out.printf("<li>model: %s</li>%n", car.getModel());
                out.printf("<li>description: %s</li>%n", car.getDescription());
                out.printf("<li>acceleration: %s</li>%n", car.getAcceleration());
                out.printf("<li>horsepower: %s</li>%n", car.getHorsepower());
                out.printf("<li>max speed: %s</li>%n", car.getMaxSpeed());
                out.printf("<li>type: %s</li>%n", car.getType());
                out.printf("<li>available: %s</li>%n", car.isAvailable());
                out.printf("</ul>%n");
            }

            out.printf("</body>%n");

            out.printf("</html>%n");

            // flush the output stream buffer
            out.flush();

            // close the output stream
            out.close();
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
     * @param req the {@code HttpServletResponse} incoming request
     * @param res the {@code HttpServletResponse} response object
     *
     * @throws IOException if any error happens during the response writing operation
     */
    public void insertCircuitOperations(HttpServletRequest req, HttpServletResponse res) throws IOException{
        //TODO: Implement the insertion of a circuit in the database
    }

}
