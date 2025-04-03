package ui.screens.order.add;


import jakarta.inject.Inject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import model.Order;
import services.*;
import ui.screens.common.BaseViewModel;

public final class OrderAddViewModel extends BaseViewModel {

    private final OrderService os;
    private final CustomerService cs;
    private final MenuItemService mis;
    private final TableService ts;

    @Inject
    public OrderAddViewModel(OrderService os, CustomerService cs, MenuItemService mis, TableService ts) {
        super(new OrderAddState(null, null, null, 0, null, null));
        this.os = os;
        this.cs = cs;
        this.mis = mis;
        this.ts = ts;
        this._state = new SimpleObjectProperty<>(new OrderAddState(null, null, null, 0, null, null));
    }

    private final ObjectProperty<OrderAddState> _state;

    public ReadOnlyObjectProperty<OrderAddState> getState() {
        return _state;
    }

    public void loadCustomers() {
        OrderAddState currentState = _state.get();
        cs.getAll()
                .peek(customers -> {
                    OrderAddState newState = new OrderAddState(null, customers, null, currentState.getNewOrderId(), null, null);
                    _state.setValue(newState);
                })
                .peekLeft(error -> {
                    OrderAddState newState = new OrderAddState(error, null, null, currentState.getNewOrderId(), null, null);
                    _state.setValue(newState);
                });
    }

    public void addOrder(Order order) {
        os.save(order)
                .peek(key -> {
                    OrderAddState newState = new OrderAddState(null, null, null, key, null, null);
                    _state.setValue(newState);
                })
                .peekLeft(error -> {
                    OrderAddState newState = new OrderAddState(error, null, null, _state.get().getNewOrderId(), null, null);
                    _state.setValue(newState);
                });
    }

    public void loadMenuItems() {
        OrderAddState currentState = _state.get();
        mis.getAll()
                .peek(menuItems -> {
                    OrderAddState newState = new OrderAddState(null, null, null, currentState.getNewOrderId(), menuItems, null);
                    _state.setValue(newState);
                })
                .peekLeft(error -> {
                    OrderAddState newState = new OrderAddState(error, null, null, currentState.getNewOrderId(), null, null);
                    _state.setValue(newState);
                });
    }

    public void loadTableList() {
        OrderAddState currentState = _state.get();
        ts.getAll()
                .peek(tables -> {
                    OrderAddState newState = new OrderAddState(null, null, null, currentState.getNewOrderId(), null, tables);
                    _state.setValue(newState);
                })
                .peekLeft(error -> {
                    OrderAddState newState = new OrderAddState(error, null, null, currentState.getNewOrderId(), null, null);
                    _state.setValue(newState);
                });
    }
}
