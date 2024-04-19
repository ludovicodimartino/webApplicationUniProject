package it.unipd.dei.webapp.wacar.servlet;

import it.unipd.dei.webapp.wacar.dao.ListCarByAvailabilityDAO;
import it.unipd.dei.webapp.wacar.dao.ListCircuitByCarTypeDAO;
import it.unipd.dei.webapp.wacar.dao.InsertOrderDAO;
import it.unipd.dei.webapp.wacar.resource.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.message.StringFormattedMessage;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Handle all the steps to create an order.
 *
 * @author Manuel Rigobello (manuel.rigobello@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class OrderServlet  extends AbstractDatabaseServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // Take op
        String op = req.getRequestURI();
        op = op.substring(op.lastIndexOf("create-order") + 13);
        LOGGER.info("op POST {}", op);

        switch (op) {
            case "cars":
            case "cars/":
                LogContext.setIPAddress(req.getRemoteAddr());
                LogContext.setAction(Actions.CREATE_ORDER_LIST_CARS);

                listAvailableCars(req, res);
                break;
            case "circuits":
            case "circuits/":
                LogContext.setIPAddress(req.getRemoteAddr());
                LogContext.setAction(Actions.CREATE_ORDER_LIST_SUITABLE_CIRCUIT);

                listSuitableCircuits(req, res);
                break;
            case "complete-order":
            case "complete-order/":
                LogContext.setIPAddress(req.getRemoteAddr());
                LogContext.setAction(Actions.CREATE_ORDER_SET_OTHER_INFO);

                completeOrder(req, res);
                break;
            case "recap":
            case "recap/":
                LogContext.setIPAddress(req.getRemoteAddr());
                LogContext.setAction(Actions.INSERT_ORDER);

                showRecap(req, res);
                break;
        }
        // case op = cars => fai quello che c'è qua sotto
        // case op = circuits => fai quello che c'è dentro la servlet ordercircuit
        // case op = complete-order => mostra ultimo form con data e numero di giri
        // case op = add-order => aggiungi ordine in tabella e fai redirect a home ()
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doGet(req, res);
    }


    private void listAvailableCars(HttpServletRequest req, HttpServletResponse res) {
        List<Car> cars = null;
        Message m = null;
        final boolean available = true;

        try {
            cars = new ListCarByAvailabilityDAO(getConnection(), available).access().getOutputParam();

            if(!"".equals(available)) {
                m = new Message("Cars successfully searched.");
                req.setAttribute("message", m);
            }

            LOGGER.info("Cars that have available = {} are successfully searched", available);
        } catch (SQLException e) {
            m = new Message("Unable to search for cars: unexpected error while accessing the database.", "E5A1", e.getMessage());
            req.setAttribute("message", m);

            LOGGER.error("Unable to search for cars: unexpected error while accessing the database.", e);
        }

        try {
            req.setAttribute("message",m);
            req.setAttribute("carsList", cars);

            req.getRequestDispatcher("/jsp/create-order.jsp").forward(req, res);
        } catch(Exception e) {
            m = new Message("Error while loading the list of cars.");
            LOGGER.error(new StringFormattedMessage("Error while loading the list of cars."));

            req.setAttribute("message",m);
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
        }
    }


    private void listSuitableCircuits(HttpServletRequest req, HttpServletResponse res) {
        String carBrand = req.getParameter("carBrand");
        String carModel = req.getParameter("carModel");
        String carType = req.getParameter("carType");

        List<Circuit> circuits = null;
        Message m = null;

        if (carBrand == "" || carModel == "" || carType == "") {
            LOGGER.error("Invalid request: missing required attributes.");
            m = new Message("Cannot create the order: wrong or missing attributes.", "E4A9", null);
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            circuits = new ListCircuitByCarTypeDAO(getConnection(), carType).access().getOutputParam();

            m = new Message(String.format("List of circuits where carType = {} are successfully retrieved.", carType));

            LOGGER.info("List of circuits where carType = {} can race are successfully searched", carType);
        } catch (SQLException e) {
            m = new Message("Unable to search for circuits: unexpected error while accessing the database.", "E5A1",
                    e.getMessage());
            req.setAttribute("message", m);

            LOGGER.error("Unable to search for circuits: unexpected error while accessing the database.", e);
        }

        try {
            req.setAttribute("circuitsList", circuits);
            req.setAttribute("carBrand", carBrand);
            req.setAttribute("carModel", carModel);
            req.getRequestDispatcher("/jsp/create-order.jsp").forward(req, res);
        } catch(Exception e) {
            LOGGER.error(new StringFormattedMessage("Unable to send response when searching for circuits"));
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
        }
    }

    private void completeOrder(HttpServletRequest req, HttpServletResponse res) {
        String carBrand = req.getParameter("carBrand");
        String carModel = req.getParameter("carModel");
        String circuitName = req.getParameter("circuitName");
        String lapPrice = req.getParameter("circuitLapPrice");

        try {
            req.setAttribute("circuitName", circuitName);
            req.setAttribute("carBrand", carBrand);
            req.setAttribute("carModel", carModel);
            req.setAttribute("lapPrice", lapPrice);

            req.getRequestDispatcher("/jsp/create-order.jsp").forward(req, res);
        } catch(Exception e) {
            LOGGER.error(new StringFormattedMessage("Unable to send response when creating latest page: {}", e));
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
        }
    }

    private void showRecap(HttpServletRequest req, HttpServletResponse res) {
        User user = (User) req.getSession().getAttribute("account");

        Message m = null;
        Order order = null;
        String account = "";
        java.sql.Date date;
        String carBrand;
        String carModel;
        String circuitName;
        Timestamp createdAt;
        int nLaps;
        int lapPrice;

        try {
            account = user.getEmail();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date d = df.parse(req.getParameter("date"));
            date = new java.sql.Date(d.getTime());
            carBrand = req.getParameter("carBrand");
            carModel = req.getParameter("carModel");
            circuitName = req.getParameter("circuitName");
            createdAt = new Timestamp(System.currentTimeMillis());
            nLaps = Integer.parseInt(req.getParameter("nLaps"));
            lapPrice = Integer.parseInt(req.getParameter("lapPrice"));

            order = new Order(account, date, carBrand, carModel, circuitName, createdAt, nLaps, nLaps * lapPrice);
        } catch (Exception e) {
            LOGGER.error("Invalid request: %s", e.getMessage());
        }

        try {
            new InsertOrderDAO(getConnection(), order).access().getOutputParam();
        } catch (SQLException e) {
            String errorMsg = e.getMessage().substring(e.getMessage().indexOf(":") + 2, e.getMessage().indexOf(" Where:"));
            m = new Message(errorMsg, "E5A5", e.getMessage());
            req.setAttribute("message", m);

            LOGGER.error("Unable to insert the new order: %s", errorMsg);
        }

        try {
            req.setAttribute("newOrder", order);
            LOGGER.info("Redirect to recap-order.jsp");

            req.getRequestDispatcher("/jsp/recap-order.jsp").forward(req, res);
        } catch(Exception e) {
            LOGGER.error(new StringFormattedMessage("Unable to send response when creating the recap order page: %s", e.getMessage()));
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
        }
    }
}
