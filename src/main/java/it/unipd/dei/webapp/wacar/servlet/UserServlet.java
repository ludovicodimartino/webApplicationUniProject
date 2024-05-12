package it.unipd.dei.webapp.wacar.servlet;

import it.unipd.dei.webapp.wacar.dao.GetUserByEmailDAO;
import it.unipd.dei.webapp.wacar.dao.UserLoginDAO;
import it.unipd.dei.webapp.wacar.dao.UserRegisterDAO;
import it.unipd.dei.webapp.wacar.filter.LoginFilter;
import it.unipd.dei.webapp.wacar.resource.LogContext;
import it.unipd.dei.webapp.wacar.resource.Message;
import it.unipd.dei.webapp.wacar.resource.User;
import it.unipd.dei.webapp.wacar.utils.ErrorCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Base64;

/**
 * Servlet for managing user-related operations.
 * This servlet handles various user actions such as login, signup, logout, updating account information and creating orders.
 *
 * @author Filippo Galli (filippo.galli@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
@WebServlet(name = "UserServlet", value = "/user/*")
public class UserServlet extends AbstractDatabaseServlet {

    /**
     * The Base64 decoder
     */
    private static final Base64.Decoder DECODER = Base64.getDecoder();

    /**
     * The Base64 encoder
     */
    private static final Base64.Encoder ENCODER = Base64.getEncoder();

    /**
     * Handles HTTP GET requests for user-related operations.
     * Manages various user actions such as login, signup, logout, updating account information and creating orders.
     *
     * @param req the HttpServletRequest object containing the request information
     * @param res the HttpServletResponse object for sending response to the client
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs while processing the request
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        LogContext.setIPAddress(req.getRemoteAddr());
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute(LoginFilter.ACCOUNT_ATTRIBUTE);
        boolean isUserLogged = user!=null;
        if (isUserLogged) {
            LogContext.setUser(user.getEmail());
            req.setAttribute("accountType", user.getType());
        }
        else {
            LogContext.setUser("User not logged");
        }

        LogContext.setResource(req.getRequestURI());


        String op = req.getRequestURI();
        op = op.substring(op.lastIndexOf("wacar") + 6);

        LOGGER.info("op GET {}",op);


        switch (op){

            case "login/":
                req.getSession().invalidate();
                LogContext.setAction("LOGIN");
                req.getRequestDispatcher("/jsp/login.jsp").forward(req, res);
                break;
            case "signup/":
                req.getSession().invalidate();
                LogContext.setAction("SIGNUP");
                req.getRequestDispatcher("/jsp/signup.jsp").forward(req, res);
                break;
            case "logout/":
                // logout and return to homepage
                LogContext.setAction("LOGOUT");
                logoutOperations(req, res);
                break;
            case "updateAccount/":
                LogContext.setAction("UPDATE ACCOUNT");
                req.getRequestDispatcher("/jsp/updateAccount.jsp").forward(req, res);
                break;
            case "user/create-order/cars":
                LogContext.setAction("CREATE_ORDER");
                req.getRequestDispatcher("user/create-order/cars").forward(req, res);
                break;
            case "user/user-info":

                LogContext.setAction("USER INFO");
                if (isUserLogged) {
                    req.getRequestDispatcher("/jsp/userPage.jsp").forward(req, res);
                }
                else {
                    Message m = new Message("Login FAILED");
                    LOGGER.error("stacktrace {}:", m.getMessage());
                }
                break;

            default:
                ErrorCode err = ErrorCode.UNSUPPORTED_OPERATION;
                Message m = new Message("he operation required it's not supported",err.getErrorCode(),err.getErrorMessage());
                req.setAttribute("message", m);
                LOGGER.warn("Unsupported operation. {}", m.getMessage());
                res.setStatus(err.getHTTPCode());
        }




    }

    /**
     * Handles HTTP GET requests for user-related operations.
     * Manages various user actions such as login, signup and logout.
     *
     * @param req the HttpServletRequest object containing the request information
     * @param res the HttpServletResponse object for sending response to the client
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs while processing the request
     */
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
                Message m = new Message("Insert an email",ErrorCode.RESOURCE_NOT_FOUND.getErrorCode(),ErrorCode.RESOURCE_NOT_FOUND.getErrorMessage());
                req.setAttribute("message", m);
                LOGGER.error("problems with fields: {}", m.getMessage());
        }
    }

    /**
     * Performs logout operations for the user.
     *
     * @param request  HTTP servlet request
     * @param response HTTP servlet response
     * @throws IOException if an I/O error occurs
     */
    public void logoutOperations(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(LoginFilter.ACCOUNT_ATTRIBUTE);
        if (user != null) {
            LOGGER.info("Session found {} ", session);
            LOGGER.info("the USER {} logged out", user.getEmail());
            request.getSession().invalidate();
        } else {
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

        User user;
        Message m;

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
                    m = new Message("The user does not exist",ec.getErrorCode(),ec.getErrorMessage());
                    LOGGER.error("problems with user: {}", m.getMessage());
                    req.setAttribute("message", m);
                    req.getRequestDispatcher("/jsp/login.jsp").forward(req, res);
                }
                else{
                    m = new Message("Login success");
                    req.setAttribute("message", m);
                    LOGGER.info("the user {} LOGGED IN", user.getEmail());
                    LOGGER.info("User account type: {}", user.getType());

                    // activate a session to keep the user data
                    HttpSession session = req.getSession();
                    session.setAttribute(LoginFilter.ACCOUNT_ATTRIBUTE, user);
                    LogContext.setUser(email);

                    // Create cookie
                    String authInfo = email + ":" + password;
                    byte[] bytes = ENCODER.encode(authInfo.getBytes());

                    String token = "BASIC " + new String(bytes, StandardCharsets.UTF_16);
                    
                    LOGGER.info("Token: " + token);

                    Cookie authCookie = new Cookie("Authorization", token);
                    authCookie.setHttpOnly(true);
                    res.addCookie(authCookie);

                    // login credentials were correct: we redirect the user to the homepage
                    // now the session is active and its data can be used to change the homepage
                    res.sendRedirect(req.getContextPath()+"/home");

                }
            }
            else
            {
                if (email == null || email.isEmpty()) {
                    //the email is null (was not set on the parameters) or an empty string
                    //notify this to the user
                    m = new Message("Insert an email",ErrorCode.EMAIL_MISSING.getErrorCode(),ErrorCode.EMAIL_MISSING.getErrorMessage());
                    req.setAttribute("message", m);
                    LOGGER.error("problems with fields: {}", m.getMessage());

                } else if (password == null || password.isEmpty()) {
                    //the password was empty
                    ErrorCode ec = ErrorCode.PASSWORD_MISSING;
                    m = new Message("Insert the password",ec.getErrorCode(),ec.getErrorMessage());
                    LOGGER.error("problems with fields: {}", m.getMessage());
                    req.setAttribute("message", m);
                    req.getRequestDispatcher("/jsp/login.jsp").forward(req, res);
                }
                // check password is compliant
                else if (!password.matches(regex_psw)){
                    ErrorCode ec = ErrorCode.PASSWORD_NOT_COMPLIANT;

                    m = new Message("This password is not compliant",ec.getErrorCode(),ec.getErrorMessage());

                    LOGGER.error("problems with fields: {}", m.getMessage());
                    req.setAttribute("message", m);
                    req.getRequestDispatcher("/jsp/login.jsp").forward(req, res);

                }
                // check email is compliant
                else if (!email.matches(regex_email)){
                    ErrorCode ec = ErrorCode.MAIL_NOT_COMPLIANT;

                    m = new Message("This is not an email",ec.getErrorCode(),ec.getErrorMessage());
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
                        m = new Message("The user does not exist",ec.getErrorCode(),ec.getErrorMessage());
                        LOGGER.error("problems with user: {}", m.getMessage());
                        req.setAttribute("message", m);
                        req.getRequestDispatcher("/jsp/login.jsp").forward(req, res);

                    }
                    else{
                        m = new Message("Login success");
                        req.setAttribute("message", m);
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
            req.setAttribute("message", m);
            LOGGER.error("stacktrace:", e);
        }
        catch (NumberFormatException e){
            m = new Message("An error occurred handling numbers","E200",e.getMessage());
            req.setAttribute("message", m);
            LOGGER.error("stacktrace:", e);
        }

        finally{
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

        Message m;
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
            if (email == null || email.isEmpty() ||
                    password == null || password.isEmpty() ||
                    name == null || name.isEmpty() ||
                    address == null || address.isEmpty() ||
                    surname == null || surname.isEmpty()) {

                fieldEmpty = true;
                ErrorCode ec = ErrorCode.EMPTY_INPUT_FIELDS;
                m = new Message("Some fields are empty", ec.getErrorCode(),ec.getErrorMessage());
                LOGGER.error("problems with fields: {}", m.getMessage());
                req.setAttribute("message", m);
                req.getRequestDispatcher("/jsp/signup.jsp").forward(req, res);
            }
            // check password is compliant
            else if (!password.matches(regex_psw)) {
                ErrorCode ec = ErrorCode.PASSWORD_NOT_COMPLIANT;
                m = new Message("This password is not compliant",ec.getErrorCode(),ec.getErrorMessage());

                LOGGER.error("problems with fields: {}", m.getMessage());
                req.setAttribute("message", m);
                req.getRequestDispatcher("/jsp/signup.jsp").forward(req, res);

            }
            // check email is compliant
            else if (!email.matches(regex_email)) {
                ErrorCode ec = ErrorCode.MAIL_NOT_COMPLIANT;

                m = new Message("This is not an email",ec.getErrorCode(),ec.getErrorMessage());
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
                m = new Message("This user already exists", ec.getErrorCode(),ec.getErrorMessage());
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



}






