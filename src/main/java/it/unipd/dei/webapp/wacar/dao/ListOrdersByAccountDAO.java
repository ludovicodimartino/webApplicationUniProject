package it.unipd.dei.webapp.wacar.dao;

import com.oracle.wls.shaded.org.apache.xpath.operations.Or;
import it.unipd.dei.webapp.wacar.resource.Order;
import it.unipd.dei.webapp.wacar.resource.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Searches the order of a specific user.
 *
 * @author Michele Scapinello (michele.scapinello@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class ListOrdersByAccountDAO extends AbstractDAO<List<Order>> {

    private static final String STATEMENT_ORDER_BY_EMAIL = "SELECT * FROM assessment.order WHERE account = ?;";
    private final String account;

    /**
     * Creates a new object for searching circuits where a specific type of car can race.
     *
     * @param con   the connection to the database.
     * @param user  the specific user.
     */
    public ListOrdersByAccountDAO(final Connection con, final User user){
        super(con);
        this.account = user.getEmail();
    }

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
            if(pstmt != null)
                pstmt.close();
        }
        this.outputParam = orders;
    }
}

