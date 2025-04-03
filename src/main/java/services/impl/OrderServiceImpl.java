package services.impl;

import common.constants.Constants;
import common.constants.ErrorConstants;
import dao.DaoOrders;
import io.vavr.control.Either;
import jakarta.inject.Inject;
import model.Order;
import model.error.RestaurantError;
import model.error.order.OrderDataRetrievalError;
import model.error.order.OrderValidationError;
import services.OrderService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderServiceImpl implements OrderService {

    private final DaoOrders daoOrders;

    @Inject
    public OrderServiceImpl(DaoOrders daoOrders) {
        this.daoOrders = daoOrders;
    }

    @Override
    public Either<RestaurantError, ArrayList<Order>> getAll() {
        return daoOrders.getAll();
    }

    @Override
    public Either<RestaurantError, Order> get(int id) {
        Order order = new Order(id);
        return daoOrders.get(order);
    }

    @Override
    public Either<RestaurantError, ArrayList<Order>> getAll(int id) {
        Order order = new Order(id);
        return daoOrders.getAll(order);
    }

    @Override
    public Either<RestaurantError, ArrayList<Order>> getAllByDate(LocalDate date) {
        Either<RestaurantError, ArrayList<Order>> result = daoOrders.getAll();
        if (result.isRight()) {
            ArrayList<Order> allOrders = result.get();
            ArrayList<Order> dateOrders;
            try {
                dateOrders = allOrders.stream()
                        .filter(order -> order.getOrderDate().toLocalDate().equals(date))
                        .collect(Collectors.toCollection(ArrayList::new));
            } catch (Exception e) {
                return Either.left(new OrderDataRetrievalError(ErrorConstants.ENTITY_NOT_FOUND_ERROR_CODE, ErrorConstants.ENTITY_NOT_FOUND_ERROR));
            }
            return Either.right(dateOrders);
        } else {
            return Either.left(result.getLeft());
        }
    }

    @Override
    public Either<RestaurantError, Integer> save(Order order) {
        if (order != null) {
            return daoOrders.save(order);
        } else {
            return Either.left(new OrderValidationError(ErrorConstants.INVALID_ENTITY_DATA_ERROR_CODE, ErrorConstants.INVALID_ENTITY_DATA_ERROR));
        }
    }

    @Override
    public Either<RestaurantError, Integer> delete(Order order) {
        if (order != null) {
            return daoOrders.delete(order);
        } else {
            return Either.left(new OrderValidationError(ErrorConstants.INVALID_ENTITY_DATA_ERROR_CODE, ErrorConstants.INVALID_ENTITY_DATA_ERROR));
        }
    }

    @Override
    public Either<RestaurantError, Integer> update(Order newOrder) {
        if (newOrder == null) {
            return Either.left(new OrderValidationError(ErrorConstants.INVALID_ENTITY_DATA_ERROR_CODE, ErrorConstants.INVALID_ENTITY_DATA_ERROR));
        } else {
            return daoOrders.update(newOrder);
        }
    }

}
