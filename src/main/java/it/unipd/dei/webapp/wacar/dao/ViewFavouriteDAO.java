package it.unipd.dei.webapp.wacar.dao;

import it.unipd.dei.webapp.wacar.resource.Favourite;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class InsertFavouriteDAO extends AbstractDAO{
    private static final String STATEMENT_REGISTRATION = "INSERT INTO assessment.favourite (circuit, \"carBrand\", \"carModel\", \"user\", \"createdAt\") VALUES ('?', '?', '?', '?', '?');";
    private final Favourite favourite;

    public InsertFavouriteDAO(final Connection con, final Favourite favourite) {
        super(con);
        this.favourite = favourite;
    }

    @Override
    protected void doAccess() throws Exception {
        PreparedStatement stmnt = null;
        ResultSet rs = null;
        // the results of the search

        try {
            stmnt = con.prepareStatement(STATEMENT_REGISTRATION);
            stmnt.setString(1, favourite.getCircuit());
            stmnt.setString(2, favourite.getCarBrand());
            stmnt.setString(3, favourite.getCarModel());
            stmnt.setInt(4, favourite.getUser());
            stmnt.setTimestamp(5, favourite.getCreatedAt());


            stmnt.execute();
            LOGGER.info("New Favourite inserted in data: ", favourite.getCreatedAt().toString());


        } finally {

            if (stmnt != null) {
                stmnt.close();
            }


        }
    }
}
