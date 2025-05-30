package ui.screens.order.add;

import common.constants.Constants;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXTableView;
import jakarta.inject.Inject;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
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
    private Text amountText;
    @FXML
    private MFXComboBox<Table> orderATableComboBox;
    @FXML
    private MFXComboBox<Integer> orderAQuantityComboBox;
    @FXML
    private MFXComboBox<MenuItem> orderAMenuItemComboBox;
    @FXML
    private MFXTableView<OrderItem> orderItemsATableView;
    @FXML
    private ImageView backgroundImage;

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

        // ComboBox initialization
        initializeQuantityCB();
        initializeCustomerCB();
        initializeTableCB();
        initializeMenuItemCB();

        // Load background image
        backgroundImage.setImage(new Image(getClass().getResourceAsStream(Constants.ADD_ORDER_BACKGROUND_IMAGE)));
        orderItemsATableView.getTableColumns().forEach(column -> column.setPrefWidth(200.0));

        if (!getPrincipalController().isAdmin()) {
            orderACustomerComboBox.setDisable(true);
        }

        // Set the total to 0€
        updateTotalAmount();
    }

    private void initializeQuantityCB() {
        List<Integer> range = IntStream.rangeClosed(1, 10).boxed().toList();
        orderAQuantityComboBox.setItems(FXCollections.observableArrayList(range));
    }

    private void initializeTableCB() {
        vm.loadTableList();
        List<Table> tables = vm.getState().get().getTableList();
        orderATableComboBox.setItems(FXCollections.observableList(tables));
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
            fieldsMissing = orderACustomerComboBox.getSelectedItem() == null ||
                    orderATableComboBox.getSelectedItem() == null ||
                    orderItemsATableView.getItems().isEmpty();
        } else {
            fieldsMissing = orderATableComboBox.getSelectedItem() == null ||
                    orderItemsATableView.getItems().isEmpty();
        }

        if (fieldsMissing) {
            getPrincipalController().showErrorAlert(ScreenConstants.MISSING_FIELDS + ScreenConstants.ORDER_NOT_ADDED);
        } else {
            LocalDateTime now = LocalDateTime.now().withNano(0);
            newOrder = new Order(orderATableComboBox.getSelectedItem().getId(), getPrincipalController().getCustomerId(), now);
            newOrder.setOrderItems(orderItemsATableView.getItems());

            vm.addOrder(newOrder);
            clearAllFields();
            getPrincipalController().showInfoAlert(ScreenConstants.SUCCESSFUL_ACTION + ScreenConstants.ORDER_ADDED);
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
        updateTotalAmount(); // Reset the total amount
    }

    @FXML
    private void addOrderItem() {
        if (orderAMenuItemComboBox.getSelectedItem() == null || orderAQuantityComboBox.getSelectedItem() == null) {
            getPrincipalController().showErrorAlert(ScreenConstants.MISSING_FIELDS + ScreenConstants.ORDER_ITEM_NOT_ADDED);
        } else {
            MenuItem menuItem = orderAMenuItemComboBox.getSelectedItem();
            int quantity = orderAQuantityComboBox.getSelectedItem();

            OrderItem newOrderItem = new OrderItem(0, menuItem, quantity);
            orderItemsATableView.getItems().add(newOrderItem);

            orderAMenuItemComboBox.getSelectionModel().clearSelection();
            orderAQuantityComboBox.getSelectionModel().clearSelection();

            updateTotalAmount(); // Update the total amount
        }
    }

    @FXML
    private void removeOrderItem() {
        List<OrderItem> selectedOrderItems = orderItemsATableView.getSelectionModel().getSelectedValues();

        if (!selectedOrderItems.isEmpty()) {
            List<OrderItem> orderItems = orderItemsATableView.getItems();
            orderItems.removeAll(selectedOrderItems);
            orderItemsATableView.setItems(FXCollections.observableArrayList(orderItems));

            updateTotalAmount(); // Update the total amount
        } else {
            getPrincipalController().showInfoAlert(ScreenConstants.ACTION_NOT_COMPLETED + ScreenConstants.EMPTY_TABLE);
        }
    }

    private void updateTotalAmount() {
        double total = orderItemsATableView.getItems().stream()
                .mapToDouble(item -> item.getMenuItem().getPrice() * item.getQuantity())
                .sum();
        amountText.setText(String.format("%.2f", total));
    }
}
