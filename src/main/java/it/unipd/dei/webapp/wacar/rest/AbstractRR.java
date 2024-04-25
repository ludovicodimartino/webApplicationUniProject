/*
 * Copyright 2018-2023 University of Padua, Italy
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.unipd.dei.webapp.wacar.rest;

import it.unipd.dei.webapp.wacar.dao.UserLoginDAO;
import it.unipd.dei.webapp.wacar.resource.Actions;
import it.unipd.dei.webapp.wacar.resource.LogContext;
import it.unipd.dei.webapp.wacar.resource.Message;
import it.unipd.dei.webapp.wacar.resource.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.message.StringFormatterMessageFactory;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Base64;

/**
 * Represents a generic REST resource.
 *
 * @author Manuel Rigobello (manuel.rigobello@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public abstract class AbstractRR implements RestResource {

    /**
     * A LOGGER available for all the subclasses.
     */
    protected static final Logger LOGGER = LogManager.getLogger(AbstractRR.class,
            StringFormatterMessageFactory.INSTANCE);

    /**
     * The Base64 decoder
     */
    private static final Base64.Decoder DECODER = Base64.getDecoder();

    /**
     * The connection pool to the database
     */
    private DataSource ds;

    /**
     * The JSON MIME media type
     */
    protected static final String JSON_MEDIA_TYPE = "application/json";

    /**
     * The JSON UTF-8 MIME media type
     */
    protected static final String JSON_UTF_8_MEDIA_TYPE = "application/json; charset=utf-8";

    /**
     * The any MIME media type
     */
    protected static final String ALL_MEDIA_TYPE = "*/*";

    /**
     * The HTTP request
     */
    protected final HttpServletRequest req;

    /**
     * The HTTP response
     */
    protected final HttpServletResponse res;

    /**
     * The connection to the database
     */
    protected final Connection con;

    /**
     * The {@link Actions} performed by this REST resource.
     */
    private final String action;

    /**
     * Creates a new REST resource.
     *
     * @param action the action performed by this REST resource.
     * @param req    the HTTP request.
     * @param res    the HTTP response.
     * @param con    the connection to the database.
     */
    protected AbstractRR(final String action, final HttpServletRequest req, final HttpServletResponse res, final Connection con) {

        if (action == null || action.isBlank()) {
            LOGGER.warn("Action is null or empty.");
        }
        this.action = action;
        LogContext.setAction(action);

        if (req == null) {
            LOGGER.error("The HTTP request cannot be null.");
            throw new NullPointerException("The HTTP request cannot be null.");
        }
        this.req = req;

        if (res == null) {
            LOGGER.error("The HTTP response cannot be null.");
            throw new NullPointerException("The HTTP response cannot be null.");
        }
        this.res = res;

        if (con == null) {
            LOGGER.error("The connection cannot be null.");
            throw new NullPointerException("The connection cannot be null.");
        }
        this.con = con;
    }

    /**
     * Serves the REST request. Checks if the request method and/or MIME media type are allowed,
     * and if not, returns without further processing. The appropriate error message is sent by the
     * {@code checkMethodMediaType} method. If processing the request encounters an exception,
     * an error message is logged, and a corresponding error message JSON response is sent.
     * Finally, removes action and resource log contexts.
     *
     * @throws IOException If an I/O error occurs while serving the REST request.
     */
    @Override
    public void serve() throws IOException {

        try {
            // check if there exists a valid authorization header
            // if it exists, check if the user is of type "USER"
            // otherwise, prevent to send the response
            if (!checkIsLoggedIn(req, res)) {
                return;
            }

            // if the request method and/or the MIME media type are not allowed, return.
            // Appropriate error message sent by {@code checkMethodMediaType}
            if (!checkMethodMediaType(req, res)) {
                return;
            }

            doServe();
        } catch (Throwable t) {
            LOGGER.error("Unable to serve the REST request.", t);

            final Message m = new Message(String.format("Unable to serve the REST request: %s.", action), "E5A1",
                    t.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());
        } finally {
            LogContext.removeAction();
            LogContext.removeResource();
        }
    }

    /**
     * Performs the actual logic needed for serving the REST request.
     * <p>
     * Subclasses have to implement this method in order to define the actual strategy for serving the REST request.
     *
     * @throws IOException if any error occurs in the client/server communication.
     */
    protected abstract void doServe() throws IOException;

    /**
     * Checks that the request method and MIME media type are allowed.
     * <p>
     * Subclasses may override it to customize their behaviour, e.g. not limiting the MIME media types to JSON.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @return {@code true} if the request method and the MIME type are allowed; {@code false} otherwise.
     * @throws IOException if any error occurs in the client/server communication.
     */
    protected boolean checkMethodMediaType(final HttpServletRequest req, final HttpServletResponse res) throws
            IOException {

        final String method = req.getMethod();
        final String contentType = req.getHeader("Content-Type");
        final String accept = req.getHeader("Accept");
        final OutputStream out = res.getOutputStream();

        Message m = null;

        if (accept == null) {
            LOGGER.error("Output media type not specified. Accept request header missing.");
            m = new Message("Output media type not specified.", "E4A1", "Accept request header missing.");
            res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            m.toJSON(out);
            return false;
        }

        if (!accept.contains(JSON_MEDIA_TYPE) && !accept.equals(ALL_MEDIA_TYPE)) {
            LOGGER.error(
                    "Unsupported output media type. Resources are represented only in application/json. Requested representation is %s.",
                    accept);
            m = new Message(
                    "Unsupported output media type. Resources are represented only in application/json.",
                    "E4A2",
                    String.format("Requested representation is %s.", accept));
            res.setStatus(HttpServletResponse.SC_NOT_ACCEPTABLE);
            m.toJSON(out);
            return false;
        }

        // if the method is supposed to send a body, check its MIME media type
        switch (method) {
            case "GET":
            case "DELETE":
                // nothing to do
                break;

            case "POST":
            case "PUT":
                if (contentType == null) {
                    LOGGER.error("Input media type not specified. Content-Type request header missing.");
                    m = new Message("Input media type not specified.", "E4A3", "Content-Type request header missing.");
                    res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    m.toJSON(out);
                    return false;
                }

                if (!contentType.contains(JSON_MEDIA_TYPE)) {
                    LOGGER.error(
                            "Unsupported input media type. Resources are represented only in application/json. Submitted representation is %s.",
                            contentType);
                    m = new Message("Unsupported input media type. Resources are represented only in application/json.",
                            "E4A4", String.format("Submitted representation is %s.", contentType));
                    res.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
                    m.toJSON(out);
                    return false;
                }

                break;
            default:
                LOGGER.error("Unsupported operation. Requested operation %s.", method);
                m = new Message("Unsupported operation.", "E4A5", String.format("Requested operation %s.", method));
                res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                m.toJSON(out);
                return false;
        }

        return true;
    }

    /**
     * Checks that the user is logged in to perform the operation.
     *
     * @param req the HTTP request.
     * @param res the HTTP response.
     * @return {@code true} if the user type can access to the resource; {@code false} otherwise.
     * @throws IOException if any error occurs in the client/server communication.
     */
    protected boolean checkIsLoggedIn(final HttpServletRequest req, final HttpServletResponse res) throws IOException {
        Message m = null;
        // get the authorization information
        final String auth = req.getHeader("Authorization");

        if (auth == null || auth.isBlank()) {
            LOGGER.error("No authorization header sent by the client");
            m = new Message("No authorization header sent by the client", "E4B3", "No authorization header sent by the client");
            m.toJSON(res.getOutputStream());

            return false; // user is not authenticated
        }

        // if it is not HTTP Basic authentication, warn that there is no valid authentication method
        if (!auth.toUpperCase().startsWith("BASIC ")) {
            LOGGER.error("No valid authorization header sent by the client");
            m = new Message("No authorization header sent by the client", "E4B3", "No authorization header sent by the client");
            m.toJSON(res.getOutputStream());

            return false;
        }

        // perform Base64 decoding, pair = username:password
        final String pair = new String(DECODER.decode(auth.substring(6)));
        // the JNDI lookup context
        InitialContext cxt;
        try {
            cxt = new InitialContext();
            ds = (DataSource) cxt.lookup("java:/comp/env/jdbc/WaCar");
        } catch (NamingException e) {
            ds = null;
            LOGGER.error("Unable to acquire the connection pool to the database.", e);

            return false;
        }

        // userDetails[0] is the username; userDetails[1] is the password
        final String[] userDetails = pair.split(":", 2);
        final String password = DigestUtils.md5Hex(userDetails[1]).toUpperCase();
        String endpoint = req.getRequestURI();
        endpoint = endpoint.substring(endpoint.lastIndexOf("rest") + 4);
        final boolean isUser = endpoint.startsWith("/user"); // true: is a user; false: is an admin
        final User u = new User(userDetails[0], password);

        try {
            // if the user is successfully authenticated, create a Session and store the user there
            User authUser = new UserLoginDAO(ds.getConnection(), u).access().getOutputParam();

            if ((isUser && authUser.getType().equals("USER")) || (!isUser && authUser.getType().equals("ADMIN"))) {
                if (authUser != null) {
                    // create a  new session
                    HttpSession session = req.getSession(true);
                    session.setAttribute("account", authUser);
                    session.setAttribute("Authorization", auth);
                } else {
                    m = new Message("No authorization header sent by the client", "E4B3", "No authorization header sent by the client");
                    m.toJSON(res.getOutputStream());

                    return false;
                }
            } else {
                m = new Message("You are not authorized to perform this operation", "E4B3", "You are not authorized to perform this operation");
                m.toJSON(res.getOutputStream());

                return false;
            }
        } catch (SQLException e) {
            LOGGER.error("Unable to serve the REST request.", e);

            m = new Message("Unable to serve the REST request.", "E5A1",
                    e.getMessage());
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            m.toJSON(res.getOutputStream());

            return false;
        }

        return true;
    }
}
