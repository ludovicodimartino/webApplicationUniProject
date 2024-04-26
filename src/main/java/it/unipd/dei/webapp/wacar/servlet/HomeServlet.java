package it.unipd.dei.webapp.wacar.servlet;

import it.unipd.dei.webapp.wacar.resource.LogContext;
import it.unipd.dei.webapp.wacar.resource.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * HomeServlet class serves as the controller for the home page of the application.
 * This servlet handles GET requests to the "/home" URL pattern.
 * Depending on the user's role (if logged in), it logs the current role and forwards the request to the
 * appropriate view.
 */
@WebServlet(name = "HomeServlet", value = "/home")
public class HomeServlet extends AbstractDatabaseServlet{

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LogContext.setIPAddress(request.getRemoteAddr());
        LogContext.setResource(request.getRequestURI());
        LogContext.setAction("HOME");
        String op = request.getRequestURI();
        LOGGER.info("op GET {}",op);
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("account");
        request.getRequestDispatcher("/jsp/home.jsp").forward(request, response);

        if (user!=null){

            String role = user.getType();
            LOGGER.info("Current role {}", user.getType());
            LogContext.setUser(user.getEmail());

            if (role.equals("USER")) {
                LOGGER.info("account logged is User");
            }
            else if (role.equals("ADMIN")) {
                LOGGER.info("account logged is Admin");
            }
            else {
                LOGGER.info("Role not found");

            }

        }
        else {
            LogContext.setUser("User not logged");
            LOGGER.info("USER NOT LOGGED");
        }



    }



}

