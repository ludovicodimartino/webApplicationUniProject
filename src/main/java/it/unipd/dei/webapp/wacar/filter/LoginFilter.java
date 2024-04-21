package it.unipd.dei.webapp.wacar.filter;

import it.unipd.dei.webapp.wacar.resource.User;
import it.unipd.dei.webapp.wacar.resource.LogContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.Base64;

/**
 * LoginFilter class is responsible for controlling access to pages that require user authentication.
 * If the user is authenticated, the filter allows the request to pass through to the requested resource
 * without modifying or saving the session. If the user is not authenticated, the filter redirects
 * the user to the login page in order to perform authentication.
 */
public class LoginFilter extends AbstractFilter {

    /**
     * A LOGGER available for all the subclasses
     */
    final static Logger LOGGER = LogManager.getLogger(LoginFilter.class);

        /**
     * The Base64 decoder
     */
    private static final Base64.Decoder DECODER = Base64.getDecoder();

    /**
     * The name of the user attribute in the session
     */
    public static final String ACCOUNT_ATTRIBUTE = "account";

    /**
     * The configuration of the filter
     */
    private FilterConfig config = null;

    /**
     * The connection pool to the database
     */
    private DataSource ds;

    @Override
    public void init(FilterConfig config) throws ServletException {
        if (config == null) {
            LOGGER.error("Filter configuration cannot be null.");
            throw new ServletException("Filter configuration cannot be null.");
        }
        this.config = config;

        // the JNDI lookup context
        InitialContext cxt;

        try {
            cxt = new InitialContext();
            ds = (DataSource) cxt.lookup("java:/comp/env/jdbc/WaCar");
        } catch (NamingException e) {
            ds = null;

            LOGGER.error("Unable to acquire the connection pool to the database.", e);

            throw new ServletException("Unable to acquire the connection pool to the database", e);
        }
    }

    /**
     * Performs the filtering of requests to control access based on user authentication.
     * If the user is authenticated, the request is allowed to pass through to the requested resource.
     * If the user is not authenticated, the filter redirects the user to the login page.
     *
     * @param req    HTTP servlet request
     * @param res    HTTP servlet response
     * @param chain  FilterChain for invoking the next filter in the chain
     * @throws IOException      if an I/O error occurs
     * @throws ServletException if a servlet-specific error occurs
     */
    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        LogContext.setIPAddress(req.getRemoteAddr());

        LOGGER.info(String.format("request URL = %s", req.getRequestURL()));

        HttpSession session = req.getSession(false);
        if (session != null) {
            User user = (User) session.getAttribute(ACCOUNT_ATTRIBUTE);

            if (user != null && user.getType().equals("USER")) {
                LOGGER.info(String.format("Session: %s", session));
                LOGGER.info(String.format("Role: %s", user.getType()));
                LogContext.setUser(user.getEmail());
                res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
                res.setHeader("Pragma", "no-cache"); // HTTP 1.0.

                chain.doFilter(req, res); // User is logged in, just continue request.
            } else {
                LOGGER.warn(String.format("It is required to be authenticated to access to the resource %s with method %s.", req.getRequestURI(), req.getMethod()));
                LOGGER.info("Forwarding the request to the login page");
                req.getRequestDispatcher("/jsp/login.jsp").forward(req,res);
            }
        } else {
            LOGGER.warn(String.format("It is required to be authenticated to access to the resource %s with method %s.", req.getRequestURI(), req.getMethod()));
            LOGGER.info("Forwarding the request to the login page");
            req.getRequestDispatcher("/jsp/login.jsp").forward(req,res);
        }
    }
}
