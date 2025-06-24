package it.polimi.ingsw.galaxytruckers.view.guiElements;

import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract registry for loading and providing images for GUI components.
 * <p>
 * This class manages a mapping from integer IDs to JavaFX {@link Image} objects. Subclasses should call
 * {@link #loadImages(Map)} to populate the registry with images from file paths. Images can then be retrieved
 * by their ID using {@link #getImage(int)}.
 * </p>
 */
public abstract class GuiImageRegistry {
    /**
     * Mapping from image IDs to their loaded JavaFX Image objects.
     */
    private final Map<Integer, Image> idToImage = new HashMap<>();

    /**
     * Loads images from the given mapping of IDs to file paths.
     *
     * @param idToPath a map from image IDs to their file paths
     */
    protected void loadImages(Map<Integer, Path> idToPath) {
        for (int id : idToPath.keySet()) {
            try (InputStream is = Files.newInputStream(idToPath.get(id))) {
                Image image = new Image(is);
                if (image.isError()) {
                    System.err.println("Image failed to load: " + image.getException());
                }
                idToImage.put(id, image);
            } catch (IOException e) {
                System.err.println("Path not found: " + idToPath.get(id) + " for image " + id);
            }
        }
    }

    /**
     * Retrieves the image associated with the given ID.
     *
     * @param id the image ID
     * @return the JavaFX Image, or null if not found
     */
    public Image getImage(int id) {
        return idToImage.get(id);
    }
}
