package it.unipd.dei.webapp.wacar.servlet;

import it.unipd.dei.webapp.wacar.dao.GetCarDAO;
import it.unipd.dei.webapp.wacar.dao.GetCircuitDAO;
import it.unipd.dei.webapp.wacar.dao.ListOrdersByEmailDAO;
import it.unipd.dei.webapp.wacar.filter.LoginFilter;
import it.unipd.dei.webapp.wacar.resource.*;
import it.unipd.dei.webapp.wacar.utils.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * Servlet to handle the listing of the orders of a specific user.
 * Retrieves orders for a user based on their email and forwards the data to a JSP page for rendering.
 *
 * @author Michele Scapinello (michele.scapinello@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */

@WebServlet(name = "ListOrdersByEmailServlet")
public class ListOrdersByEmailServlet extends AbstractDatabaseServlet {
    /**
     * Handles GET requests to retrieve and list orders for a specific user.
     *
     * @param req the HttpServletRequest object representing the client's request
     * @param res the HttpServletResponse object representing the response to be sent to the client
     * @throws IOException      if an I/O error occurs while processing the request
     * @throws ServletException if the servlet encounters difficulty while handling the request
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction(Actions.GET_ORDERS_BY_ACCOUNT);

        String op = req.getRequestURI();

        LOGGER.info("op GET {}", op);
        HttpSession session = req.getSession();

        User user = (User) req.getSession().getAttribute(LoginFilter.ACCOUNT_ATTRIBUTE);
        if (user != null) {
            req.setAttribute("accountType", user.getType());
            LogContext.setUser(user.getEmail());
        } else {
            LogContext.setUser("NOT_LOGGED");
        }

        Message m;
        List<Order> orders = null;
        Map<Integer, Car> cars = new HashMap<>();
        Map<Integer, Circuit> circuits = new HashMap<>();

        // Initialize the before_current_date and after_current_date lists
        List<Order> before_current_date = new ArrayList<>();
        List<Order> after_current_date = new ArrayList<>();

        try {
            assert user != null;
            orders = new ListOrdersByEmailDAO(getConnection(), user.getEmail()).access().getOutputParam();

            for (Order order: orders){
                Car car = new GetCarDAO(getConnection(), order.getCarBrand(), order.getCarModel()).access().getOutputParam();
                Circuit circuit = new GetCircuitDAO(getConnection(), order.getCircuit()).access().getOutputParam();
                cars.put(order.getId(), car);
                circuits.put(order.getId(), circuit);
            }

            LOGGER.info("Order size: {}", orders.size());
            LOGGER.info("User mail: {}", user.getEmail());



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

        assert orders != null;
        if (!orders.isEmpty()) {
            Date currentDate = new Date();

            ArrayList<Boolean> difference3days = new ArrayList<Boolean>();

            // Iterate over each object in the ArrayList
            for (int i = 0; i < orders.size(); i++) {
                // Get the order date from the object
                Date orderDate = orders.get(i).getDate();

                // Calculate the difference in days between the order date and the current date
                long differenceMillis = orderDate.getTime() - currentDate.getTime();

                // Check if the order date is before or after the current date
                if (differenceMillis > 0) {
                    after_current_date.add(orders.get(i));
                } else {
                    before_current_date.add(orders.get(i));
                }
            }

            after_current_date.sort(Comparator.comparing(Order::getDate));
            before_current_date.sort(Comparator.comparing(Order::getDate));

            for (Order o : after_current_date) {
                // Get the order date from the object
                Date orderDate = o.getDate();

                // Calculate the difference in days between the order date and the current date
                long differenceMillis = orderDate.getTime() - currentDate.getTime();

                long differenceDays = TimeUnit.DAYS.convert(differenceMillis, TimeUnit.MILLISECONDS);
                // Check if the difference is greater than 3 days

                difference3days.add(differenceDays > 3);
                // Debugging: Print out differenceDays and other relevant information
                LOGGER.info("Difference in days for order " + o.getId() + ": " + differenceDays);
                LOGGER.info("Is difference greater than 3 days? " + difference3days.getLast());

            }

            // Set the array as an attribute to be passed to the JSP file
            req.setAttribute("modifyAvailable", difference3days);
            req.setAttribute("after", after_current_date);
            req.setAttribute("before", before_current_date);
        }
        req.setAttribute("cars", cars);
        req.setAttribute("circuits", circuits);
        req.setAttribute("message", m);

        req.getRequestDispatcher("/jsp/list-orders.jsp").forward(req, res);
        session.removeAttribute("errorMessage");
        session.removeAttribute("confirmMessage");
    }
}
