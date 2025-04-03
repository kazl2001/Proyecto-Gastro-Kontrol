package dao;

import io.vavr.control.Either;
import model.Order;
import model.error.RestaurantError;

import java.util.List;

public interface DaoOrdersXML {
    Either<RestaurantError, Integer> save(List<Order> orderList);
}
