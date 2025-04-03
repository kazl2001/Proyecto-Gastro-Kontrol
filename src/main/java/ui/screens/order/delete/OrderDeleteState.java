package ui.screens.order.delete;

import lombok.Getter;
import model.Order;
import model.OrderItem;
import model.error.RestaurantError;
import ui.screens.common.BaseState;

import java.util.List;

@Getter
public final class OrderDeleteState extends BaseState {

    private final List<Order> orderList;
    private final List<OrderItem> orderItemList;
    private final boolean hasOrderItems;

    public OrderDeleteState(RestaurantError error, List<Order> orderList, List<OrderItem> orderItemList, boolean hasOrderItems) {
        super(error);
        this.orderList = orderList;
        this.orderItemList = orderItemList;
        this.hasOrderItems = hasOrderItems;
    }
}
