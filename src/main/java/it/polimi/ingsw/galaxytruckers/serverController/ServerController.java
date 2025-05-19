package it.polimi.ingsw.galaxytruckers.serverController;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.FourColors;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Lobby;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;

import java.awt.*;
import java.util.UUID;

public class ServerController implements ServerControllerInterface {

    @Override
    public Player registerNickname(String nickname) {
        return Player.addPlayer(nickname);
    }

    @Override
    public void newGame(String creatorName, Level level, int numPlayers) {
        Player creator = Player.getPlayer(creatorName);
        Lobby newLobby = new Lobby(creator, level, numPlayers);
    }

    @Override
    public void joinLobby(String nickname, UUID lobbyID) {
        Lobby.getLobby(lobbyID).addPlayer(Player.getPlayer(nickname));
    }

    @Override
    public void leaveLobby(String nickname) {
        //TODO : decide whether to implement this method
    }

    //TODO: make color selection automatic
    @Override
    public void chooseColor(String nickname, FourColors color) {
        Player player = Player.getPlayer(nickname);
        Lobby lobby = player.getLobby()
                .orElseThrow(() -> new IllegalArgumentException("You are not in a lobby"));
        lobby.chooseColor(player, color);
    }

    //TODO: implement these methods (calls to model methods with lobby state check
    @Override
    public void requestRandComponent(String nickname) {

    }

    @Override
    public void requestComponent(String nickname, int componentID) {

    }

    @Override
    public void rejectComponent(String nickname) {

    }

    @Override
    public void stashComponent(String nickname) {

    }

    @Override
    public void grabStashedComponent(String nickname, int index) {

    }

    @Override
    public void placeComponent(String nickname, Point point, int orientation) {

    }

    @Override
    public void flipHourglass(String nickname) {

    }

    @Override
    public void placeShipOnFlightBoard(String nickname, int startingPosition) {

    }

    @Override
    public void acquireForecast(String nickname, int deckIndex) {

    }

    @Override
    public void releaseForecast(String nickname) {

    }

    @Override
    public void removeComponent(String nickname, Point point) {

    }

    @Override
    public void chooseShipPiece(String nickname, int pieceIndex) {

    }

    @Override
    public void initializeCabin(String nickname, Point point, CrewType crewType) {

    }

    @Override
    public void drawCard(String nickname) {

    }

    @Override
    public void activateComponent(String nickname, Point point) {

    }

    @Override
    public void loseCrew(String nickname, Point point) {

    }

    @Override
    public void grabReward(String nickname, boolean rewardGrabbed) {

    }

    @Override
    public void placeGoods(String nickname, Point point, GoodsType goodsType) {

    }

    @Override
    public void removeGoods(String nickname, Point point, GoodsType goodsType) {

    }

    @Override
    public void loseGoods(String nickname, Point point) {

    }

    @Override
    public void useBattery(String nickname, Point point) {

    }

    @Override
    public void choosePlanet(String nickname, int choice) {

    }

    @Override
    public void goNext(String nickname) {

    }

    @Override
    public void giveUp(String nickname) {

    }
}
