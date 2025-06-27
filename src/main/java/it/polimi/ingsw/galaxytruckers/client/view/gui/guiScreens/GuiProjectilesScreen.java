package it.polimi.ingsw.galaxytruckers.client.view.gui.guiScreens;

import it.polimi.ingsw.galaxytruckers.client.controller.ClientControllerInterface;
import it.polimi.ingsw.galaxytruckers.client.view.gui.guiElements.PurpleVBox;
import it.polimi.ingsw.galaxytruckers.client.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.client.model.adventureCards.Projectile;
import it.polimi.ingsw.galaxytruckers.client.model.state.HandleProjectileState;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.InputStream;

/**
 * GUI screen for handling projectile events in the game.
 * <p>
 * This screen is shown when a projectile (such as a meteor) needs to be handled by the player.
 * It displays the projectile and allows the player to select a shield or battery to respond if it is their turn.
 * </p>
 *
 * @author (your name or team)
 */
public class GuiProjectilesScreen extends GuiActivationScreen {
    private final Projectile projectile;
    private final InputStream bigFirePath = getClass().getResourceAsStream("/textures/Projectiles/BigFire.png");
    private final InputStream smallFirePath = getClass().getResourceAsStream("/textures/Projectiles/SmallFire.png");
    private final InputStream bigMeteorPath = getClass().getResourceAsStream("/textures/Projectiles/BigMeteor.png");
    private final InputStream smallMeteorPath = getClass().getResourceAsStream("/textures/Projectiles/SmallMeteor.png");

    /**
     * Constructs a new GuiProjectilesScreen.
     *
     * @param model the client model
     * @param controller the controller to communicate with the server
     * @param handleProjectileState the state containing the projectile to handle
     */
    public GuiProjectilesScreen(ClientModel model, ClientControllerInterface controller, HandleProjectileState handleProjectileState) {
        super(model, controller, handleProjectileState);
        this.projectile = handleProjectileState.getProjectile();
        if (isMyTurn()) {
            guiLog.log("Select a shield to activate or a battery to use");
        }
    }

    /**
     * Returns a VBox containing the UI elements for free use actions and the projectile display.
     *
     * @return VBox with UI elements
     */
    @Override
    protected VBox getFreeUseVBox() {
        VBox superVBox = super.getFreeUseVBox();
        superVBox.getChildren().add(getProjectileVBox());
        return superVBox;
    }

    /**
     * Creates and returns a PurpleVBox displaying the projectile image and information.
     *
     * @return PurpleVBox with projectile details
     */
    private PurpleVBox getProjectileVBox() {
        PurpleVBox projectileBox = new PurpleVBox(5);
        projectileBox.setAlignment(Pos.CENTER);
        Image projectileImage = new Image(switch (projectile.type()) {
            case BIGMETEOR -> bigMeteorPath;
            case SMALLMETEOR -> smallMeteorPath;
            case BIGFIRE -> bigFirePath;
            case SMALLFIRE -> smallFirePath;
        });
        ImageView projectileView = new ImageView(projectileImage);
        projectileView.setFitWidth(50);
        projectileView.setFitHeight(50);
        projectileView.setRotate(projectile.direction().getAngle());

        Label rollLabel = new Label("⚄: "+projectile.roll());
        rollLabel.setFont(new Font(24));
        rollLabel.setTextFill(Color.WHITE);

        projectileBox.getChildren().addAll(projectileView, rollLabel);
        return projectileBox;
    }
}
