package it.polimi.ingsw.galaxytruckers.client.view.gui.guiElements;

import it.polimi.ingsw.galaxytruckers.client.controller.ComponentRegistry;

/**
 * Singleton registry for GUI component images.
 * <p>
 * This class loads and provides access to images for ship components used in the GUI.
 * It extends {@link GuiImageRegistry} and loads images based on the mapping provided by
 * {@link ComponentRegistry}. Images are loaded once and can be retrieved by their component ID.
 * </p>
 */
public class GuiComponentRegistry extends GuiImageRegistry{
    /**
     * The singleton instance of the registry.
     */
    private static GuiComponentRegistry instance;

    /**
     * Returns the singleton instance of the registry, creating it if necessary.
     *
     * @return the singleton instance
     */
    public static GuiComponentRegistry getInstance() {
        if (instance == null) {
            instance = new GuiComponentRegistry();
        }
        return instance;
    }

    /**
     * Private constructor that loads component images from the registry mapping.
     */
    private GuiComponentRegistry() {
        loadImages(ComponentRegistry.getInstance().getIdToImagePath());
    }
}
