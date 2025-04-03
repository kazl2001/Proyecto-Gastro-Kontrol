package ui.screens.customer.delete;

import lombok.Getter;
import model.Customer;
import model.Order;
import model.error.RestaurantError;
import ui.screens.common.BaseState;

import java.util.List;

@Getter
public final class CustomerDeleteState extends BaseState {

    private final List<Customer> customerList;
    private final List<Order> orderList;


    public CustomerDeleteState(RestaurantError error, List<Customer> customerList, List<Order> orderList) {
        super(error);
        this.customerList = customerList;
        this.orderList = orderList;
    }
}
