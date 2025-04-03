package ui.screens.customer.add;

import lombok.Getter;
import lombok.Setter;
import model.Customer;
import model.error.RestaurantError;
import ui.screens.common.BaseState;

import java.util.List;

@Getter
public final class CustomerAddState extends BaseState {

    private final List<Customer> customerList;

    public CustomerAddState(RestaurantError error, List<Customer> customerList) {
        super(error);
        this.customerList = customerList;
    }
}

