package ui.screens.order.list;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTableView;
import jakarta.inject.Inject;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import model.Customer;
import model.Order;
import model.OrderItem;
import ui.screens.common.BaseScreenController;

import java.net.URL;
import java.util.ResourceBundle;

public class OrderListController extends BaseScreenController implements Initializable {

    @FXML
    private MFXDatePicker ordersLDatePicker;
    @FXML
    private Text amountText;
    @FXML
    private MFXTableView<OrderItem> orderItemsLTableView;
    @FXML
    private MFXComboBox<Customer> customerNameComboBox;
    @FXML
    private MFXTableView<Order> ordersLTableView;
    private Order selectedOrder;
    private final OrderListViewModel vm;

    @Inject
    public OrderListController(OrderListViewModel vm) {
        this.vm = vm;
    }

    @Override
    public void loadedPrincipal() {
        if(getPrincipalController().isAdmin()){
            vm.getOrderList();
        } else {
            vm.getOrderListByCustomerId(getPrincipalController().getCustomerId());
        }
        getPrincipalController().createOrdersTable(ordersLTableView);
        getPrincipalController().createOrderItemsTable(orderItemsLTableView);
        vm.getCustomerList();

        ordersLTableView.getSelectionModel().selectionProperty().addListener((observableValue, order, orderNew) -> {
            if (ordersLTableView.getSelectionModel().getSelectedValues().get(0) != null) {
                selectedOrder = ordersLTableView.getSelectionModel().getSelectedValues().get(0);
            }
            if (selectedOrder != null) {
                vm.getOrderItems(selectedOrder);
            }
        });

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stateListeners();
    }

    private void stateListeners() {
        vm.getState().addListener((observableValue, orderUpdateState, orderUpdateStateNew) -> {
            if (orderUpdateStateNew.getError() != null) {
                getPrincipalController().showErrorAlert(orderUpdateStateNew.getError().getErrorMessage() + orderUpdateStateNew.getError().getErrorNum());
            }
            if (orderUpdateStateNew.getOrderList() != null) {
                ordersLTableView.getItems().clear();
                ordersLTableView.getItems().addAll(orderUpdateStateNew.getOrderList());
            }
            if(!Double.isNaN(orderUpdateStateNew.getTotalAmount())){
                amountText.setText(String.valueOf(orderUpdateStateNew.getTotalAmount()));
            }
            if (orderUpdateStateNew.getOrderItemList() != null) {
                orderItemsLTableView.getItems().clear();
                orderItemsLTableView.getItems().addAll(orderUpdateStateNew.getOrderItemList());
            }
            if (orderUpdateStateNew.getCustomerList() != null) {
                customerNameComboBox.getItems().clear();
                orderUpdateStateNew.getCustomerList().forEach(customer -> customerNameComboBox.getItems().add(customer));
            }
        });
    }

    @FXML
    private void filterByDate() {
        vm.getOrderListByOrderDate(ordersLDatePicker.getValue());
    }

    @FXML
    private void filterByCustomer() {
        vm.getOrderListByCustomerId(customerNameComboBox.getSelectionModel().getSelectedItem().getId());
    }
    @FXML
    private void clearFilters() {
        // Clean the filters fields
        ordersLDatePicker.setValue(null);
        customerNameComboBox.clear();

        if(getPrincipalController().isAdmin()){
            vm.getOrderList();
        } else {
            vm.getOrderListByCustomerId(getPrincipalController().getCustomerId());
        }
    }


}
