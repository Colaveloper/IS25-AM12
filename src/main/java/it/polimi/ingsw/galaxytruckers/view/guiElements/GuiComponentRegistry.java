package it.polimi.ingsw.galaxytruckers.view.guiElements;

import it.polimi.ingsw.galaxytruckers.view.controller.ComponentRegistry;

public class GuiComponentRegistry extends GuiImageRegistry{
    private static GuiComponentRegistry instance;

    public static GuiComponentRegistry getInstance() {
        if (instance == null) {
            instance = new GuiComponentRegistry();
        }
        return instance;
    }

    private GuiComponentRegistry() {
        loadImages(ComponentRegistry.getInstance().getIdToImagePath());
    }
}
