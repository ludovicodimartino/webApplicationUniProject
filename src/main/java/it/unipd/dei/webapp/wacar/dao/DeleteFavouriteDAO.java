package it.unipd.dei.webapp.wacar.dao;

import it.unipd.dei.webapp.wacar.resource.Favourite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DeleteFavouriteDAO extends AbstractDAO{
    private static final String DELETE_FAVOURITE = "DELETE FROM assessment.favourite WHERE circuit = ?, account = ?, carModel = ?, carBrand = ?;";
    private String circuit;
    private String account;
    private String carModel;
    private final Favourite favourite;
    /**
     *
     * @param con Used to access the database
     * @param favourite
     */
    public DeleteFavouriteDAO(final Connection con, Favourite favourite) {
        super(con);
        if(favourite == null){
            LOGGER.error("The Favourite cannot be null");
            throw new NullPointerException("The favourite cannot be null");
        }
        this.favourite = favourite;
    }

    @Override
    protected void doAccess() throws Exception {
        PreparedStatement stmnt = null;
        try {
            stmnt = con.prepareStatement(DELETE_FAVOURITE);
            stmnt.setString(1, favourite.getCircuit());
            stmnt.setString(2, favourite.getAccount());
            stmnt.setString(3, favourite.getCarModel());
            stmnt.setString(4, favourite.getCarBrand());
            stmnt.executeUpdate(); // Use executeUpdate for DELETE queries
            LOGGER.info("Favorite [" + favourite.getCircuit() + ", " +
                    favourite.getAccount() + ", " +
                    favourite.getCarModel() + "] has been removed");
        } finally {
            if (stmnt != null) {
                stmnt.close();
            }
        }
        // Set the deleted favourite as the outputParam
        this.outputParam = favourite;
    }

}