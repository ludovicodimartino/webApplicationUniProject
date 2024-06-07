package it.unipd.dei.webapp.wacar.dao;

import it.unipd.dei.webapp.wacar.resource.Favourite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GetFavouriteDAO extends AbstractDAO<Favourite> {
        /**
     * The SQL statement to be executed.
     */
    private static final String FAVOURITE_GET_STATEMENT = "SELECT * FROM assessment.favourite WHERE circuit=? AND \"carBrand\"=? AND \"carModel\"=? AND account=?";

    /**
     * The favourite to be retrieved from the database.
     */
    private final Favourite favourite;

    /**
     * Creates a new object for retrieving the favourite from the database.
     *
     * @param con the connection to the database.
     * @param favourite the favourite to be retrieved from the database.
     */
    public GetFavouriteDAO(final Connection con, final Favourite favourite) {
        super(con);

        if (favourite == null) {
            LOGGER.error("The favourite cannot be null.");
            throw new NullPointerException("The favourite cannot be null.");
        }

        this.favourite = favourite;
    }

    @Override
    protected void doAccess() throws Exception {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        Favourite retrievedFavourite = null;
        
        try {
            LOGGER.info(favourite.getAccount());
            pstmt = con.prepareStatement(FAVOURITE_GET_STATEMENT);  
            pstmt.setString(1, favourite.getCircuit());
            pstmt.setString(2, favourite.getCarBrand());
            pstmt.setString(3, favourite.getCarModel());
            pstmt.setString(4, favourite.getAccount());
            
            rs = pstmt.executeQuery();
            if(rs.next()){
                retrievedFavourite = new Favourite(
                    rs.getString("circuit"),
                    rs.getString("carBrand"),
                    rs.getString("carModel"),
                    rs.getString("account"),
                    rs.getTimestamp("createdAt")
                );
                LOGGER.info("Favourite of user %s has been found.", favourite.getAccount());
            } else {
                LOGGER.warn(String.format("Favourite with circuit %s and car %s %s of user %s is not found in the database.", favourite.getCircuit(), favourite.getCarBrand(), favourite.getCarModel(), favourite.getAccount()));
                throw new SQLException(String.format("Favourite not found."), "02000");
            }
        } finally {
            if (rs != null)
                rs.close();
            if (pstmt != null)
                pstmt.close();
        }

        outputParam = retrievedFavourite;
    }
}
