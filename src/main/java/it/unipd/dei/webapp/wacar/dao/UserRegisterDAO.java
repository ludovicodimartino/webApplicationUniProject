package it.unipd.dei.webapp.wacar.dao;

import it.unipd.dei.webapp.wacar.resource.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * This class provides functionality for registering a user in the database.
 * @author Filippo Galli (filippo.galli@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class UserRegisterDAO extends AbstractDAO<User>{

    /**
     * The SQL statement to be executed.
     */
    private static final String STATEMENT_REGISTRATION = "INSERT INTO assessment.account(email, password, name, surname, address, type) VALUES (?, ?, ?, ?, ?, 'USER')";
    private final User user;

    /**
     * Creates a new object for retrieving the User from the database.
     *
     * @param con the connection to the database.
     * @param user the User object containing user information to be retrieved.
     */
    public UserRegisterDAO(final Connection con, final User user) {
        super(con);
        this.user = user;
    }

    /**
     * Performs the actual logic needed for accessing the database.
     *
     * @throws Exception if there is any issue.
     */
    @Override
    protected void doAccess() throws Exception {
        PreparedStatement stmnt = null;
        ResultSet rs1 = null;
        // the results of the search

        try {
            stmnt = con.prepareStatement(STATEMENT_REGISTRATION);
            stmnt.setString(1, user.getEmail());
            stmnt.setString(2, user.getPassword());
            stmnt.setString(3, user.getName());
            stmnt.setString(4, user.getSurname());
            stmnt.setString(5, user.getAddress());


            stmnt.execute();
            LOGGER.info("User registered {}.", user.getEmail());


        } finally {

            if (stmnt != null) {
                stmnt.close();
            }


        }
    }



}
