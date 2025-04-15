package ui.screens.welcome;

import common.constants.Constants;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import ui.screens.common.BaseScreenController;
import ui.screens.common.ScreenConstants;

public class WelcomeController extends BaseScreenController {

    @FXML
    private Text userWTextArea;

    @FXML
    private ImageView backgroundImage;

    @Override
    public void loadedPrincipal() {
        userWTextArea.setText(getPrincipalController().getUsername() + ScreenConstants.EXCLAMATION_MARK);
        // Load the image
        backgroundImage.setImage(new Image(getClass().getResourceAsStream(Constants.WELCOME_BACKGROUND_IMAGE)));
        backgroundImage.setOpacity(0.9);

    }
}
