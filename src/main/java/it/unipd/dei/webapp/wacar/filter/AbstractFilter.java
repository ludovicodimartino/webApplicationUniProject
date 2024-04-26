package it.unipd.dei.webapp.wacar.filter;

import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpFilter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The AbstractFilter class provides a skeletal implementation of the Filter interface
 * Extending this class allows filter implementations to focus on a more specific filtering logic
 */
public class AbstractFilter extends HttpFilter {

    /**
     * A LOGGER available for all the subclasses
     */
    Logger logger;

    /**
     * Initializes the filter.
     *
     * @param config containing the filter's configuration and initialization parameters
     * @throws ServletException if an exception occurs that interrupts the filter's normal operation
     */
    @Override
    public void init(FilterConfig config) throws ServletException {
        // If you have any <init-param> in web.xml, then you could get them
        // here by config.getInitParameter("name") and assign it as field.
        logger = LogManager.getLogger(this.getClass());

    }

    /**
     * Cleans up resources associated with the filter.
     */
    @Override
    public void destroy() {
        // If you have assigned any expensive resources as field of
        // this Filter class, then you could clean/close them here.
    }


}
