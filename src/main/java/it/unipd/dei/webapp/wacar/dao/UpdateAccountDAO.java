package it.unipd.dei.webapp.wacar.dao;

import it.unipd.dei.webapp.wacar.resource.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UpdateAccountDAO extends AbstractDAO{
    private static final String UPDATE_STATEMENT = "UPDATE assessment.account SET password = ?, address = ? WHERE email = ?;";
    private final User user;

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
