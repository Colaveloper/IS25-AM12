package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import it.polimi.ingsw.galaxytruckers.network.client.ControllerToServer;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import it.polimi.ingsw.galaxytruckers.view.guiElements.*;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.Hourglass;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.SecondShipBuildingState;
import it.polimi.ingsw.galaxytruckers.view.model.state.StateActions;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * GUI screen for the second ship building phase of the game.
 * <p>
 * This screen manages the UI and logic for the second round of ship building, including
 * handling the hourglass timer, forecast display, and player stashes. It extends the base
 * GuiShipBuildingScreen to provide additional features specific to the second phase.
 * </p>
 *
 * @author (your name or team)
 */
public class GuiSecondShipBuildingScreen extends GuiShipBuildingScreen {

    private final GuiForecast guiForecast;
    private final Map<ShipBoard, GuiStash> guiStashes;
    private Button hourglassButton;
    private final SecondShipBuildingState gameState;
    private boolean isFirstRender = true;
    private boolean hasLoggedEndMessage = false;
    private HBox cardsHBox;

    /**
     * Constructs a new GuiSecondShipBuildingScreen.
     *
     * @param model the client model
     * @param controller the controller to communicate with the server
     * @param state the state for the second ship building phase
     */
    public GuiSecondShipBuildingScreen(ClientModel model, ControllerToServer controller, SecondShipBuildingState state) {
        super(model, controller, state);
        this.gameState = state;
        guiForecast = new GuiForecast(state.getBlockedForecasts(), getGuiController());
        guiStashes = new HashMap<>();
        for (ShipBoard shipBoard : model.getGame().getShipBoards()) {
            guiStashes.put(shipBoard, new GuiStash(shipBoard.getStashedComponents(), getGuiController()));
        }

        setupHourglassButton();
    }

    private void setupHourglassButton() {
        hourglassButton = new Button("⏳ 60");
        hourglassButton.setFont(Font.font("System", FontWeight.BOLD, 14));
        hourglassButton.setPadding(new Insets(5, 10, 5, 10));
        hourglassButton.setOnAction(e -> {
            SecondShipBuildingState gameState = (SecondShipBuildingState) state;
            Hourglass hourglass = gameState.getHourglass();

            if (hourglass != null && !hourglass.getIsRunning()) {
                if (hourglass.getFlipsLeft() == 1) {
                    guiLog.log("This is the FINAL hourglass flip! You must place your ship on the flight board to use it.");
                }
                if (state.getAvailableActions().contains(StateActions.FLIP_HOURGLASS)) {
                    controller.flipHourglass();
                }
            }
        });

        updateHourglassButton();
        guiButtonBox.getChildren().add(hourglassButton);

        Timeline hourglassTimeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateHourglassButton()));
        hourglassTimeline.setCycleCount(Timeline.INDEFINITE);
        hourglassTimeline.play();
    }

    private void updateHourglassButton() {
        Platform.runLater(() -> {
            Hourglass hourglass = gameState.getHourglass();

            if (hourglass != null) {
                if (hourglass.getIsRunning()) {
                    long timeLeft = hourglass.getTimeLeft();
                    hourglassButton.setText("⏳ " + timeLeft);

                    if (timeLeft <= 10) {
                        hourglassButton.setTextFill(Color.RED);
                      } else if (timeLeft <= 20) {
                        hourglassButton.setTextFill(Color.ORANGE);
                      } else {
                        hourglassButton.setTextFill(Color.BLACK);
                      }

                      if (isFirstRender && hourglass.getFlipsLeft() == 0) {
                        guiLog.log("WARNING: This is the FINAL hourglass flip!");
                        isFirstRender = false;
                      }
                      hasLoggedEndMessage = false;
                } else {
                    hourglassButton.setText("⏳");
                    hourglassButton.setTextFill(Color.BLACK);

                    isFirstRender = true;

                    if (!hasLoggedEndMessage && hourglass.getFlipsLeft() < 3) {
                        if (hourglass.getFlipsLeft() == 1) {
                            guiLog.log("Hourglass ended, ready for another flip. Final flip available once you place your ship on the flight board.");
                        } else {
                            guiLog.log("Hourglass ended, ready for another flip.");
                        }
                        hasLoggedEndMessage = true;
                    }
                }
            }
        });
        cardsHBox = new HBox(20);
    }

    @Override
    protected VBox getShipBoardVBox(ShipBoard shipBoard) {
        VBox getShipBoardVBox = new VBox(5);
        getShipBoardVBox.setAlignment(Pos.CENTER);

        PurpleVBox shipBoardVBox = guiShipBoards.get(shipBoard);

        HBox handAndStashBox = new HBox();
        handAndStashBox.getChildren().addAll(guiHands.get(shipBoard), guiStashes.get(shipBoard));

        getShipBoardVBox.getChildren().addAll(shipBoardVBox, handAndStashBox);

        return getShipBoardVBox;
    }

    @Override
    protected VBox getFreeUseVBox() {
        VBox freeUseVBox = new VBox(5);
        freeUseVBox.getChildren().addAll(guiForecast, guiComponentBank);
        return freeUseVBox;
    }

    @Override
    public void notifyStashComponent(ShipBoard shipBoard, Component component) {
        guiStashes.get(shipBoard).notifyStash(component);
        guiHands.get(shipBoard).notifyClearHand();
    }

    @Override
    public void notifyStashComponent(ShipBoard shipBoard, Component component, Point oldPosition) {
        guiStashes.get(shipBoard).notifyStash(component);
        guiShipBoards.get(shipBoard).notifyRemoveComponent(oldPosition);
        guiStatBox.notifyChange();
    }

    @Override
    public void notifyGrabStashedComponent(ShipBoard shipBoard, int index, Component component) {
        guiStashes.get(shipBoard).notifyGrab(index);
        guiHands.get(shipBoard).notifySetHand(component);
    }

    @Override
    public void notifyFlipHourglass(ShipBoard shipBoard) {
        updateHourglassButton();

        // Log a message similar to CLI implementation
        SecondShipBuildingState gameState = (SecondShipBuildingState) state;
        Hourglass hourglass = gameState.getHourglass();
        if (hourglass != null) {
            if (hourglass.getFlipsLeft() == 0) {
                guiLog.log("Hourglass flipped for the FINAL time!");
                guiLog.log("All players must complete their ships before the timer ends!");
            } else {
                guiLog.log("Hourglass flipped!");
            }
        }
    }

    @Override
    public void notifyHourglassEnd() {
        updateHourglassButton();
    }

    @Override
    public void notifyPeekForecast(ShipBoard shipBoard, int deckIndex) {
        if (shipBoard == myShipBoard) {
            guiForecast.notifyMePeekForecast();
            Platform.runLater(() -> {
                guiLog.log("See the forecast and release it to continue building");

                Button releaseButton = new Button("Release");
                releaseButton.setOnAction(e -> controller.releaseForecast());

                guiButtonBox.getChildren().clear();
                guiButtonBox.getChildren().add(releaseButton);

                guiStatBox.notifyChange();
            });
        } else {
            Platform.runLater(() -> {
                guiForecast.notifyOtherPeekForecast(shipBoard, deckIndex);
                guiLog.log("The deck number "+(deckIndex+1)+ " has been taken");
            });
        }
    }

    @Override
    public void setForecastDeck(List<AdventureCard> adventureCards) {
        guiForecast.setForecastDeck(adventureCards);

    }

    @Override
    public void notifyReleaseForecast(ShipBoard shipBoard, int deckIndex) {
        if (shipBoard == myShipBoard) {
            guiForecast.notifyMeReleaseForecast();
            Platform.runLater(() -> {
                guiLog.log("Now you can continue builing");
                guiButtonBox.getChildren().clear();
                setupHourglassButton();
            });
        } else {
            Platform.runLater(() -> {
                guiForecast.notifyOtherReleaseForecast(shipBoard, deckIndex);
                guiLog.log("The deck number "+(deckIndex+1)+ " has been released");
            });
        }
    }

    @Override
    protected GuiController getGuiController() {
        return new GuiController() {
            @Override
            public void placeShipOnFlightBoard(int position) {
                if (state.getAvailableActions().contains(StateActions.PLACE_SHIP_ON_FLIGHTBOARD)) {
                    controller.placeShipOnFlightboard(position);
                }
            }

            @Override
            public void requestRandComponent() {
                if (state.getAvailableActions().contains(StateActions.REQUEST_RAND_COMPONENT)) {
                    if (!state.componentInHand()) {
                        controller.requestRandComponent();
                    } else {
                        guiLog.log("Don't be greedy! Free you hand first");
                    }
                }
            }

            @Override
            public void rejectComponent() {
                if (state.getAvailableActions().contains(StateActions.REJECT_COMPONENT)) {
                    controller.rejectComponent();
                }
            }

            @Override
            public void requestComponent(int id) {
                if (state.getAvailableActions().contains(StateActions.REQUEST_COMPONENT)) {
                    controller.requestComponent(id);
                }
            }

            @Override
            public void rotateHandComponent() {
                if (myShipBoard.getLastComponent() != null) {
                    Direction currentDirection = myShipBoard.getLastComponent().getOrientation();
                    Direction newDirection = currentDirection.getLeft();
                    myShipBoard.getLastComponent().setOrientation(newDirection);
                    guiHands.get(myShipBoard).setComponentDirection(newDirection);
                }
            }

            @Override
            public void stashComponent() {
                if (state.getAvailableActions().contains(StateActions.STASH_COMPONENT)) {
                    controller.stashComponent();
                }
            }

            @Override
            public void grabStashedComponent(int i) {
                if (state.getAvailableActions().contains(StateActions.GRAB_STASHED_COMPONENT)) {
                    controller.grabStashedComponent(i);
                }
            }

            @Override
            public void handlePointPress(Point point) {
                // clicking on a component that has been placed on the ship
                if (myShipBoard.getComponentMap().containsKey(point)) {
                    // we grab the component only if it hasn't been welded
                    if (myShipBoard.getLastPosition() != null &&
                            myShipBoard.getLastPosition().equals(point) &&
                            state.getAvailableActions().contains(StateActions.GRAB_PLACED_COMPONENT)) {
                        Component component = myShipBoard.getComponentMap().get(point);
                        controller.grabPlacedComponent();
                        guiShipBoards.get(myShipBoard).notifyRemoveComponent(point);
                        guiHands.get(myShipBoard).notifySetHand(component);
                    }
                }
                // place the component on an empty space on the ship
                else if (myShipBoard.getShipArea().contains(point) &&
                        !myShipBoard.getComponentMap().containsKey(point) &&
                        myShipBoard.getLastComponent() != null &&
                        myShipBoard.getLastPosition() == null) {
                    if (state.getAvailableActions().contains(StateActions.PLACE_COMPONENT)) {
                        controller.placeComponent(point, myShipBoard.getLastComponent().getOrientation());
                    }
                }
            }

            @Override
            public void acquireForecast(int finalI) {
                if (state.getAvailableActions().contains(StateActions.ACQUIRE_FORECAST)) {
                    controller.acquireForecast(finalI);
                }
            }

            @Override
            public void releaseForecast() {
                if (state.getAvailableActions().contains(StateActions.RELEASE_FORECAST)) {
                    controller.releaseForecast();
                }
            }
        };
    }
}

