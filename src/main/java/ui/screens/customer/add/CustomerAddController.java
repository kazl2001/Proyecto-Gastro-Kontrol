package ui.screens.customer.add;

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
import model.Credential;
import model.Customer;
import ui.screens.common.BaseScreenController;
import ui.screens.common.ScreenConstants;

import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

public class CustomerAddController extends BaseScreenController implements Initializable {

    @FXML
    private MFXTextField usernameATextField;
    @FXML
    private MFXTextField passwordATextField;
    @FXML
    private MFXDatePicker dobADatePicker;
    @FXML
    private MFXTextField firstNameATextField;
    @FXML
    private MFXTextField lastNameATextField;
    @FXML
    private MFXTextField emailATextField;
    @FXML
    private MFXTextField phoneATextField;
    @FXML
    private MFXTableView<Customer> customersATableView;
    @FXML
    private ImageView backgroundImage;

    private final CustomerAddViewModel vm;

    @Inject
    public CustomerAddController(CustomerAddViewModel vm) {
        this.vm = vm;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        stateChangeCustomerList();
    }

    private void stateChangeCustomerList() {
        vm.getState().addListener((observableValue, customerAddState, customerAddStateNew) -> Platform.runLater(() -> {
            if (customerAddStateNew.getError() != null) {
                getPrincipalController().showErrorAlert(customerAddStateNew.getError().getErrorMessage() + customerAddStateNew.getError().getErrorNum());
            }
            if (customerAddStateNew.getCustomerList() != null) {
                customersATableView.getItems().clear();
                customersATableView.getItems().addAll(customerAddStateNew.getCustomerList());
            }
        }));
    }

    @Override
    public void loadedPrincipal() {
        vm.getCustomerList();
        getPrincipalController().createCustomersTable(customersATableView);
        // Load the background image
        backgroundImage.setImage(new Image(getClass().getResourceAsStream(Constants.ADD_CUSTOMER_BACKGROUND_IMAGE)));
        customersATableView.getTableColumns().forEach(column -> column.setPrefWidth(200.0)); // Set a size for the table view
    }

    private Optional<Customer> getCustomerFromScreen() {
        if (!areFieldsEmpty()) {
            return Optional.of(new Customer(firstNameATextField.getText(), lastNameATextField.getText(), getCustomerDob(), emailATextField.getText(), getCustomerPhone()));
        } else {
            getPrincipalController().showErrorAlert(ScreenConstants.MISSING_FIELDS);
            return Optional.empty();
        }
    }

    private Optional<Credential> getCredentialFromScreen() {
        if (!areFieldsEmpty()) {
            return Optional.of(new Credential(0, usernameATextField.getText(), passwordATextField.getText()));
        } else {
            getPrincipalController().showErrorAlert(ScreenConstants.MISSING_FIELDS);
            return Optional.empty();
        }
    }

    private LocalDate getCustomerDob() {
        if (dobADatePicker.getValue() == null) {
            return LocalDate.now();
        } else return dobADatePicker.getValue();
    }

    private String getCustomerPhone() {
        if (phoneATextField.getText().isEmpty() || phoneATextField.getText() == null || phoneATextField.getText().isBlank() || phoneATextField.getText().length() != 9) {
            return "";
        } else return phoneATextField.getText();
    }

    private boolean areFieldsEmpty() {
        return firstNameATextField.getText().isEmpty() || lastNameATextField.getText().isEmpty() || emailATextField.getText().isEmpty() || usernameATextField.getText().isEmpty() || passwordATextField.getText().isEmpty();
    }

    private void clearFields() {
        firstNameATextField.clear();
        lastNameATextField.clear();
        emailATextField.clear();
        phoneATextField.clear();
        dobADatePicker.clear();
        usernameATextField.clear();
        passwordATextField.clear();
    }

    @FXML
    private void addCustomer() {
        // Check if the phone number is exactly 9 digits
        if (phoneATextField.getText().length() != 9 || !phoneATextField.getText().matches("\\d+")) {
            // Show error message if phone number is invalid
            getPrincipalController().showErrorAlert(ErrorConstants.ERROR_SAVING_PHONE);
            return;
        }
        Optional<Customer> customerOptional = getCustomerFromScreen();
        Optional<Credential> credentialOptional = getCredentialFromScreen();
        if (customerOptional.isPresent() && credentialOptional.isPresent()) {
            Customer customer = customerOptional.get();
            customer.setCredential(credentialOptional.get());
            vm.addCustomer(customer);
            clearFields();
        }
    }


}
