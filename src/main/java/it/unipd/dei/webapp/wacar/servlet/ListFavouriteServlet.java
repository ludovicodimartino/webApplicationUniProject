package it.unipd.dei.webapp.wacar.servlet;

import it.unipd.dei.webapp.wacar.dao.ListFavouriteDAO;
import it.unipd.dei.webapp.wacar.dao.ListOrdersByAccountDAO;
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
 * Servlet to handle the list of favourites
 *
 * @author
 * @version 1.00
 * @since 1.00
 */

@WebServlet(name = "ListOrdersByAccountServlet")
public class ListFavouriteServlet extends AbstractDatabaseServlet{

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
            m = new Message("Unexpected error while searching for favourites in database");
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
