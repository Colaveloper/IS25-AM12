package it.polimi.ingsw.galaxytruckers.view;

import javafx.application.Application;
import javafx.stage.Stage;

public class JFXApp extends Application {
    private static GuiView guiView;

    public static void setGuiView(GuiView guiView) {
        JFXApp.guiView = guiView;
    }

    @Override
    public void start(Stage stage) throws Exception {
        guiView.setStage(stage);
        stage.show();
    }
}
