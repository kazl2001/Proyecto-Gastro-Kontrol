package ui.screens.customer.delete;

import common.constants.ErrorConstants;
import io.github.palexdev.materialfx.controls.MFXTableView;
import jakarta.inject.Inject;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import model.Customer;
import model.Order;
import ui.screens.common.BaseScreenController;
import ui.screens.common.ScreenConstants;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomerDeleteController extends BaseScreenController implements Initializable {

    @FXML
    private MFXTableView<Order> ordersDTableView;
    @FXML
    private MFXTableView<Customer> customersDTableView;
    private Customer selectedCustomer;
    private final CustomerDeleteViewModel vm;


    @Inject
    public CustomerDeleteController(CustomerDeleteViewModel vm) {
        this.vm = vm;
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stateChangeCustomerList();
    }

    @Override
    public void loadedPrincipal() {
        vm.getCustomerList();
        getPrincipalController().createCustomersTable(customersDTableView);
        getPrincipalController().createOrdersTable(ordersDTableView);

        //Upon selection, this method will populate the orders table with those of the selected customer
        customersDTableView.getSelectionModel().selectionProperty().addListener((observableValue, customer, customerNew) -> {
            if (customersDTableView.getSelectionModel().getSelectedValues() != null) {
                selectedCustomer = customersDTableView.getSelectionModel().getSelectedValues().get(0);
            }
            if (selectedCustomer != null) {
                vm.getOrderList(selectedCustomer.getId());
            }
        });
    }

    private void stateChangeCustomerList() {
        vm.getState().addListener((observableValue, customerDeleteState, customerDeleteStateNew) -> Platform.runLater(() -> {
            if (customerDeleteStateNew.getError() != null && !customerDeleteStateNew.getError().getErrorMessage().equals(ErrorConstants.CONFIRMATION_NEEDED_ERROR)) {
                getPrincipalController().showErrorAlert(customerDeleteStateNew.getError().getErrorMessage() + customerDeleteStateNew.getError().getErrorNum());
                if (customerDeleteStateNew.getError().getErrorMessage().equals(ErrorConstants.DATA_RETRIEVAL_ERROR_NOT_FOUND)) {
                    ordersDTableView.getItems().clear();
                }
            }
            if (customerDeleteStateNew.getCustomerList() != null) {
                customersDTableView.getItems().clear();
                customersDTableView.getItems().addAll(customerDeleteStateNew.getCustomerList());
            }
            if (customerDeleteStateNew.getOrderList() != null) {
                ordersDTableView.getItems().clear();
                ordersDTableView.getItems().addAll(customerDeleteStateNew.getOrderList());
            }
        }));
    }

    @FXML
    private void deleteCustomer() {
        boolean confirmed;
        if (selectedCustomer != null) {
            vm.deleteCustomer(selectedCustomer, false);
            clearFields();
            if (vm.getState().getValue().getError() != null && vm.getState().getValue().getError().getErrorMessage().equals(ErrorConstants.CONFIRMATION_NEEDED_ERROR)) {
                confirmed = getPrincipalController().showConfirmationAlert(ScreenConstants.CUSTOMER_HAS_ORDERS_DELETION_CONFIRMATION);
                if (confirmed) {
                    vm.deleteCustomer(selectedCustomer, true);
                    clearFields();
                }
            }
        }
    }

    private void clearFields() {
        if (vm.getState().getValue().getError() == null) {
            getPrincipalController().showInfoAlert(ScreenConstants.SUCCESSFUL_ACTION + ScreenConstants.CUSTOMER_DELETED);
            ordersDTableView.getItems().clear();
            if (!customersDTableView.getItems().isEmpty()) {
                customersDTableView.getSelectionModel().selectIndex(0);
            }
        }
    }
}
