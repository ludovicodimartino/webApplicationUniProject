package it.unipd.dei.webapp.wacar.servlet;

import it.unipd.dei.webapp.wacar.dao.*;
import it.unipd.dei.webapp.wacar.resource.*;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.SQLException;

//TODO: meccanismo di risposta
//TODO: sistema action loggers

@WebServlet(name = "UserServlet", value = "/user/*")
public class UserServlet extends AbstractDatabaseServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LogContext.setIPAddress(request.getRemoteAddr());
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("account");
        boolean isUserLogged = user!=null;
        if (isUserLogged) {
            LogContext.setUser(user.getEmail());
        }
        else {
            LogContext.setUser("User not logged");
        }

        LogContext.setResource(request.getRequestURI());


        String op = request.getRequestURI();
        op = op.substring(op.lastIndexOf("user") + 5);

        LOGGER.info("op GET {}",op);


        switch (op){

            case "login/":
                request.getSession().invalidate();
                LogContext.setAction("LOGIN");
                request.getRequestDispatcher("/html/login.html").forward(request, response);
                break;
            case "signup/":
                request.getSession().invalidate();
                LogContext.setAction("SIGNUP");
                request.getRequestDispatcher("/html/signup.html").forward(request, response);
                break;
            case "logout/":
                // logout and return to homepage
                logoutOperations(request, response);
                break;
            case "create-order/cars":
                request.getRequestDispatcher("/create-order/cars").forward(request, response);
                break;
            case "":

                LogContext.setAction("USER INFO");
                if (isUserLogged) {
                    Message m = new Message("Login success");
                    request.getRequestDispatcher("/jsp/userPage.jsp").forward(request, response);
                    writePage(user,m,response);
                }
                else {
                    Message m = new Message("Login FAILED");
                    LOGGER.error("stacktrace {}:", m.getMessage());
                }
                break;

            default:
                Message m = new Message("An error occurred default","E200","Operation Unknown");
                LOGGER.error("stacktrace {}:", m.getMessage());
        }




    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        //take the request uri
        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setResource(req.getRequestURI());
        LogContext.setAction("LOGIN");
        String op = req.getRequestURI();

        //remove everything prior to /user/ (included) and use the remainder as
        //indicator for the required operation
        op = op.substring(op.lastIndexOf("user") + 5);
        LOGGER.info("op POST {}",op);

        switch (op){

            case "login/":
                // the requested operation is login
                loginOperations(req, res, false);
                break;

            case "signup/":
                // the requested operation is register
                registrationOperations(req, res);
                break;

            case "logout/":
                // logout and return to homepage
                logoutOperations(req, res);
                break;


            default:
                Message m = new Message("An error occurred default","E200","Operation Unknown");
                LOGGER.error("stacktrace {}:", m.getMessage());
                // the requested operation is unknown
//                writeError(res, ErrorCode.OPERATION_UNKNOWN);
        }
    }


    public void logoutOperations(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("account");
        if (user!=null) {
            LOGGER.info("Session found {} ",session);

            LOGGER.info("the STUDENT {} logged out",user.getEmail());

            request.getSession().invalidate();
        }
        else{
            LOGGER.info("User NULL");
        }
        response.sendRedirect(request.getContextPath() + "/");
    }





    public void loginOperations(HttpServletRequest req, HttpServletResponse res, boolean isValid) throws ServletException, IOException {

        User user = null;
        Message m = null;

        String regex_psw = "^(?=.*[A-Z])(?=.*[0-9]).{8,}$";
        String regex_email  = "^[a-z0-9+_.-]+@[a-z0-9.-]+\\.[a-z]{2,}$";

        try {
            //take from the request, the parameters (email and password)
            String email = req.getParameter("email");
            String password = req.getParameter("password");


            LOGGER.info("user {} is trying to login",email);

            if(isValid){
                email = email.toLowerCase();
                password = DigestUtils.md5Hex(req.getParameter("password")).toUpperCase();
                LOGGER.info("email to lower {}",email);
                User u = new User(email,password);
                // try to find the user in the database
                user = new UserLoginDAO(getConnection(),u).access().getOutputParam();
                LOGGER.info("email to lower2 {}", user);
                //the UserDAO will tell us if the email exists and the password
                //matches
                if (user == null){
                    //if not, tell it to the user
                    m = new Message("The user does not exist","E200","Missing user");
                    LOGGER.error("problems with user: {}", m.getMessage());

                }
                else{
                    m = new Message("Login success");
                    LOGGER.info("the user {} LOGGED IN", user.getEmail());
                    LOGGER.info("User account type: {}", user.getType());

                    // activate a session to keep the user data
                    HttpSession session = req.getSession();
                    session.setAttribute("account", user);
                    session.setAttribute("role", user.getType());
                    LogContext.setUser(email);

                    // login credentials were correct: we redirect the user to the homepage
                    // now the session is active and its data can be used to change the homepage
                    res.sendRedirect(req.getContextPath()+"/home");

                }
            }
            else
            {
                if (email == null || email.equals("")) {
                    //the email is null (was not set on the parameters) or an empty string
                    //notify this to the user
                    m = new Message("Insert an email","E200","Missing fields");
                    LOGGER.error("problems with fields: {}", m.getMessage());

                } else if (password == null || password.equals("")) {
                    //the password was empty

                    m = new Message("Insert the password","E200","Missing fields");
                    LOGGER.error("problems with fields: {}", m.getMessage());

                }
                // check password is compliant
                else if (!password.matches(regex_psw)){
                    m = new Message("This password is not compliant","E200","Password not compliant");

                    LOGGER.error("problems with fields: {}", m.getMessage());

                }
                // check email is compliant
                else if (!email.matches(regex_email)){
                    m = new Message("This is not an email","E200","Email not compliant");
                    LOGGER.error("problems with fields: {}", m.getMessage());

                }
                else{

                    //try to authenticate the user
                    email = email.toLowerCase();
                    password = DigestUtils.md5Hex(req.getParameter("password")).toUpperCase();
                    LOGGER.info("email to lower {}",email);
                    User u = new User(email,password);
                    // try to find the user in the database
                    user = new UserLoginDAO(getConnection(),u).access().getOutputParam();
                    LOGGER.info("email to lower2 {}", user);



                    //the UserDAO will tell us if the email exists and the password
                    //matches
                    if (user == null){
                        //if not, tell it to the user
                        m = new Message("The user does not exist","E200","Missing user");
                        LOGGER.error("problems with user: {}", m.getMessage());

                    }
                    else{
                        m = new Message("Login success");
                        LOGGER.info("the user {} LOGGED IN", user.getEmail());
                        LOGGER.info("name {}", user.getName());
                        LOGGER.info("surname {}", user.getSurname());

                        // activate a session to keep the user data
                        HttpSession session = req.getSession();
                        session.setAttribute("account", user);
                        session.setAttribute("role", user.getType());
                        LogContext.setUser(email);

                        // login credentials were correct: we redirect the user to the homepage
                        // now the session is active and its data can be used to change the homepage
                        res.sendRedirect(req.getContextPath()+"/home");


                    }
                }
            }

        } catch (SQLException e){
            m = new Message("An error occurred SQL","E200",e.getMessage());
            LOGGER.error("stacktrace:", e);
        }
        catch (NumberFormatException e){
            m = new Message("An error occurred handling numbers","E200",e.getMessage());
            LOGGER.error("stacktrace:", e);
        }

        finally{
            writePage(user,m,res);
            LogContext.removeIPAddress();
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }


    public void registrationOperations(HttpServletRequest req, HttpServletResponse res) throws IOException {

        User user = null;
        Message m = null;
        boolean fieldEmpty = false;
        try {

            //get the registration patameters
            String email = req.getParameter("email");
            String password = req.getParameter("password");
            String name = req.getParameter("name");
            String surname = req.getParameter("surname");
            String address = req.getParameter("address");

            LOGGER.info("user {} is trying to register", email);
            // regex to validate email and password
            String regex_psw = "^(?=.*[A-Z])(?=.*[0-9]).{8,}$";
            String regex_email = "^[a-z0-9+_.-]+@[a-z0-9.-]+\\.[a-z]{2,}$";

            //check that all registrations parameters have been set and are not null
            if (email == null || email.equals("") ||
                    password == null || password.equals("") ||
                    name == null || name.equals("") ||
                    address == null || address.equals("") ||
                    surname == null || surname.equals("")) {

                fieldEmpty = true;
                m = new Message("Some fields are empty", "E200", "Missing fields");
                LOGGER.error("problems with fields: {}", m.getMessage());

            }
            // check password is compliant
            else if (!password.matches(regex_psw)) {
                m = new Message("This password is not compliant", "E200", "Password not compliant");

                LOGGER.error("problems with fields: {}", m.getMessage());

            }
            // check email is compliant
            else if (!email.matches(regex_email)) {
                m = new Message("This is not an email", "E200", "Email not compliant");
                LOGGER.error("problems with fields: {}", m.getMessage());

            }

            LOGGER.info("Checking if user already exists");
            boolean userExists = new GetUserByEmailDAO(getConnection(), new User(email)).access().getOutputParam();

            if (userExists) {
                // the user has already signed up with this email
                m = new Message("This user already exists", "E200", "User already existing");
                LOGGER.error("problems with fields: {}", m.getMessage());

            }
            else if (!fieldEmpty){
                email = email.toLowerCase();
                password = DigestUtils.md5Hex(req.getParameter("password")).toUpperCase();

                //else, create a new user resource
                User user_to_reg = new User(email, password, name, surname, address);
                //pass it to the dao to register it
                new UserRegisterDAO(getConnection(), user_to_reg).access().getOutputParam();
                LOGGER.info("REGISTERED STUDENT {}", email);

                //if the registration ended correctly, forward the user to the
                //login service: note that, now the login service will login the user
                //and create the session. We are not redirecting the user to the
                //login page

                loginOperations(req, res, true);


            }
            else{
                m = new Message("At least one field  is empty", "E200", "Deadline expired");
                LOGGER.error("problems with fields: {}", m.getMessage());
            }

        } catch (SQLException | ServletException e) {
            throw new RuntimeException(e);
        }
    }

    public void writePage(User s, Message m, HttpServletResponse res) throws IOException{

        try{
            if(m == null){
                m = new Message("An error occurred - null","E200","Unknown error");
            }
            // set the MIME media type of the response
            res.setContentType("text/html; charset=utf-8");

            // get a stream to write the response
            PrintWriter out = res.getWriter();

            // write the HTML page
            out.printf("<!DOCTYPE html>%n");

            out.printf("<html lang=\"en\">%n");
            out.printf("<head>%n");
            out.printf("<meta charset=\"utf-8\">%n");
            out.printf("<title>User</title>%n");
            out.printf("</head>%n");

            out.printf("<body>%n");


            if (m.isError()) {
                out.printf("<h1>LOGIN USER - ERROR</h1>%n");
                out.printf("<hr/>%n");
                out.printf("<ul>%n");
                out.printf("<li>error code: %s</li>%n", m.getErrorCode());
                out.printf("<li>message: %s</li>%n", m.getMessage());
                out.printf("<li>details: %s</li>%n", m.getErrorDetails());
                out.printf("</ul>%n");
            } else {
                out.printf("<h1>USER PAGE - SUCCESS</h1>%n");
                out.printf("<hr/>%n");
                out.printf("<p>%s</p>%n", m.getMessage());
                out.printf("<ul>%n");
                out.printf("<li>surname: %s</li>%n", s.getSurname());
                out.printf("<li>name: %s</li>%n", s.getName());
                out.printf("<li>email: %s</li>%n", s.getEmail());
                out.printf("<li>address: %s</li>%n", s.getAddress());
                out.printf("</ul>%n");
            }

            out.printf("</body>%n");

            out.printf("</html>%n");

            // flush the output stream buffer
            out.flush();

            // close the output stream
            out.close();
        } catch (IOException ex) {
            LOGGER.error(new StringFormattedMessage("Unable to send response when logging user %d.", s.getEmail()), ex);
            throw ex;
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
            LogContext.removeResource();
        }


    }

}






