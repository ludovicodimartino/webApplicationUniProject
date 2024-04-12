package it.unipd.dei.webapp.wacar.servlet;

import it.unipd.dei.webapp.wacar.resource.Car;
import it.unipd.dei.webapp.wacar.resource.Message;
import it.unipd.dei.webapp.wacar.dao.ListCarDAO;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet(name = "CarListServlet", value = "/car_list")
public class CarListServlet extends AbstractDatabaseServlet {
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

        LogContext.setIPAddress(req.getRemoteAddr());
        LogContext.setAction("GET_ALL_CARS_FROM_DATABASE");

        List<Car> cars = null;
        Message m = null;

        try {
            cars = new ListCarDAO(getConnection()).access().getOutputParam();

            m = new Message("Cars successfully retrieved");

            LOGGER.info("Cars successfully retrieved from database without parameters");
        } catch (SQLException ex) {
            m = new Message("Cannot search for cars: unexpected error while accessing the database.", "E200", ex.getMessage());

            LOGGER.error("Cannot search for cars: unexpected error while accessing the database.", ex);
        }

        try {
            // set the MIME media type of the response
            res.setContentType("text/html; charset=utf-8");
            // get a stream to write the response
            PrintWriter out = res.getWriter();
            // write the HTML page
            out.printf("<!DOCTYPE html>%n");
            out.printf("<html lang=\"en\">%n");
            out.printf("<head>%n");
            out.printf("<meta charset=\"utf-8\">%n");
            out.printf("<title>Search Employee</title>%n");
            out.printf("</head>%n");

            out.printf("<body>%n");
            out.printf("<h1>Search Employee</h1>%n");
            out.printf("<hr/>%n");

            if (m.isError()) {
                out.printf("<ul>%n");
                out.printf("<li>error code: %s</li>%n", m.getErrorCode());
                out.printf("<li>message: %s</li>%n", m.getMessage());
                out.printf("<li>details: %s</li>%n", m.getErrorDetails());
                out.printf("</ul>%n");
            } else {
                out.printf("<p>%s</p>%n", m.getMessage());

                out.printf("<table>%n");
                out.printf("<tr>%n");
                out.printf("<td>Badge</td><td>Surname</td><td>Age</td><td>Salary</td>%n");
                out.printf("</tr>%n");

                for (Car car : cars) {
                    out.println("<tr>");
                    out.printf("<td>%s</td> <td>%s</td> <td>%s</td> <td>%d</td> <td>%d</td> <td>%d</td> <td>%s</td> <td>%s</td>%n",
                            car.getBrand(),
                            car.getModel(),
                            car.getType(),
                            car.getHorsepower(),
                            car.getAcceleration(),
                            car.getMaxSpeed(),
                            car.getDescription(),
                            car.isAvailable() ? "Yes" : "No"); // Assuming isAvailable() returns a boolean
                    out.println("</tr>");
                }
                out.println("</table>");
            }
            out.printf("</body>%n");
            out.printf("</html>%n");
            // flush the output stream buffer
            out.flush();
            // close the output stream
            out.close();
        }
        catch (IOException ex) {
            LOGGER.error("Unable to send response when creating employee %d.", ex);
            throw ex;
        } finally {
            LogContext.removeIPAddress();
            LogContext.removeAction();
            LogContext.removeUser();
        }
    }
}

