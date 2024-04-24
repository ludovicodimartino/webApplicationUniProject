package it.unipd.dei.webapp.wacar.servlet;

import it.unipd.dei.webapp.wacar.dao.UpdateAccountDAO;
import it.unipd.dei.webapp.wacar.resource.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet for handling HTTP POST requests to update a user's account information.
 * This servlet processes the incoming POST request to update the account details,
 * such as password and address, for the logged-in user. It retrieves the updated
 * information from the request parameters, creates a new User object with the updated
 * data, and then uses a UpdateAccountDAO object to update the user's account in the
 * database. If an error occurs during the update process, an error message is logged.
 *
 * @author Alessandro Leonardi (alessandro.leonardi@studenti.unipd.it)
 */
public class UpdateAccountServlet extends AbstractDatabaseServlet{
    /**
     * Handles the HTTP post request to update the account of the user
     *
     * @param request the {@code HttpServletRequest} incoming request from the client
     * @param response the {@code HttpServletResponse} response object from the server
     * @throws ServletException if any problem occurs while executing the servlet.
     * @throws IOException if any error occurs in the client/server communication.
     */
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
            LOGGER.error("Invalid request");
        }
     try {
         new UpdateAccountDAO(getConnection(), updatedUser).access().getOutputParam();
     } catch (SQLException e) {
         LOGGER.error("Unable to update the account " + e.getMessage());
         }


     }

}
