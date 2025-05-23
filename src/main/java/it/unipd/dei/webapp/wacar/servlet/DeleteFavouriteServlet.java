package it.unipd.dei.webapp.wacar.servlet;

import it.unipd.dei.webapp.wacar.dao.DeleteFavouriteDAO;
import it.unipd.dei.webapp.wacar.resource.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.message.StringFormattedMessage;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet for handling HTTP POST requests to delete a favorite from the user's list of favorites.
 * This servlet processes the incoming POST request to delete a favorite. It retrieves the necessary
 * parameters from the request, including circuit name, car brand, and car model to identify the
 * favorite to be deleted. It then attempts to delete the favorite using a DeleteFavouriteDAO object.
 * After deletion, it logs the success message and forwards the request to a JSP page for displaying
 * the updated list of favorites. If an error occurs during the deletion process, an error message
 * is set in the request attributes.
 *
 * @author Alessandro Leonardi (alessandro.leonardi@studenti.unipd.it)
 */
public class DeleteFavouriteServlet extends AbstractDatabaseServlet{

    /**
     * Handles HTTP POST requests for deleting a favorite from the user's list of favorites.
     * This method retrieves the necessary parameters from the request, including circuit name,
     * car brand, and car model to identify the favorite to be deleted. It then attempts to
     * delete the favorite using a DeleteFavouriteDAO object. After deletion, it logs the
     * success message and forwards the request to a JSP page for displaying the updated list
     * of favorites. If an error occurs during the deletion process, an error message is set
     * in the request attributes.
     *
     * @param req  The HttpServletRequest object containing the request parameters.
     * @param res  The HttpServletResponse object for sending the response.
     * @throws IOException      If an I/O error occurs while processing the request or response.
     * @throws ServletException If the servlet encounters difficulty while handling the request.
     */
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction(Actions.DELETE_FAVOURITE);
        Message m;
        Favourite favouriteToDelete = null;
        DeleteFavouriteDAO dao = null;

        HttpSession session = req.getSession();
        if (session == null) {
            LOGGER.error("No session. User cannot delete the Favourite");
            LogContext.removeIPAddress();
            LogContext.removeAction();
            throw new ServletException("No session. User cannot delete the favourite.");
        }

        User user = (User) session.getAttribute("account");
        try{
            String circuitName = req.getParameter("circuitName");
            String carBrand = req.getParameter("carBrand");
            String carModel = req.getParameter("carModel");
            favouriteToDelete = new Favourite(circuitName, carBrand, carModel, user.getEmail());
        }
        catch(Exception ex)
        {
            LOGGER.error(new StringFormattedMessage("No user in session. User cannot delete the Favourite"));
            LogContext.setUser("NOT_LOGGED");
            LogContext.removeIPAddress();
            LogContext.removeAction();
        }

        try {
            dao = new DeleteFavouriteDAO(getConnection(), favouriteToDelete);
            dao.access(); // This should delete the favourite
            Favourite deletedFavourite = (Favourite) dao.getOutputParam();
            LOGGER.info("Favorite [" + deletedFavourite.getCircuit() + ", " +
                    deletedFavourite.getAccount() + ", " +
                    deletedFavourite.getCarModel() + "] has been removed");

            // Optionally, you can set a success message in the request
            req.setAttribute("message", new Message("Favorite has been deleted successfully"));

        } catch (SQLException ex) {
            m= new Message("Error while deleting the favourite ", "E5A6", ex.getMessage());
            LOGGER.error("Error deleting favourite", ex);
            // Set an error message
            req.setAttribute("message", new Message("Error deleting favourite. Please try again later."));
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
            LogContext.removeResource();
            // Redirect to home
            res.sendRedirect("/wacar/user/list-favourite");
        }
    }
}
