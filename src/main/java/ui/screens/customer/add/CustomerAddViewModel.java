package ui.screens.customer.add;

import jakarta.inject.Inject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import model.Customer;
import services.CustomerService;
import ui.screens.common.BaseViewModel;

public final class CustomerAddViewModel extends BaseViewModel {

    private final CustomerService cs;

    @Inject
    public CustomerAddViewModel(CustomerService cs) {
        super(new CustomerAddState(null, null));
        this.cs = cs;
        this._state = new SimpleObjectProperty<>(new CustomerAddState(null, null));
    }

    private final ObjectProperty<CustomerAddState> _state;

    public ReadOnlyObjectProperty<CustomerAddState> getState() {
        return _state;
    }

    public void getCustomerList() {
        cs.getAll()
                .peek(customerList -> _state.setValue(new CustomerAddState(null, customerList)))
                .peekLeft(error -> _state.setValue(new CustomerAddState(error, null)));
    }

    public void addCustomer(Customer customer) {
        cs.save(customer).peek(key -> _state.setValue(new CustomerAddState(null, null)))
                .peekLeft(error -> _state.setValue(new CustomerAddState(error, null)));
        if (_state.getValue().getError() == null) {
            getCustomerList();
        }
    }

}
