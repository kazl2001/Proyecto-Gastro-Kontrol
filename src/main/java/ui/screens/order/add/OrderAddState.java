package ui.screens.order.add;

import lombok.Getter;
import model.Customer;
import model.MenuItem;
import model.OrderItem;
import model.Table;
import model.error.RestaurantError;
import ui.screens.common.BaseState;

import java.util.List;

@Getter
public final class OrderAddState extends BaseState {

    private final List<Customer> customerList;
    private final List<OrderItem> orderItemList;
    private final int newOrderId;
    private final List<MenuItem> menuItemList;
    private final List<Table> tableList;

    public OrderAddState(RestaurantError error, List<Customer> customerList, List<OrderItem> orderItemList, int newOrderId, List<MenuItem> menuItemList, List<Table> tableList) {
        super(error);
        this.customerList = customerList;
        this.orderItemList = orderItemList;
        this.newOrderId = newOrderId;
        this.menuItemList = menuItemList;
        this.tableList = tableList;
    }
}
