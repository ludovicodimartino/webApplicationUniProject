package it.unipd.dei.webapp.wacar.dao;


import it.unipd.dei.webapp.wacar.resource.Order;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Insert a new order in the database.
 *
 * @author Manuel Rigobello (manuel.rigobello@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class InsertOrderDAO extends AbstractDAO<Order> {
    private static final String WITHOUT_ID_ORDER_INSERT_STATEMENT = "INSERT INTO assessment.\"order\"(date, \"carBrand\", \"carModel\", circuit, \"createdAt\", \"nLaps\", price, account) VALUES (?, ?, ?, ?, ?, ?, ?, ?) RETURNING *;";
    private static final String WITH_ID_ORDER_INSERT_STATEMENT = "INSERT INTO assessment.\"order\"(id, date, \"carBrand\", \"carModel\", circuit, \"createdAt\", \"nLaps\", price, account) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING *;";
    private static final String ORDER_DELETE_STATEMENT = "DELETE FROM assessment.\"order\" WHERE id = ?;";
    private final Order order;

    /**
     * Creates a new InsertOrderDAO object for the insertion of a new order.
     *
     * @param con   the connection to the database.
     * @param order the new order
     */
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
        Order o = null;
        ResultSet rs = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

        try {
            if (order.getId() <= 0) {
                pstmt = con.prepareStatement(WITHOUT_ID_ORDER_INSERT_STATEMENT); // Id is set by default by PostgreSQL

                pstmt.setDate(1, order.getDate());
                pstmt.setString(2, order.getCarBrand());
                pstmt.setString(3, order.getCarModel());
                pstmt.setString(4, order.getCircuit());
                pstmt.setTimestamp(5, order.getCreatedAt());
                pstmt.setInt(6, order.getNLaps());
                pstmt.setInt(7, order.getPrice());
                pstmt.setString(8, order.getAccount());

                rs = pstmt.executeQuery();

                if (rs.next()) {
                    java.util.Date inputDate;
                    java.sql.Date date;

                    try {
                        inputDate = df.parse(rs.getString("date"));
                        date = new java.sql.Date(inputDate.getTime());

                        o = new Order(rs.getInt("id"), rs.getString("account"), date, rs.getString("carBrand"), rs.getString("carModel"), rs.getString("circuit"), rs.getTimestamp("createdAt"), rs.getInt("nLaps"), rs.getInt("price"));
        
                        LOGGER.info("New oder with id = %d has been inserted correctly.", o.getId());
                    } catch (Exception e) {
                        LOGGER.error("Invalid request: %s", e.getMessage());
                    }
                    
                }
            } else {
                pstmt = con.prepareStatement(ORDER_DELETE_STATEMENT);
                pstmt.setInt(1, order.getId());
                // User has modified one of their order so, before inserting the new one, the old order is deleted
                if (pstmt.executeUpdate() > 0) { // Delete old order
                    LOGGER.info("New oder with id = %d has been deleted correctly.", order.getAccount());
                }

                pstmt = con.prepareStatement(WITH_ID_ORDER_INSERT_STATEMENT);
                pstmt.setInt(1, order.getId());
                pstmt.setDate(2, order.getDate());
                pstmt.setString(3, order.getCarBrand());
                pstmt.setString(4, order.getCarModel());
                pstmt.setString(5, order.getCircuit());
                pstmt.setTimestamp(6, order.getCreatedAt());
                pstmt.setInt(7, order.getNLaps());
                pstmt.setInt(8, order.getPrice());
                pstmt.setString(9, order.getAccount());

                rs = pstmt.executeQuery();

                if (rs.next()) {
                    java.util.Date inputDate;
                    java.sql.Date date;

                    try {
                        inputDate = df.parse(rs.getString("date"));
                        date = new java.sql.Date(inputDate.getTime());
                        
                        o = new Order(rs.getInt("id"), rs.getString("account"), date, rs.getString("carBrand"), rs.getString("carModel"), rs.getString("circuit"), Timestamp.valueOf(rs.getString("createdAt")), rs.getInt("nLaps"), rs.getInt("price"));
        
                        LOGGER.info("New oder with id = %d has been inserted correctly.", o.getId());
                    } catch (Exception e) {
                        LOGGER.error("Invalid request: %s", e.getMessage());
                    }
                    
                }
            }

            con.commit();
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }

        outputParam = o;
    }
}
