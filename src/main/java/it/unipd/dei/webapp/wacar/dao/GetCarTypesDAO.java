package it.unipd.dei.webapp.wacar.dao;

import it.unipd.dei.webapp.wacar.resource.CarType;

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
public class GetCarTypesDAO extends AbstractDAO<List<CarType>> {
    private static final String STATEMENT_CAR_TYPE = "SELECT * FROM assessment.\"carType\"";

    public GetCarTypesDAO(Connection con) {
        super(con);
    }

    @Override
    protected void doAccess() throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // the results of the search
        final List<CarType> carTypes = new ArrayList<>();

        try {
            pstmt = con.prepareStatement(STATEMENT_CAR_TYPE);

            // Execute the query
            rs = pstmt.executeQuery();

            while (rs.next()) {
                carTypes.add(new CarType(rs.getString("name")));
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
