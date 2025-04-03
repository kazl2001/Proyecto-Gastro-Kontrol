package dao.impl.jdbc_spring;

import common.constants.ErrorConstants;
import dao.DaoOrderItems;
import dao.connection.DBConnectionPool;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.OrderItem;
import model.error.RestaurantError;
import model.error.database.GeneralDatabaseError;
import model.error.item.OrderItemDataRetrievalError;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class DaoOrderItemsImpl implements DaoOrderItems {

    private final DBConnectionPool pool;

    @Inject
    public DaoOrderItemsImpl(DBConnectionPool pool) {
        this.pool = pool;
    }

    @Override
    public Either<RestaurantError, ArrayList<OrderItem>> getAll(OrderItem item) {
        try {
            int id = item.getOrderId();
            JdbcTemplate jdbcTemplate = new JdbcTemplate(pool.getDataSource());
            ArrayList<OrderItem> orderItems = (ArrayList<OrderItem>) jdbcTemplate.query(QueryStrings.GET_ALL_ORDER_ITEMS_BY_ORDER_ID_SPRING, new OrderItemMapper(), id);
            return Either.right(orderItems);
        } catch (Exception e) {
            return Either.left(new GeneralDatabaseError(ErrorConstants.GENERAL_DATABASE_ERROR_CODE, ErrorConstants.GENERAL_DATABASE_ERROR));
        }
    }

    @Override
    public Either<RestaurantError, OrderItem> get(OrderItem item) {
        Either<RestaurantError, OrderItem> result;
        try {
            int id = item.getId();
            JdbcTemplate jdbcTemplate = new JdbcTemplate(pool.getDataSource());
            List<OrderItem> orderItemsList = jdbcTemplate.query(QueryStrings.GET_ORDER_ITEM_BY_ID_SPRING, new OrderItemMapper(), id);
            if(orderItemsList.isEmpty()) {
                result = Either.left(new OrderItemDataRetrievalError(ErrorConstants.DATA_RETRIEVAL_ERROR_NOT_FOUND_ERROR_CODE, ErrorConstants.DATA_RETRIEVAL_ERROR_NOT_FOUND));
            } else {
                OrderItem orderItem = orderItemsList.get(0);
                result = Either.right(orderItem);
            }
        } catch (Exception e) {
            result = Either.left(new GeneralDatabaseError(ErrorConstants.GENERAL_DATABASE_ERROR_CODE, ErrorConstants.GENERAL_DATABASE_ERROR));
        }
        return result;
    }

}
