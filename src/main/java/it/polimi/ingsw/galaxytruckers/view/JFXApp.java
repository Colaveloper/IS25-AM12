package it.polimi.ingsw.galaxytruckers.view;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * JavaFX application entry point for the Galaxy Truckers game GUI.
 * This class serves as a bridge between the JavaFX application lifecycle
 * and the game's GUI view system.
 */
public class JFXApp extends Application {
    /** The GUI view instance that will be attached to this JavaFX application */
    private static GuiView guiView;

    /**
     * Sets the GUI view to be used by this JavaFX application.
     * This method should be called before the JavaFX application is launched.
     *
     * @param guiView The GUI view instance to be used
     */
    public static void setGuiView(GuiView guiView) {
        JFXApp.guiView = guiView;
    }

    /**
     * Initializes the JavaFX application stage.
     * This method is called automatically by the JavaFX runtime
     * after the application is launched. It delegates to the
     * GuiView to set up the stage with the game's user interface.
     *
     * @param stage The primary stage for this application
     * @throws Exception If an error occurs during initialization
     */
    @Override
    public void start(Stage stage) throws Exception {
        guiView.setStage(stage);
        stage.show();
    }
}
