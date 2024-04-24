package it.unipd.dei.webapp.wacar.dao;

import it.unipd.dei.webapp.wacar.resource.Order;
import it.unipd.dei.webapp.wacar.resource.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the data access object (DAO) for retrieving orders of a specific user from the database.
 * It searches orders by the user's email and provides methods to access the retrieved order data.
 *
 * @author Michele Scapinello (michele.scapinello@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class ListOrdersByEmailDAO extends AbstractDAO<List<Order>> {
    // SQL statement to select orders by user email
    private static final String STATEMENT_ORDER_BY_EMAIL = "SELECT * FROM assessment.order WHERE account = ?;";
    // The email of the specific user
    private final String account;

    /**
     * Constructs a new ListOrdersByEmailDAO object with the given database connection and user email.
     *
     * @param con   the connection to the database
     * @param email the email of the specific user
     */
    public ListOrdersByEmailDAO(final Connection con, final String email) {
        super(con);
        this.account = email;
    }

    /**
     * Performs the access to the data source by executing the query and retrieving the orders.
     *
     * @throws Exception if an SQL exception occurs while accessing the database
     */
    @Override
    protected void doAccess() throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        final List<Order> orders = new ArrayList<>();

        try {
            pstmt = con.prepareStatement(STATEMENT_ORDER_BY_EMAIL);
            pstmt.setString(1, account);

            rs = pstmt.executeQuery();

            while (rs.next()) {
                orders.add(new Order(
                        rs.getInt("id"),
                        rs.getString("account"),
                        rs.getDate("date"),
                        rs.getString("carBrand"),
                        rs.getString("carModel"),
                        rs.getString("circuit"),
                        rs.getTimestamp("createdAt"),
                        rs.getInt("nLaps"),
                        rs.getInt("price")));
                LOGGER.info("Order(s) of user {} successfully retrieved", account);
            }
        } finally {
            if (rs != null)
                rs.close();
            if (pstmt != null)
                pstmt.close();
        }
        this.outputParam = orders;
    }
}

