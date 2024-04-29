package it.unipd.dei.webapp.wacar.rest;

import it.unipd.dei.webapp.wacar.dao.ListCarDAO;
import it.unipd.dei.webapp.wacar.resource.Actions;
import it.unipd.dei.webapp.wacar.resource.Car;
import it.unipd.dei.webapp.wacar.resource.Message;
import it.unipd.dei.webapp.wacar.resource.ResourceList;
import it.unipd.dei.webapp.wacar.utils.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * A REST resource for listing {@link it.unipd.dei.webapp.wacar.resource.Car}(s)
 *
 * @author Michele Scapinello (michele.scapinello@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class ListCarsRR extends AbstractRR {

    /**
     * Creates a new REST resource for listing {@code Cars}(s).
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public ListCarsRR(final HttpServletRequest req, HttpServletResponse res, Connection con) {
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
        List<Car> cars = null;
        try {

            // creates a new DAO for accessing the database and lists the cars
            cars = new ListCarDAO(con).access().getOutputParam();

            if (cars != null) {
                LOGGER.info("Car(s) successfully listed.");
                LOGGER.info(cars.getFirst().getModel());

                res.setStatus(HttpServletResponse.SC_OK);
                new ResourceList<>(cars).toJSON(res.getOutputStream());
            } else {
                LOGGER.error("Fatal error while listing car(s).");

                m = new Message(
                        ErrorCode.UNEXPECTED_ERROR.getErrorMessage(),
                        ErrorCode.UNEXPECTED_ERROR.getErrorCode(),
                        null);

                res.setStatus(ErrorCode.UNEXPECTED_ERROR.getHTTPCode());
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error(
                    "Cannot list car(s): unexpected database error.", ex);

            m = new Message(
                    ErrorCode.UNEXPECTED_ERROR.getErrorMessage(),
                    ErrorCode.UNEXPECTED_ERROR.getErrorCode(),
                    ex.getMessage());

            res.setStatus(ErrorCode.UNEXPECTED_ERROR.getHTTPCode());
            m.toJSON(res.getOutputStream());
        }
    }
}
