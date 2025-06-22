package it.polimi.ingsw.galaxytruckers.network.client;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.Direction;

import java.awt.*;
import java.util.UUID;

public class ServerHandlerStub implements ServerHandler{
    @Override
    public boolean reconnect() {
        return false;
    }

    @Override
    public void dropConnection() {

    }

    @Override
    public void registerNickname(String myNickname) {

    }

    @Override
    public void requestNewGame(Level level, int playerN) {

    }

    @Override
    public void joinLobby(UUID lobbyID) {

    }

    @Override
    public void leaveLobby() {

    }

    @Override
    public void requestRandComponent() {

    }

    @Override
    public void requestComponent(int componentID) {

    }

    @Override
    public void rejectComponent() {

    }

    @Override
    public void stashComponent() {

    }

    @Override
    public void grabPlacedComponent() {

    }

    @Override
    public void grabStashedComponent(int index) {

    }

    @Override
    public void placeComponent(Point point, Direction orientation) {

    }

    @Override
    public void flipHourglass() {

    }

    @Override
    public void placeShipOnFlightBoard(int startingPosition) {

    }

    @Override
    public void placeShipOnFlightBoard() {

    }

    @Override
    public void acquireForecast(int deckIndex) {

    }

    @Override
    public void releaseForecast() {

    }

    @Override
    public void removeComponent(Point point) {

    }

    @Override
    public void chooseShipPiece(int pieceIndex) {

    }

    @Override
    public void initializeCabin(Point point, CrewType crewType) {

    }

    @Override
    public void drawCard() {

    }

    @Override
    public void activateComponent(Point point) {

    }

    @Override
    public void loseCrew(Point point) {

    }

    @Override
    public void grabReward() {

    }

    @Override
    public void placeGoods(Point point, GoodsType goodsType) {

    }

    @Override
    public void removeGoods(Point point, GoodsType goodsType) {

    }

    @Override
    public void loseGoods(Point point) {

    }

    @Override
    public void useBattery(Point point) {

    }

    @Override
    public void choosePlanet(int choice) {

    }

    @Override
    public void goNext() {

    }

    @Override
    public void giveUp() {

    }
}
