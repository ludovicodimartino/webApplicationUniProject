package it.unipd.dei.webapp.wacar.dao;
import it.unipd.dei.webapp.wacar.resource.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GetUserByEmailDAO extends AbstractDAO<Boolean> {
    private static final String STATEMENT_USER_INFO = "SELECT id,email,password,name,surname,address FROM assessment.account WHERE email = ?;";


    private final String email;

    public GetUserByEmailDAO(final Connection con, final User user) {
        super(con);
        this.email = user.getEmail();
    }

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
