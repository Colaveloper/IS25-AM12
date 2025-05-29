package it.polimi.ingsw.galaxytruckers.view;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.network.client.ClientController;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.CheatCodes;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.CliScreen;
import it.polimi.ingsw.galaxytruckers.view.cliScreens.ScreenFactory;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.MetaState;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.view.model.state.GameState;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class CliView implements View {
    ClientModel model;
    ClientController controller;
    ScreenFactory screenFactory;
    CliScreen currentScreen;
    MetaState metaState;

    public CliView(ClientController controller, ClientModel model) {
        startInputLoop();
        this.controller = controller;
        this.model = model;
        this.screenFactory = new ScreenFactory();
    }

    public void updateScreen() {
        if (isToUpdate()) {
            this.metaState = model.getMetaState();
            currentScreen = screenFactory.createCliScreen(model, controller);
        }
        currentScreen.render();
    }

    private boolean isToUpdate() {
        if (model.getMetaState() != metaState) return true;
        return model.getGame().getCurrentState() != null &&
                model.getGame().getCurrentState().getClass() != currentScreen.getState().getClass();
    }

    private void startInputLoop() {
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            String input;
            while (true) {
                if(model.isCheatOn() && !CheatCodes.cheatEmpty()){
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
                    //todo: this is called also when it s not your turn where u don t have to check invalid input format
                    //todo: ask the screen what to print, screens then prints either not your turn or a specific message
                    System.out.println("Invalid format, please check your input");
                    input = scanner.nextLine();
                }

                currentScreen.parseAndInvoke(input);
            }
        }).start();
    }

    @Override
    public void notifyMetaState(MetaState metaState) {
        currentScreen = screenFactory.createCliScreen(model, controller);
        currentScreen.render();
    }

    @Override
    public void notifyCurrentState(GameState gameState) {
        currentScreen = screenFactory.createCliScreen(model, controller);
        currentScreen.render();
    }

    @Override
    public void notifyRequestRandComponent(ShipBoard shipBoard, Component component) {
        currentScreen.notifyRequestRandComponent(shipBoard, component);
        currentScreen.render();
    }

    @Override
    public void notifyRequestComponent(ShipBoard shipBoard, Component component) {

    }

    @Override
    public void notifyRejectComponent(ShipBoard shipBoard, Component component) {

    }

    @Override
    public void notifyStashComponent(ShipBoard shipBoard, Component component) {

    }

    @Override
    public void notifyGrabStashedComponent(ShipBoard shipBoard, int index, Component component) {

    }

    @Override
    public void notifyPlaceComponent(ShipBoard shipBoard, Point point, int orientation) {

    }

    @Override
    public void notifyFlipHourglass(ShipBoard shipBoard) {

    }

    @Override
    public void notifyHourglassEnd() {

    }

    @Override
    public void notifyFlightBoardPosition(ShipBoard shipBoard, int position) {

    }

    @Override
    public void notifyPeekForecast(ShipBoard shipBoard, int deckIndex) {

    }

    @Override
    public void setForecastDeck(List<AdventureCard> adventureCards) {

    }

    @Override
    public void notifyReleaseForecast(ShipBoard shipBoard, int index) {

    }

    @Override
    public void notifyRemoveComponent(ShipBoard shipBoard, Point point) {

    }

    @Override
    public void notifyChooseShipPiece(ShipBoard shipBoard, int pieceIndex, List<Point> removed) {

    }

    @Override
    public void notifyShipNotConnected(ShipBoard shipBoard, List<Set<Point>> shipPieces) {

    }

    @Override
    public void notifyShipValidated(ShipBoard shipBoard) {

    }

    @Override
    public void notifyInitializeCabin(ShipBoard shipBoard, Point point, CrewType crewType, int numResidents) {

    }

    @Override
    public void notifyDrawCard(AdventureCard adventureCard) {

    }

    @Override
    public void notifyActivateComponent(ShipBoard shipBoard, Point point) {

    }

    @Override
    public void notifyLoseCrew(ShipBoard shipBoard, Point point) {

    }

    @Override
    public void notifyGrabReward(ShipBoard shipBoard, boolean rewardGrabbed) {

    }

    @Override
    public void notifyPlaceGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {

    }

    @Override
    public void notifyRemoveGoods(ShipBoard shipBoard, Point point, GoodsType goodsType) {

    }

    @Override
    public void notifyUseBattery(ShipBoard shipBoard, Point point) {

    }

    @Override
    public void notifyChoosePlanet(ShipBoard shipBoard, int choice) {

    }

    @Override
    public void notifyGiveUp(ShipBoard shipBoard) {

    }

    @Override
    public void setFinalScores(Map<Player, Integer> finalScores) {

    }

    @Override
    public void notifyRejectComponent(ShipBoard shipBoard, Component component, Point oldPosition) {

    }

    @Override
    public void notifyStashComponent(ShipBoard shipBoard, Component component, Point oldPosition) {

    }

    @Override
    public void notifyPlaceComponent(ShipBoard shipBoard, Point newPoint, int orientation, Point oldPosition) {

    }
}
