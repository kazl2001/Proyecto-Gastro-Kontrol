package ui.screens.customer.update;

import lombok.Getter;
import model.Customer;
import model.error.RestaurantError;
import ui.screens.common.BaseState;

import java.util.List;

@Getter
public final class CustomerUpdateState extends BaseState {

    private final List<Customer> customerList;

    public CustomerUpdateState(RestaurantError error, List<Customer> customerList) {
        super(error);
        this.customerList = customerList;
    }
}
