package it.polimi.ingsw.galaxytruckers.serverController.events;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;

import java.util.Map;
import java.util.stream.Collectors;

public record PlaceShipOnFlightBoardEvent(String playerName, Map<String, Integer> playerToPlace) implements Event {
    public static PlaceShipOnFlightBoardEvent from(ShipBoard shipBoard, FlightBoard flightBoard) {
        return new PlaceShipOnFlightBoardEvent(
                Player.getPlayer(shipBoard).getNickname(),
                flightBoard.getShipToPlace().entrySet().stream()
                        .collect(Collectors.toMap(e -> Player.getPlayer(e.getKey()).getNickname(), Map.Entry::getValue))
        );
    }

    @Override
    public void accept(EventVisitor visitor) {
        visitor.visit(this);
    }
}
