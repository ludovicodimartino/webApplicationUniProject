package it.unipd.dei.webapp.wacar.dao;


import it.unipd.dei.webapp.wacar.resource.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Date;

/**
 * Insert a new order in the database.
 *
 * @author Manuel Rigobello (manuel.rigobello@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class InsertOrderDAO extends AbstractDAO<Order> {
    private static final String ORDER_INSERT_STATEMENT = "INSERT INTO assessment.\"order\"(date, \"carBrand\", \"carModel\", circuit, \"createdAt\", \"nLaps\", price, account) VALUES (?, ?, ?, ?, ?, ?, ?, ?);";

    private final Order order;

    public InsertOrderDAO(final Connection con, final Order order) {
        super(con);

        if (order == null) {
            LOGGER.error("The order cannot be null.");
            throw new NullPointerException("The order cannot be null.");
        }

        this.order = order;
    }

    @Override
    protected void doAccess() throws SQLException {
        PreparedStatement pstmt = null;
        con.setAutoCommit(false); // For an eventual rollback

        try {
            pstmt = con.prepareStatement(ORDER_INSERT_STATEMENT);

            pstmt.setDate(1, order.getDate());
            pstmt.setString(2, order.getCarBrand());
            pstmt.setString(3, order.getCarModel());
            pstmt.setString(4, order.getCircuit());
            pstmt.setTimestamp(5, order.getCreatedAt());
            pstmt.setInt(6, order.getNLaps());
            pstmt.setInt(7, order.getPrice());
            pstmt.setInt(8, order.getAccount());

            LOGGER.info("car brand: " + order.getCarBrand());
            LOGGER.info("car model: " + order.getCarModel());
            LOGGER.info("circuit: " + order.getCircuit());
            pstmt.execute();
            LOGGER.info("Order inserted: " + order);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage());
            con.rollback();
            throw e;
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }
}
