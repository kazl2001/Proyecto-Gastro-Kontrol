package ui.main;

import jakarta.enterprise.inject.se.SeContainer;
import jakarta.enterprise.inject.se.SeContainerInitializer;
import jakarta.enterprise.util.AnnotationLiteral;
import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class DIJavafx extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        SeContainerInitializer initializer = SeContainerInitializer.newInstance();
        final SeContainer container = initializer.initialize();
        primaryStage.setMinWidth(1920);
        primaryStage.setMinHeight(1080);
        primaryStage.setResizable(false); // Resize the window
        primaryStage.setMaximized(true);// Maximize the window when you start it
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/media/logo.png"))); // Logo Image load

        container.getBeanManager().getEvent().select(new AnnotationLiteral<StartupScene>() {
        }).fire(primaryStage);
    }

}
