package ui.screens.order.add;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTableView;
import jakarta.inject.Inject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import model.*;
import ui.screens.common.BaseScreenController;
import ui.screens.common.ScreenConstants;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

public class OrderAddController extends BaseScreenController implements Initializable {

    @FXML
    private MFXComboBox<Customer> orderACustomerComboBox;
    @FXML
    private MFXComboBox<Table> orderATableComboBox;
    @FXML
    private MFXComboBox<Integer> orderAQuantityComboBox;
    @FXML
    private MFXComboBox<MenuItem> orderAMenuItemComboBox;
    @FXML
    private MFXTableView<OrderItem> orderItemsATableView;
    private final OrderAddViewModel vm;


    @Inject
    public OrderAddController(OrderAddViewModel vm) {
        this.vm = vm;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        vm.loadCustomers();
    }

    @Override
    public void loadedPrincipal() {
        getPrincipalController().createOrderItemsTable(orderItemsATableView);

        //ComboBox initialization
        initializeQuantityCB();
        initializeCustomerCB();
        initializeTableCB();
        initializeMenuItemCB();

        if (!getPrincipalController().isAdmin()) {
            orderACustomerComboBox.setDisable(true);
        }

    }

    private void initializeQuantityCB() {
        List<Integer> range = IntStream.rangeClosed(1, 10).boxed().toList();
        orderAQuantityComboBox.setItems(FXCollections.observableArrayList(range));
    }

    private void initializeTableCB() {
        vm.loadTableList();
        List<Table> tables = vm.getState().get().getTableList();

        ObservableList<Table> observableTables = FXCollections.observableList(tables);
        orderATableComboBox.setItems(observableTables);
    }

    private void initializeCustomerCB() {
        vm.loadCustomers();
        List<Customer> customers = vm.getState().get().getCustomerList();
        orderACustomerComboBox.setItems(FXCollections.observableArrayList(customers));
    }

    private void initializeMenuItemCB() {
        vm.loadMenuItems();
        List<MenuItem> menuItems = vm.getState().get().getMenuItemList();
        orderAMenuItemComboBox.setItems(FXCollections.observableArrayList(menuItems));
    }

    @FXML
    private void addOrder() {
        Order newOrder;
        boolean fieldsMissing;
        if (getPrincipalController().isAdmin()) {
            fieldsMissing = orderACustomerComboBox.getSelectedItem() == null || orderATableComboBox.getSelectedItem() == null || orderItemsATableView.getItems().isEmpty();
        } else {
            fieldsMissing = orderATableComboBox.getSelectedItem() == null || orderItemsATableView.getItems().isEmpty();
        }
        if (fieldsMissing) {
            getPrincipalController().showErrorAlert(ScreenConstants.MISSING_FIELDS + ScreenConstants.ORDER_NOT_ADDED);
        } else {
            LocalDateTime now = LocalDateTime.now();

            now = now.withNano(0);

            newOrder = new Order(orderATableComboBox.getSelectedItem().getId(), getPrincipalController().getCustomerId(), now);
            if (orderItemsATableView.getItems().isEmpty()) {
                getPrincipalController().showErrorAlert(ScreenConstants.MISSING_FIELDS + ScreenConstants.ORDER_NOT_ADDED);
            } else {
                newOrder.setOrderItems(orderItemsATableView.getItems());
                vm.addOrder(newOrder);
                clearAllFields();
                getPrincipalController().showInfoAlert(ScreenConstants.SUCCESSFUL_ACTION + ScreenConstants.ORDER_ADDED);
            }
        }
    }

    private void clearAllFields() {
        if (!orderACustomerComboBox.isDisabled()) {
            orderACustomerComboBox.getSelectionModel().clearSelection();
        }
        orderATableComboBox.getSelectionModel().clearSelection();
        orderAQuantityComboBox.getSelectionModel().clearSelection();
        orderAMenuItemComboBox.getSelectionModel().clearSelection();
        orderItemsATableView.getItems().clear();
    }

    @FXML
    private void addOrderItem() {
        if (orderAMenuItemComboBox.getSelectedItem() == null || orderAQuantityComboBox.getSelectedItem() == null) {
            getPrincipalController().showErrorAlert(ScreenConstants.MISSING_FIELDS + ScreenConstants.ORDER_ITEM_NOT_ADDED);
        } else {
            MenuItem menuItem = orderAMenuItemComboBox.getSelectedItem();
            OrderItem newOrderItem = new OrderItem(0, menuItem, orderAQuantityComboBox.getSelectedItem());

            orderItemsATableView.getItems().add(newOrderItem);

            orderAMenuItemComboBox.getSelectionModel().clearSelection();
            orderAQuantityComboBox.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void removeOrderItem() {
        List<OrderItem> selectedOrderItems = orderItemsATableView.getSelectionModel().getSelectedValues();

        if (!selectedOrderItems.isEmpty()) {
            List<OrderItem> orderItems = orderItemsATableView.getItems();
            orderItems.removeAll(selectedOrderItems);
            orderItemsATableView.setItems(FXCollections.observableArrayList(orderItems));

        } else {
            getPrincipalController().showInfoAlert(ScreenConstants.ACTION_NOT_COMPLETED + ScreenConstants.EMPTY_TABLE);
        }
    }
}
