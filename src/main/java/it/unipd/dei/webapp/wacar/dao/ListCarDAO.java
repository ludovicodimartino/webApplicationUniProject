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
    }

    /**
     * Executes the SQL query to retrieve all cars from the database and populates a list of Car objects with the results.
     * Each Car object is created using the data fetched from the ResultSet obtained by executing the PreparedStatement.
     * The PreparedStatement is prepared using the SQL statement defined in the STATEMENT constant.
     * Once all cars are retrieved and added to the list, the method sets the outputParam field to the list of cars.
     *
     * @throws Exception If an error occurs while accessing the database or processing the ResultSet.
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
                        rs.getInt("0-100"),
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
