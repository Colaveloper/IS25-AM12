package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.view.DescriptionUtils;
import it.polimi.ingsw.galaxytruckers.view.enums.Highlights;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.*;
import java.util.List;

public class CliAllShips extends CliElement {
    private final Map<Player, CliShipBoard> playerToCliShip;

    public CliAllShips(Map<ShipBoard, Player> shipToPlayer) {
        playerToCliShip = new HashMap<>();
        shipToPlayer.forEach(( ship,player) -> {
                CliShipBoard cliShipBoard = new CliShipBoard(ship, player.getNickname());
            playerToCliShip.put(player, cliShipBoard);
        });
    }

    public void highlightPoints(Player player, Set<Point> points, Highlights color){
        playerToCliShip.get(player).highlightPoints(points, color);
    }

    @Override
    public List<String> getNewDescription() {
        List<String> description = new ArrayList<>();

        for (Map.Entry<Player, CliShipBoard> entry : playerToCliShip.entrySet()) {
            description = DescriptionUtils.sideBySide(
                    description,
                    new CliShipAndHand(
                            entry.getValue().shipBoard,
                            entry.getKey().getNickname()
                    ).getNewDescription()
            );
        }

        return description;
    }
}
