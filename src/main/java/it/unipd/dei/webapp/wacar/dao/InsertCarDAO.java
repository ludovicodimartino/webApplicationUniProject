package it.unipd.dei.webapp.wacar.dao;

import it.unipd.dei.webapp.wacar.resource.Car;
import it.unipd.dei.webapp.wacar.resource.CarCircuitSuitability;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * This class is the data access object for inserting a new record of a car in the database.
 *
 * @author Ludovico Di Martino (ludovico.dimartino@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class InsertCarDAO extends AbstractDAO<Car> {

    /**
     * The SQL statement to be executed.
     */
    private static final String STATEMENT = "INSERT INTO assessment.car (brand, model, \"type\", horsepower, \"0-100\", \"maxSpeed\", description, available, image, imageMediaType) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    /**
     * The {@link Car} object to be inserted into the database.
     */
    private final Car car;


    /**
     * Creates a new object for inserting a new Car into the database.
     *
     * @param con the connection to the database.
     * @param car the {@link Car} object to be inserted into the database.
     */
    public InsertCarDAO(final Connection con, final Car car) {
        super(con);

        if (car == null) {
            LOGGER.error("The car cannot be null.");
            throw new NullPointerException("The car cannot be null.");
        }

        this.car = car;
    }

    @Override
    protected void doAccess() throws Exception {

        try (PreparedStatement stmnt = con.prepareStatement(STATEMENT)) {
            stmnt.setString(1, car.getBrand());
            stmnt.setString(2, car.getModel());
            stmnt.setString(3, car.getType());
            stmnt.setInt(4, car.getHorsepower());
            stmnt.setFloat(5, car.getAcceleration());
            stmnt.setInt(6, car.getMaxSpeed());
            stmnt.setString(7, car.getDescription());
            stmnt.setBoolean(8, car.isAvailable());
            stmnt.setBytes(9, car.getImage());
            stmnt.setString(10, car.getImageMediaType());

            stmnt.execute();
            LOGGER.info("Car inserted: " + car);
        }
    }
}
