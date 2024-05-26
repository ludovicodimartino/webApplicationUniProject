package it.unipd.dei.webapp.wacar.rest;

import it.unipd.dei.webapp.wacar.dao.GetCircuitDAO;
import it.unipd.dei.webapp.wacar.resource.Actions;
import it.unipd.dei.webapp.wacar.resource.Circuit;
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
public class GetCircuitRR extends AbstractRR {

    /**
     * Creates a new REST resource for listing {@code Cars}(s).
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public GetCircuitRR(final HttpServletRequest req, HttpServletResponse res, Connection con) {
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
        Circuit circuit = null;

        
        try {
            // parse the URI path to extract the order id
			String path = req.getRequestURI();
			path = path.substring(path.lastIndexOf("circuit/") + 8).replace("%20", " ");

            LOGGER.info(System.out.printf("Required circuit is %s", path));

            // creates a new DAO for accessing the database and get the car
            circuit = new GetCircuitDAO(con, path).access().getOutputParam();

            if (circuit != null) {
                LOGGER.info("Circuit successfully retrieved.");

                res.setStatus(HttpServletResponse.SC_OK);
                circuit.toJSON(res.getOutputStream());
            } else {
                final String msgText = System.out.printf("Fatal error while retrieving car %s.", path).toString();
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
