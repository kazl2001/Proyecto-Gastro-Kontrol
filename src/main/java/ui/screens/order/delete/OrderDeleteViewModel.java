package ui.screens.order.delete;

import jakarta.inject.Inject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import model.Order;
import services.OrderItemService;
import services.OrderService;
import ui.screens.common.BaseViewModel;

public final class OrderDeleteViewModel extends BaseViewModel {

    private final OrderService os;
    private final OrderItemService ois;

    @Inject
    public OrderDeleteViewModel(OrderService os, OrderItemService ois) {
        super(new OrderDeleteState(null, null, null, false));
        this.os = os;
        this.ois = ois;
        this._state = new SimpleObjectProperty<>(new OrderDeleteState(null, null, null, false));
    }

    private final ObjectProperty<OrderDeleteState> _state;

    public ReadOnlyObjectProperty<OrderDeleteState> getState() {
        return _state;
    }

    public void getOrderList() {
        os.getAll()
                .peek(orderList -> _state.setValue(new OrderDeleteState(null, orderList, null, false)))
                .peekLeft(error -> _state.setValue(new OrderDeleteState(error, null, null, false)));
    }

    public void getOrderListByCustomerId(int id){
        os.getAll(id)
                .peek(orderList -> _state.setValue(new OrderDeleteState(null, orderList, null, false)))
                .peekLeft(error -> _state.setValue(new OrderDeleteState(error, null, null, false)));
    }

    public void deleteOrder(Order order) {
        os.delete(order)
                .peekLeft(error -> _state.setValue(new OrderDeleteState(error, null, null, false)));
    }

    public void getItems(Order order){
        ois.getAll(order.getId())
                .peek(orderItems -> _state.setValue(new OrderDeleteState(null, null, orderItems, false)))
                .peekLeft(error -> _state.setValue(new OrderDeleteState(error, null, null, false)));
    }

}
