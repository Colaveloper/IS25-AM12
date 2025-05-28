package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.view.DescriptionUtils;
import it.polimi.ingsw.galaxytruckers.view.enums.Highlights;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.*;
import java.util.List;

public class CliAllShips extends CliElement {
    private final List<CliShipBoard> playerToCliShip;

    public CliAllShips(Map<ShipBoard, Player> shipToPlayer) {
        playerToCliShip = new ArrayList<>();
        shipToPlayer.forEach(( ship,player) -> {
                CliShipBoard cliShipBoard = new CliShipBoard(ship, player.getNickname());
                cliShipBoard.addObserver(this);
            playerToCliShip.add(cliShipBoard);
        });
    }

    @Override
    protected List<String> getNewDescription() {
        List<String> description = new ArrayList<>();

        for (CliShipBoard cliShipBoard : playerToCliShip) {
            description = DescriptionUtils.sideBySide(
                    description,
                    cliShipBoard.getDescription()
            );
        }

        description = DescriptionUtils.borderAndTitle(description, "all ships");

        return description;
    }
}
