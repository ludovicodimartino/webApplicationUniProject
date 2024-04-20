package it.unipd.dei.webapp.wacar.servlet;
import it.unipd.dei.webapp.wacar.dao.LoadCircuitImageDAO;
import it.unipd.dei.webapp.wacar.resource.Actions;
import it.unipd.dei.webapp.wacar.resource.Circuit;
import it.unipd.dei.webapp.wacar.resource.LogContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class LoadCircuitImageServlet extends AbstractDatabaseServlet {
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setAction(Actions.LOAD_CIRCUIT_IMAGE);

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
