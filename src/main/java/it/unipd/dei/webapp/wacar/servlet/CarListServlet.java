package it.unipd.dei.webapp.wacar.servlet;

import it.unipd.dei.webapp.wacar.resource.Actions;
import it.unipd.dei.webapp.wacar.resource.Car;
import it.unipd.dei.webapp.wacar.resource.Message;
import it.unipd.dei.webapp.wacar.dao.ListCarDAO;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import it.unipd.dei.webapp.wacar.resource.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet to list all the cars.
 *
 * @author Michele Scapinello (michele.scapinello@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
@WebServlet(name = "CarListServlet", value = "/car_list/")
public class CarListServlet extends AbstractDatabaseServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("account");
        if (user != null) {
            req.setAttribute("account", user);
            LogContext.setUser(user.getName());
        }
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction(Actions.GET_ALL_CARS);

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
        req.getRequestDispatcher("/jsp/ListCar.jsp").forward(req, res);
    }
}

