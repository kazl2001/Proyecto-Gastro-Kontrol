package dao.impl.jdbc_spring;

import common.constants.DbConstants;
import common.constants.ErrorConstants;
import dao.DaoMenuItems;
import dao.connection.DBConnectionPool;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import model.MenuItem;
import model.error.RestaurantError;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Log4j2
public class DaoMenuItemsImpl implements DaoMenuItems {

    private final DBConnectionPool pool;

    @Inject
    public DaoMenuItemsImpl(DBConnectionPool pool) {
        this.pool = pool;
    }

    @Override
    public Either<RestaurantError, ArrayList<MenuItem>> getAll() {
        PreparedStatement pStmt;
        ResultSet rs;
        try (Connection con = pool.getConnection()) {
            pStmt = con.prepareStatement(QueryStrings.GET_ALL_MENU_ITEMS);

            rs = pStmt.executeQuery();
            return Either.right(readRS(rs));

        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            return Either.left(new RestaurantError(e.getErrorCode(), ErrorConstants.GENERAL_DATABASE_ERROR));
        }
    }

    private ArrayList<MenuItem> readRS(ResultSet rs) {
        ArrayList<MenuItem> listMenuItem = new ArrayList<>();
        try {
            while (rs.next()) {
                int id = rs.getInt(DbConstants.MENU_ITEM_ID);
                String name = rs.getString(DbConstants.NAME);
                String description = rs.getString(DbConstants.DESCRIPTION);
                double price = rs.getDouble(DbConstants.PRICE);
                listMenuItem.add(new MenuItem(id, name, description, price));
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return listMenuItem;
    }

}
