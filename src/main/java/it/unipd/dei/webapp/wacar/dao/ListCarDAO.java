package it.unipd.dei.webapp.wacar.dao;

import it.unipd.dei.webapp.wacar.resource.Car;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is the data access object for listing the cars in the database
 *
 * @author Michele Scapinello (michele.scapinello@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class ListCarDAO extends AbstractDAO<List<Car>> {
    private static final String STATEMENT = "SELECT * FROM assessment.car";

    /**
     * Creates a new DAO object.
     *
     * @param con the connection to be used for accessing the database.
     */
    public ListCarDAO(Connection con) {
        super(con);
        // Possible controls over the returned Car object?
    }

    /**
     * Performs the access to the data source by executing the query and returning the results
     */
    @Override
    protected void doAccess() throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        final List<Car> cars = new ArrayList<Car>();

        try {
            pstmt = con.prepareStatement(STATEMENT);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                cars.add(new Car(
                        rs.getString("brand"),
                        rs.getString("model"),
                        rs.getString("description"),
                        rs.getInt("maxSpeed"),
                        rs.getInt("horsepower"),
                        rs.getInt("acceleration"),
                        rs.getBoolean("available"),
                        rs.getString("type"),
                        rs.getBytes("image"),
                        rs.getString("imageMediaType")));
            }

            LOGGER.info("All cars successfully retrieved");
        } finally {
            if (rs != null)
                rs.close();
            if (pstmt != null)
                pstmt.close();
        }
        this.outputParam = cars;
    }
}
