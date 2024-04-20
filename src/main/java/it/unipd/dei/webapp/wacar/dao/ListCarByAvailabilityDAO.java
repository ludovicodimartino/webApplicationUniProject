package it.unipd.dei.webapp.wacar.dao;

import it.unipd.dei.webapp.wacar.resource.Car;

import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * List the cars that are available.
 *
 * @author Manuel Rigobello (manuel.rigobello@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class ListCarByAvailabilityDAO extends AbstractDAO<List<Car>> {
    private static final String STATEMENT_CAR_LIST_BY_AVAILABILITY = "SELECT c.brand, c.model, c.type, c.horsepower, c.\"0-100\", c.\"maxSpeed\", c.description FROM assessment.car as c WHERE c.available = ?";
    private final boolean available;


    /**
	 * Creates a new object for searching available cars.
	 *
	 * @param con    the connection to the database.
	 */
    public ListCarByAvailabilityDAO(final Connection con, final boolean available) {
        super(con);
        this.available = available;
    }

    @Override
    public final void doAccess() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // Result of the search
        final List<Car> cars = new ArrayList<>();

        try {
            pstmt = con.prepareStatement(STATEMENT_CAR_LIST_BY_AVAILABILITY);
            pstmt.setBoolean(1, available);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                cars.add(new Car(rs.getString("brand"), rs.getString("model"), rs.getString("description"), rs.getInt("maxSpeed"), rs.getInt("horsepower"), rs.getInt("0-100"), available, rs.getString("type"), null, ""));
            }

            if (available) {
                LOGGER.info("Available car(s) successfully listed.");
            } else {
                LOGGER.info("Not available car(s) successfully listed.");
            }
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }
        }

        this.outputParam = cars;
    }
}
