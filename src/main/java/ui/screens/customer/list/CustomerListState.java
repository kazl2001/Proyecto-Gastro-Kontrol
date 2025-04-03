package ui.screens.customer.list;

import lombok.Getter;
import model.Customer;
import model.error.RestaurantError;
import ui.screens.common.BaseState;

import java.util.List;

@Getter
public final class CustomerListState extends BaseState {

    private final List<Customer> customerList;

    public CustomerListState(RestaurantError error, List<Customer> customerList) {
        super(error);
        this.customerList = customerList;
    }
}
