package it.unipd.dei.webapp.wacar.dao;

import it.unipd.dei.webapp.wacar.resource.CarCircuitSuitability;
import it.unipd.dei.webapp.wacar.resource.Type;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is the data access object used to get the list of car-circuit suitability.
 *
 * @author Ludovico Di Martino (ludovico.dimartino@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class ListCarCircuitSuitabilityDAO extends AbstractDAO<List<CarCircuitSuitability>> {

    private static final String STATEMENT = "SELECT * FROM assessment.\"carCircuitSuitability\"";

    public ListCarCircuitSuitabilityDAO(Connection con) {
        super(con);
    }

    /**
     * Performs the actual logic needed for accessing the database.
     *
     * @throws Exception if there is any issue.
     */
    @Override
    protected void doAccess() throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the results of the search
        final List<CarCircuitSuitability> cCSuit = new ArrayList<>();

        try {
            pstmt = con.prepareStatement(STATEMENT);

            // Execute the query
            rs = pstmt.executeQuery();

            while (rs.next()) {
                cCSuit.add(new CarCircuitSuitability(rs.getString("carType"), rs.getString("circuitType")));
            }

            LOGGER.info("Car-circuit suitability list successfully retrieved.");
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }
        }
        this.outputParam = cCSuit;
    }
}
