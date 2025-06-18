package it.polimi.ingsw.galaxytruckers.view.guiElements;

import it.polimi.ingsw.galaxytruckers.view.controller.AdventureCardRegistry;
import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class GuiAdventureCardRegistry extends GuiImageRegistry{
    private static GuiAdventureCardRegistry instance;

    public static GuiAdventureCardRegistry getInstance() {
        if (instance == null) {
            instance = new GuiAdventureCardRegistry();
        }
        return instance;
    }

    private GuiAdventureCardRegistry() {
        loadImages(AdventureCardRegistry.getInstance().getIdToImagePath());
    }
}
