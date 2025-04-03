package ui.screens.order.update;

import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import jakarta.inject.Inject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import model.*;
import ui.screens.common.BaseScreenController;
import ui.screens.common.ScreenConstants;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

public class OrderUpdateController extends BaseScreenController implements Initializable {

    @FXML
    private MFXTextField orderUHourTextField;
    @FXML
    private MFXTextField orderUMinuteTextField;
    @FXML
    private MFXTextField orderUSecondTextField;
    @FXML
    private MFXTableView<Order> orderUTableView;
    @FXML
    private MFXDatePicker orderDateUDatePicker;
    @FXML
    private MFXComboBox<Integer> customerUComboBox;
    @FXML
    private MFXComboBox<Table> tableUComboBox;
    @FXML
    private MFXComboBox<Integer> quantityUComboBox;
    @FXML
    private MFXComboBox<MenuItem> menuItemUComboBox;
    @FXML
    private MFXTableView<OrderItem> orderItemsUTableView;
    private Order selectedOrder;
    private List<Table> tablesList;
    private final OrderUpdateViewModel vm;

    @Inject
    public OrderUpdateController(OrderUpdateViewModel vm) {
        this.vm = vm;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stateListeners();
        vm.loadCustomers();
        customerUComboBox.setDisable(true);
    }

    @Override
    public void loadedPrincipal() {
        getPrincipalController().createOrdersTable(orderUTableView);
        getPrincipalController().createOrderItemsTable(orderItemsUTableView);

        //ComboBox initialization
        initializeQuantityCB();
        initializeCustomerCB();
        initializeTableCB();
        initializeMenuItemCB();

        if (getPrincipalController().isAdmin()) {
            customerUComboBox.setDisable(true);
            vm.getOrderList();
        } else {
            vm.getOrderListByCustomerId(getPrincipalController().getCustomerId());
            customerUComboBox.setDisable(true);
        }

        //Upon selection, this method will populate the date field with the selected order's date
        orderUTableView.getSelectionModel().selectionProperty().addListener((observableValue, order, orderNew) -> {
            if (!orderUTableView.getItems().isEmpty()) {
                if (orderUTableView.getSelectionModel().getSelectedValues().get(0) != null) {
                    selectedOrder = orderUTableView.getSelectionModel().getSelectedValues().get(0);
                }
                if (selectedOrder != null) {
                    orderDateUDatePicker.setValue(selectedOrder.getOrderDate().toLocalDate());
                    orderUHourTextField.setText(String.valueOf(selectedOrder.getOrderDate().toLocalTime().getHour()));
                    orderUMinuteTextField.setText(String.valueOf(selectedOrder.getOrderDate().toLocalTime().getMinute()));
                    orderUSecondTextField.setText(String.valueOf(selectedOrder.getOrderDate().toLocalTime().getSecond()));
                    tableUComboBox.setValue(tablesList.get(selectedOrder.getTableId() - 1));
                    if (getPrincipalController().isAdmin() && customerUComboBox.isDisabled()) {
                        customerUComboBox.setValue(selectedOrder.getCustomerId());
                    }
                    vm.loadOrderItems(selectedOrder.getId());
                }
            }
        });
    }

    private void initializeQuantityCB() {
        List<Integer> range = IntStream.rangeClosed(1, 10).boxed().toList();
        quantityUComboBox.setItems(FXCollections.observableArrayList(range));
    }

    private void initializeTableCB() {
        vm.loadTables();
        List<Table> tables = vm.getState().get().getTableList();
        tablesList = tables;

        ObservableList<Table> observableTables = FXCollections.observableList(tables);
        tableUComboBox.setItems(observableTables);
    }

    private void initializeCustomerCB() {
        List<Integer> customers = vm.getState().get().getCustomerList().stream().map(Customer::getId).toList();
        customerUComboBox.setItems(FXCollections.observableArrayList(customers));
    }

    private void initializeMenuItemCB() {
        vm.loadMenuItems();
        List<MenuItem> menuItems = vm.getState().get().getMenuItemList();
        menuItemUComboBox.setItems(FXCollections.observableArrayList(menuItems));
    }

    private void stateListeners() {
        vm.getState().addListener((observableValue, orderUpdateState, orderUpdateStateNew) -> {
            if (orderUpdateStateNew.getError() != null) {
                getPrincipalController().showErrorAlert(orderUpdateStateNew.getError().getErrorMessage() + orderUpdateStateNew.getError().getErrorNum());
            }
            if (orderUpdateStateNew.getOrderList() != null) {
                orderUTableView.getItems().clear();
                orderUTableView.getItems().addAll(orderUpdateStateNew.getOrderList());
            }
            if (orderUpdateStateNew.getOrderItemList() != null) {
                orderItemsUTableView.getItems().clear();
                orderItemsUTableView.getItems().addAll(orderUpdateStateNew.getOrderItemList());
            }
        });
    }

    private Optional<Order> getOrderFromScreen() {
        int id;
        if (!areFieldsEmptyOrder()) {
            if (getPrincipalController().isAdmin()) {
                id = customerUComboBox.getValue();
            } else {
                id = getPrincipalController().getCustomerId();
            }
            return Optional.of(new Order(
                    selectedOrder.getId(),
                    tableUComboBox.getValue().getId(),
                    id,
                    orderDateUDatePicker.getValue().atTime(Integer.parseInt(orderUHourTextField.getText()), Integer.parseInt(orderUMinuteTextField.getText()), Integer.parseInt(orderUSecondTextField.getText()))));
        } else {
            getPrincipalController().showErrorAlert(ScreenConstants.MISSING_FIELDS);
            return Optional.empty();
        }
    }

    private boolean areFieldsEmptyOrder() {
        boolean emptyFields;
        if (getPrincipalController().isAdmin()) {
            emptyFields = orderUHourTextField.getText().isEmpty() ||
                    orderUMinuteTextField.getText().isEmpty() ||
                    orderUSecondTextField.getText().isEmpty() ||
                    orderDateUDatePicker.getValue() == null ||
                    customerUComboBox.getValue() == null ||
                    tableUComboBox.getValue() == null;
        } else {
            emptyFields = orderUHourTextField.getText().isEmpty() ||
                    orderUMinuteTextField.getText().isEmpty() ||
                    orderUSecondTextField.getText().isEmpty() ||
                    orderDateUDatePicker.getValue() == null ||
                    tableUComboBox.getValue() == null;
        }
        return emptyFields;
    }

    private void clearFields() {
        orderUHourTextField.clear();
        orderUMinuteTextField.clear();
        orderUSecondTextField.clear();
        orderDateUDatePicker.setValue(null);
        if (!customerUComboBox.isDisabled()) {
            customerUComboBox.clear();
        }
        tableUComboBox.clear();
        quantityUComboBox.clear();
        menuItemUComboBox.clear();
        orderItemsUTableView.getItems().clear();
    }

    private void actionDone() {
        clearFields();
        if (!orderUTableView.getItems().isEmpty()) {
            orderUTableView.getSelectionModel().selectIndex(0);
        }
        if(getPrincipalController().isAdmin()){
            vm.getOrderList();
        } else {
            vm.getOrderListByCustomerId(getPrincipalController().getCustomerId());
        }
        getPrincipalController().showInfoAlert(ScreenConstants.SUCCESSFUL_ACTION + ScreenConstants.ORDER_MODIFIED);
    }

    private boolean orderEqualsScreenOrder(Optional<Order> opt, Order order) {
        if (opt.isPresent()) {
            Order orderToCompare = new Order(
                    opt.get().getId(),
                    opt.get().getTableId(),
                    opt.get().getCustomerId(),
                    opt.get().getOrderDate());
            return orderToCompare.equals(order);
        } else return false;
    }

    private boolean orderItemsEqualsScreenList() {
        List<OrderItem> list = vm.getState().get().getOrderItemList();
        ObservableList<OrderItem> obv = orderItemsUTableView.getItems();
        return obv.size() == list.size() && IntStream.range(0, obv.size()).allMatch(i -> obv.get(i).equals(list.get(i)));
    }

    @FXML
    private void updateOrder() {
        Optional<Order> orderOptional = getOrderFromScreen();
        if (orderOptional.isPresent()) {
            if (orderEqualsScreenOrder(orderOptional, selectedOrder)) {
                if (orderItemsEqualsScreenList()) { //IF NO CHANGES WERE MADE
                    getPrincipalController().showErrorAlert(ScreenConstants.NO_CHANGES_MADE);
                } else { //IF ONLY ORDER ITEMS WERE CHANGED
                    Order order = orderOptional.get();
                    order.setCustomerId(0);

                    order.setOrderItems(orderItemsUTableView.getItems());

                    vm.updateOrder(order);
                    actionDone();
                }
            } else if (vm.getState().get().getOrderItemList() == orderItemsUTableView.getItems()) { //IF ONLY ORDER WAS CHANGED
                if (!areFieldsEmptyOrder()) {
                    vm.updateOrder(orderOptional.get());
                    actionDone();
                }
            } else if (!areFieldsEmptyOrder()) {
                Order order = orderOptional.get();
                order.setOrderItems(orderItemsUTableView.getItems());
                vm.updateOrder(order);
                actionDone();
            }
        }
    }


    //CREATE ORDER-ITEM WITH MENU ITEM ALREADY
    @FXML
    private void addOrderItem() {
        if (menuItemUComboBox.getSelectedItem() == null || quantityUComboBox.getSelectedItem() == null) {
            getPrincipalController().showErrorAlert(ScreenConstants.MISSING_FIELDS + ScreenConstants.ORDER_ITEM_NOT_ADDED);
        } else {
            int orderId = selectedOrder.getId();
            MenuItem menuItem = menuItemUComboBox.getSelectedItem();
            OrderItem newOrderItem = new OrderItem(orderId, menuItem, quantityUComboBox.getSelectedItem());
            orderItemsUTableView.getItems().add(newOrderItem);
            getPrincipalController().showInfoAlert(ScreenConstants.SUCCESSFUL_ACTION + ScreenConstants.ORDER_ITEM_ADDED);
        }
    }

    @FXML
    private void removeOrderItem() {
        List<OrderItem> selectedOrderItems = orderItemsUTableView.getSelectionModel().getSelectedValues();

        if (!selectedOrderItems.isEmpty()) {
            List<OrderItem> orderItems = orderItemsUTableView.getItems();
            orderItems.removeAll(selectedOrderItems);

            if (orderItems.isEmpty()) {
                orderItemsUTableView.getItems().clear();
            } else {
                orderItemsUTableView.setItems(FXCollections.observableArrayList(orderItems));
            }

            getPrincipalController().showInfoAlert(ScreenConstants.SUCCESSFUL_ACTION + ScreenConstants.ORDER_ITEM_REMOVED);
        } else {
            getPrincipalController().showInfoAlert(ScreenConstants.SUCCESSFUL_ACTION + ScreenConstants.ORDER_ITEM_NOT_REMOVED);
        }
    }
}
