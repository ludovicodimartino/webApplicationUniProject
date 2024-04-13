package it.unipd.dei.webapp.wacar.dao;

import it.unipd.dei.webapp.wacar.resource.Circuit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Searches the circuits where a type of car can race.
 *
 * @author Manuel Rigobello (manuel.rigobello@studentiSearches the circuits in which the type of the car can race into..unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class ListCircuitByNameDAO extends AbstractDAO<Circuit> {
    private static final String STATEMENT_CIRCUIT_BY_NAME = "SELECT * FROM assessment.circuit as c WHERE c.\"name\" = ?";
    private final String name;

    /**
	 * Creates a new object for searching circuit whth the specified name.
	 *
	 * @param con    the connection to the database.
	 * @param name the name of the circuit
	 */
    public ListCircuitByNameDAO(final Connection con, final String name) {
        super(con);
        this.name = name;
    }

    @Override
    public final void doAccess() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // Result of the search
        Circuit circuit = null;

        try {
            pstmt = con.prepareStatement(STATEMENT_CIRCUIT_BY_NAME);
            pstmt.setString(1, name);

            rs = pstmt.executeQuery();

            circuit = new Circuit(rs.getString("name"), rs.getString("type"), rs.getInt("length"), rs.getInt("cornersNumber"), rs.getString("address"), rs.getString("description"), rs.getInt("lapPrice"), rs.getBytes("image"), rs.getString("imageMimeType"));
            LOGGER.info(String.format("Circuit with name %s can race successfully listed.", name));
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }
        }

        this.outputParam = circuit;
    }
}
