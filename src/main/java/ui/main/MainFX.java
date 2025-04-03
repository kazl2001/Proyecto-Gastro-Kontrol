package ui.main;

import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ui.screens.common.ScreenConstants;
import ui.screens.principal.PrincipalController;

import java.io.IOException;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class MainFX {
    @Inject
    FXMLLoader fxmlLoader;

    public void start(@Observes @StartupScene Stage stage) throws IOException {
        try {
            ResourceBundle r = ResourceBundle.getBundle(ScreenConstants.APP_TITLE_PROPERTY_ROUTE);

            fxmlLoader.setResources(r);
            Parent fxmlParent = fxmlLoader.load(getClass().getResourceAsStream(ScreenConstants.FXML_ROUTE_PRINCIPAL));
            PrincipalController controller = fxmlLoader.getController();
            controller.setStage(stage);
            stage.setTitle(r.getString(ScreenConstants.APP_TITLE));
            stage.setScene(new Scene(fxmlParent));
            stage.show();
        } catch (IOException e) {
            Logger.getLogger(getClass().getName()).severe(e.getMessage());
            e.printStackTrace();
            System.exit(0);
        }
    }

}
