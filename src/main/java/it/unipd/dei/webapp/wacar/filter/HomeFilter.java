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
 * HomeFilter class redirects requests for HTML resources to the home page.
 *
 * This filter intercepts incoming requests and redirects them to the home page ("/home").
 * It is used to prevent direct access to HTML resources and ensure that users always start
 * from the home page.
 */
public class HomeFilter extends AbstractFilter{

    final static Logger LOGGER = LogManager.getLogger(HomeFilter.class);

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {


        LOGGER.info("Trying to access html resources. Redirecting to the home");
        // Redirect to the home
        res.sendRedirect(req.getContextPath() + "/home");


    }
}
