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

import it.unipd.dei.webapp.wacar.dao.GetFavouriteDAO;
import it.unipd.dei.webapp.wacar.resource.Actions;
import it.unipd.dei.webapp.wacar.resource.Favourite;
import it.unipd.dei.webapp.wacar.resource.Order;
import it.unipd.dei.webapp.wacar.resource.User;
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
public final class GetFavouriteRR extends AbstractRR {

	/**
	 * Creates a new REST resource for getting a {@code Favourite}.
	 *
	 * @param req the HTTP request.
	 * @param res the HTTP response.
	 * @param con the connection to the database.
	 */
	public GetFavouriteRR(final HttpServletRequest req, final HttpServletResponse res, Connection con) {
		super(Actions.GET_FAVOURITE, req, res, con);
	}


	@Override
	protected void doServe() throws IOException {

		Favourite f = null;
		Message m = null;

		try {
			final User loggedUser = (User) req.getSession().getAttribute("account");

			// final Favourite favouriteToSearch = Favourite.fromJSON(req.getInputStream());
			final Favourite favouriteToSearch = new Favourite(
				req.getParameter("circuitName"),
				req.getParameter("carBrand"),
				req.getParameter("carModel"),
				loggedUser.getEmail());
			f = new GetFavouriteDAO(con, favouriteToSearch).access().getOutputParam();

			if (f != null) {
				LOGGER.info("Favourite successfully retrieved.");

				m = new Message("Your favourite has been retrieved.", "E200", null);
				res.setStatus(HttpServletResponse.SC_FOUND);
				m.toJSON(res.getOutputStream());
				f.toJSON(res.getOutputStream());
			} else { // it should not happen
				LOGGER.error("Favourite not found.");

				m = new Message("Favourite not found.", ErrorCode.RESOURCE_NOT_FOUND.getErrorCode(), null);
				res.setStatus(HttpServletResponse.SC_NOT_FOUND);
				m.toJSON(res.getOutputStream());
			}
		} catch (EOFException ex) {
			LOGGER.warn("Cannot search the favourite: no favourite JSON object found in the request.", ex);

			m = new Message("Cannot search the favourite: no favourite JSON object found in the request.", "E4A8", ex.getMessage());
			res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			m.toJSON(res.getOutputStream());
		} catch (SQLException ex) {
			LOGGER.info(ex.getSQLState());
			if (ex.getSQLState().equals("02000")) {
				m = new Message("Favourite not found.", ErrorCode.RESOURCE_NOT_FOUND.getErrorCode(), ex.getMessage());
				req.setAttribute("message", m);
				res.setStatus(HttpServletResponse.SC_NOT_FOUND);
				LOGGER.error("Favourite not found.");
			} else {
				LOGGER.error("Cannot search the favourite: unexpected database error.", ex);
	
				m = new Message("Cannot search the favourite: unexpected database error.", "E5A1", ex.getMessage());
				res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
				
			m.toJSON(res.getOutputStream());
		}
	}
}
