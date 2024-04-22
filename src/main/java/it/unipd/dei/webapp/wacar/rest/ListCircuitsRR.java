package it.unipd.dei.webapp.wacar.rest;

import it.unipd.dei.webapp.wacar.dao.ListCircuitDAO;
import it.unipd.dei.webapp.wacar.resource.Actions;
import it.unipd.dei.webapp.wacar.resource.Circuit;
import it.unipd.dei.webapp.wacar.resource.Message;
import it.unipd.dei.webapp.wacar.resource.ResourceList;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A REST resource for listing {@link it.unipd.dei.webapp.wacar.resource.Circuit}(s)
 *
 * @author Michele Scapinello (michele.scapinello@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class ListCircuitsRR extends AbstractRR {

    /**
     * Creates a new REST resource for listing {@code Circuit}(s).
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @param con the connection to the database.
     */
    public ListCircuitsRR(final HttpServletRequest req, HttpServletResponse res, Connection con) {
        super(Actions.GET_ALL_CARS, req, res, con);
    }

    /**
     * Performs the actual logic needed to list the circuits.
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    @Override
    protected void doServe() throws IOException {
        Message m = null;
        List<Circuit> circuits = null;
        try {

            // creates a new DAO for accessing the database and lists the circuits
            circuits = new ListCircuitDAO(con).access().getOutputParam();

            if (circuits != null) {
                LOGGER.info("Circuit(s) successfully listed.");
                LOGGER.info(circuits.getFirst().getName());
                res.setStatus(HttpServletResponse.SC_OK);
                //List<Circuit> tmp_circuits = new ArrayList<>();
                new ResourceList<>(circuits).toJSON(res.getOutputStream());
            } else {
                LOGGER.error("Fatal error while listing circuits(s).");
                m = new Message(
                        "Cannot list circuit(s): unexpected error.",
                        "E5A1",
                        null);

                res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                m.toJSON(res.getOutputStream());
            }
        } catch (SQLException ex) {
            LOGGER.error(
                    "Cannot list circuit(s): unexpected database error.", ex);

            m = new Message(
                    "Cannot list circuit(s): unexpected database error.",
                    "E5A1",
                    ex.getMessage());

            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        } catch(IndexOutOfBoundsException | NullPointerException | NumberFormatException ex) {
                LOGGER.warn("Cannot list the circuits: wrong format for URI %s.", ex);

                m = new Message(
                        "Cannot list the circuits: wrong format for URI %s.",
                        "E4A7",
                        ex.getMessage());
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                m.toJSON(res.getOutputStream());
        }
    }
}

