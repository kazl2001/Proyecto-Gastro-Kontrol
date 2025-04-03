package ui.screens.customer.update;

import jakarta.inject.Inject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import model.Customer;
import services.CustomerService;
import ui.screens.common.BaseViewModel;

public final class CustomerUpdateViewModel extends BaseViewModel {

    private final CustomerService cs;

    @Inject
    public CustomerUpdateViewModel(CustomerService cs) {
        super(new CustomerUpdateState(null, null));
        this.cs = cs;
        this._state = new SimpleObjectProperty<>(new CustomerUpdateState(null, null));
    }

    private final ObjectProperty<CustomerUpdateState> _state;

    public ObjectProperty<CustomerUpdateState> getState() {
        return _state;
    }

    public void getCustomerList() {
        cs.getAll()
                .peek(customerList -> _state.setValue(new CustomerUpdateState(null, customerList)))
                .peekLeft(error -> _state.setValue(new CustomerUpdateState(error, null)));
    }

    public void updateCustomer(Customer newCustomer) {
        cs.update(newCustomer)
                .peek(customerList -> _state.setValue(new CustomerUpdateState(null, null)))
                .peekLeft(error -> _state.setValue(new CustomerUpdateState(error, null)));
        getCustomerList();
    }


}
