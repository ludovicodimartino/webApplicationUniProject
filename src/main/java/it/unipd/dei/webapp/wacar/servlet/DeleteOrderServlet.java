package it.unipd.dei.webapp.wacar.servlet;

import it.unipd.dei.webapp.wacar.dao.DeleteOrderDAO;
import it.unipd.dei.webapp.wacar.resource.*;
import it.unipd.dei.webapp.wacar.utils.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

public class DeleteOrderServlet extends AbstractDatabaseServlet {

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setAction(Actions.UPDATE_ACCOUNT);
        final HttpSession session = req.getSession(false);
        Message m;

        if (session == null) {
            String errorMessage = "No session. User cannot delete order.";
            LOGGER.error(errorMessage);
            LogContext.removeIPAddress();
            LogContext.removeAction();
            throw new ServletException(errorMessage);
        }

        User user = (User) session.getAttribute("account");
        String orderIdStr = req.getParameter("orderId");

        try {
            int orderId = Integer.parseInt(orderIdStr);
            Order delete_order = new DeleteOrderDAO(getConnection(), orderId).access().getOutputParam();
            LOGGER.info("Order [" + delete_order.getAccount() + ", " +
                    delete_order.getCircuit() + ", " +
                    delete_order.getCarModel() + "] has been removed");
        } catch (SQLException ex) {
            m = new Message("Error while deleting an order ", ErrorCode.CANNOT_DELETE_RESOURCE.getErrorCode(), ex.getMessage());
            LOGGER.error("Error deleting order", ex);
            // Set an error message
            req.setAttribute("message", new Message("Error deleting order. Please try again later."));
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
            LogContext.removeResource();
            res.sendRedirect("/wacar/user/listOrdersByAccount");
        }
    }
}
