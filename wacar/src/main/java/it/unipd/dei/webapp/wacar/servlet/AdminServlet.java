package it.unipd.dei.webapp.wacar.servlet;

import it.unipd.dei.webapp.wacar.resource.*;

import java.io.IOException;

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
            op = op.substring(op.lastIndexOf("admin") + 6);
            switch (op){
                case "insertCar/":
                    request.getRequestDispatcher("/html/insertCar.html").forward(request, response);
                    break;
                case "":
//                    HttpSession session = request.getSession();
//                    User user = (User) session.getAttribute("account");
//                    if (user!=null) {
//                        Message m = new Message("Login success");
//                        writePage(user,m,response);
//                    }
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

    public void insertCarOperations(HttpServletRequest req, HttpServletResponse res){
        //TODO: Finish implementing the insertion of a car in the database
        String model = req.getParameter("model");
        String brand = req.getParameter("brand");
        String description = req.getParameter("description");
        int maxSpeed = Integer.parseInt(req.getParameter("maxSpeed"));
        int horsepower = Integer.parseInt(req.getParameter("horsepower"));
        int acceleration = Integer.parseInt(req.getParameter("acceleration"));

        //TODO: Add controls on maxSpeed, horsepower and acceleration (e.g. the maxSPeed can be a value between 10 and 500)


    }

    public void insertCircuitOperations(HttpServletRequest req, HttpServletResponse res){
        //TODO: Implement the insertion of a circuit in the database
    }

}
