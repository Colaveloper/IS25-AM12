package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.view.DescriptionUtils;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CliAllShipsHandAndStash extends CliElement{
    private final List<CliShipHandAndStash> playerToCliShip;

    public CliAllShipsHandAndStash(Map<ShipBoard, Player> shipToPlayer) {
        playerToCliShip = new ArrayList<>();
        shipToPlayer.forEach(( ship,player) -> {
            CliShipHandAndStash cliShipBoardHandAndStash = new CliShipHandAndStash(ship, player.getNickname());
            cliShipBoardHandAndStash.addObserver(this);
            playerToCliShip.add(cliShipBoardHandAndStash);
        });
    }

    @Override
    protected List<String> getNewDescription() {
        List<String> description = new ArrayList<>();

        for (CliShipHandAndStash cliShipBoardHandAndStash : playerToCliShip) {
            description = DescriptionUtils.sideBySide(
                    description,
                    cliShipBoardHandAndStash.getDescription()
            );
        }

        return description;
    }
}
