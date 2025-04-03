package ui.screens.login;

import io.github.palexdev.materialfx.controls.MFXTextField;
import jakarta.inject.Inject;
import javafx.fxml.FXML;
import ui.screens.common.BaseScreenController;


public class LoginController extends BaseScreenController {

    @FXML
    private MFXTextField userField;
    @FXML
    private MFXTextField passField;

    private final LoginViewModel vm;

    @Inject
    public LoginController(LoginViewModel vm) {
        this.vm = vm;
    }

    @FXML
    private void login() {
        addStateListener();
        String user = userField.getText();
        String password = passField.getText();
        vm.login(user, password);
    }

    public void addStateListener() {
        vm.getState().addListener((observableValue, loginState, loginStateNew) -> {
            if (loginStateNew.getError() != null) {
                getPrincipalController().showErrorAlert(loginStateNew.getError().getErrorMessage() + loginStateNew.getError().getErrorNum());
            }
            if (loginStateNew.getCustomerId() != 0) {
                getPrincipalController().setCustomerId(loginStateNew.getCustomerId());
                getPrincipalController().setUsername(userField.getText());
                vm.checkAdmin(userField.getText(), passField.getText());
                if (vm.getState().get().isAdmin()) {
                    getPrincipalController().loginAdmin();
                } else {
                    getPrincipalController().loginCustomer();
                }
            }
        });
    }
}
