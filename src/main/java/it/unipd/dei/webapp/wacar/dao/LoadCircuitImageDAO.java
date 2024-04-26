package it.unipd.dei.webapp.wacar.dao;

import it.unipd.dei.webapp.wacar.resource.Circuit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class represents the data access object (DAO) for loading the image of a circuit from the database.
 * It executes a query to retrieve the image and its media type based on the given circuit name.
 *
 * @author Ludovico Di Martino (ludovico.dimartino@studenti.unipd.it)
 * @author Michele Scapinello (michele.scapinello@studenti.unipd.it)
 * @version 1.0
 * @since 1.0
 */
public class LoadCircuitImageDAO extends AbstractDAO<Circuit> {
    /**
     * The SQL statement to be executed.
     */
    private static final String STATEMENT = "SELECT image, imageMediaType FROM assessment.circuit WHERE name=?";

    /**
     * The name of the circuit.
     */
    private final String name;


    /**
     * Create a new DAO to load the circuit image.
     *
     * @param con the connection to the database
     * @param name the name of the circuit
     */
    public LoadCircuitImageDAO(final Connection con, final String name) {
        super(con);
        this.name = name;
    }

    @Override
    protected void doAccess() throws SQLException {
        PreparedStatement stmnt = null;
        ResultSet rs = null;

        Circuit circuit;

        try {
            stmnt = con.prepareStatement(STATEMENT);
            stmnt.setString(1, name);

            rs = stmnt.executeQuery();

            if (rs.next()) {
                circuit = new Circuit(
                        null,
                        null,
                        Integer.MIN_VALUE,
                        Integer.MIN_VALUE,
                        null,
                        null,
                        Integer.MIN_VALUE,
                        false,
                        rs.getBytes("image"),
                        rs.getString("imageMediaType"));

                LOGGER.info(String.format("Image for circuit %s successfully loaded.", name));
            } else {
                LOGGER.warn(String.format("Circuit %s not found.", name));
                throw new SQLException(String.format("Circuit %s not found.", name), "NOT_FOUND");
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmnt != null) {
                stmnt.close();
            }
        }

        this.outputParam = circuit;
    }
}
