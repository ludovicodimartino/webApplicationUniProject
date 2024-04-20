package it.unipd.dei.webapp.wacar.servlet;
import it.unipd.dei.webapp.wacar.dao.LoadCarImageDAO;
import it.unipd.dei.webapp.wacar.resource.Actions;
import it.unipd.dei.webapp.wacar.resource.Car;
import it.unipd.dei.webapp.wacar.resource.LogContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class LoadCarImageServlet extends AbstractDatabaseServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setAction(Actions.LOAD_CAR_IMAGE);

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
