package it.unipd.dei.webapp.wacar.dao;

import it.unipd.dei.webapp.wacar.resource.Circuit;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * This class is the data access object for inserting a new record of a circuit in the database.
 *
 * @author Ludovico Di Martino (ludovico.dimartino@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class InsertCircuitDAO extends AbstractDAO<Circuit> {

    private static final String CAR_INSERT_STATEMENT = "INSERT INTO assessment.circuit (name, \"type\", length, \"cornersNumber\", address, description, \"lapPrice\", available, image, imageMediaType) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private final Circuit circuit;

    public InsertCircuitDAO(final Connection con, final Circuit circuit) {
        super(con);

        if (circuit == null) {
            LOGGER.error("The circuit cannot be null.");
            throw new NullPointerException("The circuit cannot be null.");
        }

        this.circuit = circuit;
    }

    @Override
    protected void doAccess() throws Exception {

        try (PreparedStatement stmnt = con.prepareStatement(CAR_INSERT_STATEMENT)) {
            stmnt.setString(1, circuit.getName());
            stmnt.setString(2, circuit.getType());
            stmnt.setInt(3, circuit.getLength());
            stmnt.setInt(4, circuit.getCornersNumber());
            stmnt.setString(5, circuit.getAddress());
            stmnt.setString(6, circuit.getDescription());
            stmnt.setInt(7, circuit.getLapPrice());
            stmnt.setBoolean(8, circuit.getAvailable());
            stmnt.setBytes(9, circuit.getImage());
            stmnt.setString(10, circuit.getImageMediaType());

            stmnt.execute();
            LOGGER.info("Circuit inserted: " + circuit);
        }
    }
}
