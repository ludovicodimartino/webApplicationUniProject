package it.unipd.dei.webapp.wacar.dao;


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
public class GetOrderByIdAndUserEmailDAO extends AbstractDAO<Order> {
    private static final String ORDER_GET_STATEMENT = "SELECT * FROM assessment.\"order\" AS o WHERE o.\"account\" = ? AND o.\"id\" = ?;";
    private final Integer orderId;
    private final String email;

    /**
     * Constructs a new GetOrderByIdAndUserEmailDAO object with the given database connection, the user email and the order.
     *
     * @param con     the connection to the database
     * @param email   the email of the specific user
     * @param orderId the id of the specified order
     */
    public GetOrderByIdAndUserEmailDAO(final Connection con, final String email, final Integer orderId) {
        super(con);

        if (email.isEmpty() || email.isBlank()) {
            LOGGER.error("The email is not valid.");
            throw new NullPointerException("The email is not valid.");
        }

        if (orderId <= 0) {
            LOGGER.error("The order id is not valid.");
            throw new NullPointerException("The order id is not valid.");
        }

        this.orderId = orderId;
        this.email = email;
    }

    @Override
    protected void doAccess() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Order o = null;

        try {
            pstmt = con.prepareStatement(ORDER_GET_STATEMENT);
            pstmt.setString(1, email);
            pstmt.setInt(2, orderId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                o = new Order(rs.getInt("id"), rs.getString("account"), rs.getDate("date"), rs.getString("carBrand"), rs.getString("carModel"), rs.getString("circuit"), rs.getTimestamp("createdAt"), rs.getInt("nLaps"), rs.getInt("price"));

                LOGGER.info("Oder with id = %d has been retrieve correctly.", orderId);
            } else {
                LOGGER.warn("Order with id %d of account %s not found.", orderId, email);
                throw new SQLException(String.format("Order with id %d of account %s not found.", orderId, email), "NOT_FOUND");
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
