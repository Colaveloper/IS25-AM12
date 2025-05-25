package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.FourColors;
import it.polimi.ingsw.galaxytruckers.network.client.ConfigFactory;
import it.polimi.ingsw.galaxytruckers.view.DescriptionUtils;
import it.polimi.ingsw.galaxytruckers.view.cliElements.CliComponents.CliComponent;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;
import javafx.beans.property.ObjectProperty;

import java.io.IOException;
import java.util.*;

public class CliAllShips extends CliElement {
    private final Map<Player, CliShipBoard> playerToCliShip;

    public CliAllShips(Map<ShipBoard, Player> shipToPlayer) {
        playerToCliShip = new HashMap<>();
        shipToPlayer.forEach(( ship,player) -> {
                CliShipBoard cliShipBoard = new CliShipBoard(ship, player.getNickname());
            playerToCliShip.put(player, cliShipBoard);
        });
    }

    @Override
    public List<String> getDescription() {
        List<String> description = new ArrayList<>();

        for (Map.Entry<Player, CliShipBoard> entry : playerToCliShip.entrySet()) {
            description = DescriptionUtils.sideBySide(
                    description,
                    new CliShipAndHand(
                            entry.getValue().shipBoard,
                            entry.getKey().getNickname()
                    ).getDescription()
            );
        }

        return description;
    }
}
