/*
 * Copyright 2023 University of Padua, Italy
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

import it.unipd.dei.webapp.wacar.dao.GetOrderByIdAndUserEmailDAO;
import it.unipd.dei.webapp.wacar.resource.Actions;
import it.unipd.dei.webapp.wacar.resource.LogContext;
import it.unipd.dei.webapp.wacar.resource.Message;
import it.unipd.dei.webapp.wacar.resource.Order;
import it.unipd.dei.webapp.wacar.resource.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.EOFException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * A REST resource for searching an {@link Order} by its id.
 *
 * @author Manuel Rigobello (manuel.rigobello@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class GetOrderByIdAndUserEmailRR extends AbstractRR {

	/**
	 * Creates a new REST resource for searching {@code Order} by its id.
	 *
	 * @param req the HTTP request.
	 * @param res the HTTP response.
	 * @param con the connection to the database.
	 */
	public GetOrderByIdAndUserEmailRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
		super(Actions.GET_ORDERS_BY_ID, req, res, con);
	}


	@Override
	protected void doServe() throws IOException {

		Order o = null;
		Message m = null;
		int orderId = -1;

		try {
			// parse the URI path to extract the order id
			String path = req.getRequestURI();
			path = path.substring(path.lastIndexOf("/") + 1);

			orderId = Integer.parseInt(path);
			User user = (User) req.getSession().getAttribute("account");

			LogContext.setResource(Integer.toString(orderId));

			// creates a new DAO for accessing the database and searches the order by the id
			o = new GetOrderByIdAndUserEmailDAO(con, user.getEmail(), orderId).access().getOutputParam();

			if (o != null) {
				LOGGER.info("Order successfully searched by id %d.", orderId);

				res.setStatus(HttpServletResponse.SC_OK);
				o.toJSON(res.getOutputStream());
			} else { // it should not happen
				LOGGER.error("Fatal error while searching order.");

				m = new Message("Cannot search order: unexpected error.", "E5A1", null);
				res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				m.toJSON(res.getOutputStream());
			}
		} catch (NumberFormatException ex) {
			LOGGER.warn("Cannot find the order: wrong format for URI /user/order/{orderId}.", ex);

			m = new Message("Cannot find the order: wrong format for URI /user/order/{orderId}.", "E4A7",
					ex.getMessage());
			res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			m.toJSON(res.getOutputStream());
		} catch (SQLException ex) {
			LOGGER.error(String.format("Cannot find the order %d.", orderId), ex);

			m = new Message("Cannot find the order %s: unexpected database error.", "E5A1", ex.getMessage());
			res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			m.toJSON(res.getOutputStream());
		}
	}
}
