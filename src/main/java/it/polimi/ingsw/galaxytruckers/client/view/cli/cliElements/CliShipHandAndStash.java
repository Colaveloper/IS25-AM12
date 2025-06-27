package it.polimi.ingsw.galaxytruckers.client.view.cli.cliElements;

import it.polimi.ingsw.galaxytruckers.client.view.cli.DescriptionUtils;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.client.model.shipBuilding.ShipBoard;

import java.util.ArrayList;
import java.util.List;

/**
 * CLI ship board including hand and stash, for level SECOND games
 */
public class CliShipHandAndStash extends CliShipBoard {

    private final CliHand cliHand;
    private final CliStash cliStash;

    /**
     * Creates a new CLI representation of a ship's hand and stash.
     * Initializes the hand with the last component of the ship board, or an empty hand if none exists.
     * Initializes the stash with the components currently stashed in the ship board.
     *
     * @param shipBoard The ship board containing the components
     * @param nickname  The nickname of the player owning this ship
     */
    public CliShipHandAndStash(ShipBoard shipBoard, String nickname) {
        super(shipBoard, nickname);

        if (shipBoard.getLastComponent() != null) {
            cliHand = new CliHand(shipBoard.getLastComponent());
        } else {
            cliHand = new CliHand();
        }
        cliStash = new CliStash(shipBoard.getStashedComponents());
    }

    /**
     * Returns the nickname of this CLI ship hand and stash.
     * This is used for display purposes in the CLI.
     *
     * @return The nickname of the player owning this ship
     */
    public String getNickname() {
        return "Cli Ship Hand and Stash";
    }

    /**
     * Handles the action of stashing a component.
     * @param component the component to be stashed
     */
    public void onStash(Component component) {
        cliStash.onStash(component);
        setDirty();
    }

    /**
     * Handles the action of grabbing a stashed component by its index.
     * @param index the index of the stashed component to grab
     */
    public void onGrabStashed(int index) {
        cliStash.onGrab(index);
        setDirty();
    }

    /**
     * Clears the hand of the ship.
     */
    public void clearHand() {
        cliHand.clearHand();
        setDirty();
    }

    /**
     * Sets the hand of the ship to a specific component.
     * @param component the component to set as the hand
     */
    public void setHand(Component component) {
        cliHand.setHand(component);
        setDirty();
    }

    @Override
    public List<String> getNewDescription(){
        List<String> description = new ArrayList<>();
        description.addAll(super.getNewDescription());
        description.addAll(
                DescriptionUtils.sideBySide(
                        cliHand.getDescription(),
                        cliStash.getDescription()
                )
        );
        return description;
    }
}
