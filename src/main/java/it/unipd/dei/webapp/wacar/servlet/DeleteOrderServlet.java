package it.unipd.dei.webapp.wacar.servlet;

import it.unipd.dei.webapp.wacar.dao.DeleteOrderDAO;
import it.unipd.dei.webapp.wacar.filter.LoginFilter;
import it.unipd.dei.webapp.wacar.resource.*;
import it.unipd.dei.webapp.wacar.utils.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

public class DeleteOrderServlet extends AbstractDatabaseServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setAction(Actions.DELETE_ORDER);
        LogContext.setResource(req.getRequestURI());
        final HttpSession session = req.getSession(false);
        Message m;

        if (session == null) {
            String errorMessage = "No session. User cannot delete order.";
            LOGGER.error(errorMessage);
            LogContext.removeIPAddress();
            LogContext.removeAction();
            throw new ServletException(errorMessage);
        }

        User user = (User) req.getSession().getAttribute(LoginFilter.ACCOUNT_ATTRIBUTE);
        if (user != null) {
            req.setAttribute("accountType", user.getType());
            LogContext.setUser(user.getEmail());
        } else {
            LogContext.setUser("NOT_LOGGED");
        }
        String orderIdStr = req.getParameter("orderId");

        try {
            int orderId = Integer.parseInt(orderIdStr);
            Order delete_order = new DeleteOrderDAO(getConnection(), orderId).access().getOutputParam();
            LOGGER.info("Order " + orderId + " has been removed");
        } catch (SQLException ex) {
            m = new Message("Error while deleting an order ", ErrorCode.CANNOT_DELETE_RESOURCE.getErrorCode(), ex.getMessage());
            LOGGER.error("Error deleting order", ex);
            req.setAttribute("message", new Message("Error deleting order. Please try again later."));
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
            LogContext.removeResource();
            res.sendRedirect("/wacar/user/listOrdersByAccount");
        }
    }
}
