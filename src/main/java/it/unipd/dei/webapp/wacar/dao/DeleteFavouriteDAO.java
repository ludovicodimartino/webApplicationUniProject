package it.unipd.dei.webapp.wacar.dao;

import it.unipd.dei.webapp.wacar.resource.Favourite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Data Access Object (DAO) for deleting a favorite from the database.
 * This DAO class executes an SQL DELETE statement to remove a favorite
 * entry from the 'favourite' table in the database. It takes a Favourite
 * object as input and uses its attributes (circuit, account, car model,
 * car brand) to identify and delete the corresponding favorite.
 *
 * @author Alessandro Leonardi (alessandro.leonardi@studenti.unipd.it)
 */
public class DeleteFavouriteDAO extends AbstractDAO{
    private static final String DELETE_FAVOURITE = "DELETE FROM assessment.favourite WHERE circuit = ?, account = ?, carModel = ?, carBrand = ?;";
    private String circuit;
    private String account;
    private String carModel;
    private final Favourite favourite;

    /**
     * Constructs a DeleteFavouriteDAO object with the given Connection and Favourite.
     *
     * @param con       The Connection object to the database.
     * @param favourite The Favourite object representing the favorite to be deleted.
     * @throws NullPointerException If the provided Favourite object is null.
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