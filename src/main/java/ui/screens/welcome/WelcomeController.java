package ui.screens.welcome;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import ui.screens.common.BaseScreenController;
import ui.screens.common.ScreenConstants;

public class WelcomeController extends BaseScreenController {

    @FXML
    private Text userWTextArea;

    @Override
    public void loadedPrincipal() {
        userWTextArea.setText(getPrincipalController().getUsername() + ScreenConstants.EXCLAMATION_MARK);
    }
}
