package ui.screens.customer.add;

import io.github.palexdev.materialfx.controls.MFXDatePicker;
import io.github.palexdev.materialfx.controls.MFXTableView;
import io.github.palexdev.materialfx.controls.MFXTextField;
import jakarta.inject.Inject;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
        if (phoneATextField.getText().isEmpty() || phoneATextField.getText() == null || phoneATextField.getText().isBlank()) {
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
