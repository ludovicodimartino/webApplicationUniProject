package it.unipd.dei.webapp.wacar.rest;

import it.unipd.dei.webapp.wacar.dao.InsertFavouriteDAO;
import it.unipd.dei.webapp.wacar.dao.ListCarByAvailabilityDAO;
import it.unipd.dei.webapp.wacar.dao.ListCircuitByCarTypeDAO;
import it.unipd.dei.webapp.wacar.resource.*;
import it.unipd.dei.webapp.wacar.utils.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * A REST resource for listing {@link Car}(s)
 *
 * @author Michele Scapinello (michele.scapinello@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class ListCircuitByCarTypeRR extends AbstractRR {

    /**
     * The type of car that is suitable for the retrieved circuits
     */
    String carType = null;

    /**
     * Creates a new REST resource for listing {@code Cars}(s).
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public ListCircuitByCarTypeRR(final HttpServletRequest req, HttpServletResponse res, Connection con, String carType) {
        super(Actions.CREATE_ORDER_LIST_SUITABLE_CIRCUIT, req, res, con);
        LOGGER.info("inside constructor RR");
        this.carType = carType;
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
        LOGGER.info("dentro doServe");

        Message m = null;
        List<Circuit> circuits = null;
        try {
            LOGGER.info("carType: " + carType);
            LOGGER.info("con: ");
            LOGGER.info(con);

            // creates a new DAO for accessing the database and lists the circuits
            circuits = new ListCircuitByCarTypeDAO(con, carType).access().getOutputParam();

            LOGGER.info(circuits.getFirst().getName());

            if (circuits != null) {
                LOGGER.info("Circuit(s) successfully listed.");

                res.setStatus(HttpServletResponse.SC_OK);
                new ResourceList<>(circuits).toJSON(res.getOutputStream());
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
