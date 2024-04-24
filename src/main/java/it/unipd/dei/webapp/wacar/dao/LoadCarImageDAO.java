package it.unipd.dei.webapp.wacar.dao;

import it.unipd.dei.webapp.wacar.resource.Car;
import it.unipd.dei.webapp.wacar.resource.LogContext;
import it.unipd.dei.webapp.wacar.resource.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoadCarImageDAO extends AbstractDAO<Car> {
    private static final String STATEMENT = "SELECT image, imageMediaType FROM assessment.car WHERE brand=? and model=?";

    private final String brand;

    private final String model;

    public LoadCarImageDAO(final Connection con, final String brand, final String model) {
        super(con);
        this.brand = brand;
        this.model = model;
    }

    @Override
    protected void doAccess() throws SQLException {
        PreparedStatement stmnt = null;
        ResultSet rs = null;

        // the results of the search
        Car car;

        try {
            stmnt = con.prepareStatement(STATEMENT);
            stmnt.setString(1, brand);
            stmnt.setString(2, model);

            rs = stmnt.executeQuery();

            if (rs.next()) {
                car = new Car(null, null, null, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, false, null, rs.getBytes("image"), rs.getString("imageMediaType"));
                LOGGER.info(String.format("Image for car %s %s successfully loaded.", brand, model));
            } else {
                LOGGER.warn(String.format("Car %s %s not found.", brand, model));
                throw new SQLException(String.format("Car %s %s not found.", brand, model), "NOT_FOUND");
            }


        } finally {
            if (rs != null) {
                rs.close();
            }

            if (stmnt != null) {
                stmnt.close();
            }

        }

        this.outputParam = car;
    }
}
