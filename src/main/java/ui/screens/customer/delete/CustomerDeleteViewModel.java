package ui.screens.customer.delete;

import jakarta.inject.Inject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import model.Customer;
import services.CustomerService;
import services.OrderService;
import ui.screens.common.BaseViewModel;

public final class CustomerDeleteViewModel extends BaseViewModel {

    private final CustomerService cs;

    private final OrderService os;

    @Inject
    public CustomerDeleteViewModel(CustomerService cs, OrderService os) {
        super(new CustomerDeleteState(null, null, null));
        this.cs = cs;
        this.os = os;
        this._state = new SimpleObjectProperty<>(new CustomerDeleteState(null, null, null));
    }

    private final ObjectProperty<CustomerDeleteState> _state;

    public ReadOnlyObjectProperty<CustomerDeleteState> getState() {
        return _state;
    }

    public void getCustomerList() {
        cs.getAll()
                .peek(customerList -> _state.setValue(new CustomerDeleteState(null, customerList, null)))
                .peekLeft(error -> _state.setValue(new CustomerDeleteState(error, null, null)));
    }

    public void getOrderList(int id) {
        os.getAll(id)
                .peek(orderList -> _state.setValue(new CustomerDeleteState(null, null, orderList)))
                .peekLeft(error -> _state.setValue(new CustomerDeleteState(error, null, null)));
    }

    public void deleteCustomer(Customer customer, Boolean confirmed) {
        cs.delete(customer, confirmed)
                .peek(integer -> getCustomerList())
                .peekLeft(error -> {
                    CustomerDeleteState newState = new CustomerDeleteState(error, null, null);
                    _state.setValue(newState);
                });
    }
}
