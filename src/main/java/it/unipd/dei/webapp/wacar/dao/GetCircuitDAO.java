package it.unipd.dei.webapp.wacar.dao;

import it.unipd.dei.webapp.wacar.resource.Circuit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is the data access object used to get the circuit.
 *
 * @author Ludovico Di Martino (ludovico.dimartino@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class GetCircuitDAO extends AbstractDAO<Circuit> {
    /**
     * The SQL statement to be executed.
     */
    private static final String STATEMENT = "SELECT * from assessment.circuit WHERE name=?";

    /**
     * The circuit name to be retrieved from the database.
     */
    private final String name;

    /**
     * Creates a new object for retrieving the circuit from the database.
     *
     * @param con the connection to the database.
     * @param name the circuit name to be retrieved from the database.
     */
    public GetCircuitDAO(Connection con, final String name) {
        super(con);

        if (name == null) {
            LOGGER.error("The circuit name cannot be null.");
            throw new NullPointerException("The circuit cannot be null.");
        }

        this.name = name;
    }

    /**
     * Performs the actual logic needed for accessing the database.
     *
     * @throws Exception if there is any issue.
     */
    @Override
    protected void doAccess() throws Exception {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Circuit retrievedCircuit = null;

        try {

            stmt = con.prepareStatement(STATEMENT);
            stmt.setString(1, name);

            rs = stmt.executeQuery();

            if(rs.next()){
                retrievedCircuit = new Circuit(rs.getString("name"),
                        rs.getString("type"),
                        rs.getInt("length"),
                        rs.getInt("cornersNumber"),
                        rs.getString("address"),
                        rs.getString("description"),
                        rs.getInt("lapPrice"),
                        rs.getBoolean("available"),
                        rs.getBytes("image"),
                        rs.getString("imageMediaType"));
                LOGGER.info(String.format("Circuit %s successfully retrieved from the database.", retrievedCircuit.getName()));
            } else {
                LOGGER.warn(String.format("Circuit %s not found in the database.", name));
                throw new SQLException(String.format("Circuit %s not found.", name), "NOT_FOUND");
            }

        } finally {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
        }
        this.outputParam = retrievedCircuit;
    }
}
