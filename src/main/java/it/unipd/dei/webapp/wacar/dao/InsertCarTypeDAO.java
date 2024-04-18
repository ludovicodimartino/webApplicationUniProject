package it.unipd.dei.webapp.wacar.dao;

import it.unipd.dei.webapp.wacar.resource.Type;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * This class is the data access object for inserting a new record of a car type in the database.
 *
 * @author Ludovico Di Martino (ludovico.dimartino@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class InsertCarTypeDAO extends AbstractDAO {

    /**
     * The SQL statement to be executed.
     */
    private static final String STATEMENT = "INSERT INTO assessment.\"carType\" (name) VALUES (?)";

    /**
     * The car type to be stored into the database.
     */
    private final Type carType;

    /**
     * Creates a new object for storing the car type into the database.
     *
     * @param con the connection to the database.
     * @param carType the car type to be stored into the database.
     */
    public InsertCarTypeDAO(Connection con, Type carType) {
        super(con);

        if (carType == null) {
            LOGGER.error("The car type cannot be null.");
            throw new NullPointerException("The car type cannot be null.");
        }

        this.carType = carType;
    }

    /**
     * Performs the actual logic needed for accessing the database.
     *
     * @throws Exception if there is any issue.
     */
    @Override
    protected void doAccess() throws Exception {

        try (PreparedStatement stmt = con.prepareStatement(STATEMENT)) {
            stmt.setString(1, carType.getName());
            stmt.execute();

            LOGGER.info(String.format("Car Type %s successfully stored in the database.", carType.getName()));
        }
    }
}
