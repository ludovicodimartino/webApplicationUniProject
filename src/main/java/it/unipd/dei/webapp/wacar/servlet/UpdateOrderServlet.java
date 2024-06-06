package it.unipd.dei.webapp.wacar.servlet;

import it.unipd.dei.webapp.wacar.dao.GetCircuitDAO;
import it.unipd.dei.webapp.wacar.dao.GetOrderByIdAndUserEmailDAO;
import it.unipd.dei.webapp.wacar.dao.UpdateOrderDAO;
import it.unipd.dei.webapp.wacar.resource.*;
import it.unipd.dei.webapp.wacar.utils.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * Servlet for handling HTTP GET and POST requests to update a user's order information.
 *
 * @author Filippo Galli (filippo.galli@studenti.unipd.it)
 */
public class UpdateOrderServlet extends AbstractDatabaseServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LogContext.setIPAddress(request.getRemoteAddr());
        LogContext.setAction(Actions.UPDATE_ACCOUNT);
        HttpSession session = request.getSession(false);

        if (session == null) {
            String errorMessage = "No session. User cannot modify order.";
            LOGGER.error(errorMessage);
            LogContext.removeIPAddress();
            LogContext.removeAction();
            throw new ServletException(errorMessage);
        }

        User user = (User) session.getAttribute("account");
        String orderIdStr = request.getParameter("orderId");

        try {
            int orderId = Integer.parseInt(orderIdStr);
            Order oldOrder = new GetOrderByIdAndUserEmailDAO(getConnection(), user.getEmail(), orderId).access().getOutputParam();

            if (!user.getEmail().equals(oldOrder.getAccount())) {
                String errorMessage = "No session. User cannot modify order.";
                LOGGER.error(errorMessage);
                LogContext.removeIPAddress();
                LogContext.removeAction();
                throw new ServletException(errorMessage);
            }

            LOGGER.info("Order {} date {} retrieved", oldOrder.getId(), oldOrder.getDate());

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            java.sql.Date date = new java.sql.Date(df.parse(request.getParameter("date")).getTime());
            int nLaps = Integer.parseInt(request.getParameter("nLaps"));

            java.util.Date currentDate = new java.util.Date();
            long differenceInMillis = date.getTime() - currentDate.getTime();
            long differenceInDays = TimeUnit.DAYS.convert(differenceInMillis, TimeUnit.MILLISECONDS);

            if (differenceInDays < 3) {
                handleFieldErrors(session, response, oldOrder, "Select a valid date\n");
                return;
            }

            if (nLaps < 1) {
                handleFieldErrors(session, response, oldOrder, "Insert a valid number of laps");
                return;
            }

            Circuit circuit = new GetCircuitDAO(getConnection(), oldOrder.getCircuit()).access().getOutputParam();
            int newPrice = circuit.getLapPrice() * nLaps;

            LOGGER.info("Old price: {} \nNew price: {}", oldOrder.getPrice(), newPrice);

            Order newOrder = new Order(oldOrder.getId(), date, nLaps, newPrice);
            LOGGER.info("New order info {} date {} retrieved", newOrder.getId(), newOrder.getDate());

            session.setAttribute("order", null);
            session.removeAttribute("errorMessage");
            Message m = new Message("Order successfully modified");
            session.setAttribute("order", oldOrder);
            session.setAttribute("confirmMessage", m);

            new UpdateOrderDAO(getConnection(), newOrder).access().getOutputParam();

        } catch (SQLException | ParseException | NumberFormatException e) {
            String errorMsg = e.getMessage().substring(e.getMessage().indexOf(":") + 2, e.getMessage().indexOf(" Where:"));
            Message m = new Message("Unable to update the order's information", "E5A1", e.getMessage());
            request.setAttribute("message", m);
            LOGGER.error("Unable to update the order's information: {}", errorMsg);
            throw new ServletException(e);
        }

        response.sendRedirect("/wacar/user/listOrdersByAccount");
        LogContext.removeIPAddress();
        LogContext.removeAction();
    }

    private void handleFieldErrors(HttpSession session, HttpServletResponse response, Order oldOrder, String errorMessage) throws IOException {
        ErrorCode ec = ErrorCode.EMPTY_INPUT_FIELDS;
        Message m = new Message(errorMessage, ec.getErrorCode(), ec.getErrorMessage());
        session.setAttribute("order", oldOrder);
        session.setAttribute("errorMessage", m);
        LOGGER.error("Problems with fields: {}", m.getMessage());
        response.sendRedirect("/wacar/user/listOrdersByAccount");
    }
}
