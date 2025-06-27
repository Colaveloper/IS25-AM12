package it.polimi.ingsw.galaxytruckers.client.view.gui.guiElements;

import it.polimi.ingsw.galaxytruckers.client.controller.AdventureCardRegistry;

/**
 * Singleton registry for GUI adventure card images.
 * <p>
 * This class loads and provides access to images for adventure cards used in the GUI.
 * It extends {@link GuiImageRegistry} and loads images based on the mapping provided by
 * {@link AdventureCardRegistry}. Images are loaded once and can be retrieved by their card ID.
 * </p>
 */
public class GuiAdventureCardRegistry extends GuiImageRegistry{
    /**
     * The singleton instance of the registry.
     */
    private static GuiAdventureCardRegistry instance;

    /**
     * Returns the singleton instance of the registry, creating it if necessary.
     *
     * @return the singleton instance
     */
    public static GuiAdventureCardRegistry getInstance() {
        if (instance == null) {
            instance = new GuiAdventureCardRegistry();
        }
        return instance;
    }

    /**
     * Private constructor that loads adventure card images from the registry mapping.
     */
    private GuiAdventureCardRegistry() {
        loadImages(AdventureCardRegistry.getInstance().getIdToImagePath());
    }
}
