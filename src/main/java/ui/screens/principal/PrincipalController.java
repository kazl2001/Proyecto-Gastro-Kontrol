package ui.screens.principal;


import io.github.palexdev.materialfx.controls.MFXTableColumn;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.cell.MFXTableRowCell;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import model.Customer;
import model.Order;
import model.OrderItem;
import ui.screens.common.BaseScreenController;
import ui.screens.common.ScreenConstants;
import ui.screens.common.Screens;

import java.io.IOException;
import java.util.Optional;

@Log4j2
public class PrincipalController {

    @FXML
    private MenuBar principalMenu;
    @FXML
    public BorderPane root;
    final Instance<Object> instance;
    private Stage primaryStage;
    private final Alert alert;
    @Getter
    @Setter
    private String username;
    @Getter
    private boolean admin;
    @Getter
    @Setter
    private int customerId;


    @Inject
    public PrincipalController(Instance<Object> instance) {
        this.instance = instance;
        alert = new Alert(Alert.AlertType.NONE);
    }

    //SCREEN LOADING/CHANGING

    public void initialize() {
        logout();
    }

    private void loadScreen(Screens screen) {
        screenChange(loadScreen(screen.getRoute()));
    }

    private Pane loadScreen(String route) {
        Pane screenPane = null;
        try {

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setControllerFactory(controller -> instance.select(controller).get());
            screenPane = fxmlLoader.load(getClass().getResourceAsStream(route));
            BaseScreenController screenController = fxmlLoader.getController();
            screenController.setPrincipalController(this);
            screenController.loadedPrincipal();

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return screenPane;
    }

    private void screenChange(Pane newScreen) {
        root.setCenter(newScreen);
    }


    public void setStage(Stage stage) {
        primaryStage = stage;
    }

    //MENU ACTIONS

    @FXML
    private void menuClick(ActionEvent actionEvent) {

        switch (((MenuItem) actionEvent.getSource()).getId()) {
            case ScreenConstants.MENU_ITEM_LOGOUT -> logout();
            case ScreenConstants.MENU_ITEM_EXIT -> exit();
            case ScreenConstants.MENU_ITEM_CUSTOMER_LIST -> loadScreen(Screens.CUSTOMER_LIST_SCREEN);
            case ScreenConstants.MENU_ITEM_CUSTOMER_ADD -> loadScreen(Screens.CUSTOMER_ADD_SCREEN);
            case ScreenConstants.MENU_ITEM_CUSTOMER_UPDATE -> loadScreen(Screens.CUSTOMER_UPDATE_SCREEN);
            case ScreenConstants.MENU_ITEM_CUSTOMER_DELETE -> loadScreen(Screens.CUSTOMER_DELETE_SCREEN);
            case ScreenConstants.MENU_ITEM_ORDER_LIST -> loadScreen(Screens.ORDER_LIST_SCREEN);
            case ScreenConstants.MENU_ITEM_ORDER_ADD -> loadScreen(Screens.ORDER_ADD_SCREEN);
            case ScreenConstants.MENU_ITEM_ORDER_UPDATE -> loadScreen(Screens.ORDER_UPDATE_SCREEN);
            case ScreenConstants.MENU_ITEM_ORDER_DELETE -> loadScreen(Screens.ORDER_DELETE_SCREEN);
            default -> loadScreen(Screens.WELCOME_SCREEN);
        }
    }

    public void exit() {
        primaryStage.close();
        Platform.exit();
    }

    //ALERTS

    public void showErrorAlert(String message) {
        alert.setAlertType(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.getDialogPane().setId(ScreenConstants.ALERT_DIALOG_PANE_ID);
        alert.getDialogPane().lookupButton(ButtonType.OK).setId(ScreenConstants.ALERT_CONFIRMATION_BUTTON_ID);
        alert.showAndWait();
    }

    public void showInfoAlert(String message) {
        alert.setAlertType(Alert.AlertType.INFORMATION);
        alert.setContentText(message);
        alert.getDialogPane().setId(ScreenConstants.ALERT_DIALOG_PANE_ID);
        alert.getDialogPane().lookupButton(ButtonType.OK).setId(ScreenConstants.ALERT_CONFIRMATION_BUTTON_ID);
        alert.showAndWait();
    }


    public boolean showConfirmationAlert(String message) {
        alert.setAlertType(Alert.AlertType.CONFIRMATION);
        alert.setContentText(message);
        alert.getDialogPane().setId(ScreenConstants.ALERT_DIALOG_PANE_ID);

        ButtonType confirmButtonType = new ButtonType(ScreenConstants.OK, ButtonBar.ButtonData.OK_DONE);
        alert.getButtonTypes().setAll(confirmButtonType, ButtonType.CANCEL);

        Optional<ButtonType> result = alert.showAndWait();

        return result.isPresent() && result.get() == confirmButtonType;
    }

    public void showWarningAlert(String message) {
        alert.setAlertType(Alert.AlertType.WARNING);
        alert.setContentText(message);
        alert.getDialogPane().setId(ScreenConstants.ALERT_DIALOG_PANE_ID);
        alert.getDialogPane().lookupButton(ButtonType.OK).setId(ScreenConstants.ALERT_CONFIRMATION_BUTTON_ID);
        alert.showAndWait();
    }

    //LOGIN & LOGOUT
    public void loginAdmin() {
        for (Menu menu : principalMenu.getMenus()) {
            for (MenuItem menuItem : menu.getItems()) {
                menuItem.setVisible(!menuItem.getId().equals(ScreenConstants.MENU_ITEM_ORDER_ADD));
            }
        }
        principalMenu.setVisible(true);
        admin = true;
        loadScreen(Screens.WELCOME_SCREEN);
    }

    public void loginCustomer() {
        for (Menu menu : principalMenu.getMenus()) {
            for (MenuItem menuItem : menu.getItems()) {
                if (menuItem.getId().equals(ScreenConstants.MENU_ITEM_ORDER_LIST) || menuItem.getId().equals(ScreenConstants.MENU_ITEM_ORDER_ADD) || menuItem.getId().equals(ScreenConstants.MENU_ITEM_ORDER_UPDATE) || menuItem.getId().equals(ScreenConstants.MENU_ITEM_ORDER_DELETE)) {
                    menuItem.setVisible(true);
                } else if (menuItem.getId().equals(ScreenConstants.MENU_ITEM_CUSTOMER_LIST) || menuItem.getId().equals(ScreenConstants.MENU_ITEM_CUSTOMER_ADD) || menuItem.getId().equals(ScreenConstants.MENU_ITEM_CUSTOMER_UPDATE) || menuItem.getId().equals(ScreenConstants.MENU_ITEM_CUSTOMER_DELETE)) {
                    menuItem.setVisible(false);
                }
            }
        }
        principalMenu.setVisible(true);
        admin = false;
        loadScreen(Screens.WELCOME_SCREEN);

    }

    public void logout() {
        principalMenu.setVisible(false);
        loadScreen(Screens.LOGIN_SCREEN);
    }

    //TABLE INITIALIZATION

    public void createCustomersTable(MFXTableView<Customer> table) {

        //we set the table to allow only one selection at a time
        table.getSelectionModel().setAllowsMultipleSelection(false);

        //we create the columns
        MFXTableColumn<Customer> fistNameCol = new MFXTableColumn<>(ScreenConstants.TABLE_COLUMN_FIRST_NAME);
        MFXTableColumn<Customer> lastNameCol = new MFXTableColumn<>(ScreenConstants.TABLE_COLUMN_LAST_NAME);
        MFXTableColumn<Customer> dobCol = new MFXTableColumn<>(ScreenConstants.TABLE_COLUMN_DOB);
        MFXTableColumn<Customer> emailCol = new MFXTableColumn<>(ScreenConstants.TABLE_COLUMN_EMAIL);
        MFXTableColumn<Customer> phoneCol = new MFXTableColumn<>(ScreenConstants.TABLE_COLUMN_PHONE);
        dobCol.setPrefWidth(100);
        emailCol.setPrefWidth(150);

        //we specify the source of information for each column
        fistNameCol.setRowCellFactory(customer -> new MFXTableRowCell<>(Customer::getFirstName));
        lastNameCol.setRowCellFactory(customer -> new MFXTableRowCell<>(Customer::getLastName));
        dobCol.setRowCellFactory(customer -> new MFXTableRowCell<>(Customer::getDateOfBirth));
        emailCol.setRowCellFactory(customer -> new MFXTableRowCell<>(Customer::getEmail));
        phoneCol.setRowCellFactory(customer -> new MFXTableRowCell<>(Customer::getPhone));

        //we add the columns to the table
        table.getTableColumns().addAll(fistNameCol, lastNameCol, dobCol, emailCol, phoneCol);
    }

    public void createOrderItemsTable(MFXTableView<OrderItem> table) {
        table.getSelectionModel().setAllowsMultipleSelection(false);

        MFXTableColumn<OrderItem> menuItemCol = new MFXTableColumn<>(ScreenConstants.TABLE_COLUMN_MENU_ITEM);
        MFXTableColumn<OrderItem> quantityCol = new MFXTableColumn<>(ScreenConstants.TABLE_COLUMN_QUANTITY);
        menuItemCol.setPrefWidth(200);

        menuItemCol.setRowCellFactory(orderItem -> new MFXTableRowCell<>(OrderItem::getMenuItem));
        quantityCol.setRowCellFactory(orderItem -> new MFXTableRowCell<>(OrderItem::getQuantity));

        table.getTableColumns().addAll(menuItemCol, quantityCol);
    }

    public void createOrdersTable(MFXTableView<Order> table) {
        table.getSelectionModel().setAllowsMultipleSelection(false);

        MFXTableColumn<Order> tableCol = new MFXTableColumn<>(ScreenConstants.TABLE_COLUMN_TABLE);
        MFXTableColumn<Order> dateCol = new MFXTableColumn<>(ScreenConstants.TABLE_COLUMN_DATE);
        dateCol.setPrefWidth(150);

        tableCol.setRowCellFactory(order -> new MFXTableRowCell<>(Order::getTableId));
        dateCol.setRowCellFactory(order -> new MFXTableRowCell<>(Order::getOrderDate));

        table.getTableColumns().addAll(tableCol, dateCol);
    }

}
