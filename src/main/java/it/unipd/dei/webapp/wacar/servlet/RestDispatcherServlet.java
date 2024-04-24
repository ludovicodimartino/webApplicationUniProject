/*
 * Copyright 2018 University of Padua, Italy
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

package it.unipd.dei.webapp.wacar.servlet;

import it.unipd.dei.webapp.wacar.resource.Car;
import it.unipd.dei.webapp.wacar.resource.Order;
import it.unipd.dei.webapp.wacar.resource.LogContext;
import it.unipd.dei.webapp.wacar.resource.Message;
import it.unipd.dei.webapp.wacar.rest.GetOrderByIdAndUserEmailRR;
import it.unipd.dei.webapp.wacar.rest.ListCarsRR;
import it.unipd.dei.webapp.wacar.rest.ListCircuitsRR;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Dispatches the request to the proper REST resource.
 *
 * @author Manuel Rigobello (manuel.rigobello@studenti.unipd.it)
 * @version 1.00
 * @since 1.00
 */
public final class RestDispatcherServlet extends AbstractDatabaseServlet {

	/**
	 * The JSON UTF-8 MIME media type
	 */
	private static final String JSON_UTF_8_MEDIA_TYPE = "application/json; charset=utf-8";

	@Override
	protected void service(final HttpServletRequest req, final HttpServletResponse res) throws IOException {

		LogContext.setIPAddress(req.getRemoteAddr());

		final OutputStream out = res.getOutputStream();

		try {

			// if the requested resource was a Circuit a Car or an Order, delegate its processing and return
			if (processListCar(req, res) || processOrder(req, res) || processListCircuits(req, res)) {
				return;
			}

			// if none of the above process methods succeeds, it means an unknown resource has been requested
			LOGGER.warn("Unknown resource requested: {}", req.getRequestURI());

			final Message m = new Message("Unknown resource requested.", "E4A6",
					String.format("Requested resource is %s.", req.getRequestURI()));
			res.setStatus(HttpServletResponse.SC_NOT_FOUND);
			res.setContentType(JSON_UTF_8_MEDIA_TYPE);
			m.toJSON(out); // write itself to the output socket
		} catch (Throwable t) {
			LOGGER.error("Unexpected error while processing the REST resource.", t);

			final Message m = new Message("Unexpected error.", "E5A1", t.getMessage());
			res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			m.toJSON(out);
		} finally {

			// ensure to always flush and close the output stream
			if (out != null) {
				out.flush();
				out.close();
			}

			LogContext.removeIPAddress();
		}
	}

	/**
	 * Checks whether the request if for a {@link it.unipd.dei.webapp.wacar.resource.Circuit}(s) resource(s) and, in case, processes it.
	 *
	 * @param req the HTTP request.
	 * @param res the HTTP response.
	 * @return {@code true} if the request was for a {@code Circuit}; {@code false} otherwise.
	 * @throws Exception if any error occurs.
	 */
	private boolean processListCircuits(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
		LOGGER.info("process list circuit");

		final String method = req.getMethod();

		String path = req.getRequestURI();
		Message m = null;

		// Requested resource was not a list of circuits
		if (path.lastIndexOf("rest/circuit") <= 0) {
			return false;
		}

		// strip everything until after the /circuit
		path = path.substring(path.lastIndexOf("circuit") + 7);

		if (path.isEmpty() || path.equals("/")) {

			switch (method) {
				case "GET":
					new ListCircuitsRR(req, res, getConnection()).serve();
					break;
				case "POST":
					break;
				default:
					LOGGER.warn("Unsupported operation for URI /circuit: {}", method);

					m = new Message(
							"Unsupported operation for URI /circuit.",
							"E4A5",
							String.format("Requested operation %s.", method));
					res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
					m.toJSON(res.getOutputStream());
					break;
			}
		}
		/*

		else if (path.contains("available")) {

			path = path.substring(path.lastIndexOf("available") + 9);

			if (path.length() == 0 || path.equals("/")) {
				//the uri is rest/category/available
				LOGGER.warn("Wrong format for URI /categories/available/{sagra}. Requested URI: %s.", req.getRequestURI());

				m = new Message("Wrong format for URI /categories/available/{sagra}.", "E4A7",
						String.format("Requested URI: %s.", req.getRequestURI()));
				res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				m.toJSON(res.getOutputStream());
			} else {
				//the uri is rest/category/available/{sagra}
				switch (method) {
					case "GET":
						new ListCategoriesAvailableRR(req, res, getConnection()).serve();
						break;
					default:
						LOGGER.warn("Unsupported operation for URI /categories/available/{sagra}: %s.", method);

						m = new Message("Unsupported operation for URI /categories/available/{sagra}.", "E4A5",
								String.format("Requested operation %s.", method));
						res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
						m.toJSON(res.getOutputStream());
						break;
				}

			}
		} else {
			LOGGER.warn("Wrong format for URI /categories: too many arguments. Requested URI: %s.", req.getRequestURI());

			m = new Message("Wrong format for URI /categories: too many arguments.", "E4A7",
					String.format("Requested URI: %s.", req.getRequestURI()));
			res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			m.toJSON(res.getOutputStream());
		}

		*/

		return true;
	}
	/**
	 * Checks whether the request if for a list of {@link Car}s and, in case, processes it.
	 *
	 * @param req the HTTP request.
	 * @param res the HTTP response.
	 * @return {@code true} if the request was for a {@code Product}; {@code false} otherwise.
	 * @throws Exception if any error occurs.
	 */
	private boolean processListCar(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
		LOGGER.info("Processing list car");

		final String method = req.getMethod();

		String path = req.getRequestURI();
		Message m = null;

		// the requested resource was not a list of cars
		if (path.lastIndexOf("rest/cars") <= 0) {
			return false;
		}

		// strip everything until after the /create-order
		path = path.substring(path.lastIndexOf("cars") + 4);

		// the request URI is: /
		// if method GET, list the available cars
		if (path.length() == 0 || path.equals("/")) {

			switch (method) {
				case "GET":
					new ListCarsRR(req, res, getConnection()).serve();
					break;
				default:
					LOGGER.warn("Unsupported operation for URI /: %s.", method);

					m = new Message("Unsupported operation for URI /.", "E4A5",
							String.format("Requested operation %s.", method));
					res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
					m.toJSON(res.getOutputStream());
					break;
			}
		}
//		} else {
//			// the request URI is: /employee/salary/{salary}
//			if (path.contains("salary")) {
//				path = path.substring(path.lastIndexOf("salary") + 6);
//
//				if (path.length() == 0 || path.equals("/")) {
//					LOGGER.warn("Wrong format for URI /employee/salary/{salary}: no {salary} specified. Requested URI: %s.", req.getRequestURI());
//
//					m = new Message("Wrong format for URI /employee/salary/{salary}: no {salary} specified.", "E4A7",
//							String.format("Requested URI: %s.", req.getRequestURI()));
//					res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//					m.toJSON(res.getOutputStream());
//				} else {
//					switch (method) {
//						case "GET":
//							new SearchEmployeeBySalaryRR(req, res, getConnection()).serve();
//
//							break;
//						default:
//							LOGGER.warn("Unsupported operation for URI /employee/salary/{salary}: %s.", method);
//
//							m = new Message("Unsupported operation for URI /employee/salary/{salary}.", "E4A5",
//									String.format("Requested operation %s.", method));
//							res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
//							m.toJSON(res.getOutputStream());
//							break;
//					}
//				}
//			} else {
//				// the request URI is: /employee/{badge}
//
//				switch (method) {
//					case "GET":
//						new ReadEmployeeRR(req, res, getConnection()).serve();
//						break;
//					case "PUT":
//						new UpdateEmployeeRR(req, res, getConnection()).serve();
//						break;
//					case "DELETE":
//						new DeleteEmployeeRR(req, res, getConnection()).serve();
//						break;
//					default:
//						LOGGER.warn("Unsupported operation for URI /employee/{badge}: %s.", method);
//
//						m = new Message("Unsupported operation for URI /employee/{badge}.", "E4A5",
//								String.format("Requested operation %s.", method));
//						res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
//						m.toJSON(res.getOutputStream());
//				}
//			}
//		}

		return true;

	}

	/**
	 * Checks whether the request if for visualizing an {@link Order} and, in case, processes it.
	 *
	 * @param req the HTTP request.
	 * @param res the HTTP response.
	 * @return {@code true} if the request was for a {@code Product}; {@code false} otherwise.
	 * @throws Exception if any error occurs.
	 */
	private boolean processOrder(final HttpServletRequest req, final HttpServletResponse res) throws Exception {
		final String method = req.getMethod();

		String path = req.getRequestURI();
		Message m = null;

		// the requested resource was not a list of cars
		if (path.lastIndexOf("user/order") <= 0) {
			LOGGER.info("Return false");
			return false;
		}

		// strip everything until after the /order
		path = path.substring(path.lastIndexOf("order") + 5);

		// the request URI is: /
		// if method GET, list the available order of the logged in account
		if (path.length() == 0 || path.equals("/")) {
			LOGGER.info("path /");
			switch (method) {
				case "GET":
//					new ListCarRR(req, res, getConnection()).serve(); // TODO
					break;
				default:
					LOGGER.warn("Unsupported operation for URI /: %s.", method);

					m = new Message("Unsupported operation for URI /.", "E4A5",
							String.format("Requested operation %s.", method));
					res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
					m.toJSON(res.getOutputStream());
					break;
			}
		} else {
			// the request URI is: /order/{orderId}
			path = path.substring(path.lastIndexOf("/") + 1);
			LOGGER.info("path + " + path);

			if (path.length() == 0 || path.equals("/")) {
				LOGGER.warn("Wrong format for URI /order/{orderId}: no {orderId} specified. Requested URI: %s.", req.getRequestURI());

				m = new Message("Wrong format for URI /order/{orderId}: no {orderId} specified.", "E4A7",
						String.format("Requested URI: %s.", req.getRequestURI()));
				res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
				m.toJSON(res.getOutputStream());
			} else {
				switch (method) {
					case "GET":
						new GetOrderByIdAndUserEmailRR(req, res, getConnection()).serve();

						break;
					default:
						LOGGER.warn("Unsupported operation for URI /order/{orderId}: %s.", method);

						m = new Message("Unsupported operation for URI /order/{orderId}.", "E4A5",
								String.format("Requested operation %s.", method));
						res.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
						m.toJSON(res.getOutputStream());
						break;
				}
			}
		}

		return true;
	}
}
