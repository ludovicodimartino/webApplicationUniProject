package it.unipd.dei.webapp.wacar.servlet;

import it.unipd.dei.webapp.wacar.dao.InsertFavouriteDAO;
import it.unipd.dei.webapp.wacar.dao.UpdateAccountDAO;
import it.unipd.dei.webapp.wacar.resource.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
public class UpdateAccountServlet extends AbstractDatabaseServlet{
 @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LogContext.setIPAddress(request.getRemoteAddr());
        LogContext.setAction(Actions.UPDATE_ACCOUNT);
        final HttpSession session = request.getSession(false);
        if (session == null) {
            LOGGER.error(String.format("No session. User cannot create a new favourite."));
            LogContext.removeIPAddress();
            LogContext.removeAction();
            throw new ServletException(String.format("No session. User cannot create a new favourite."));
        }
        User user = (User) session.getAttribute("account");
        LOGGER.info(String.format("Authorized access by %s for updating the account.", user.getEmail()));
     Message m = null;
     String password= null;
     String address = null;
     User updatedUser = null;
        try {
            password = request.getParameter("password");
            address = request.getParameter("address");
            // Create a User object with the updated information
            updatedUser = new User(user.getEmail(), password, address);
        }catch (Exception e) {
            LOGGER.error("Invalid request: ", e.getMessage());
        }
     try {
         new UpdateAccountDAO(getConnection(), updatedUser).access().getOutputParam();
     } catch (SQLException e) {
         LOGGER.error("Unable to update the account " + e.getMessage());
         }


     }

}
