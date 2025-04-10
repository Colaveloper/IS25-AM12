package it.polimi.ingsw.galaxytruckers.view;

import javafx.scene.image.Image;

import javax.smartcardio.Card;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ClientGameModel {
    private String currentPlayerNickname;
    private String myNickname;

    private FlightBoard flightBoard;
    private Map<String, Shipboard> playerToShip;

    private AdventureCard adventureCard;

    public void setCurrentPlayerNickname(String currentPlayerNickname) {
        this.currentPlayerNickname = currentPlayerNickname;
    }

    public void setFlightBoard(FlightBoard flightBoard) {
        this.flightBoard = flightBoard;
    }

    public void setPlayerToShip(Map<String, Shipboard> playerToShip) {
        this.playerToShip = playerToShip;
    }

    public String getCurrentPlayerNickname() {
        return currentPlayerNickname;
    }

    public List<String> getFlightBoardDescription() {
        return flightBoard.getDescription();
    }

    public void setMyNickname(String myNickname) {
        this.myNickname = myNickname;
    }

    public List<String> getMyShipBoardDescription() {
        return playerToShip.get(myNickname).getDescription();
    }

    public void setCurrentCard(int cardId) throws IOException {
        this.adventureCard = new AdventureCard(cardId);
    }

    public List<String> getCardDescription() {
        return adventureCard.getDescription();
    }

    public String getCardName() {
        return adventureCard.getCardName();
    }
}
