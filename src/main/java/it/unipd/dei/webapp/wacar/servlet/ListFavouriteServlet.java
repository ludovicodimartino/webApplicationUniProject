package it.unipd.dei.webapp.wacar.servlet;

import it.unipd.dei.webapp.wacar.dao.ListFavouriteDAO;
import it.unipd.dei.webapp.wacar.resource.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Servlet for handling HTTP GET requests to list a user's favorites.
 * This servlet retrieves the list of favorites associated with the user
 * from the database using a ListFavouriteDAO object. It then sets the
 * retrieved list as a request attribute for a JSP page to display. If
 * an error occurs during the retrieval process, an error message is set
 * in the request attributes.
 *
 * @author Alessandro Leonardi (alessandro.leonardi@studenti.unipd.it)
 */

public class ListFavouriteServlet extends AbstractDatabaseServlet{

    /**
     * Handles HTTP GET requests for listing a user's favorites.
     * This method retrieves the list of favorites associated with the user
     * from the database using a ListFavouriteDAO object. It then sets the
     * retrieved list as a request attribute for a JSP page to display. If
     * an error occurs during the retrieval process, an error message is set
     * in the request attributes.
     *
     * @param req  The HttpServletRequest object containing the request parameters.
     * @param res  The HttpServletResponse object for sending the response.
     * @throws IOException      If an I/O error occurs while processing the request or response.
     * @throws ServletException If the servlet encounters difficulty while handling the request.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction(Actions.LIST_FAVOURITES);
        String op = req.getRequestURI();
        LOGGER.info("op GET {}", op);
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("account");

        Message m = null;
        List<Favourite> favouritesList = null;

        try {
            favouritesList = (List<Favourite>) new ListFavouriteDAO(getConnection(), user).access().getOutputParam();
            m = new Message("Favourites have been retrieved");
            LOGGER.info("All favourites have been retrieved");
        } catch (SQLException ex) {
            m = new Message("Unexpected error while searching for favourites in database. ","E5A1",ex.getMessage());
            LOGGER.error("Unexpected error while searching for favourites in database. ", ex);
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
            LogContext.removeResource();
        }

        // Set attributes for JSP
        req.setAttribute("favouritesList", favouritesList);
        req.setAttribute("message", m);

        req.getRequestDispatcher("/jsp/list-favourites.jsp").forward(req, res);
    }
}
