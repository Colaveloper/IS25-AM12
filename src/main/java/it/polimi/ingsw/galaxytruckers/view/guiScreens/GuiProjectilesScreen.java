package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.guiElements.PurpleVBox;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.Projectile;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.HandleProjectileState;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.io.InputStream;

public class GuiProjectilesScreen extends GuiActivationScreen {
    private final Projectile projectile;
    private final InputStream bigFirePath = getClass().getResourceAsStream("/textures/Projectiles/BigFire.png");
    private final InputStream smallFirePath = getClass().getResourceAsStream("/textures/Projectiles/SmallFire.png");
    private final InputStream bigMeteorPath = getClass().getResourceAsStream("/textures/Projectiles/BigMeteor.png");
    private final InputStream smallMeteorPath = getClass().getResourceAsStream("/textures/Projectiles/SmallMeteor.png");

    public GuiProjectilesScreen(ClientModel model, ControllerToServer controller, HandleProjectileState handleProjectileState) {
        super(model, controller, handleProjectileState);
        this.projectile = handleProjectileState.getProjectile();
        if (isMyTurn()) {
            guiLog.log("Select a shield to activate or a battery to use");
        }
    }

    @Override
    protected VBox getFreeUseVBox() {
        VBox superVBox = super.getFreeUseVBox();
        superVBox.getChildren().add(getProjectileVBox());
        return superVBox;
    }

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
