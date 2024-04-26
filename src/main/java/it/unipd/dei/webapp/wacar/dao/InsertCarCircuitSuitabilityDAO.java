package it.unipd.dei.webapp.wacar.dao;

import it.unipd.dei.webapp.wacar.resource.CarCircuitSuitability;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * This class is the data access object for inserting a new record of a car-circuit suitability in the database.
 *
 * @author Ludovico Di Martino (ludovico.dimartino@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class InsertCarCircuitSuitabilityDAO extends AbstractDAO<CarCircuitSuitability> {
    /**
     * The SQL statement to be executed.
     */
    private static final String STATEMENT = "INSERT INTO assessment.\"carCircuitSuitability\" (\"carType\", \"circuitType\") VALUES (?, ?)";

    /**
     * The {@link CarCircuitSuitability} object to be inserted into the database.
     */
    private final CarCircuitSuitability cCSuit;

    /**
     * Create a new object for inserting a new carCircuitSuitability into the database.
     *
     * @param con the connection to the database.
     * @param cCSuit the {@link CarCircuitSuitability} object to be inserted into the database.
     */
    public InsertCarCircuitSuitabilityDAO(Connection con, CarCircuitSuitability cCSuit) {
        super(con);

        if (cCSuit == null) {
            LOGGER.error("The car circuit suitability cannot be null.");
            throw new NullPointerException("The car circuit suitability cannot be null.");
        }

        this.cCSuit = cCSuit;
    }

    /**
     * Performs the actual logic needed for accessing the database.
     *
     * @throws Exception if there is any issue.
     */
    @Override
    protected void doAccess() throws Exception {
        try (PreparedStatement stmnt = con.prepareStatement(STATEMENT)) {
            stmnt.setString(1, cCSuit.getCarType());
            stmnt.setString(2, cCSuit.getCircuitType());

            stmnt.execute();
        }
    }
}