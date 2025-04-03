package services;

import io.vavr.control.Either;
import model.Order;
import model.error.RestaurantError;

import java.time.LocalDate;
import java.util.ArrayList;

public interface OrderService {
    Either<RestaurantError, ArrayList<Order>> getAll();

    Either<RestaurantError, Order> get(int id);

    Either<RestaurantError, ArrayList<Order>> getAll(int id);

    Either<RestaurantError, ArrayList<Order>> getAllByDate(LocalDate date);

    Either<RestaurantError, Integer> save(Order order);

    Either<RestaurantError, Integer> delete(Order order);

    Either<RestaurantError, Integer> update(Order newOrder);

}
