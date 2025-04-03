package dao.impl.jdbc_spring;

import common.constants.DbConstants;
import model.MenuItem;
import model.OrderItem;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderItemMapper implements RowMapper<OrderItem> {

    @Override
    public OrderItem mapRow(ResultSet rs, int rowNum) throws SQLException {
        OrderItem oi = new OrderItem();
        oi.setId(rs.getInt(DbConstants.ORDER_ITEM_ID));
        oi.setOrderId(rs.getInt(DbConstants.ORDER_ID));
        MenuItem mi = new MenuItem(
                rs.getInt(DbConstants.MENU_ITEM_ID),
                rs.getString(DbConstants.NAME),
                rs.getString(DbConstants.DESCRIPTION),
                rs.getDouble(DbConstants.PRICE)
        );
        oi.setMenuItem(mi);
        oi.setQuantity(rs.getInt(DbConstants.QUANTITY));
        return oi;
    }
}
