package it.unipd.dei.webapp.wacar.servlet;

import it.unipd.dei.webapp.wacar.dao.LoadCircuitImageDAO;
import it.unipd.dei.webapp.wacar.resource.Actions;
import it.unipd.dei.webapp.wacar.resource.Circuit;
import it.unipd.dei.webapp.wacar.resource.LogContext;
import it.unipd.dei.webapp.wacar.resource.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Servlet responsible for loading and serving the image of a circuit based on the provided name.
 *
 * @author Michele Scapinello (michele.scapinello@studenti.unipd.it)
 * @version 1.0
 * @since 1.0
 */
public class LoadCircuitImageServlet extends AbstractDatabaseServlet {
    /**
     * Handles HTTP GET requests to load and serve the image of a circuit.
     *
     * @param req The HttpServletRequest object containing the request parameters.
     * @param res The HttpServletResponse object used to send the response.
     * @throws ServletException If the servlet encounters a servlet-specific problem.
     * @throws IOException      If an I/O error occurs while handling the request.
     */
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setAction(Actions.LOAD_CIRCUIT_IMAGE);

        User user = (User) req.getSession().getAttribute("account");
        if (user != null) {
            req.setAttribute("account", user);
            LogContext.setUser(user.getEmail());
        } else {
            LogContext.setUser("NOT_LOGGED");
        }

        String name;

        Circuit circuit;

        try {

            name = req.getParameter("name");

            LogContext.setResource(req.getRequestURI());

            circuit = new LoadCircuitImageDAO(getConnection(), name).access().getOutputParam();

            res.setContentType(circuit.getImageMediaType());
            res.getOutputStream().write(circuit.getImage());
            res.getOutputStream().flush();

            LOGGER.info(String.format("Image for circuit %s successfully sent.", name));


        } catch (Exception e) {
            LOGGER.error("Unable to load the circuit image.", e);

            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
            LogContext.removeUser();
        }

    }
}
