package it.polimi.ingsw.galaxytruckers.view.guiElements;

import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public abstract class GuiImageRegistry {
    private final Map<Integer, Image> idToImage = new HashMap<>();

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

    public Image getImage(int id) {
        return idToImage.get(id);
    }
}
