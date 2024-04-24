package it.unipd.dei.webapp.wacar.servlet;

import it.unipd.dei.webapp.wacar.dao.ListCircuitDAO;
import it.unipd.dei.webapp.wacar.filter.LoginFilter;
import it.unipd.dei.webapp.wacar.resource.Actions;
import it.unipd.dei.webapp.wacar.resource.Circuit;
import it.unipd.dei.webapp.wacar.resource.Message;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import it.unipd.dei.webapp.wacar.resource.User;
import it.unipd.dei.webapp.wacar.resource.LogContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet to list all circuits
 *
 * @author Michele Scapinello (michele.scapinello@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
@WebServlet(name = "ListCircuitServlet", value = "/circuit_list/")
public class ListCircuitServlet extends AbstractDatabaseServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction(Actions.GET_ALL_CIRCUITS);

        String op = req.getRequestURI();
        LOGGER.info("op GET {}", op);

        User user = (User) req.getSession().getAttribute(LoginFilter.ACCOUNT_ATTRIBUTE);
        if (user != null) {
            req.setAttribute("accountType", user.getType());
            LogContext.setUser(user.getEmail());
        } else {
            LogContext.setUser("NOT_LOGGED");
        }

        List<Circuit> circuits = null;
        Message m = null;

        try {
            circuits = new ListCircuitDAO(getConnection()).access().getOutputParam();

            m = new Message("Circuits successfully retrieved");

            LOGGER.info("Circuits successfully retrieved");

        } catch (SQLException ex) {
            m = new Message("Cannot search for circuits: unexpected error while accessing the database.", "E200", ex.getMessage());

            LOGGER.error("Cannot search for circuits: unexpected error while accessing the database.", ex);
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
            LogContext.removeUser();
            LogContext.removeResource();
        }

        // Set the list of cars and the message as request attributes
        req.setAttribute("circuits", circuits);
        req.setAttribute("message", m);

        // Forward the request to the JSP page
        req.getRequestDispatcher("/jsp/list-circuit.jsp").forward(req, res);
    }
}
