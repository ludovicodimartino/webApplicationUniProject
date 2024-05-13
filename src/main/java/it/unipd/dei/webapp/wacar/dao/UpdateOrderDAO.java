package it.unipd.dei.webapp.wacar.dao;


import it.unipd.dei.webapp.wacar.resource.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * update an order.
 *
 * @author Filippo Galli (filippo.galli@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class UpdateOrderDAO extends AbstractDAO<Order> {
    private static final String ORDER_UPDATE_STATEMENT = "UPDATE assessment.\"order\" SET \"date\" = ?, \"nLaps\" = ?, \"price\" = ? WHERE \"id\" = ?";

    private final Order order;

    /**
     * Constructs a new UpdateOrderDAO object with the id of the order.
     *
     * @param con the connection to the database
     * @param order the order object
     */
    public UpdateOrderDAO(final Connection con, final Order order) {
        super(con);
        if(order == null)
        {
            LOGGER.error("The user cannot be null.");
            throw new NullPointerException("The user suitability cannot be null.");
        }
        this.order = order;
    }

    @Override
    protected void doAccess() throws SQLException {
        PreparedStatement stmnt = null;
        con.setAutoCommit(false); // For an eventual rollback

        try {
            stmnt = con.prepareStatement(ORDER_UPDATE_STATEMENT);
            stmnt.setDate(1, order.getDate());
            stmnt.setInt(2, order.getNLaps());
            stmnt.setInt(3, order.getPrice());
            stmnt.setInt(4, order.getId());


            stmnt.execute();

            LOGGER.info("The order with id = %d has been updated correctly.", order.getId());

            con.commit();
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            if (stmnt != null) {
                stmnt.close();
            }
        }

    }
}
