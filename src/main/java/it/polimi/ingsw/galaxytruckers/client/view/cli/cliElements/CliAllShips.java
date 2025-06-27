package it.polimi.ingsw.galaxytruckers.client.view.cli.cliElements;

import it.polimi.ingsw.galaxytruckers.client.view.cli.DescriptionUtils;

import java.util.*;
import java.util.List;

/**
 * Represents a CLI element that displays all ships in the game
 */
public class CliAllShips extends CliElement {
    private final List<CliShipBoard> cliShips;

    /**
     * Creates a new CLI element that composes all {@link CliShipBoard} in the game,
     * whether they have a stash or not.
     *
     * @param ships A list of CliShipBoard objects representing the ships to be displayed
     */
    public CliAllShips(List<? extends CliShipBoard> ships) {
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
