package it.unipd.dei.webapp.wacar.dao;

import it.unipd.dei.webapp.wacar.resource.Favourite;
import it.unipd.dei.webapp.wacar.resource.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) for listing a user's favorites from the database.
 * This DAO class executes a SQL query to retrieve the list of favorites
 * associated with a user's email. It constructs a list of Favourite objects
 * based on the retrieved data and sets it as the output parameter. The list
 * of favorites can then be accessed through the getOutputParam() method after
 * calling the access() method.
 *
 * @author Alessandro Leonardi (alessandro.leonardi@studenti.unipd.it)
 */
public class ListFavouriteDAO extends AbstractDAO{
    private static final String LIST_FAVOURITE = "SELECT * FROM assessment.favourite where account=?;";
    private final String email;

    /**
     * Constructs a ListFavouriteDAO object with the given Connection and User.
     *
     * @param con  The Connection object to the database.
     * @param user The User object representing the user whose favorites are to be listed.
     */
    public ListFavouriteDAO(final Connection con, final User user) {
        super(con);
        this.email = user.getEmail();
    }

    /**
     * /**
     * Executes the retrieval of user's favorites from the database.
     * This method constructs a SQL query to retrieve the user's favorites
     * based on their email. It then executes the query, constructs Favourite
     * objects from the result set, and sets the list of favorites as the output
     * parameter.

     * @throws Exception    If an SQL exception occurs while executing the query.
     */
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
