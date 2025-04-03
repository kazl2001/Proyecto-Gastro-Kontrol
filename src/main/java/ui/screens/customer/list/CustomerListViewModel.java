package ui.screens.customer.list;

import jakarta.inject.Inject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import services.CustomerService;
import ui.screens.common.BaseViewModel;

public final class CustomerListViewModel extends BaseViewModel {

    private final CustomerService cs;

    @Inject
    public CustomerListViewModel(CustomerService cs) {
        super(new CustomerListState(null, null));
        this.cs = cs;
        this._state = new SimpleObjectProperty<>(new CustomerListState(null, null));
    }

    private final ObjectProperty<CustomerListState> _state;

    public ReadOnlyObjectProperty<CustomerListState> getState() {
        return _state;
    }

    public void getCustomerList() {
        cs.getAll()
                .peek(customerList -> _state.setValue(new CustomerListState(null, customerList)))
                .peekLeft(error -> _state.setValue(new CustomerListState(error, null)));
    }


}
