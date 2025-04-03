package ui.screens.order.update;

import lombok.Getter;
import model.*;
import model.error.RestaurantError;
import ui.screens.common.BaseState;

import java.util.List;

@Getter
public final class OrderUpdateState extends BaseState {

    private final List<Order> orderList;
    private final List<OrderItem> orderItemList;
    private final List<Customer> customerList;
    private final List<Table> tableList;
    private final List<MenuItem> menuItemList;

    public OrderUpdateState(RestaurantError error, List<Order> orderList, List<OrderItem> orderItemList, List<Customer> customerList, List<Table> tableList, List<MenuItem> menuItemList) {
        super(error);
        this.orderList = orderList;
        this.orderItemList = orderItemList;
        this.customerList = customerList;
        this.tableList = tableList;
        this.menuItemList = menuItemList;
    }
}
