package it.unipd.dei.webapp.wacar.dao;

import it.unipd.dei.webapp.wacar.resource.Favourite;
import it.unipd.dei.webapp.wacar.resource.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ListFavouriteDAO extends AbstractDAO{
    private static final String LIST_FAVOURITE = "SELECT * FROM assessment.favourite where email='?';";
    private final String email;
    /**
     *
     * @param con Used to access the database
     * @param favourite
     */
    public ListFavouriteDAO(final Connection con, final User user) {
        super(con);
        this.email = user.getEmail();
    }

    @Override
    protected void doAccess() throws Exception {
        PreparedStatement stmnt = null;
        ResultSet rs = null;
        // the results of the search
        final List<Favourite> favourites = new ArrayList<>();
        try {
            stmnt = con.prepareStatement(LIST_FAVOURITE);
            stmnt.setString(1,email);
            rs = stmnt.executeQuery();
            while (rs.next())
            {
                 favourites.add(new Favourite(
                         rs.getString("circuit"),
                         rs.getString("carBrand"),
                         rs.getString("carModel"),
                         rs.getString("account"),
                         rs.getTimestamp("createdAt")
                 ));
            }


            stmnt.execute();
            LOGGER.info("favorites have been shown");


        } finally {
            if(rs != null) {
                rs.close();
            }
            if(stmnt != null) {
                stmnt.close();
            }

        }
        this.outputParam = favourites;
    }
}
