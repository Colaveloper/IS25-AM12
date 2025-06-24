package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.state.AdventureState;
import it.polimi.ingsw.galaxytruckers.view.model.state.ChooseShipPieceState;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.StateActions;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import java.awt.Point;
import java.util.List;
import java.util.Set;

/**
 * GUI screen for handling the selection and removal of disconnected ship pieces.
 * <p>
 * This screen is shown when a player must choose a disconnected piece of their ship to keep.
 * It highlights all available ship pieces, allows the player to select one, and provides UI controls
 * for confirming. The screen updates the log and UI based on the player's turn.
 * </p>
 *
 * @author (your name or team)
 */
public class GuiShipPieceChoiceScreen extends GuiAdventureScreen {
    private final IntegerProperty selectedPieceIndex = new SimpleIntegerProperty(-1);
    private final List<Set<Point>> shipPieces;

    /**
     * Constructs a new GuiShipPieceChoiceScreen.
     *
     * @param model the client model
     * @param controller the controller to communicate with the server
     * @param gameState the current adventure state (should be ChooseShipPieceState)
     */
    public GuiShipPieceChoiceScreen(ClientModel model, ControllerToServer controller, AdventureState gameState) {
        super(model, controller, gameState);
        ChooseShipPieceState chooseState = (ChooseShipPieceState) gameState;
        this.shipPieces = chooseState.getShipPieces();
        highlightShipPieces();
        if (isMyTurn()) {
            guiLog.log("Your turn: select a disconnected ship piece to remove.");
            setupButtonBox();
        } else {
            guiLog.log("Wait for your turn to remove a ship piece.");
        }
    }

    private void highlightShipPieces() {
        List<Color> colors = List.of(Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.PURPLE, Color.BROWN, Color.PINK, Color.YELLOW);
        for (int i = 0; i < shipPieces.size(); i++) {
            Color color = colors.get(i % colors.size());
            guiShipBoards.get(myShipBoard).highlightPoints(shipPieces.get(i), color);
        }
    }

    private void setupButtonBox() {
        guiButtonBox.getChildren().clear();
        VBox actionButtons = new VBox(5);
        actionButtons.setPadding(new Insets(5));

        Button removeButton = new Button("Keep Selected Piece");
        removeButton.disableProperty().bind(
            Bindings.createBooleanBinding(
                () -> selectedPieceIndex.get() < 0 || selectedPieceIndex.get() >= shipPieces.size(),
                selectedPieceIndex
            )
        );
        removeButton.setOnAction(_ -> {
            controller.chooseShipPiece(selectedPieceIndex.get());
            selectedPieceIndex.set(-1);
            getGuiController().goNext();
        });

        actionButtons.getChildren().addAll(removeButton);
        guiButtonBox.getChildren().add(actionButtons);
    }

    @Override
    protected GuiController getGuiController() {
        return new GuiController() {
            @Override
            public void handlePointPress(Point point) {
                if (!isMyTurn()) return;
                highlightShipPieces();
                for (int i = 0; i < shipPieces.size(); i++) {
                    if (shipPieces.get(i).contains(point)) {
                        selectedPieceIndex.set(i);
                        guiLog.log("Selected piece #" + (i+1));
                        guiShipBoards.get(myShipBoard).highlightPoints(shipPieces.get(i), Color.YELLOW);
                        break;
                    }
                }
            }

            @Override
            public void goNext() {
                if (state.getAvailableActions().contains(StateActions.GO_NEXT)) {
                    controller.goNext();
                }
            }
        };
    }
}
