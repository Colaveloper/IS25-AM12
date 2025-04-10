package it.polimi.ingsw.galaxytruckers.serverController;

import it.polimi.ingsw.galaxytruckers.serverController.lobby.Lobby;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;

import java.awt.*;
import java.util.UUID;

public class Controller implements ControllerInterface {

    public void registerNickname(String nickname) {
        Player.addPlayer(nickname);
    }

    public UUID newGame(String creatorName, Level level, int numPlayers) {
        Player creator = Player.getPlayer(creatorName);
        Lobby newLobby = new Lobby(creator, level, numPlayers);
        return newLobby.getId();
    }

    public void joinLobby(String nickname, UUID lobbyID) {
        Lobby.getLobby(lobbyID).addPlayer(Player.getPlayer(nickname));
    }

    @Override
    public void leaveLobby(String nickname) {
        //TODO : decide whether to implement this method
    }

    @Override
    public void chooseColor(String nickname, Colors color) {
        Player player = Player.getPlayer(nickname);
        Lobby lobby = player.getLobby()
                .orElseThrow(() -> new IllegalArgumentException("You are not in a lobby"));
        lobby.chooseColor(player, color);
    }

    @Override
    public void saveGame(UUID lobbyID) {
        //TODO: implement saving current game state on disk
    }

    //TODO: implement these methods (calls to model methods with lobby state check
    @Override
    public void requestRandComponent(String nickname) {

    }

    @Override
    public void requestComponent(String nickname, UUID componentID) {

    }

    @Override
    public void rotateComponent(String nickname) {

    }

    @Override
    public void rejectComponent(String nickname) {

    }

    @Override
    public void stashComponent(String nickname) {

    }

    @Override
    public void grabStashedComponent(String nickname) {

    }

    @Override
    public void placeComponent(String nickname, Point point) {

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
    public void initializeCabin(CrewType crewType) {

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
    public void useBattery(String nickname, Point point) {

    }

    @Override
    public void giveUp(String nickname) {

    }

}
