package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliShipBoard;
import it.polimi.ingsw.galaxytruckers.view.enums.ProjectileType;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.Projectile;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.HandleProjectileState;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.awt.*;
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
            updateContextBox("Select a component to activate or a battery to use");
        }
    }

    @Override
    protected VBox getFullShip(ShipBoard shipBoard) {
        VBox layout = super.getFullShip(shipBoard);
        layout.getChildren().add(createGuiProjectile());
        return layout;
    }

    private Node createGuiProjectile() {
        HBox projectileBox = new HBox(5);
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

    @Override
    public void notifyRemoveComponent(ShipBoard shipBoard, Point point) {
        guiShipBoards.get(shipBoard).notifyRemoveComponent(point);
    }
}
