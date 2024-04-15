package it.unipd.dei.webapp.wacar.dao;

import it.unipd.dei.webapp.wacar.resource.Circuit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoadCircuitImageDAO extends AbstractDAO<Circuit> {
    private static final String STATEMENT = "SELECT image, imageMediaType FROM assessment.circuit WHERE name=?";

    private final String name;


    public LoadCircuitImageDAO(final Connection con, final String name) {
        super(con);
        this.name = name;
    }

    @Override
    protected void doAccess() throws SQLException {
        PreparedStatement stmnt = null;
        ResultSet rs = null;

        Circuit circuit;

        try {
            stmnt = con.prepareStatement(STATEMENT);
            stmnt.setString(1, name);

            rs = stmnt.executeQuery();

            if (rs.next()) {
                circuit = new Circuit(
                        null,
                        null,
                        Integer.MIN_VALUE,
                        Integer.MIN_VALUE,
                        null,
                        null,
                        Integer.MIN_VALUE,
                        false,
                        rs.getBytes("image"),
                        rs.getString("imageMediaType"));

                LOGGER.info(String.format("Image for circuit %s successfully loaded.", name));
            } else {
                LOGGER.warn(String.format("Circuit %s not found.", name));
                throw new SQLException(String.format("Circuit %s not found.", name), "NOT_FOUND");
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (stmnt != null) {
                stmnt.close();
            }
        }

        this.outputParam = circuit;
    }
}
