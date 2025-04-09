package it.polimi.ingsw.galaxytruckers.view;

import javafx.scene.image.Image;

import java.util.List;
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


    private StringBuilder currentPlayerSection() {
        return new StringBuilder(
                "Current player is: " + currentPlayerNickname + "\n" + flightBoard.getDescription()
        );
    }
    
    private StringBuilder flightboardSection() {
        StringBuilder result = new StringBuilder();
        for(Map.Entry<String, Shipboard> e : playerToShip.entrySet()) {
            result.append("\n")
                    .append(e.getKey())
                    .append("\n");

            for(String line : e.getValue().getDescription()) {
                result.append(line).append("\n");
            }
        }
        return result;
    }

    private List<String> buildDrawCardView() {
        //switch on current card
        StringBuilder result = new StringBuilder();

        result.append(currentPlayerSection());
        result.append(flightboardSection());
        
        return List.of(result.toString());
    }

    private List<String> buildShipBuildingView() {
        return null;
    }
    
    @Override
    public List<String> getDescription() {
        //switch on current state
        List<String> result = buildDrawCardView();

        return result;
    }
}
