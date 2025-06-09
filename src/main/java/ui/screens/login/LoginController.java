package ui.screens.login;

import common.constants.Constants;
import common.constants.ErrorConstants;
import io.github.palexdev.materialfx.controls.MFXTextField;
import jakarta.inject.Inject;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import ui.screens.common.BaseScreenController;


public class LoginController extends BaseScreenController {

    @FXML
    private MFXTextField userField;
    @FXML
    private MFXTextField passField;

    @FXML
    private ImageView imageUser;
    @FXML
    private ImageView imagePassword;

    @FXML
    private ImageView logoApp;
    private final LoginViewModel vm;

    @Inject
    public LoginController(LoginViewModel vm) {
        this.vm = vm;
    }

    @FXML
    private void initialize() {
        try {
            // Images loading
            Image imageU = new Image(getClass().getResource(Constants.USER_IMAGE).toExternalForm());
            imageUser.setImage(imageU);

            Image imageP = new Image(getClass().getResource(Constants.PASSWORD_IMAGE).toExternalForm());
            imagePassword.setImage(imageP);

            Image imageLogo = new Image(getClass().getResource(Constants.LOGO_IMAGE).toExternalForm());
            logoApp.setImage(imageLogo);

            // Password Field Control
            passField.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, event -> {
                if (event.isControlDown() && event.getCode() == javafx.scene.input.KeyCode.C) {
                    event.consume(); // Disallows the Ctrl + C
                }
            });
            // ENTER key triggers to login
            passField.setOnKeyPressed(event -> {
                if (event.getCode() == javafx.scene.input.KeyCode.ENTER) {
                    login();
                }
            });
        } catch (Exception ex) {
            System.err.println(ErrorConstants.ERROR_LOADING_IMAGE + ex.getMessage());
        }
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

    //FORGOT YOUR PASSWORD LINK
    @FXML
    private void openPasswordRecoveryLink() {
        try {
            java.awt.Desktop.getDesktop().browse(new java.net.URI(Constants.LINKFYP));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
