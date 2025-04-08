package it.polimi.ingsw.galaxytruckers.view;

import javafx.scene.image.Image;

import java.util.Map;

public class ClientGameModel implements Physical {
    private String currentPlayerNickname;

    private FlightBoard flightBoard;

    private Map<String, Shipboard> playerToShip;

    public void setCurrentPlayerNickname(String currentPlayerNickname) {
        this.currentPlayerNickname = currentPlayerNickname;
    }

    public void setFlightBoard(FlightBoard flightBoard) {
        this.flightBoard = flightBoard;
    }

    public void setPlayerToShip(Map<String, Shipboard> playerToShip) {
        this.playerToShip = playerToShip;
    }

    @Override
    public Image getImage() {
        return null;
    }

    @Override
//    public Character[][] getDescription() {
        public String getDescription() {
        StringBuilder result =
                new StringBuilder("Current player is: " + currentPlayerNickname + "\n"
                        + flightBoard.getDescription());

        for(Map.Entry<String, Shipboard> e : playerToShip.entrySet()) {
            result.append("\n")
                    .append(e.getKey())
                    .append("\n")
                    .append(e.getValue().getDescription());
        }

        return result.toString();
    }
}
