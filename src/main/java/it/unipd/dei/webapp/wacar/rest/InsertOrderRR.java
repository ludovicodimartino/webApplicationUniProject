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

import it.unipd.dei.webapp.wacar.dao.InsertOrderDAO;
import it.unipd.dei.webapp.wacar.dao.ListCarByAvailabilityDAO;
import it.unipd.dei.webapp.wacar.resource.Actions;
import it.unipd.dei.webapp.wacar.resource.Order;
import it.unipd.dei.webapp.wacar.resource.Message;
import it.unipd.dei.webapp.wacar.resource.ResourceList;
import it.unipd.dei.webapp.wacar.utils.ErrorCode;
import it.unipd.dei.webapp.wacar.resource.LogContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.EOFException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * A REST resource for inserting a new {@link Order}.
 *
 * @author Manuel Rigobello (manuel.rigobello@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class InsertOrderRR extends AbstractRR {

	/**
	 * Creates a new REST resource for listing {@code Employee}s.
	 *
	 * @param req the HTTP request.
	 * @param res the HTTP response.
	 * @param con the connection to the database.
	 */
	public InsertOrderRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
		super(Actions.INSERT_ORDER, req, res, con);
	}


	@Override
	protected void doServe() throws IOException {

		Order o = null;
		Message m = null;

		try {
			final Order order = Order.fromJSON(req.getInputStream());
			LogContext.setResource(Integer.toString(order.getId()));

			o = new InsertOrderDAO(con, order).access().getOutputParam();

			if (o != null) {
				LOGGER.info("Order successfully created.");

				m = new Message("Your order has been added.", "E200", null);

				res.setStatus(HttpServletResponse.SC_CREATED);
				m.toJSON(res.getOutputStream());
			} else { // it should not happen
				LOGGER.error("Fatal error while inserting order.");

				m = new Message("Cannot insert the order: unexpected error.", "E5A1", null);
				res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				m.toJSON(res.getOutputStream());
			}
		} catch (EOFException ex) {
			LOGGER.warn("Cannot insert the order: no order JSON object found in the request.", ex);

			m = new Message("Cannot insert the order: no order JSON object found in the request.", "E4A8", ex.getMessage());
			res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			m.toJSON(res.getOutputStream());
		} catch (SQLException ex) {
			LOGGER.error("Cannot insert the order: unexpected database error.", ex);

			// Exception raised by the trigger
			if (ex.getSQLState().equals("P0001")) {
				//Prepare message to be show
				String errorMsg = ex.getMessage().split("ERROR: ")[1].split("Where:")[0];
				m = new Message(errorMsg, ErrorCode.CANNOT_CREATE_RESOURCE.getErrorCode(), errorMsg);

				res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			} else {
				m = new Message("Cannot insert the order: unexpected database error.", "E5A1", ex.getMessage());
				
				res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
			m.toJSON(res.getOutputStream());
		}
	}
}
