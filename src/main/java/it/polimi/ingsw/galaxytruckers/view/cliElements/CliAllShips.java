package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.view.DescriptionUtils;
import it.polimi.ingsw.galaxytruckers.view.enums.Highlights;
import it.polimi.ingsw.galaxytruckers.view.model.Player;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import java.awt.*;
import java.util.*;
import java.util.List;

public class CliAllShips extends CliElement {
    private final List<CliShipBoard> cliShips;

    public CliAllShips(List<? extends CliShipBoard> ships) {
        //posso mettere una cliShipBoard qualsiasi invece di specificare se ha stash o no
        //togliendo anche la necessità di specificare quando uso CliAllShipsHandAndStash
        this.cliShips = new ArrayList<>(ships);
    }

    @Override
    protected List<String> getNewDescription() {
        List<String> description = new ArrayList<>();

        for (CliShipBoard cliShipBoard : cliShips) {
            description = DescriptionUtils.sideBySide(
                    description,
                    cliShipBoard.getDescription()
            );
        }

        description = DescriptionUtils.borderAndTitle(description, "all ships");

        return description;
    }
}
