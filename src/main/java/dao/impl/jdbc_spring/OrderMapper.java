package dao.impl.jdbc_spring;

import common.constants.DbConstants;
import model.Order;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderMapper implements RowMapper<Order> {

    @Override
    public Order mapRow(ResultSet rs, int rowNum) throws SQLException {
        Order o = new Order();
        o.setId(rs.getInt(DbConstants.ORDER_ID));
        o.setTableId(rs.getInt(DbConstants.TABLE_ID));
        o.setCustomerId(rs.getInt(DbConstants.CUSTOMER_ID));
        o.setOrderDate(rs.getTimestamp(DbConstants.ORDER_DATE).toLocalDateTime());
        o.setOrderItems(null);
        return o;
    }
}
