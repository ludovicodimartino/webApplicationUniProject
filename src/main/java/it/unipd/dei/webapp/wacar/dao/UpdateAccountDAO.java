package it.unipd.dei.webapp.wacar.dao;

import it.unipd.dei.webapp.wacar.resource.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UpdateAccountDAO extends AbstractDAO{
    private static final String STATEMENT_REGISTRATION = "UPDATE assessment.account SET email= 'admin', password = 'Admin123', name = 'admin', surname = 'admin', address = 'admin1@example.com' WHERE id = '2';";
    private final User user;

    public UpdateAccountDAO(final Connection con, final User user) {
        super(con);
        this.user = user;
    }

    @Override
    protected void doAccess() throws Exception {
        PreparedStatement stmnt = null;
        ResultSet rs = null;
        // the results of the search

        try {
            stmnt = con.prepareStatement(STATEMENT_REGISTRATION);
            stmnt.setString(1, user.getEmail());
            stmnt.setString(2, user.getPassword());
            stmnt.setString(3, user.getName());
            stmnt.setString(4, user.getSurname());
            stmnt.setString(5, user.getAddress());


            stmnt.execute();
            LOGGER.info("User updated {}.", user.getEmail());


        } finally {

            if (stmnt != null) {
                stmnt.close();
            }


        }
    }
}
