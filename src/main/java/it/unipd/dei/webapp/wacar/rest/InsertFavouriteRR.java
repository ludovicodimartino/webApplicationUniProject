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

import it.unipd.dei.webapp.wacar.dao.InsertFavouriteDAO;
import it.unipd.dei.webapp.wacar.resource.Actions;
import it.unipd.dei.webapp.wacar.resource.Favourite;
import it.unipd.dei.webapp.wacar.resource.Order;
import it.unipd.dei.webapp.wacar.utils.ErrorCode;
import it.unipd.dei.webapp.wacar.resource.Message;
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
public final class InsertFavouriteRR extends AbstractRR {

	/**
	 * Creates a new REST resource for inserting a {@code Favourite}.
	 *
	 * @param req the HTTP request.
	 * @param res the HTTP response.
	 * @param con the connection to the database.
	 */
	public InsertFavouriteRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
		super(Actions.INSERT_FAVOURITE, req, res, con);
	}


	@Override
	protected void doServe() throws IOException {

		Favourite f = null;
		Message m = null;

		try {
			final Favourite favourite = Favourite.fromJSON(req.getInputStream());

			f = new InsertFavouriteDAO(con, favourite).access().getOutputParam();

			if (f != null) {
				LOGGER.info("Favourite successfully added.");

				res.setStatus(HttpServletResponse.SC_CREATED);
				f.toJSON(res.getOutputStream());
			} else { // it should not happen
				LOGGER.error("Fatal error while inserting favourite.");

				m = new Message("Cannot insert the favourite: unexpected error.", "E5A1", null);
				res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
				m.toJSON(res.getOutputStream());
			}
		} catch (EOFException ex) {
			LOGGER.warn("Cannot insert the favourite: no favourite JSON object found in the request.", ex);

			m = new Message("Cannot insert the favourite: no favourite JSON object found in the request.", "E4A8", ex.getMessage());
			res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			m.toJSON(res.getOutputStream());
		} catch (SQLException ex) {
			if (ex.getSQLState().equals("23505")) {
				m = new Message("You already have a favourite with the same car and circuit.", "E5A1", ex.getMessage());
				req.setAttribute("message", m);
				res.setStatus(ErrorCode.CANNOT_CREATE_RESOURCE.getHTTPCode());
				LOGGER.error("Unable to insert the new favourite: %s" + ex.getMessage());
			} else {
				LOGGER.error("Cannot insert the favourite: unexpected database error.", ex);
	
				m = new Message("Cannot insert the favourite: unexpected database error.", "E5A1", ex.getMessage());
				res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
				
			m.toJSON(res.getOutputStream());
		}
	}
}
