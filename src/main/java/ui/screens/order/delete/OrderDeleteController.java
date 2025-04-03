package ui.screens.order.delete;

import io.github.palexdev.materialfx.controls.MFXTableView;
import jakarta.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import model.Order;
import model.OrderItem;
import ui.screens.common.BaseScreenController;
import ui.screens.common.ScreenConstants;

import java.net.URL;
import java.util.ResourceBundle;

public class OrderDeleteController extends BaseScreenController implements Initializable {

    @FXML
    private MFXTableView<Order> orderDTableView;
    @FXML
    private MFXTableView<OrderItem> orderItemsDTableView;
    private Order selectedOrder;
    private final OrderDeleteViewModel vm;

    @Inject
    public OrderDeleteController(OrderDeleteViewModel vm) {
        this.vm = vm;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stateChangeOrderList();
    }

    @Override
    public void loadedPrincipal() {
        if (getPrincipalController().isAdmin()) {
            vm.getOrderList();
        } else {
            vm.getOrderListByCustomerId(getPrincipalController().getCustomerId());
        }

        getPrincipalController().createOrdersTable(orderDTableView);
        getPrincipalController().createOrderItemsTable(orderItemsDTableView);

        orderDTableView.getSelectionModel().selectionProperty().addListener((observableValue, customer, customerNew) -> {
            if (orderDTableView.getSelectionModel().getSelectedValues().get(0) != null) {
                selectedOrder = orderDTableView.getSelectionModel().getSelectedValues().get(0);
            }
            if (selectedOrder != null) {
                vm.getItems(selectedOrder);
                stateChangeOrderItemList();
            }
        });
    }


    private void stateChangeOrderList() {
        vm.getState().addListener((observableValue, orderDeleteState, orderDeleteStateNew) -> {
            if (orderDeleteStateNew.getError() != null) {
                getPrincipalController().showErrorAlert(orderDeleteStateNew.getError().getErrorMessage() + orderDeleteStateNew.getError().getErrorNum());
            }
            if (orderDeleteStateNew.getOrderList() != null) {
                orderDTableView.getItems().clear();
                orderDTableView.getItems().addAll(orderDeleteStateNew.getOrderList());
            }
        });
    }

    private void stateChangeOrderItemList() {
        vm.getState().addListener((observableValue, orderDeleteState, orderDeleteStateNew) -> {
            if (orderDeleteStateNew.getError() != null) {
                getPrincipalController().showErrorAlert(orderDeleteStateNew.getError().getErrorMessage() + orderDeleteStateNew.getError().getErrorNum());
            }
            if (orderDeleteStateNew.getOrderItemList() != null) {
                orderItemsDTableView.getItems().clear();
                orderItemsDTableView.getItems().addAll(orderDeleteStateNew.getOrderItemList());
            }
        });
    }

    @FXML
    private void deleteOrder() {
        if (selectedOrder != null) {
            selectedOrder.setOrderItems(orderItemsDTableView.getItems());
            if (selectedOrder.getOrderItems().isEmpty()) {
                getPrincipalController().showWarningAlert(ScreenConstants.EMPTY_ORDER);
            } else {
                vm.deleteOrder(selectedOrder);
                clearFields();
            }
        }
    }

    private void clearFields(){
        if (vm.getState().get().getError() == null) {
            if (getPrincipalController().isAdmin()) {
                vm.getOrderList();
            } else {
                vm.getOrderListByCustomerId(getPrincipalController().getCustomerId());
            }
            getPrincipalController().showInfoAlert(ScreenConstants.SUCCESSFUL_ACTION + ScreenConstants.ORDER_DELETED);
            orderItemsDTableView.getItems().clear();
            if (!orderDTableView.getItems().isEmpty()) {
                orderDTableView.getSelectionModel().selectIndex(0);
            }
        }
    }
}
