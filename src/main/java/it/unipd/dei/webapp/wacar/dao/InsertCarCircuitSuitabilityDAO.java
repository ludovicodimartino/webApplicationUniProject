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
    private static final String STATEMENT = "INSERT INTO assessment.\"carCircuitSuitability\" (\"carType\", \"circuitType\") VALUES (?, ?)";
    private final CarCircuitSuitability cCSuit;

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