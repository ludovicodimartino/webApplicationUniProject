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

    /**
     * Handles the HTTP post request to update the order of the user
     *
     * @param request  the {@code HttpServletRequest} incoming request from the client
     * @param response the {@code HttpServletResponse} response object from the server
     * @throws ServletException if any problem occurs while executing the servlet.
     * @throws IOException      if any error occurs in the client/server communication.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LogContext.setIPAddress(request.getRemoteAddr());
        LogContext.setAction(Actions.UPDATE_ACCOUNT);
        final HttpSession session = request.getSession(false);

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

            if (!Objects.equals(user.getEmail(), oldOrder.getAccount())) {
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
                ErrorCode ec = ErrorCode.PASSWORD_NOT_COMPLIANT;
                Message m = new Message("Date inserted not valid", ec.getErrorCode(), ec.getErrorMessage());
                session.setAttribute("order", oldOrder);
                LOGGER.error("problems with fields: {}", m.getMessage());
                request.setAttribute("message", m);
                request.getRequestDispatcher("/jsp/modifyOrder.jsp").forward(request, response);
                return;
            }

            Circuit circuit = new GetCircuitDAO(getConnection(), oldOrder.getCircuit()).access().getOutputParam();
            int newPrice = circuit.getLapPrice() * nLaps;

            LOGGER.info("old price: {} \nnew price: {}", oldOrder.getPrice(), newPrice);

            session.setAttribute("order", null);
            Order newOrder = new Order(oldOrder.getId(), date, nLaps, newPrice);
            LOGGER.info("New order info {} date {} retrieved", oldOrder.getId(), oldOrder.getDate());

            new UpdateOrderDAO(getConnection(), newOrder).access().getOutputParam();

        } catch (SQLException | ParseException e) {
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
}
