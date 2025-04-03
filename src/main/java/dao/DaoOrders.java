package dao;

import io.vavr.control.Either;
import model.Order;
import model.error.RestaurantError;

import java.util.ArrayList;

public interface DaoOrders {
    Either<RestaurantError, ArrayList<Order>> getAll();

    Either<RestaurantError, ArrayList<Order>> getAll(Order order);

    Either<RestaurantError, Order> get(Order order);

    Either<RestaurantError, Integer> save(Order order);

    Either<RestaurantError, Integer> update(Order order);

    Either<RestaurantError, Integer> delete(Order order);
}
