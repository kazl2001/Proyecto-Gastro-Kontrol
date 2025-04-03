package ui.screens.common;

import lombok.Getter;
import ui.screens.customer.add.CustomerAddViewModel;
import ui.screens.customer.delete.CustomerDeleteViewModel;
import ui.screens.customer.list.CustomerListViewModel;
import ui.screens.customer.update.CustomerUpdateViewModel;
import ui.screens.login.LoginViewModel;
import ui.screens.order.add.OrderAddViewModel;
import ui.screens.order.delete.OrderDeleteViewModel;
import ui.screens.order.list.OrderListViewModel;
import ui.screens.order.update.OrderUpdateViewModel;

public sealed class BaseViewModel permits CustomerAddViewModel, CustomerDeleteViewModel, CustomerListViewModel, CustomerUpdateViewModel, LoginViewModel, OrderAddViewModel, OrderDeleteViewModel, OrderListViewModel, OrderUpdateViewModel {


    public BaseViewModel(BaseState state) {
    }
}
