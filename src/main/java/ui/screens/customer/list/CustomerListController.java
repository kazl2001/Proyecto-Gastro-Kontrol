package ui.screens.customer.list;

import common.constants.Constants;
import io.github.palexdev.materialfx.controls.MFXTableView;
import jakarta.inject.Inject;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Customer;
import ui.screens.common.BaseScreenController;

import java.net.URL;
import java.util.ResourceBundle;

public class CustomerListController extends BaseScreenController implements Initializable {

    @FXML
    private MFXTableView<Customer> customersLTableView;
    @FXML
    private ImageView backgroundImage;

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
        // Load the background image
        backgroundImage.setImage(new Image(getClass().getResourceAsStream(Constants.LIST_CUSTOMER_BACKGROUND_IMAGE)));
        customersLTableView.getTableColumns().forEach(column -> column.setPrefWidth(200.0)); // Set a size for the table view

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
