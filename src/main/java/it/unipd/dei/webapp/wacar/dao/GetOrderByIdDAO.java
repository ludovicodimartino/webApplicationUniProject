package it.unipd.dei.webapp.wacar.dao;


import it.unipd.dei.webapp.wacar.resource.Circuit;
import it.unipd.dei.webapp.wacar.resource.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Insert a new order in the database.
 *
 * @author Manuel Rigobello (manuel.rigobello@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class GetOrderByIdDAO extends AbstractDAO<Order> {
    private static final String ORDER_GET_STATEMENT = "SELECT * FROM assessment.\"order\" AS o WHERE o.\"id\" = ?;";
    private final Integer orderId;

    public GetOrderByIdDAO(final Connection con, final Integer orderId) {
        super(con);

        if (orderId <= 0) {
            LOGGER.error("The order id is not valid.");
            throw new NullPointerException("The order id is not valid.");
        }

        this.orderId = orderId;
    }

    @Override
    protected void doAccess() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Order o = null;

        try {
            pstmt = con.prepareStatement(ORDER_GET_STATEMENT);
            pstmt.setInt(1, orderId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                o = new Order(rs.getInt("id"), rs.getString("account"), rs.getDate("date"), rs.getString("carBrand"), rs.getString("carModel"), rs.getString("circuit"), rs.getTimestamp("createdAt"), rs.getInt("nLaps"), rs.getInt("price"));

                LOGGER.info("Oder with id = %d has been retrieve correctly.", orderId);
            } else {
                LOGGER.warn("Order with id %d not found.", orderId);
                throw new SQLException(String.format("Order with id %d not found..", orderId), "NOT_FOUND");
            }

        } finally {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
        }

        this.outputParam = o;
    }
}
