package it.unipd.dei.webapp.wacar.dao;

import it.unipd.dei.webapp.wacar.resource.Car;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class is the data access object used to get the car.
 *
 * @author Ludovico Di Martino (ludovico.dimartino@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class GetCarDAO extends AbstractDAO<Car> {
    /**
     * The SQL statement to be executed.
     */
    private static final String STATEMENT = "SELECT * FROM assessment.car WHERE brand=? AND model=?";

    /**
     * The car brand to be retrieved from the database.
     */
    private final String brand;

    /**
     * The car model to be retrieved from the database.
     */
    private final String model;

    /**
     * Creates a new object for retrieving the car from the database.
     *
     * @param con   the connection to the database.
     * @param brand the car brand to be retrieved from the database.
     * @param model the car model to be retrieved from the database.
     */
    public GetCarDAO(Connection con, final String brand, final String model) {
        super(con);

        if (brand == null || model == null) {
            LOGGER.error("The car brand and model cannot be null.");
            throw new NullPointerException("The car brand and model cannot be null.");
        }

        this.brand = brand;
        this.model = model;
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

        Car retrievedCar = null;

        try {

            stmt = con.prepareStatement(STATEMENT);
            stmt.setString(1, brand);
            stmt.setString(2, model);

            rs = stmt.executeQuery();

            if (rs.next()) {
                retrievedCar = new Car(rs.getString("brand"),
                        rs.getString("model"),
                        rs.getString("description"),
                        rs.getInt("maxSpeed"),
                        rs.getInt("horsepower"),
                        rs.getFloat("0-100"),
                        rs.getBoolean("available"),
                        rs.getString("type"),
                        rs.getBytes("image"),
                        rs.getString("imageMediaType"));

                LOGGER.info(String.format("Car %s %s successfully retrieved from the database.", retrievedCar.getBrand(), retrievedCar.getModel()));
            } else {
                LOGGER.warn(String.format("Car %s %s not found in the database.", brand, model));
                throw new SQLException(String.format("Car %s %s not found.", brand, model), "NOT_FOUND");
            }


        } finally {
            if (rs != null)
                rs.close();
            if (stmt != null)
                stmt.close();
        }
        this.outputParam = retrievedCar;
    }
}
