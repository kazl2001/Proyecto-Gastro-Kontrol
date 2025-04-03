package ui.screens.customer.list;

import io.github.palexdev.materialfx.controls.MFXTableView;
import jakarta.inject.Inject;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import model.Customer;
import ui.screens.common.BaseScreenController;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomerListController extends BaseScreenController implements Initializable {

    @FXML
    private MFXTableView<Customer> customersLTableView;

    private final CustomerListViewModel vm;

    @Inject
    public CustomerListController(CustomerListViewModel vm) {
        this.vm = vm;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stateChangeCustomerList();
    }

    @Override
    public void loadedPrincipal() {
        vm.getCustomerList();
        getPrincipalController().createCustomersTable(customersLTableView);
    }

    private void stateChangeCustomerList() {
        vm.getState().addListener((observableValue, customerListState, customerListStateNew) -> Platform.runLater(() -> {
            if (customerListStateNew.getError() != null) {
                getPrincipalController().showErrorAlert(customerListStateNew.getError().getErrorMessage() + customerListStateNew.getError().getErrorNum());
            }
            if (customerListStateNew.getCustomerList() != null) {
                customersLTableView.getItems().clear();
                customersLTableView.getItems().addAll(customerListStateNew.getCustomerList());
            }
        }));
    }

}
