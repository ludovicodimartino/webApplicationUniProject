package it.unipd.dei.webapp.wacar.dao;

import it.unipd.dei.webapp.wacar.resource.Favourite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class DeleteFavouriteDAO extends AbstractDAO{
    private static final String DELETE_FAVOURITE = "DELETE FROM assessment.favourite WHERE circuit = '?', account ='?', carModel='?';";
    private String circuit;
    private String account;
    private String carModel;

    /**
     *
     * @param con Used to access the database
     * @param favourite
     */
    public DeleteFavouriteDAO(final Connection con, final Favourite favourite) {
        super(con);
        if(favourite == null){
            LOGGER.error("The Favourite cannot be null");
            throw new NullPointerException("The favourite cannot be null");
        }
        
    }

    @Override
    protected void doAccess() throws Exception {
        PreparedStatement stmnt = null;
        // the results of the search
        final List<Favourite> favourites = new ArrayList<>();
        try {
            stmnt = con.prepareStatement(DELETE_FAVOURITE);
            stmnt.setString(1, circuit);
            stmnt.setString(2, account);
            stmnt.setString(3, carModel);
            stmnt.execute();
            LOGGER.info("favorite ["+ circuit+" , "+ account+" , "+ carModel+"] has been removed");
        } finally {
            if(stmnt != null) {
                stmnt.close();
            }


        }
    }

}
