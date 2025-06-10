package it.polimi.ingsw.galaxytruckers.view.guiElements;

import it.polimi.ingsw.galaxytruckers.view.controller.AdventureCardRegistry;
import javafx.scene.image.Image;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class GuiAdventureCardRegistry {
    private static GuiAdventureCardRegistry instance;
    private final Map<Integer, Image> idToImage;

    public static GuiAdventureCardRegistry getInstance() {
        if (instance == null) {
            instance = new GuiAdventureCardRegistry();
        }
        return instance;
    }

    private GuiAdventureCardRegistry() {
        idToImage = new HashMap<>();
        loadImages();
    }

    private void loadImages() {
        AdventureCardRegistry adventureCardRegistry = AdventureCardRegistry.getInstance();
        Map<Integer, Path> idToPath = adventureCardRegistry.getIdToImagePath();
        for (int i = 1; i < adventureCardRegistry.getSize(); i++) {
            try (InputStream is = Files.newInputStream(idToPath.get(i))) {
                Image image = new Image(is);
                if (image.isError()) {
                    System.err.println("Image failed to load: " + image.getException());
                }
                idToImage.put(i, image);
            } catch (IOException e) {
                System.err.println("Path not found: " + idToPath.get(i) + " for image " + i);
            }
        }
    }

    public Image getImage(int id) {
        return idToImage.get(id);
    }
}
