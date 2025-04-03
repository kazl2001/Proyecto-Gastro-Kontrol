package dao.impl.jdbc_spring;

import common.constants.DbConstants;
import common.constants.ErrorConstants;
import dao.DaoTables;
import dao.connection.DBConnectionPool;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import model.Table;
import model.error.RestaurantError;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Log4j2
public class DaoTablesImpl implements DaoTables {
    private final DBConnectionPool pool;

    @Inject
    public DaoTablesImpl(DBConnectionPool pool) {
        this.pool = pool;
    }


    @Override
    public Either<RestaurantError, ArrayList<Table>> getAll() {
        ResultSet rs;
        PreparedStatement pStmt;
        try (Connection con = pool.getConnection()) {
            pStmt = con.prepareStatement(QueryStrings.GET_ALL_TABLES);

            rs = pStmt.executeQuery();
            return Either.right(readRS(rs));

        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            return Either.left(new RestaurantError( ErrorConstants.GENERAL_DATABASE_ERROR_CODE, ErrorConstants.GENERAL_DATABASE_ERROR));
        }
    }

    private ArrayList<Table> readRS(ResultSet rs) {
        ArrayList<Table> listTable = new ArrayList<>();
        try {
            while (rs.next()) {
                int id = rs.getInt(DbConstants.TABLE_NUMBER_ID);
                int seats = rs.getInt(DbConstants.TABLE_NUMBER_ID);
                listTable.add(new Table(id, seats));
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return listTable;
    }

}
