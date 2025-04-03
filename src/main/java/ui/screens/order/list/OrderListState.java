package ui.screens.order.list;

import lombok.Getter;
import model.Customer;
import model.MenuItem;
import model.Order;
import model.OrderItem;
import model.error.RestaurantError;
import ui.screens.common.BaseState;

import java.util.List;

@Getter
public final class OrderListState extends BaseState {

    private final List<Order> orderList;
    private final List<Customer> customerList;
    private final List<OrderItem> orderItemList;
    private final List<MenuItem> menuItemList;
    private final double totalAmount;

    public OrderListState(RestaurantError error, List<Order> orderList, List<Customer> customerList, List<OrderItem> orderItemList, List<MenuItem> menuItemList, double totalAmount) {
        super(error);
        this.orderList = orderList;
        this.customerList = customerList;
        this.orderItemList = orderItemList;
        this.menuItemList = menuItemList;
        this.totalAmount = totalAmount;
    }
}
