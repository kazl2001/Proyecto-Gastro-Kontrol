package ui.screens.order.list;

import jakarta.inject.Inject;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import model.Order;
import model.OrderItem;
import services.CustomerService;
import services.OrderItemService;
import services.OrderService;
import ui.screens.common.BaseViewModel;

import java.time.LocalDate;
import java.util.List;

public final class OrderListViewModel extends BaseViewModel {

    private final OrderService os;
    private final CustomerService cs;
    private final OrderItemService ois;

    @Inject
    public OrderListViewModel(OrderService os, CustomerService cs, OrderItemService ois) {
        super(new OrderListState(null, null, null, null, null, 0));
        this.os = os;
        this.cs = cs;
        this.ois = ois;
        this._state = new SimpleObjectProperty<>(new OrderListState(null, null, null, null, null, 0));
    }

    private final ObjectProperty<OrderListState> _state;

    public ReadOnlyObjectProperty<OrderListState> getState() {
        return _state;
    }

    public void getOrderList(){
        _state.setValue(new OrderListState(null, null, null, null, null, _state.getValue().getTotalAmount()));
        os.getAll().peek(orderList -> _state.setValue(new OrderListState(null, orderList, null, null, null, _state.getValue().getTotalAmount()))).peekLeft(error -> _state.setValue(new OrderListState(error, null, null, null, null, _state.getValue().getTotalAmount())));
    }

    public void getOrderListByCustomerId(int id) {
        _state.setValue(new OrderListState(null, null, null, null, null, _state.getValue().getTotalAmount()));
        if (os.getAll(id).isRight())
            _state.setValue(new OrderListState(null, os.getAll(id).get(), null, null, null, _state.getValue().getTotalAmount()));
        else
            _state.setValue(new OrderListState(os.getAll(id).getLeft(), null, null, null, null, _state.getValue().getTotalAmount()));
    }

    public void getOrderListByOrderDate(LocalDate date) {
        _state.setValue(new OrderListState(null, null, null, null, null, _state.getValue().getTotalAmount()));
        if (os.getAllByDate(date).isRight())
            _state.setValue(new OrderListState(null, os.getAllByDate(date).get(), null, null, null, _state.getValue().getTotalAmount()));
        else
            _state.setValue(new OrderListState(os.getAllByDate(date).getLeft(), null, null, null, null, _state.getValue().getTotalAmount()));
    }

    public void getCustomerList() {
        cs.getAll()
                .peek(customerList -> _state.setValue(new OrderListState(null, null, customerList, null, null, _state.getValue().getTotalAmount())))
                .peekLeft(error -> _state.setValue(new OrderListState(error, null, null, null, null, _state.getValue().getTotalAmount())));
    }

    private void setTotalAmount(List<OrderItem> orderItemList) {
        ois.getTotalPrice(orderItemList)
                .peek(total -> _state.setValue(new OrderListState(null, null, null, null, null, total)))
                .peekLeft(error -> _state.setValue(new OrderListState(null, null, null, null, null, 0)));

    }

    public void getOrderItems(Order order) {
        ois.getAll(order.getId())
                .peek(orderItems -> {
                    _state.setValue(new OrderListState(null, null, null, orderItems, null, _state.getValue().getTotalAmount()));
                    setTotalAmount(orderItems);
                })
                .peekLeft(error -> _state.setValue(new OrderListState(error, null, null, null, null, _state.getValue().getTotalAmount())));
    }


}
