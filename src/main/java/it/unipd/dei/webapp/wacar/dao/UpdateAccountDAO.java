package it.unipd.dei.webapp.wacar.dao;

import it.unipd.dei.webapp.wacar.resource.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * Data Access Object (DAO) for updating a user's account information in the database.
 * This DAO class executes an SQL UPDATE statement to update a user's account details,
 * such as password and address, based on the provided User object. It performs the
 * update operation and logs the success message.
 *
 * @author Alessandro Leonardi (alessandro.leonardi@studenti.unipd.it)
 */
public class UpdateAccountDAO extends AbstractDAO{
    private static final String UPDATE_STATEMENT = "UPDATE assessment.account SET password = ?, address = ? WHERE email = ?;";
    private final User user;

    /**
     * Constructs an UpdateAccountDAO object with the given Connection and User.
     *
     * @param con  The Connection object to the database.
     * @param user The User object representing the user whose account is to be updated.
     * @throws NullPointerException If the provided User object is null.
     */
    public UpdateAccountDAO(final Connection con, final User user) {
        super(con);
        if(user == null)
        {
            LOGGER.error("The user cannot be null.");
            throw new NullPointerException("The user suitability cannot be null.");
        }
        this.user = user;
    }

    @Override
    protected void doAccess() throws Exception {
        PreparedStatement stmnt = null;
        ResultSet rs = null;
        // the results of the search

        try {
            stmnt = con.prepareStatement(UPDATE_STATEMENT);
            stmnt.setString(1, user.getPassword());
            stmnt.setString(2, user.getAddress());
            stmnt.setString(3, user.getEmail());
            stmnt.execute();
            LOGGER.info("User updated {}.", user.getEmail());


        } finally {

            if (stmnt != null) {
                stmnt.close();
            }


        }
    }
}
