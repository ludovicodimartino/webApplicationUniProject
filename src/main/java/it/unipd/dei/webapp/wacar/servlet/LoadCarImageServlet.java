package it.unipd.dei.webapp.wacar.servlet;

import it.unipd.dei.webapp.wacar.dao.LoadCarImageDAO;
import it.unipd.dei.webapp.wacar.resource.Actions;
import it.unipd.dei.webapp.wacar.resource.Car;
import it.unipd.dei.webapp.wacar.resource.LogContext;
import it.unipd.dei.webapp.wacar.resource.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Servlet responsible for loading and serving the image of a car based on the provided brand and model.
 *
 * @author Ludovico Di Martino (ludovico.dimartino@studenti.unipd.it)
 * @version 1.0
 * @since 1.0
 */
public class LoadCarImageServlet extends AbstractDatabaseServlet {
    /**
     * Handles HTTP GET requests to load and serve the image of a car.
     *
     * @param req The HttpServletRequest object containing the request parameters.
     * @param res The HttpServletResponse object used to send the response.
     * @throws ServletException If the servlet encounters a servlet-specific problem.
     * @throws IOException      If an I/O error occurs while handling the request.
     */
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setAction(Actions.LOAD_CAR_IMAGE);

        User user = (User) req.getSession().getAttribute("account");
        if (user != null) {
            req.setAttribute("account", user);
            LogContext.setUser(user.getEmail());
        } else {
            LogContext.setUser("NOT_LOGGED");
        }

        // request parameter
        String brand;
        String model;

        // model
        Car car;

        try {

            // retrieves the request parameter
            brand = req.getParameter("brand");
            model = req.getParameter("model");

            LogContext.setResource(req.getRequestURI());

            // creates a new object for accessing the database and loading the photo of an employees
            car = new LoadCarImageDAO(getConnection(), brand, model).access().getOutputParam();

            res.setContentType(car.getImageMediaType());
            res.getOutputStream().write(car.getImage());
            res.getOutputStream().flush();

            LOGGER.info(String.format("Image for car %s %s successfully sent.", brand, model));


        } catch (Exception e) {
            LOGGER.error("Unable to load the car image.", e);

            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
            LogContext.removeUser();
        }

    }
}
