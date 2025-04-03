package services;

import io.vavr.control.Either;
import model.OrderItem;
import model.error.RestaurantError;

import java.util.ArrayList;
import java.util.List;

public interface OrderItemService {
    Either<RestaurantError, ArrayList<OrderItem>> getAll(int id);
    Either<RestaurantError,Double> getTotalPrice(List<OrderItem> orderItemList);
}
