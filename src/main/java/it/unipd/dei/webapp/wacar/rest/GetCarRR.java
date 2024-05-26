package it.unipd.dei.webapp.wacar.rest;

import it.unipd.dei.webapp.wacar.dao.GetCarDAO;
import it.unipd.dei.webapp.wacar.resource.Actions;
import it.unipd.dei.webapp.wacar.resource.Car;
import it.unipd.dei.webapp.wacar.resource.LogContext;
import it.unipd.dei.webapp.wacar.resource.Message;
import it.unipd.dei.webapp.wacar.utils.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * A REST resource for listing {@link it.unipd.dei.webapp.wacar.resource.Car}(s)
 *
 * @author Manuel Rigobello (manuel.rigobello@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class GetCarRR extends AbstractRR {

    /**
     * Creates a new REST resource for listing {@code Cars}(s).
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public GetCarRR(final HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.GET_ALL_CARS, req, res, con);
    }

    /**
     * Performs the serving logic for listing cars. Retrieves a list of cars from the database
     * using a Data Access Object (DAO) and sends the JSON representation of the list to the response output stream.
     * If successful, sets the HTTP status code to SC_OK (200). If an error occurs during the database access,
     * sets the HTTP status code to SC_INTERNAL_SERVER_ERROR (500) and sends an error message JSON response.
     *
     * @throws IOException  If an error occurs while writing JSON data to the response output stream.
     */
    @Override
    protected void doServe() throws IOException {
        Message m = null;
        String brand = null;
        String model = null;
        Car car = null;

        
        try {
            // parse the URI path to extract the order id
			String path = req.getRequestURI();
			path = path.substring(path.lastIndexOf("car/") + 4);

            // path should be carBrand/carModel
            brand = path.split("/")[0].replace("%20", " ");
            model = path.split("/")[1].replace("%20", " ");

            LOGGER.info(System.out.printf("Required car is %s %s", brand, model));

            // creates a new DAO for accessing the database and get the car
            car = new GetCarDAO(con, brand, model).access().getOutputParam();

            if (car != null) {
                LOGGER.info("Car successfully retrieved.");

                res.setStatus(HttpServletResponse.SC_OK);
                car.toJSON(res.getOutputStream());
            } else {
                final String msgText = System.out.printf("Fatal error while retrieving car %s %s.", brand, model).toString();
                LOGGER.error(msgText);

                m = new Message(msgText, ErrorCode.UNEXPECTED_ERROR.getErrorCode(), null);

                res.setStatus(ErrorCode.UNEXPECTED_ERROR.getHTTPCode());
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error(
                    "Cannot retrieve car: unexpected database error.", ex);

            m = new Message(
                    ErrorCode.UNEXPECTED_ERROR.getErrorMessage(),
                    ErrorCode.UNEXPECTED_ERROR.getErrorCode(),
                    ex.getMessage());

            res.setStatus(ErrorCode.UNEXPECTED_ERROR.getHTTPCode());
            m.toJSON(res.getOutputStream());
        }
    }
}
