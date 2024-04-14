package it.unipd.dei.webapp.wacar.dao;

import it.unipd.dei.webapp.wacar.resource.CarCircuitSuitability;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class DeleteCarCircuitSuitabilityDAO extends AbstractDAO<CarCircuitSuitability> {
    private static final String STATEMENT = "DELETE FROM assessment.\"carCircuitSuitability\" WHERE \"carType\" = ? AND \"circuitType\" = ?;";

    final CarCircuitSuitability cCSuit;

    public DeleteCarCircuitSuitabilityDAO(Connection con, CarCircuitSuitability cCSuit) {
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
