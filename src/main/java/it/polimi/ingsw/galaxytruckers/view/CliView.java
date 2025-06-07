package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.CheatCodes;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.CliScreen;
import it.polimi.ingsw.galaxytruckers.view.model.*;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.MetaState;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;

import java.awt.*;
import java.util.*;
import java.util.List;

public class CliView extends View<CliScreen> {
    CliScreen currentScreen;

    public CliView(ClientController controller, ClientModel model) {
        super(model, controller, new CliScreenFactory());
        startInputLoop();
        notifyMetaState(MetaState.REGISTER);
    }

    //region event-notify methods
    @Override
    public void notifyMetaState(MetaState metaState) {
        currentScreen = screenFactory.createScreen(metaState, model, controller);
        currentScreen.render();
    }

    @Override
    public void notifyCurrentState(GameState gameState) {
        currentScreen = screenFactory.createScreen(gameState, model, controller);
        currentScreen.render();
    }

    @Override
    public void notifyNewLobby(Lobby lobby) {
        currentScreen.notifyNewLobby(lobby);
        currentScreen.render();
    }

    @Override
    public void notifyRemoveLobby(UUID uuid) {
        currentScreen.notifyRemoveLobby(uuid);
        currentScreen.render();
    }

    @Override
    public void notifyRequestRandComponent(ShipBoard shipBoard, Component component) {
        currentScreen.notifyRequestRandComponent(shipBoard, component);
        currentScreen.render();
    }

    @Override
    public void notifyRequestComponent(ShipBoard shipBoard, Component component) {
        currentScreen.notifyRequestComponent(shipBoard, component);
        currentScreen.render();
    }

    @Override
    public void notifyRejectComponent(ShipBoard shipBoard, Component component) {
        currentScreen.notifyRejectComponent(shipBoard, component);
        currentScreen.render();
    }

    @Override
    public void notifyStashComponent(ShipBoard shipBoard, Component component) {
        currentScreen.notifyStashComponent(shipBoard, component);
        currentScreen.render();
    }

    @Override
    public void notifyGrabStashedComponent(ShipBoard shipBoard, int index, Component component) {
        currentScreen.notifyGrabStashedComponent(shipBoard, index, component);
        currentScreen.render();
    }

    @Override
    public void notifyPlaceComponent(ShipBoard shipBoard, Point point, Direction orientation) {
        currentScreen.notifyPlaceComponent(shipBoard, point, orientation);
        currentScreen.render();
    }

    @Override
    public void notifyFlipHourglass(ShipBoard shipBoard) {
        currentScreen.notifyFlipHourglass(shipBoard);
        currentScreen.render();
    }

    @Override
    public void notifyHourglassEnd() {
        currentScreen.notifyHourglassEnd();
        currentScreen.render();
    }

    @Override
    public void notifyFlightBoardPosition(ShipBoard shipBoard, int position) {
        currentScreen.notifyFlightBoardPosition(shipBoard, position);
        currentScreen.render();
    }

    @Override
    public void notifyPeekForecast(ShipBoard shipBoard, int deckIndex) {
        currentScreen.notifyPeekForecast(shipBoard, deckIndex);
        currentScreen.render();
    }

    @Override
    public void setForecastDeck(List<AdventureCard> adventureCards) {
        currentScreen.setForecastDeck(adventureCards);
        currentScreen.render();
    }

    @Override
    public void notifyReleaseForecast(ShipBoard shipBoard, int index) {
        currentScreen.notifyReleaseForecast(shipBoard, index);
        currentScreen.render();
    }

    @Override
    public void notifyRemoveComponent(ShipBoard shipBoard, Point point) {
        currentScreen.notifyRemoveComponent(shipBoard, point);
        currentScreen.render();
    }

    @Override
    public void notifyChooseShipPiece(ShipBoard shipBoard, int pieceIndex, List<Point> removed) {
        currentScreen.notifyChooseShipPiece(shipBoard, pieceIndex, removed);
        currentScreen.render();
    }

    @Override
    public void notifyShipNotConnected(ShipBoard shipBoard, List<Set<Point>> shipPieces) {
        currentScreen.notifyShipNotConnected(shipBoard, shipPieces);
        currentScreen.render();
    }

    @Override
    public void notifyShipValidated(ShipBoard shipBoard) {
        currentScreen.notifyShipValidated(shipBoard);
        currentScreen.render();
    }

    @Override
    public void notifyInitializeCabin(ShipBoard shipBoard, Point point, CrewType crewType, int numResidents) {
        currentScreen.notifyComponentChange(shipBoard, point);
        //currentScreen.notifyInitializeCabin(shipBoard, point, crewType, numResidents);
        currentScreen.render();
    }

    @Override
    public void notifyDrawCard(AdventureCard adventureCard) {
        currentScreen.notifyDrawCard(adventureCard);
        currentScreen.render();
    }

    @Override
    public void notifyActivateComponent(ShipBoard shipBoard, Point point) {

        //currentScreen.notifyActivateComponent(shipBoard, point);
        currentScreen.render();
    }

    @Override
    public void notifyLoseCrew(ShipBoard shipBoard, Point point) {
        currentScreen.notifyComponentChange(shipBoard, point);
//        currentScreen.notifyLoseCrew(shipBoard, point);
        currentScreen.render();
    }

    @Override
    public void notifyGrabReward(ShipBoard shipBoard, boolean rewardGrabbed) {
        currentScreen.notifyGrabReward(shipBoard, rewardGrabbed);
        currentScreen.render();
    }

    @Override
    public void notifyPlaceGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {
        currentScreen.notifyComponentChange(shipBoard, point);
        //currentScreen.notifyPlaceGoods(shipBoard, point, goodsType);
        currentScreen.render();
    }

    @Override
    public void notifyRemoveGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {
        currentScreen.notifyComponentChange(shipBoard, point);
        //currentScreen.notifyRemoveGoods(shipBoard, point, goodsType);
        currentScreen.render();
    }

    @Override
    public void notifyUseBattery(ShipBoard shipBoard, Point point) {
        currentScreen.notifyComponentChange(shipBoard, point);
        //currentScreen.notifyUseBattery(shipBoard, point);
        currentScreen.render();
    }

    @Override
    public void notifyChoosePlanet(ShipBoard shipBoard, int choice) {
        currentScreen.notifyChoosePlanet(shipBoard, choice);
        currentScreen.render();
    }

    @Override
    public void notifyGiveUp(ShipBoard shipBoard) {
        currentScreen.notifyGiveUp(shipBoard);
        currentScreen.render();
    }

    @Override
    public void setFinalScores(Map<Player, Integer> finalScores) {
        currentScreen.setFinalScores(finalScores);
        currentScreen.render();
    }

    @Override
    public void notifyRejectComponent(ShipBoard shipBoard, Component component, Point oldPosition) {
        currentScreen.notifyRejectComponent(shipBoard, component, oldPosition);
        currentScreen.render();
    }

    @Override
    public void notifyStashComponent(ShipBoard shipBoard, Component component, Point oldPosition) {
        currentScreen.notifyStashComponent(shipBoard, component, oldPosition);
        currentScreen.render();
    }

    @Override
    public void notifyPlaceComponent(ShipBoard shipBoard, Point newPoint, Direction orientation, Point oldPosition) {
        currentScreen.notifyPlaceComponent(shipBoard, newPoint, orientation, oldPosition);
        currentScreen.render();
    }
    //endregion

    private void startInputLoop() {
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            String input;
            while (true) {
                if(CheatCodes.isCheatOn() && !CheatCodes.cheatEmpty()){
                    try {
                        input = CheatCodes.cheat();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                else {
                    input = scanner.nextLine();
                }

                // letting the user correct format errors
                while (!currentScreen.isInputLegal(input)) {
                    System.out.println("Invalid format, please check your input");
                    input = scanner.nextLine();
                }

                currentScreen.parseAndInvoke(input);
            }
        }).start();
    }

    @Override
    public void reportError(String message) {
        System.out.println("\u001B[31mError: " + message + "\u001B[0m");
    }
}
