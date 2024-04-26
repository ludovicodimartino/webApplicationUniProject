package it.unipd.dei.webapp.wacar.dao;
import it.unipd.dei.webapp.wacar.resource.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * This class is the data access object used to get a User object given an email.
 *
 * @author Filippo Galli (filippo.galli@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class GetUserByEmailDAO extends AbstractDAO<Boolean> {
    /**
     * The SQL statement to be executed.
     */
    private static final String STATEMENT_USER_INFO = "SELECT email,password,name,surname,address FROM assessment.account WHERE email = ?;";

    /**
     * The email address of the user to retrieve.
     */
    private final String email;

    /**
     * Creates a new object for retrieving the User from the database.
     *
     * @param con the connection to the database.
     * @param user the User object containing user information to be retrieved.
     */
    public GetUserByEmailDAO(final Connection con, final User user) {
        super(con);
        this.email = user.getEmail();
    }

    /**
     * Performs the actual logic needed for accessing the database.
     *
     * @throws Exception if there is any issue.
     */
    @Override
    protected void doAccess() throws Exception {
        PreparedStatement stmnt = null;
        ResultSet rs = null;
        boolean found = false;
        // the results of the search

        try {
            stmnt = con.prepareStatement(STATEMENT_USER_INFO);
            stmnt.setString(1, email);

            rs = stmnt.executeQuery();

            if(rs.next()){
                found = true;
                LOGGER.info("User found {}.", email);

            }else{
                LOGGER.info("User NOT found {}.", email);

            }



        } finally {
            if (rs != null) {
                rs.close();
            }

            if (stmnt != null) {
                stmnt.close();
            }

            con.close();
        }
        this.outputParam = found;
    }


}
