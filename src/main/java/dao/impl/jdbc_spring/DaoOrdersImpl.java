package dao.impl.jdbc_spring;

import common.constants.ErrorConstants;
import dao.DaoOrders;
import dao.connection.DBConnectionPool;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import lombok.extern.log4j.Log4j2;
import model.MenuItem;
import model.Order;
import model.OrderItem;
import model.error.RestaurantError;
import model.error.database.GeneralDatabaseError;
import model.error.order.OrderDataRetrievalError;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class DaoOrdersImpl implements DaoOrders {

    private final DBConnectionPool pool;
    private PreparedStatement pStmt;

    @Inject
    public DaoOrdersImpl(DBConnectionPool pool) {
        this.pool = pool;
    }

    @Override
    public Either<RestaurantError, ArrayList<Order>> getAll() {
        try {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(pool.getDataSource());
            ArrayList<Order> orders = (ArrayList<Order>) jdbcTemplate.query(QueryStrings.GET_ALL_ORDERS_SPRING, new OrderMapper());
            return Either.right(orders);
        } catch (Exception e) {
            return Either.left(new GeneralDatabaseError(ErrorConstants.GENERAL_DATABASE_ERROR_CODE, ErrorConstants.GENERAL_DATABASE_ERROR));
        }
    }

    @Override
    public Either<RestaurantError, ArrayList<Order>> getAll(Order order) {
        try {
            int id = order.getId();
            JdbcTemplate jdbcTemplate = new JdbcTemplate(pool.getDataSource());
            ArrayList<Order> orders = (ArrayList<Order>) jdbcTemplate.query(QueryStrings.GET_ALL_ORDERS_BY_CUSTOMER_ID_SPRING, new OrderMapper(), id);
            if (orders.isEmpty()) {
                return Either.left(new OrderDataRetrievalError(ErrorConstants.DATA_RETRIEVAL_ERROR_NOT_FOUND_ERROR_CODE, ErrorConstants.DATA_RETRIEVAL_ERROR_NOT_FOUND));
            } else {
                return Either.right(orders);
            }
        } catch (Exception e) {
            return Either.left(new GeneralDatabaseError(ErrorConstants.GENERAL_DATABASE_ERROR_CODE, ErrorConstants.GENERAL_DATABASE_ERROR));
        }
    }

    @Override
    public Either<RestaurantError, Order> get(Order order) {
        try {
            int id = order.getId();
            JdbcTemplate jdbcTemplate = new JdbcTemplate(pool.getDataSource());
            List<Order> orders = jdbcTemplate.query(QueryStrings.GET_ORDER_BY_ID, new OrderMapper(), id);
            if (orders.isEmpty()) {
                return Either.left(new OrderDataRetrievalError(ErrorConstants.DATA_RETRIEVAL_ERROR_NOT_FOUND_ERROR_CODE, ErrorConstants.DATA_RETRIEVAL_ERROR_NOT_FOUND));
            } else {
                return Either.right(orders.get(0));
            }
        } catch (Exception e) {
            return Either.left(new GeneralDatabaseError(ErrorConstants.GENERAL_DATABASE_ERROR_CODE, ErrorConstants.GENERAL_DATABASE_ERROR));
        }
    }


    @Override
    public Either<RestaurantError, Integer> save(Order order) {
        Either<RestaurantError, Integer> result;

        try (Connection con = pool.getConnection()) {
            con.setAutoCommit(false);
            result = saveOrder(order, con);
            if (result.isLeft()) {
                return result;
            } else {
                con.commit();
            }
            con.setAutoCommit(true);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            result = Either.left(new RestaurantError(e.getErrorCode(), ErrorConstants.GENERAL_DATABASE_ERROR));
        }
        return result;
    }

    private Either<RestaurantError, Integer> saveOrder(Order order, Connection con) {
        int generatedKey;
        Either<RestaurantError, Integer> result;
        try {
            pStmt = con.prepareStatement(QueryStrings.INSERT_ORDER, Statement.RETURN_GENERATED_KEYS);
            pStmt.setInt(1, order.getTableId());
            pStmt.setInt(2, order.getCustomerId());
            pStmt.setTimestamp(3, Timestamp.valueOf(order.getOrderDate()));

            int rowsAffected = pStmt.executeUpdate();

            if (rowsAffected == 1) {
                ResultSet rs = pStmt.getGeneratedKeys();
                generatedKey = readKeyRS(rs);
                if (generatedKey > 0) {
                    //TRY TO ADD ORDER-ITEMS HERE
                    List<OrderItem> orderItems = order.getOrderItems();
                    if (orderItems !=  null) {
                        PreparedStatement itemsStatement = con.prepareStatement(QueryStrings.INSERT_ORDER_ITEM);
                        for (OrderItem item : orderItems) {
                            itemsStatement.setInt(1, generatedKey);
                            itemsStatement.setInt(2, item.getMenuItem().getId());
                            itemsStatement.setInt(3, item.getQuantity());
                            itemsStatement.addBatch();
                        }
                        itemsStatement.executeBatch();
                    }
                    result = Either.right(generatedKey);
                } else {
                    con.rollback();
                    result = Either.left(new RestaurantError(ErrorConstants.NO_GENERATED_KEY_ERROR_CODE, ErrorConstants.NO_GENERATED_KEY));
                }
            } else {
                con.rollback();
                result = Either.left(new RestaurantError(ErrorConstants.NO_ROWS_AFFECTED_ERROR_CODE, ErrorConstants.NO_ROWS_AFFECTED));
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            result = Either.left(new RestaurantError(e.getErrorCode(), ErrorConstants.GENERAL_DATABASE_ERROR));
        }
        return result;
    }

    @Override
    public Either<RestaurantError, Integer> update(Order order) {
        Either<RestaurantError, Integer> result;

        try (Connection con = pool.getConnection()) {
            con.setAutoCommit(false);
            result = updateOrder(order, con);
            if (result.isLeft()) {
                return result;
            } else {
                con.commit();
            }
            con.setAutoCommit(true);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            result = Either.left(new RestaurantError(e.getErrorCode(), ErrorConstants.GENERAL_DATABASE_ERROR));
        }
        return result;
    }

    private Either<RestaurantError, Integer> updateOrder(Order order, Connection con) {
        int rowsAffected;
        Either<RestaurantError, Integer> result;
        try {
            if (order.getCustomerId() == 0) {
                result = updateOrderItems(con, order);
            } else {
                updateOrderItems(con, order);

                pStmt = con.prepareStatement(QueryStrings.UPDATE_ORDER);
                pStmt.setInt(1, order.getTableId());
                pStmt.setInt(2, order.getCustomerId());
                pStmt.setTimestamp(3, Timestamp.valueOf(order.getOrderDate()));
                pStmt.setInt(4, order.getId());

                rowsAffected = pStmt.executeUpdate();
                if (rowsAffected < 0) {
                    con.rollback();
                }
                result = Either.right(rowsAffected);
            }

        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            result = Either.left(new GeneralDatabaseError(e.getErrorCode(), ErrorConstants.GENERAL_DATABASE_ERROR));
        }
        return result;
    }

    private Either<RestaurantError, Integer> updateOrderItems(Connection con, Order order) {
        int rowsAffected = 0;
        try {
            if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
                //FIRST DELETE ALL ITEMS ASSOCIATED WITH THAT ORDER
                PreparedStatement deleteStatement = con.prepareStatement(QueryStrings.DELETE_ORDER_ITEMS_BY_ORDER_ID);
                deleteStatement.setInt(1, order.getId());

                rowsAffected = deleteStatement.executeUpdate();
                if (rowsAffected < 0) {
                    con.rollback();
                }

                PreparedStatement itemsStatement = con.prepareStatement(QueryStrings.INSERT_ORDER_ITEM);
                for (OrderItem item : order.getOrderItems()) {
                    MenuItem menuItem = item.getMenuItem();
                    itemsStatement.setInt(1, order.getId());
                    itemsStatement.setInt(2, menuItem.getId());
                    itemsStatement.setInt(3, item.getQuantity());
                    itemsStatement.addBatch();
                }
                int[] affectedRowsArray = itemsStatement.executeBatch();
                for (int affectedRows : affectedRowsArray) {
                    if (affectedRows < 0) {
                        con.rollback();
                    }
                }
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            return Either.left(new GeneralDatabaseError(e.getErrorCode(), ErrorConstants.GENERAL_DATABASE_ERROR));
        }
        return Either.right(rowsAffected);
    }

    @Override
    public Either<RestaurantError, Integer> delete(Order order) {
        Either<RestaurantError, Integer> result;

        try (Connection con = pool.getConnection()) {
            con.setAutoCommit(false);

            result = deleteOrder(con, order);
            if (result.isLeft()) {
                return result;
            } else {
                con.commit();
            }
            con.setAutoCommit(true);
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
            result = Either.left(new RestaurantError(e.getErrorCode(), ErrorConstants.GENERAL_DATABASE_ERROR));
        }
        return result;
    }

    private Either<RestaurantError, Integer> deleteOrder(Connection con, Order order) {
        int rowsAffected;
        Either<RestaurantError, Integer> result;
        try {
            if (order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
                PreparedStatement itemsStatement = con.prepareStatement(QueryStrings.DELETE_ORDER_ITEMS_BY_ORDER_ID);
                itemsStatement.setInt(1, order.getId());
                rowsAffected = itemsStatement.executeUpdate();
                if (rowsAffected < 0) {
                    con.rollback();
                }
            }
            pStmt = con.prepareStatement(QueryStrings.DELETE_ORDER);
            pStmt.setInt(1, order.getId());
            rowsAffected = pStmt.executeUpdate();

            if (rowsAffected < 0) {
                con.rollback();
            }
            result = Either.right(rowsAffected);
        } catch (SQLException e) {
            if (e.getErrorCode() == 1451) {
                result = Either.left(new RestaurantError(e.getErrorCode(), ErrorConstants.FOREIGN_KEY_CONSTRAINT_ERROR));
            } else {
                log.error(e.getMessage(), e);
                result = Either.left(new RestaurantError(e.getErrorCode(), ErrorConstants.GENERAL_DATABASE_ERROR));
            }
        }
        return result;
    }

    private int readKeyRS(ResultSet rs) {
        int key = 0;
        try {
            while (rs.next()) {
                key = rs.getInt(1);
            }
        } catch (SQLException e) {
            log.error(e.getMessage(), e);
        }
        return key;
    }


}
