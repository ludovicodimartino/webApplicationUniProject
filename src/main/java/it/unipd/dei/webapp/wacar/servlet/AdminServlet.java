package it.unipd.dei.webapp.wacar.servlet;

import it.unipd.dei.webapp.wacar.dao.InsertCarDAO;
import it.unipd.dei.webapp.wacar.resource.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;

@WebServlet(name = "AdminServlet", value = "/admin/*")
public class AdminServlet extends AbstractDatabaseServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String op = request.getRequestURI();
        LogContext.setIPAddress(request.getRemoteAddr());
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
                    ArrayList<String> carTypeList = new ArrayList<>(Arrays.asList("Micro", "SUV", "Supercar"));
                    request.setAttribute("carList", carTypeList);
                    request.getRequestDispatcher("/jsp/insertCar.jsp").forward(request, response);
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
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
            LogContext.removeResource();
        }


    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        //take the request uri
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        String op = req.getRequestURI();

        try{
            op = op.substring(op.lastIndexOf("admin") + 6);
            switch (op){
                case "insertCar/":
                    insertCarOperations(req, res);
                    break;

                case "insertCircuit/":
                    insertCircuitOperations(req, res);
                    break;

                default:
                    Message m = new Message("An error occurred default","E200","Operation Unknown");
                    LOGGER.info("stacktrace {}:", m.getMessage());
            }
        } catch (Exception e){
            LOGGER.error("Unable to serve request.", e);
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
     */
    public void insertCarOperations(HttpServletRequest req, HttpServletResponse res){
        Car car = null;
        Message m = null;

        try{
            //TODO: Finish implementing the insertion of a car in the database
            String model = req.getParameter("model");
            String brand = req.getParameter("brand");
            String description = req.getParameter("description");
            int maxSpeed = Integer.parseInt(req.getParameter("maxSpeed"));
            int horsepower = Integer.parseInt(req.getParameter("horsepower"));
            int acceleration = Integer.parseInt(req.getParameter("acceleration"));
            boolean availability = Boolean.parseBoolean(req.getParameter("availability"));
            String type = req.getParameter("type");

            LOGGER.info("Insert Car POST request: model: {}, " +
                    "brand: {}, description: {}, maxSpeed: {}, " +
                    "horsepower: {}, acceleration: {}, availability: {}, " +
                    "type: {}", model, brand, description, maxSpeed, horsepower, acceleration, availability, type);
            Car carTmp = new Car(brand, model, description, maxSpeed, horsepower, acceleration, availability, type, "SEGNAPOSTO");

            car = new InsertCarDAO(getConnection(), carTmp).access().getOutputParam();
            //TODO: Add controls on maxSpeed, horsepower and acceleration (e.g. the maxSPeed can be a value between 10 and 500)

        } catch (SQLException e) {
            m = new Message("An error occurred SQL","E200",e.getMessage());
            LOGGER.error("stacktrace:", e);
        }

    }

    public void insertCircuitOperations(HttpServletRequest req, HttpServletResponse res){
        //TODO: Implement the insertion of a circuit in the database
    }

}
