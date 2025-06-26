package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.guiElements.GuiAdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.AdventureState;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.Optional;

/**
 * Abstract base class for all adventure phase screens in Galaxy Truckers.
 * This screen is displayed during the adventure phase of the game when players
 * are encountering adventure cards such as pirates, planets, or other space events.
 * It extends the GuiGameScreen to provide adventure-specific UI elements and functionality.
 */
public abstract class GuiAdventureScreen extends GuiGameScreen {

    protected final AdventureState state;

    /** Container for displaying the current adventure card */
    protected VBox cardBox;
    
    /**
     * Constructs a GuiAdventureScreen with the specified model, controller, and adventure state.
     * Initializes the adventure-specific UI elements including a "Give Up" button that allows
     * players to surrender from the current game.
     *
     * @param model The client model containing all game data
     * @param controller The controller for sending commands to the server
     * @param gameState The current adventure state
     */
    public GuiAdventureScreen(ClientModel model, ControllerToServer controller, AdventureState gameState) {
        super(model, controller, gameState);
        this.state = gameState;
        Button giveUpButton = new Button("Give Up");
        giveUpButton.setOnAction(e -> {
            controller.giveUp();
            guiLog.log("You've given up, but you must continue playing this card. Giving up will take effect at the end of the card.");
        });
        guiButtonBox.getChildren().add(giveUpButton);
        guiLog.log("It's " + model.getPlayerByShip(state.getShipBoard()).getNickname() + "'s turn now.");
    }

    /**
     * Determines whether it's the current player's turn in the adventure phase.
     * This is used to enable or disable UI controls based on turn status.
     *
     * @return true if it's the current player's turn, false otherwise
     */
    protected boolean isMyTurn() {
        return state.getShipBoard() == myShipBoard;
    }

    /**
     * Creates the flexible use vertical box containing the adventure card display.
     * If the player has surrendered, it displays a message instead of the card.
     * This area shows the current adventure card that players are dealing with.
     *
     * @return A VBox containing either the current adventure card or a surrender message
     */
    @Override
    protected VBox getFreeUseVBox() {
        cardBox = new VBox(5);
        if (state.getGame().getGivenUpShips().contains(myShipBoard)) {
            Label surrenderLabel = new Label("You've surrendered, watch other players compete.");
            surrenderLabel.setStyle("-fx-text-fill: white;");
            cardBox.getChildren().add(surrenderLabel);
            return cardBox;
        }
        cardBox.setAlignment(Pos.CENTER);
        guiAdventureCard().ifPresent((guiAdventureCard)-> {
            guiAdventureCard.setFitHeight(200);
            cardBox.getChildren().add(guiAdventureCard);
        });
        return cardBox;
    }

    /**
     * Creates a vertical box containing the specified ship board.
     * This method provides a simple implementation that just displays the ship board
     * without additional UI elements that might be present in other game phases.
     *
     * @param shipBoard The ship board to be displayed
     * @return A VBox containing the ship board
     */
    @Override
    protected VBox getShipBoardVBox(ShipBoard shipBoard) {
        VBox getShipBoardVBox = new VBox(5);
        getShipBoardVBox.setAlignment(Pos.CENTER);
        getShipBoardVBox.getChildren().addAll(guiShipBoards.get(shipBoard));
        return getShipBoardVBox;
    }

    /**
     * Creates a GUI representation of the current adventure card if one exists.
     * This method encapsulates the logic for retrieving and displaying the current card.
     *
     * @return An Optional containing the GUI adventure card, or empty if no card is present
     */
    protected Optional<GuiAdventureCard> guiAdventureCard() {
        return state.getCurrentCard() == null ? Optional.empty() : Optional.of(new GuiAdventureCard(state.getCurrentCard().getId()));
    }
}
