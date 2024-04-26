package it.unipd.dei.webapp.wacar.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * This class protects the pages dedicated to the admin.
 * This filter intercepts incoming requests and redirects them to the login page.
 *
 * @author Filippo Galli (filippo.galli@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class AdminFilter extends AbstractFilter {

    final static Logger LOGGER = LogManager.getLogger(AdminFilter.class);

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpSession session = req.getSession(false);
        String unauthorizedURI = req.getContextPath() + "/login/";
        boolean loggedIn = session != null && session.getAttribute("role") != null && session.getAttribute("role").equals("ADMIN");

        if (loggedIn) {
            res.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1.
            res.setHeader("Pragma", "no-cache"); // HTTP 1.0.
            chain.doFilter(req, res); // User is logged in, just continue request.
        } else {
            res.sendRedirect(unauthorizedURI); // Not logged in, show login page.
        }

    }

}
