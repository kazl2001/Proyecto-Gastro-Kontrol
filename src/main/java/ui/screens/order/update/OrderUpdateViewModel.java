package ui.screens.order.update;

import jakarta.inject.Inject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import model.Order;
import services.*;
import ui.screens.common.BaseViewModel;

public final class OrderUpdateViewModel extends BaseViewModel {

    private final OrderService os;
    private final OrderItemService ois;
    private final CustomerService cs;
    private final MenuItemService mis;
    private final TableService ts;


    @Inject
    public OrderUpdateViewModel(OrderService os, OrderItemService ois, CustomerService cs, MenuItemService mis, TableService ts) {
        super(new OrderUpdateState(null, null, null, null, null, null));
        this.os = os;
        this.ois = ois;
        this.cs = cs;
        this.mis = mis;
        this.ts = ts;
        this._state = new SimpleObjectProperty<>(new OrderUpdateState(null, null, null, null, null, null));
    }

    private final ObjectProperty<OrderUpdateState> _state;

    public ReadOnlyObjectProperty<OrderUpdateState> getState() {
        return _state;
    }

    public void getOrderList() {
        os.getAll()
                .peek(orderList -> _state.setValue(new OrderUpdateState(null, orderList, null, _state.get().getCustomerList(), null, null)))
                .peekLeft(error -> _state.setValue(new OrderUpdateState(error, null, null, null, null, null)));
    }

    public void getOrderListByCustomerId(int id) {
        os.getAll(id)
                .peek(orderList -> _state.setValue(new OrderUpdateState(null, orderList, null, _state.get().getCustomerList(), null, null)))
                .peekLeft(error -> _state.setValue(new OrderUpdateState(error, null, null, null, null, null)));
    }

    public void loadCustomers() {
        cs.getAll()
                .peek(customers -> {
                    OrderUpdateState newState = new OrderUpdateState(null, null, null, customers, null, null);
                    _state.setValue(newState);
                })
                .peekLeft(error -> {
                    OrderUpdateState newState = new OrderUpdateState(error, null, null, null, null, null);
                    _state.setValue(newState);
                });
    }

    void updateOrder(Order newOrder) {
        os.update(newOrder)
                .peekLeft(error -> _state.setValue(new OrderUpdateState(error, null, null, null, null, null)));
    }

    public void loadOrderItems(int id) {
        ois.getAll(id)
                .peek(orderItems -> {
                    OrderUpdateState newState = new OrderUpdateState(null, null, orderItems, null, null, null);
                    _state.setValue(newState);
                })
                .peekLeft(error -> {
                    OrderUpdateState newState = new OrderUpdateState(error, null, null, null, null, null);
                    _state.setValue(newState);
                });
    }

    public void loadMenuItems() {
        mis.getAll()
                .peek(menuItems -> {
                    OrderUpdateState newState = new OrderUpdateState(null, null, null, null, null, menuItems);
                    _state.setValue(newState);
                })
                .peekLeft(error -> {
                    OrderUpdateState newState = new OrderUpdateState(error, null, null, null, null, null);
                    _state.setValue(newState);
                });
    }

    public void loadTables() {
        ts.getAll()
                .peek(tables -> {
                    OrderUpdateState newState = new OrderUpdateState(null, null, null, null, tables, null);
                    _state.setValue(newState);
                })
                .peekLeft(error -> {
                    OrderUpdateState newState = new OrderUpdateState(error, null, null, null, null, null);
                    _state.setValue(newState);
                });
    }


}
