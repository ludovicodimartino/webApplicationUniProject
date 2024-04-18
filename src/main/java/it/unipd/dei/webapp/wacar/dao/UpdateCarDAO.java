package it.unipd.dei.webapp.wacar.dao;

import it.unipd.dei.webapp.wacar.resource.Car;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * This class is the data access object for updating a record of a car in the database.
 *
 * @author Ludovico Di Martino (ludovico.dimartino@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class UpdateCarDAO extends AbstractDAO{
    /**
     * The SQL statement with the image to be executed.
     */
    private static final String STATEMENT_IMG = "UPDATE assessment.car SET \"type\"=?,\"horsepower\"=?, \"0-100\"=?, \"maxSpeed\"=?, description=?, available=?, image=?, imageMediaType=? WHERE brand = ? AND model = ?";

    /**
     * The SQL statement without the image to be executed.
     */
    private static final String STATEMENT_NO_IMG = "UPDATE assessment.car SET \"type\"=?,\"horsepower\"=?, \"0-100\"=?, \"maxSpeed\"=?, description=?, available=? WHERE brand = ? AND model = ?";

    /**
     * The car to be updated.
     */
    private final Car car;

    /**
     * Creates a new object for storing the car into the database.
     *
     * @param con the connection to the database.
     * @param car the car to be stored into the database.
     */
    public UpdateCarDAO(Connection con, Car car) {
        super(con);

        if (car == null) {
            LOGGER.error("The car cannot be null.");
            throw new NullPointerException("The car cannot be null.");
        }

        this.car = car;
    }

    /**
     * Performs the actual logic needed for accessing the database.
     * If an image is in {@code car.getImage()} the image is updated, otherwise it is not.
     *
     * @throws Exception if there is any issue.
     */
    @Override
    protected void doAccess() throws Exception {
        final boolean isImage = car.getImage() != null;
        final String statement = isImage ? STATEMENT_IMG : STATEMENT_NO_IMG;

        try (PreparedStatement stmt = con.prepareStatement(statement)) {
            stmt.setString(1, car.getType());
            stmt.setInt(2, car.getHorsepower());
            stmt.setFloat(3, car.getAcceleration());
            stmt.setInt(4, car.getMaxSpeed());
            stmt.setString(5, car.getDescription());
            stmt.setBoolean(6, car.isAvailable());
            if(isImage){
                stmt.setBytes(7, car.getImage());
                stmt.setString(8, car.getImageMediaType());
                stmt.setString(9, car.getBrand());
                stmt.setString(10, car.getModel());
            } else {
                stmt.setString(7, car.getBrand());
                stmt.setString(8, car.getModel());
            }

            stmt.execute();

            LOGGER.info(String.format("Car [%s,  %s] successfully updated in the database.", car.getBrand(), car.getModel()));
        }
    }
}
