package it.unipd.dei.webapp.wacar.dao;

import it.unipd.dei.webapp.wacar.resource.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserLoginDAO extends AbstractDAO<User>  {

    //TODO: aggiungere crittografia md5 sia in INSERT che in SELECT. es: AND password=md5(?)
    private static final String USER_LOGIN = "SELECT email,password,name,surname,address,type AS accountType FROM assessment.account WHERE email=? AND password=?;";

    private final User user;

    public UserLoginDAO(final Connection con, final User user) {
        super(con);
        this.user = user;
    }

    @Override
    protected void doAccess() throws Exception {
        PreparedStatement stmnt = null;
        ResultSet rs = null;
        // the results of the search
        User user_new = null;
        try {
            stmnt = con.prepareStatement(USER_LOGIN);
            stmnt.setString(1, user.getEmail());
            stmnt.setString(2, user.getPassword());
            rs = stmnt.executeQuery();



            if(rs.next()){
                String email = rs.getString("email");
                String password = rs.getString("password");
                String name = rs.getString("name");
                String surname = rs.getString("surname");
                String address = rs.getString("address");
                String accountType = rs.getString("accountType");

                //User returned after login
                user_new = new User(email, password, name, surname, address,accountType);
                LOGGER.info("User logged in {}", user_new.getEmail());

            }else{
                LOGGER.error("error logging in the user {}", user.getEmail());

            }

//            return null;



        } finally {
            if (rs != null) {
                rs.close();
            }

            if (stmnt != null) {
                stmnt.close();
            }

        }
        this.outputParam = user_new;
    }




}
