package dao;

import io.vavr.control.Either;
import model.OrderItem;
import model.error.RestaurantError;

import java.util.ArrayList;

public interface DaoOrderItems {
    Either<RestaurantError, ArrayList<OrderItem>> getAll(OrderItem item);

    Either<RestaurantError, OrderItem> get(OrderItem item);
}
