package it.unipd.dei.webapp.wacar.filter;

import it.unipd.dei.webapp.wacar.resource.User;
import it.unipd.dei.webapp.wacar.resource.LogContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * LoginFilter class is responsible for controlling access to pages that require user authentication.
 * If the user is authenticated, the filter allows the request to pass through to the requested resource
 * without modifying or saving the session. If the user is not authenticated, the filter redirects
 * the user to the login page in order to perform authentication.
 */
public class LoginFilter extends AbstractFilter {

    final static Logger LOGGER = LogManager.getLogger(LoginFilter.class);


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

        HttpSession session = req.getSession(false);
        LogContext.setAction("LoginFilter");
        String loginURI = req.getContextPath() + "/user/login/";

        boolean loggedIn = session != null && session.getAttribute("account") != null;



        if (loggedIn) {
            User user = (User) session.getAttribute("account");

            LOGGER.info("Session: {}",session);
            LOGGER.info("Role: {}",user.getType());
            LogContext.setUser(user.getEmail());
            res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
            res.setHeader("Pragma", "no-cache"); // HTTP 1.0.
            chain.doFilter(req, res); // User is logged in, just continue request.

        } else {
            LOGGER.info("Not logged in, show login page.");
            res.sendRedirect(loginURI); // Not logged in, show login page.
        }

    }

}
