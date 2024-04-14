package it.unipd.dei.webapp.wacar.dao;

import it.unipd.dei.webapp.wacar.resource.Car;
import it.unipd.dei.webapp.wacar.resource.Favourite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class InsertFavouriteDAO extends AbstractDAO<Car> {
    private static final String FAVOURITE_INSERT_STATEMENT = "INSERT INTO assessment.favourite(circuit, \"carBrand\", \"carModel\", \"createdAt\", account) VALUES (?, ?, ?, ?, ?);";

    private final Favourite favourite;

    public InsertFavouriteDAO(final Connection con, final Favourite favourite) {
        super(con);

        if (favourite == null) {
            LOGGER.error("The favourite cannot be null.");
            throw new NullPointerException("The favourite cannot be null.");
        }

        this.favourite = favourite;
    }

    @Override
    protected void doAccess() throws SQLException {
        PreparedStatement pstmt = null;
        con.setAutoCommit(false); // For an eventual rollback

        try {
            pstmt = con.prepareStatement(FAVOURITE_INSERT_STATEMENT);

            pstmt.setString(1, favourite.getCircuit());
            pstmt.setString(2, favourite.getCarBrand());
            pstmt.setString(3, favourite.getCarModel());
            pstmt.setTimestamp(4, favourite.getCreatedAt());
            pstmt.setString(5, favourite.getAccount());

            pstmt.execute();
            LOGGER.info("Favourite with car %s %s and circuit = %s inserted.", favourite.getCarBrand(), favourite.getCarModel(), favourite.getCircuit());
        } catch (SQLException e) {
            LOGGER.error("Cannot add new favourite: " + e.getMessage());
        } finally {
            if (pstmt != null) {
                pstmt.close();
            }
        }
    }
}
