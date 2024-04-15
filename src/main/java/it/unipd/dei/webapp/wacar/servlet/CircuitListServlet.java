package it.unipd.dei.webapp.wacar.servlet;

import it.unipd.dei.webapp.wacar.dao.ListCircuitDAO;
import it.unipd.dei.webapp.wacar.resource.Actions;
import it.unipd.dei.webapp.wacar.resource.Circuit;
import it.unipd.dei.webapp.wacar.resource.Message;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

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
@WebServlet(name = "CircuitListServlet", value = "/circuit_list/")
public class CircuitListServlet extends AbstractDatabaseServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        req.getSession().invalidate();

        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction(Actions.GET_ALL_CIRCUITS);

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
        }

        // Set the list of cars and the message as request attributes
        req.setAttribute("circuits", circuits);
        req.setAttribute("message", m);

        // Forward the request to the JSP page
        req.getRequestDispatcher("/jsp/circuit-list.jsp").forward(req, res);
    }
}
