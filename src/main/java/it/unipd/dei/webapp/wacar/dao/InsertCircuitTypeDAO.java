package it.unipd.dei.webapp.wacar.dao;

import it.unipd.dei.webapp.wacar.resource.Type;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * This class is the data access object for inserting a new record of a circuit type in the database.
 *
 * @author Ludovico Di Martino (ludovico.dimartino@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class InsertCircuitTypeDAO extends AbstractDAO {

    /**
     * The SQL statement to be executed.
     */
    private static final String STATEMENT = "INSERT INTO assessment.\"circuitType\" (name) VALUES (?)";

    /**
     * The circuit type to be stored into the database.
     */
    private final Type circuitType;

    /**
     * Creates a new object for storing the circuit type into the database.
     *
     * @param con the connection to the database.
     * @param circuitType the circuit type to be stored into the database.
     */
    public InsertCircuitTypeDAO(Connection con, Type circuitType) {
        super(con);

        if (circuitType == null) {
            LOGGER.error("The circuit type cannot be null.");
            throw new NullPointerException("The circuit type cannot be null.");
        }

        this.circuitType = circuitType;
    }

    /**
     * Performs the actual logic needed for accessing the database.
     *
     * @throws Exception if there is any issue.
     */
    @Override
    protected void doAccess() throws Exception {

        try (PreparedStatement stmt = con.prepareStatement(STATEMENT)) {
            stmt.setString(1, circuitType.getName());
            stmt.execute();

            LOGGER.info(String.format("Circuit Type %s successfully stored in the database.", circuitType.getName()));
        }
    }
}