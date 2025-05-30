package ui.screens.customer.update;

import common.constants.Constants;
import common.constants.ErrorConstants;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import jakarta.inject.Inject;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import model.Customer;
import ui.screens.common.BaseScreenController;
import ui.screens.common.ScreenConstants;

import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerUpdateController extends BaseScreenController implements Initializable {

    @FXML
    private MFXTableView<Customer> customersUTableView;
    @FXML
    private MFXTextField firstNameUTextField;
    @FXML
    private MFXTextField lastNameUTextField;
    @FXML
    private MFXTextField emailUTextField;
    @FXML
    private MFXTextField phoneUTextField;
    @FXML
    private MFXDatePicker dobUDatePicker;
    @FXML
    private ImageView backgroundImage;
    private Customer selectedCustomer;
    private final CustomerUpdateViewModel vm;

    @Inject
    public CustomerUpdateController(CustomerUpdateViewModel vm) {
        this.vm = vm;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stateChangeCustomerList();
        stateChangeError();
    }

    @Override
    public void loadedPrincipal() {
        vm.getCustomerList();
        getPrincipalController().createCustomersTable(customersUTableView);
        // Load the background image
        backgroundImage.setImage(new Image(getClass().getResourceAsStream(Constants.UPDATE_CUSTOMER_BACKGROUND_IMAGE)));
        customersUTableView.getTableColumns().forEach(column -> column.setPrefWidth(200.0)); // Set a size for the table view

        //Upon selection, this method will populate the fields with the selected customer's data
        customersUTableView.getSelectionModel().selectionProperty().addListener((observableValue, customer, customerNew) -> {
            if (!customersUTableView.getItems().isEmpty()) {
                if (customersUTableView.getSelectionModel().getSelectedValues().get(0) != null) {
                    selectedCustomer = customersUTableView.getSelectionModel().getSelectedValues().get(0);
                }
                if (selectedCustomer != null) {
                    setCustomerFields(selectedCustomer);
                }
            }
        });
    }

    private void setCustomerFields(Customer customer){
        firstNameUTextField.setText(customer.getFirstName());
        lastNameUTextField.setText(customer.getLastName());
        emailUTextField.setText(customer.getEmail());
        if(customer.getPhone() != null){
            phoneUTextField.setText(customer.getPhone());
        } else {
            phoneUTextField.setText("");
        }
        if(customer.getDateOfBirth() != null){
            dobUDatePicker.setValue(customer.getDateOfBirth());
        } else {
            dobUDatePicker.setValue(null);
            dobUDatePicker.setText("");
        }
    }

    private void stateChangeError() {
        vm.getState().addListener((observableValue, customerAddState, customerAddStateNew) -> Platform.runLater(() -> {
            if (customerAddStateNew.getError() != null) {
                getPrincipalController().showErrorAlert(customerAddStateNew.getError().getErrorMessage() + customerAddStateNew.getError().getErrorNum());
            }
        }));
    }

    private void stateChangeCustomerList() {
        vm.getState().addListener((observableValue, customerUpdateState, customerUpdateStateNew) -> Platform.runLater(() -> {
            if (customerUpdateStateNew.getError() != null) {
                getPrincipalController().showErrorAlert(customerUpdateStateNew.getError().getErrorMessage() + customerUpdateStateNew.getError().getErrorNum());
            }
            if (customerUpdateStateNew.getCustomerList() != null) {
                customersUTableView.getItems().clear();
                customersUTableView.getItems().addAll(customerUpdateStateNew.getCustomerList());
            }
        }));
    }

    private Optional<Customer> getCustomerFromScreen() {
        if (!areFieldsEmpty()) {
            return Optional.of(new Customer(
                    firstNameUTextField.getText(),
                    lastNameUTextField.getText(),
                    getCustomerDob(),
                    emailUTextField.getText(),
                    getCustomerPhone()
            ));
        } else {
            getPrincipalController().showErrorAlert(ScreenConstants.MISSING_FIELDS);
            return Optional.empty();
        }
    }

    private LocalDate getCustomerDob() {
        if ( dobUDatePicker.getText().isEmpty()) {
            return LocalDate.now();
        } else return dobUDatePicker.getValue();
    }

    private String getCustomerPhone() {
        if (phoneUTextField.getText().isEmpty() || phoneUTextField.getText() == null || phoneUTextField.getText().isBlank()) {
            return "";
        } else return phoneUTextField.getText();
    }

    private boolean areFieldsEmpty() {
        return firstNameUTextField.getText().isEmpty() || lastNameUTextField.getText().isEmpty() || emailUTextField.getText().isEmpty();
    }

    private void clearFields() {
        firstNameUTextField.clear();
        lastNameUTextField.clear();
        emailUTextField.clear();
        phoneUTextField.clear();
        dobUDatePicker.clear();
    }

    @FXML
    private void updateCustomer() {
        // Check if the phone number is exactly 9 digits
        if (phoneUTextField.getText().length() != 9 || !phoneUTextField.getText().matches("\\d+")) {
            // Show error message if phone number is invalid
            getPrincipalController().showErrorAlert(ErrorConstants.ERROR_SAVING_PHONE);
            return;
        }
        Optional<Customer> customerOptional = getCustomerFromScreen();
        if (customerOptional.isPresent() ) {
            customerOptional.get().setId(selectedCustomer.getId());
            vm.updateCustomer(customerOptional.get());
            clearFields();
            if (!customersUTableView.getItems().isEmpty()) {
                customersUTableView.getSelectionModel().selectIndex(0);
            }
            getPrincipalController().showInfoAlert(ScreenConstants.SUCCESSFUL_ACTION + ScreenConstants.CUSTOMER_MODIFIED);
        }
    }
}
