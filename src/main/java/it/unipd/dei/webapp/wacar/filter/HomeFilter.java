package it.unipd.dei.webapp.wacar.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class HomeFilter extends AbstractFilter{

    final static Logger LOGGER = LogManager.getLogger(UserFilter.class);

    @Override
    public void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {


        // Check if the request is for the home page
        if (req.getRequestURI().equals(req.getContextPath() + "/")) {
            LOGGER.info("Redirecting to the home");
            // Redirect to the home
            res.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        // Otherwise continue
        chain.doFilter(req, res);

    }
}
