package it.unipd.dei.webapp.wacar.dao;


import it.unipd.dei.webapp.wacar.resource.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Delete an order from the database.
 *
 * @author Manuel Rigobello (manuel.rigobello@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class DeleteOrderDAO extends AbstractDAO<Order> {
    private static final String ORDER_DELETE_STATEMENT = "DELETE FROM assessment.\"order\" WHERE id = ?;";

    private final int id;

    /**
     * Constructs a new DeleteOrderDAO object with the id of the order.
     *
     * @param con the connection to the database
     * @param id  the id of the specific order
     */
    public DeleteOrderDAO(final Connection con, final int id) {
        super(con);

        if (id < 0) {
            LOGGER.error("The order id cannot be negative.");
            throw new NullPointerException("The order id cannot be negative.");
        }
        this.id = id;
    }

    @Override
    protected void doAccess() throws SQLException {
        PreparedStatement pstmt = null;
        con.setAutoCommit(false); // For an eventual rollback

        try {
            pstmt = con.prepareStatement(ORDER_DELETE_STATEMENT);

            pstmt.setInt(1, id);

            pstmt.execute();

            LOGGER.info("The oder with id = %d has been deleted correctly.", id);

            con.commit();
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }
}
