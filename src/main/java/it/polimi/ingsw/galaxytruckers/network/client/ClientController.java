package it.polimi.ingsw.galaxytruckers.network.client;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.serverController.events.Event;
import it.polimi.ingsw.galaxytruckers.view.*;
import it.polimi.ingsw.galaxytruckers.view.controller.EventHandler;
import it.polimi.ingsw.galaxytruckers.view.controller.PlayerRegistry;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.MetaState;
import it.polimi.ingsw.galaxytruckers.view.model.Player;

import java.awt.*;
import java.io.IOException;
import java.util.*;

public class ClientController implements ClientControllerInterface, ControllerToServer {
    private ClientModel model;
    private VirtualServer server;
    private View view;
    private final PlayerRegistry playerRegistry = new PlayerRegistry();

    private EventHandler eventHandler;

    public void setModel(ClientModel model) {
        this.model = model;
    }

    public void setServer(VirtualServer server) {
        this.server = server;
    }

    public void setView(View view) {
        this.view = view;
    }

    public void initEventHandler() {
        this.eventHandler = new EventHandler(model, playerRegistry);
    }

//---------------------------------------INTERNAL CALLS------------------------------------------

    public void showGameCreation() {
        model.setMetaState(MetaState.CREATION);
    }

    public void setMyNickname(String nickname) {
        Player player = playerRegistry.addPlayer(nickname);
        model.setPlayer(player);
        model.setMetaState(MetaState.JOINORCREATE);
    }

//--------------------------------------UPDATES FROM THE SERVER----------------------------------

    @Override
    public void notifyEvent(Event event) {
        eventHandler.handleEvent(event);
    }


//----------------------------------------REQUESTS TO THE SERVER----------------------------------

    @Override
    public void registerNickname(String nickname) throws IllegalArgumentException {
        server.registerNickname(nickname);
    }

    @Override
    public void joinLobby(UUID lobbyID) {
        server.joinLobby(lobbyID);
    }

    @Override
    public void requestNewGame(Level level, int playersN) {
        server.requestNewGame(level, playersN);
    }

    @Override
    public void reportError(String details) {
        System.out.println("Error: " + details);
        // view.show(ChosenStrategy)
    }

    @Override
    public void goNext() {
        try {
            server.goNext();
        } catch (IllegalArgumentException e) {
            reportError("could not go on with card");
        }
    }

    @Override
    public void chooseShipPiece(int choice) {
        try {
            server.chooseShipPiece(choice);
        } catch (IllegalArgumentException e) {
            reportError("couldn't choose ship piece");
        }
    }

    @Override
    public void activateComponent(Point point) {
        try {
            server.activateComponent(point);
        } catch (IllegalArgumentException e) {
            reportError("couldn't activate component");
        }
    }


    @Override
    public void removeComponent(Point point) {
        try {
            server.removeComponent(point);
        } catch (IllegalArgumentException e) {
            reportError("couldn't remove ship piece");
        }
    }

    @Override
    public void useBattery(Point point) {
        try {
            server.useBattery(point);
        } catch (IllegalArgumentException e) {
            reportError("couldn't spend battery");
        }
    }

    @Override
    public void initializeCabin(Point point, CrewType crewType) {
        try {
            server.initializeCabin(point, crewType);
        } catch (IllegalArgumentException e) {
            reportError("couldn't initialize cabin");
        }
    }

    @Override
    public void placeShipOnFlightboard(int startingPosition) {
        try {
            server.placeShipOnFlightBoard(startingPosition);
        } catch (IllegalArgumentException e) {
            reportError("couldn't place on flightboard");
        }
    }

    @Override
    public void flipHourglass() {
        try {
            server.flipHourglass();
        } catch (IllegalArgumentException e) {
            reportError("cannot flip hourglass");
        }
    }

    @Override
    public void requestRandComponent() {
        try {
            server.requestRandComponent();
        } catch (IllegalArgumentException e) {
            reportError("random component not available");
        }
    }

    @Override
    public void requestComponent(int componentId) {
        try {
            server.requestComponent(componentId);
        } catch (IllegalArgumentException e) {
            reportError("component of id " + componentId + " not available");
        }
    }

    @Override
    public void stashComponent() {
        try {
            server.stashComponent();
        } catch (IllegalArgumentException e) {
            reportError("cannot stash component");
        }
    }

    @Override
    public void grabStashedComponent(int index) {
        try {
            server.grabStashedComponent(index);
        } catch (IllegalArgumentException e) {
            reportError("cannot grab stashed component");
        }
    }

    @Override
    public void acquireForecast(int index) {
        try {
            server.acquireForecast(index);
        } catch (IllegalArgumentException e) {
            reportError("cannot acquire forecast");
        }
    }

    @Override
    public void releaseForecast() {
        try {
            server.releaseForecast();
        } catch (IllegalArgumentException e) {
            reportError("cannot release forecast");
        }
    }

    @Override
    public void rejectComponent() {
        try {
            server.rejectComponent();
        } catch (IllegalArgumentException e) {
            reportError("cannot reject component");
        }
    }

    @Override
    public void placeComponent(Point point, int orientation) {
        try {
            server.placeComponent(point, orientation);
        } catch (IllegalArgumentException e) {
            reportError("cannot place component");
        }
    }

    @Override
    public void drawCard(){
        try{
            server.drawCard();
        } catch(IllegalArgumentException e){
            reportError("Cannot draw new card");
        }
    }

    @Override
    public void placeGoods(Point point, GoodsType good){
        try{
            server.placeGoods(point, good);
        } catch(IllegalArgumentException e){
            reportError("Cannot place good");
        }
    }

    @Override
    public void removeGoods(Point point, GoodsType good){
        try{
            server.removeGoods(point, good);
        } catch(IllegalArgumentException e){
            reportError("Cannot remove good");
        }
    }

    @Override
    public void choosePlanet(int choice){
        try{
            server.choosePlanet(choice);
        } catch(IllegalArgumentException e){
            reportError("Cannot land on planet");
        }
    }

    @Override
    public void loseCrew(Point p){
        try{
            server.loseCrew(p);
        } catch(IllegalArgumentException e){
            reportError("Cannot remove crew member");
        }
    }

    @Override
    public void grabReward(boolean g){
        try{
            server.grabReward(g);
        }catch (IllegalArgumentException e){
            reportError("Cannot grab reward");
        }
    }

    @Override
    public void loseGoods(Point p){
        try{
            server.loseGoods(p);
        } catch(IllegalArgumentException e){
            reportError("Cannot lose good");
        }
    }

    @Override
    public void giveUp(){
        try {
            server.giveUp();
        } catch(IllegalArgumentException e){
            reportError("Cannot give up");
        }
    }

    private void runAndInterceptIOE(RunnableWithIOE action) {
        try {
            action.run();
        } catch (IOException e) {
            reportError("IO Exception: " + e.getMessage());
        }
    }
}

@FunctionalInterface
interface RunnableWithIOE {
    void run() throws IOException;
}
