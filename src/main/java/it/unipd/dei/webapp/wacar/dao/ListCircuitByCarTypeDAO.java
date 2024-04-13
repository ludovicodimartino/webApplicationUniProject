package it.unipd.dei.webapp.wacar.dao;

import it.unipd.dei.webapp.wacar.resource.Circuit;

import java.util.ArrayList;
import java.util.List;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Searches the circuits where a type of car can race.
 *
 * @author Manuel Rigobello (manuel.rigobello@studentiSearches the circuits in which the type of the car can race into..unipd.it)
 * @version 1.00
 * @since 1.00
 */
public class ListCircuitByCarTypeDAO extends AbstractDAO<List<Circuit>> {
    private static final String STATEMENT_CIRCUIT_BY_CAR_TYPE = "SELECT j.name, j.type, j.length, j.\"cornersNumber\", j.address, j.description, j.\"lapPrice\", j.image FROM (assessment.circuit as c INNER JOIN assessment.\"carCircuitSuitability\" as s ON c.type = s.\"circuitType\") as j WHERE j.\"carType\" = ? AND j.available = true";
    private final String carType;

    /**
	 * Creates a new object for searching circuits where a specific type of car can race.
	 *
	 * @param con    the connection to the database.
	 * @param carType the type of car.
	 */
    public ListCircuitByCarTypeDAO(final Connection con, final String carType) {
        super(con);
        this.carType = carType;
    }

    @Override
    public final void doAccess() throws SQLException {
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        // Result of the search
        final List<Circuit> circuits = new ArrayList<>();

        try {
            pstmt = con.prepareStatement(STATEMENT_CIRCUIT_BY_CAR_TYPE);
            pstmt.setString(1, carType);

            rs = pstmt.executeQuery();
            while (rs.next()) {
                circuits.add(new Circuit(rs.getString("name"), rs.getString("type"), rs.getInt("length"), rs.getInt("cornersNumber"), rs.getString("address"), rs.getString("description"), rs.getInt("lapPrice"), rs.getBytes("image"), rs.getString("imageMediaType")));
                LOGGER.info("Circuit(s) where the car type %s can race successfully listed.", carType);
            }
        } finally {
            if (rs != null) {
                rs.close();
            }

            if (pstmt != null) {
                pstmt.close();
            }
        }

        this.outputParam = circuits;
    }
}
