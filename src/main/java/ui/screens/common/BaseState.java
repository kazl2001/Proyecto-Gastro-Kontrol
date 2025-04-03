package ui.screens.common;

import lombok.Getter;
import model.error.RestaurantError;
import ui.screens.customer.add.CustomerAddState;
import ui.screens.customer.delete.CustomerDeleteState;
import ui.screens.customer.list.CustomerListState;
import ui.screens.customer.update.CustomerUpdateState;
import ui.screens.login.LoginState;
import ui.screens.order.add.OrderAddState;
import ui.screens.order.delete.OrderDeleteState;
import ui.screens.order.list.OrderListState;
import ui.screens.order.update.OrderUpdateState;

@Getter
public sealed class BaseState permits CustomerAddState, CustomerDeleteState, CustomerListState, CustomerUpdateState, LoginState, OrderAddState, OrderDeleteState, OrderListState, OrderUpdateState {

    private final RestaurantError error;


    public BaseState(RestaurantError error) {
        this.error = error;
    }
}
