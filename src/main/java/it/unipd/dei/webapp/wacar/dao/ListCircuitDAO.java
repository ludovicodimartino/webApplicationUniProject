package it.unipd.dei.webapp.wacar.dao;

import it.unipd.dei.webapp.wacar.resource.Circuit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is the data access object for listing the circuits in the database
 *
 * @author Michele Scapinello (michele.scapinello@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class ListCircuitDAO extends AbstractDAO<List<Circuit>> {
    private static final String STATEMENT = "Select * FROM assessment.circuit";

    /**
     * Creates a new DAO object.
     *
     * @param con the connection to be used for accessing the database.
     */
    public ListCircuitDAO(Connection con) {
        super(con);
    }

    /**
     * Performs the access to the data source by executing the query and returning the results
     */
    @Override
    protected void doAccess() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        final List<Circuit> circuits = new ArrayList<>();

        try {
            pstmt = con.prepareStatement(STATEMENT);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                circuits.add(new Circuit(
                        rs.getString("name"),
                        rs.getString("type"),
                        rs.getInt("length"),
                        rs.getInt("cornersNumber"),
                        rs.getString("address"),
                        rs.getString("description"),
                        rs.getInt("lapPrice"),
                        rs.getBoolean("available"),
                        rs.getBytes("image"),
                        rs.getString("imageMediaType")));
            }

            LOGGER.info("All circuits successfully retrieved");

        } finally {
            if (rs != null)
                rs.close();
            if (pstmt != null)
                pstmt.close();
        }

        outputParam = circuits;
    }
}
