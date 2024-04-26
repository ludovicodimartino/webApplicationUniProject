package it.unipd.dei.webapp.wacar.dao;

import it.unipd.dei.webapp.wacar.resource.Type;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is the data access object used to get the car types
 *
 * @author Ludovico Di Martino (ludovico.dimartino@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class GetCarTypesDAO extends AbstractDAO<List<Type>> {
    /**
     * The SQL statement to be executed.
     */
    private static final String STATEMENT_CAR_TYPE = "SELECT * FROM assessment.\"carType\"";

    /**
     * Creates the DAO in order to get the car types.
     *
     * @param con the connection to the database.
     */
    public GetCarTypesDAO(Connection con) {
        super(con);
    }

    @Override
    protected void doAccess() throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the results of the search
        final List<Type> carTypes = new ArrayList<>();

        try {
            pstmt = con.prepareStatement(STATEMENT_CAR_TYPE);

            // Execute the query
            rs = pstmt.executeQuery();

            while (rs.next()) {
                carTypes.add(new Type(rs.getString("name")));
            }

            LOGGER.info("Car types successfully retrieved.");
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }
        }
        this.outputParam = carTypes;
    }


}
