package it.unipd.dei.webapp.wacar.servlet;

import it.unipd.dei.webapp.wacar.filter.LoginFilter;
import it.unipd.dei.webapp.wacar.resource.Actions;
import it.unipd.dei.webapp.wacar.resource.Car;
import it.unipd.dei.webapp.wacar.resource.Message;
import it.unipd.dei.webapp.wacar.resource.LogContext;
import it.unipd.dei.webapp.wacar.dao.ListCarDAO;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import it.unipd.dei.webapp.wacar.resource.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ListCarServlet.
 * Retrieves cars from the database and passes them to a JSP page to display the list of cars.
 *
 * @author Michele Scapinello (michele.scapinello@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
@WebServlet(name = "ListCarServlet", value = "/car_list/")
public class ListCarServlet extends AbstractDatabaseServlet {
    /**
     * Handles HTTP GET requests for listing cars.
     * Retrieves the list of cars from the database and forwards the request to the JSP page for rendering.
     *
     * @param req the HttpServletRequest object containing the request information
     * @param res the HttpServletResponse object for sending response to the client
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs while processing the request
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction(Actions.GET_ALL_CARS);

        String op = req.getRequestURI();
        LOGGER.info("op GET {}", op);

        User user = (User) req.getSession().getAttribute(LoginFilter.ACCOUNT_ATTRIBUTE);
        if (user != null) {
            req.setAttribute("accountType", user.getType());
            LogContext.setUser(user.getEmail());
        } else {
            LogContext.setUser("NOT_LOGGED");
        }

        List<Car> cars = null;
        Message m = null;

        try {
            cars = new ListCarDAO(getConnection()).access().getOutputParam();

            m = new Message("Cars successfully retrieved");

            LOGGER.info("Cars successfully retrieved from database without parameters");
        } catch (SQLException ex) {
            m = new Message("Cannot search for cars: unexpected error while accessing the database.", "E200", ex.getMessage());

            LOGGER.error("Cannot search for cars: unexpected error while accessing the database.", ex);
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
            LogContext.removeUser();
            LogContext.removeResource();
        }

        // Set the list of cars and the message as request attributes
        req.setAttribute("cars", cars);
        req.setAttribute("message", m);

        // Forward the request to the JSP page
        req.getRequestDispatcher("/jsp/list-car.jsp").forward(req, res);
    }
}

