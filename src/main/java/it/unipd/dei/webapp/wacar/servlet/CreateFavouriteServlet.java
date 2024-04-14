package it.unipd.dei.webapp.wacar.servlet;

import it.unipd.dei.webapp.wacar.dao.InsertFavouriteDAO;
import it.unipd.dei.webapp.wacar.resource.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;

/**
 * Create a new favourite and pass to the recap-favourite.jsp page the object to be shown on it.
 *
 * @author Manuel Rigobello (manuel.rigobello@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class CreateFavouriteServlet extends AbstractDatabaseServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setAction(Actions.INSERT_FAVOURITE);

        final HttpSession session = req.getSession(false);
        if (session == null) {
            LOGGER.error(String.format("No session. User cannot create a new favourite."));
            LogContext.removeIPAddress();
            LogContext.removeAction();
            throw new ServletException(String.format("No session. User cannot create a new favourite."));
        }
        User user = (User) session.getAttribute("account");

        LOGGER.info(String.format("Authorized access by %s for creating a new favourite.", user.getEmail()));

        Message m = null;

        Favourite favourite = null;
        String carBrand;
        String carModel;
        String circuitName;
        Timestamp createdAt;

        try {
            carBrand = req.getParameter("carBrand");
            carModel = req.getParameter("carModel");
            circuitName = req.getParameter("circuitName");
            createdAt = new Timestamp(System.currentTimeMillis());

            favourite = new Favourite(circuitName, carBrand, carModel, user.getEmail(), createdAt);
        } catch (Exception e) {
            LOGGER.error("Invalid request: {}", e.getMessage());
        }

        try {
            new InsertFavouriteDAO(getConnection(), favourite).access().getOutputParam();
        } catch (SQLException e) {
            m = new Message("Unable to insert the new order.", "E5A1", e.getMessage());
            req.setAttribute("message", m);

            LOGGER.error("Unable to insert the new favourite: " + e.getMessage());
        }

        LogContext.setAction(Actions.SHOW_FAVOURITE);

        req.setAttribute("newFavourite", favourite);
        req.getRequestDispatcher("/jsp/recap-favourite.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doGet(req, res);
    }
}
