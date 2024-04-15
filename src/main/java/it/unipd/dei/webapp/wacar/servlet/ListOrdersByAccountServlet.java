package it.unipd.dei.webapp.wacar.servlet;

import it.unipd.dei.webapp.wacar.dao.ListOrdersByAccountDAO;
import it.unipd.dei.webapp.wacar.resource.Actions;
import it.unipd.dei.webapp.wacar.resource.Message;
import it.unipd.dei.webapp.wacar.resource.Order;
import it.unipd.dei.webapp.wacar.resource.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet to handle the listing of the orders of a specific user.
 *
 * @author Michele Scapinello (michele.scapinello@studenti.unidp.it)
 * @version 1.00
 * @since 1.00
 */

@WebServlet(name = "ListOrdersByAccountServlet")
public class ListOrdersByAccountServlet extends AbstractDatabaseServlet{

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction(Actions.GET_ORDERS_BY_ACCOUNT);

        String op = req.getRequestURI();

        LOGGER.info("op GET {}", op);
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("account");

        Message m = null;
        List<Order> orders = null;

        try {
            orders = new ListOrdersByAccountDAO(getConnection(), user).access().getOutputParam();

            m = new Message("Orders for user successfully retrieved");
            LOGGER.info("Orders for user successfully retrieved");
        } catch (SQLException ex) {
            m = new Message("Cannot search for orders: unexpected error while accessing the database");
            LOGGER.error("Cannot search for orders: unexpected error while accessing the database.", ex);
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
            LogContext.removeResource();
        }

        req.setAttribute("orders", orders);
        req.setAttribute("message", m);

        req.getRequestDispatcher("/jsp/list-orders.jsp").forward(req, res);
    }
}
