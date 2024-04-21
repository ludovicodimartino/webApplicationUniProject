package it.unipd.dei.webapp.wacar.servlet;

import it.unipd.dei.webapp.wacar.dao.*;
import it.unipd.dei.webapp.wacar.filter.LoginFilter;
import it.unipd.dei.webapp.wacar.resource.*;

import java.io.IOException;
import java.io.PrintWriter;

import it.unipd.dei.webapp.wacar.utils.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.message.StringFormattedMessage;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.SQLException;

/**
 * Servlet to manage user login and registration
 *
 * This servlet handles user login, registration, and logout operations.
 * It provides functionality for processing GET and POST requests related
 * to user authentication and registration.
 *
 * @author Filippo Galli (filippo.galli@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
@WebServlet(name = "UserServlet", value = "/user/*")
public class UserServlet extends AbstractDatabaseServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LogContext.setIPAddress(request.getRemoteAddr());
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(LoginFilter.ACCOUNT_ATTRIBUTE);
        boolean isUserLogged = user!=null;
        if (isUserLogged) {
            LogContext.setUser(user.getEmail());
        }
        else {
            LogContext.setUser("User not logged");
        }

        LogContext.setResource(request.getRequestURI());


        String op = request.getRequestURI();
        op = op.substring(op.lastIndexOf("wacar") + 6);

        LOGGER.info("op GET {}",op);


        switch (op){

            case "login/":
                request.getSession().invalidate();
                LogContext.setAction("LOGIN");
                request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
                break;
            case "signup/":
                request.getSession().invalidate();
                LogContext.setAction("SIGNUP");
                request.getRequestDispatcher("/jsp/signup.jsp").forward(request, response);
                break;
            case "logout/":
                // logout and return to homepage
                logoutOperations(request, response);
                break;
            case "updateAccount/":
                LogContext.setAction("UPDATE ACCOUNT");
                request.getRequestDispatcher("/jsp/updateAccount.jsp").forward(request, response);
                break;
            case "user/create-order/cars":
                LogContext.setAction("CREATE_ORDER");
                request.getRequestDispatcher("user/create-order/cars").forward(request, response);
                break;
            case "user/user-info":

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
        op = op.substring(op.lastIndexOf("wacar") + 6);
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

    /**
     * Performs logout operations for the user.
     *
     * @param request  HTTP servlet request
     * @param response HTTP servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    public void logoutOperations(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(LoginFilter.ACCOUNT_ATTRIBUTE);
        if (user!=null) {
            LOGGER.info("Session found {} ",session);

            LOGGER.info("the USER {} logged out",user.getEmail());

            request.getSession().invalidate();
        }
        else{
            LOGGER.info("User NULL");
        }
        response.sendRedirect(request.getContextPath() + "/");
    }


    /**
     * Performs login operations for the user.
     *
     * @param req    HTTP servlet request
     * @param res    HTTP servlet response
     * @param isValid indicates whether the login attempt is valid
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
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
                    ErrorCode ec = ErrorCode.USER_NOT_EXISTS;
                    res.setStatus(ec.getHTTPCode());
                    m = new Message("The user does not exist",Integer.toString(ec.getErrorCode()),ec.getErrorMessage());
                    LOGGER.error("problems with user: {}", m.getMessage());
                    req.setAttribute("message", m);
                    req.getRequestDispatcher("/jsp/login.jsp").forward(req, res);
                }
                else{
                    m = new Message("Login success");
                    LOGGER.info("the user {} LOGGED IN", user.getEmail());
                    LOGGER.info("User account type: {}", user.getType());

                    // activate a session to keep the user data
                    HttpSession session = req.getSession();
                    session.setAttribute(LoginFilter.ACCOUNT_ATTRIBUTE, user);
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
                    ErrorCode ec = ErrorCode.EMAIL_MISSING;
                    res.setStatus(ec.getHTTPCode());
                    m = new Message("Insert an email",Integer.toString(ec.getErrorCode()),ec.getErrorMessage());
                    req.setAttribute("message", m);
                    LOGGER.error("problems with fields: {}", m.getMessage());

                } else if (password == null || password.equals("")) {
                    //the password was empty
                    ErrorCode ec = ErrorCode.PASSWORD_MISSING;
                    m = new Message("Insert the password",Integer.toString(ec.getErrorCode()),ec.getErrorMessage());
                    LOGGER.error("problems with fields: {}", m.getMessage());
                    req.setAttribute("message", m);
                    req.getRequestDispatcher("/jsp/login.jsp").forward(req, res);
                }
                // check password is compliant
                else if (!password.matches(regex_psw)){
                    ErrorCode ec = ErrorCode.PASSWORD_NOT_COMPLIANT;
                    m = new Message("This password is not compliant",Integer.toString(ec.getErrorCode()),ec.getErrorMessage());

                    LOGGER.error("problems with fields: {}", m.getMessage());
                    req.setAttribute("message", m);
                    req.getRequestDispatcher("/jsp/login.jsp").forward(req, res);

                }
                // check email is compliant
                else if (!email.matches(regex_email)){
                    ErrorCode ec = ErrorCode.MAIL_NOT_COMPLIANT;

                    m = new Message("This is not an email",Integer.toString(ec.getErrorCode()),ec.getErrorMessage());
                    LOGGER.error("problems with fields: {}", m.getMessage());
                    req.setAttribute("message", m);
                    req.getRequestDispatcher("/jsp/login.jsp").forward(req, res);

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
                        ErrorCode ec = ErrorCode.USER_NOT_EXISTS;
                        m = new Message("The user does not exist",Integer.toString(ec.getErrorCode()),ec.getErrorMessage());
                        LOGGER.error("problems with user: {}", m.getMessage());
                        req.setAttribute("message", m);
                        req.getRequestDispatcher("/jsp/login.jsp").forward(req, res);

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


    /**
     * Performs registration operations for a new user.
     *
     * @param req HTTP servlet request
     * @param res HTTP servlet response
     * @throws IOException if an I/O error occurs
     */
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
                ErrorCode ec = ErrorCode.EMPTY_INPUT_FIELDS;
                m = new Message("Some fields are empty", Integer.toString(ec.getErrorCode()),ec.getErrorMessage());
                LOGGER.error("problems with fields: {}", m.getMessage());
                req.setAttribute("message", m);
                req.getRequestDispatcher("/jsp/signup.jsp").forward(req, res);
            }
            // check password is compliant
            else if (!password.matches(regex_psw)) {
                ErrorCode ec = ErrorCode.PASSWORD_NOT_COMPLIANT;
                m = new Message("This password is not compliant",Integer.toString(ec.getErrorCode()),ec.getErrorMessage());

                LOGGER.error("problems with fields: {}", m.getMessage());
                req.setAttribute("message", m);
                req.getRequestDispatcher("/jsp/signup.jsp").forward(req, res);

            }
            // check email is compliant
            else if (!email.matches(regex_email)) {
                ErrorCode ec = ErrorCode.MAIL_NOT_COMPLIANT;

                m = new Message("This is not an email",Integer.toString(ec.getErrorCode()),ec.getErrorMessage());
                LOGGER.error("problems with fields: {}", m.getMessage());
                req.setAttribute("message", m);
                req.getRequestDispatcher("/jsp/signup.jsp").forward(req, res);

            }

            LOGGER.info("Checking if user already exists");
            boolean userExists = new GetUserByEmailDAO(getConnection(), new User(email)).access().getOutputParam();


            LOGGER.info("User exists: {}",userExists);

            if (userExists) {
                // the user has already signed up with this email
                LOGGER.error("Email {} already used",email);

                ErrorCode ec = ErrorCode.MAIL_ALREADY_USED;
                res.setStatus(ec.getHTTPCode());
                m = new Message("This user already exists", Integer.toString(ec.getErrorCode()),ec.getErrorMessage());
                req.setAttribute("message", m);
                req.getRequestDispatcher("/jsp/signup.jsp").forward(req, res);


            }
            else if (!fieldEmpty){
                email = email.toLowerCase();
                password = DigestUtils.md5Hex(req.getParameter("password")).toUpperCase();

                //else, create a new user resource
                User user_to_reg = new User(email, password, name, surname, address);
                //pass it to the dao to register it
                new UserRegisterDAO(getConnection(), user_to_reg).access().getOutputParam();
                LOGGER.info("REGISTERED USER {}", email);

                //if the registration ended correctly, forward the user to the
                //login service: note that, now the login service will login the user
                //and create the session. We are not redirecting the user to the
                //login page

                loginOperations(req, res, true);


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






