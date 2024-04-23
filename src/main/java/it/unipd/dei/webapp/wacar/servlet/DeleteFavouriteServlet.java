package it.unipd.dei.webapp.wacar.servlet;

import it.unipd.dei.webapp.wacar.dao.DeleteFavouriteDAO;
import it.unipd.dei.webapp.wacar.dao.ListOrdersByAccountDAO;
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
public class DeleteFavouriteServlet extends AbstractDatabaseServlet{
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction(Actions.DELETE_FAVOURITE);
        String op = req.getRequestURI();
        LOGGER.info("op GET{}", op);
        final HttpSession session = req.getSession();
        if(session == null) {
            LOGGER.error(String.format("No session. User cannot delete the Favourite"));
            LogContext.removeIPAddress();
            LogContext.removeAction();
            throw new ServletException(String.format("No session. User cannot delete the favourite."));
        }
        User user = (User) session.getAttribute("account");

        // Assuming you have circuitName, carModel, and carBrand from the request
        String circuitName = req.getParameter("circuitName");
        String carModel = req.getParameter("carModel");
        String carBrand = req.getParameter("carBrand");

        Favourite favouriteToDelete = new Favourite(circuitName, carBrand, carModel, user.getEmail());

        DeleteFavouriteDAO dao = null; // Declare outside try-catch to ensure availability in finally
        Favourite deletedFavourite = null; // This will hold the deleted Favourite

        try {
            dao = new DeleteFavouriteDAO(getConnection(), favouriteToDelete);
            dao.access(); // This should delete the favourite and set the deletedFavourite
            // Cast the outputParam to Favourite
            deletedFavourite = (Favourite) dao.getOutputParam();
            LOGGER.info("Favorite [" + deletedFavourite.getCircuit() + ", " +
                    deletedFavourite.getAccount() + ", " +
                    deletedFavourite.getCarModel() + "] has been removed");
        } catch (SQLException ex) {
            // Handle SQLException
            LOGGER.error("Error deleting favourite", ex);
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }
}
