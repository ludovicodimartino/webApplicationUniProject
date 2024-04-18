package it.unipd.dei.webapp.wacar.dao;

import it.unipd.dei.webapp.wacar.resource.Circuit;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * This class is the data access object for updating a record of a circuit in the database.
 *
 * @author Ludovico Di Martino (ludovico.dimartino@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class UpdateCircuitDAO extends AbstractDAO{
    /**
     * The SQL statement with the image to be executed.
     */
    private static final String STATEMENT_IMG = "UPDATE assessment.circuit SET \"type\"=?,\"length\"=?, \"cornersNumber\"=?, \"address\"=?, description=?, \"lapPrice\"=?, available=?, image=?, imageMediaType=? WHERE name=?";

    /**
     * The SQL statement without the image to be executed.
     */
    private static final String STATEMENT_NO_IMG = "UPDATE assessment.circuit SET \"type\"=?,\"length\"=?, \"cornersNumber\"=?, \"address\"=?, description=?, \"lapPrice\"=?, available=? WHERE name=?";

    /**
     * The circuit to be updated.
     */
    private final Circuit circuit;

    /**
     * Creates a new object for storing the circuit into the database.
     *
     * @param con the connection to the database.
     * @param circuit the circuit to be stored into the database.
     */
    public UpdateCircuitDAO(Connection con, Circuit circuit) {
        super(con);

        if (circuit == null) {
            LOGGER.error("The circuit cannot be null.");
            throw new NullPointerException("The circuit cannot be null.");
        }

        this.circuit = circuit;
    }

    /**
     * Performs the actual logic needed for accessing the database.
     *
     * @throws Exception if there is any issue.
     */
    @Override
    protected void doAccess() throws Exception {
        final boolean isImage = circuit.getImage() != null;
        final String statement = isImage ? STATEMENT_IMG : STATEMENT_NO_IMG;

        try (PreparedStatement stmt = con.prepareStatement(statement)) {
            stmt.setString(1, circuit.getType());
            stmt.setInt(2, circuit.getLength());
            stmt.setInt(3, circuit.getCornersNumber());
            stmt.setString(4, circuit.getAddress());
            stmt.setString(5, circuit.getDescription());
            stmt.setInt(6, circuit.getLapPrice());
            stmt.setBoolean(7, circuit.getAvailable());
            if(isImage){
                stmt.setBytes(8, circuit.getImage());
                stmt.setString(9, circuit.getImageMediaType());
                stmt.setString(10, circuit.getName());
            } else {
                stmt.setString(8, circuit.getName());
            }

            stmt.execute();

            LOGGER.info(String.format("Circuit %s successfully updated in the database.", circuit.getName()));
        }
    }
}
