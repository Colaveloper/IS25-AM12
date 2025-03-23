package it.polimi.ingsw.galaxytruckers.adventureCards;

import it.polimi.ingsw.galaxytruckers.FlightBoard;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.AdventureCard;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.Projectile;
import it.polimi.ingsw.galaxytruckers.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.state.*;
import javafx.scene.image.Image;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CombatZoneCard extends AdventureCard {
    private final int flightDayLoss;
    private final int crewLoss;
    private final List<Projectile> projectilesLeft;

    // targeted ships for each menace
    private final List<ShipBoard> retreatingShips; // with the smallest crew
    private final List<ShipBoard> depopulatingShips; // with the weakest engines
    private final List<ShipBoard> targetedShips; // with the weakest artillery

    // utility variables
    private final List<ShipBoard> targetedShipLeftPerProjectile;
    private final List<ShipBoard> damagedShipsLeftToAddress;
    private Projectile currentProjectile;
    private int crewLossLeftPerShip;
    private boolean initialized1; // before all menaces
    private boolean initialized2; // before last menace

    public CombatZoneCard(Image image, Level level, FlightBoard flightBoard, int flightDayLoss, int crewLoss, List<Projectile> projectiles) {
        super(image, level, flightBoard);
        this.flightDayLoss = flightDayLoss;
        this.crewLoss = crewLoss;
        this.crewLossLeftPerShip = crewLoss;
        this.projectilesLeft = projectiles;

        retreatingShips = new ArrayList<>();
        depopulatingShips = new ArrayList<>();
        targetedShips = new ArrayList<>();
        targetedShipLeftPerProjectile = new ArrayList<>();
        damagedShipsLeftToAddress = new ArrayList<>();
    }

    @Override
    public GameState nextStep() {
        // Initialization and retreating smallest crewed ships
        if (!initialized1) {
            int minCrewSize = flightBoard.getOrderedShips()
                    .stream()
                    .mapToInt(ShipBoard::getCrewSize)
                    .min()
                    .orElseThrow(() -> new IllegalStateException("No players detected"));
            int minEnginePower = flightBoard.getOrderedShips()
                    .stream()
                    .mapToInt(ShipBoard::getEnginePower)
                    .min()
                    .orElseThrow(() -> new IllegalStateException("No players detected"));
            int minFirePower = flightBoard.getOrderedShips()
                    .stream()
                    .mapToInt(ShipBoard::getFirePower)
                    .min()
                    .orElseThrow(() -> new IllegalStateException("No players detected"));

            retreatingShips.addAll(flightBoard.getOrderedShips().stream()
                    .filter(s -> s.getCrewSize() == minCrewSize)
                    .toList());
            depopulatingShips.addAll(flightBoard.getOrderedShips().stream()
                    .filter(s -> s.getCrewSize() == minEnginePower)
                    .toList());
            targetedShips.addAll(flightBoard.getOrderedShips().stream()
                    .filter(s -> s.getCrewSize() == minFirePower)
                    .toList());

            for (ShipBoard s : retreatingShips) {
                flightBoard.displaceShip(s, -flightDayLoss); // TODO: verify call order
            }
            initialized1 = true;
            currentShipBoard = depopulatingShips.removeFirst();  // TODO: verify call order
        }

        // depopulating ships with the weakest engines
        if (!depopulatingShips.isEmpty()) { // still ships to query
            if (crewLossLeftPerShip>0) { // still crew to lose
                crewLossLeftPerShip--;
                return new ChooseCrewToLoseState(currentShipBoard);
            } else { // query next ship
                currentShipBoard = depopulatingShips.removeFirst();
                crewLossLeftPerShip = crewLoss;
                return new ChooseCrewToLoseState(currentShipBoard);
            }
        }

        // firing at ships with the weakest artillery
        if (!initialized2) {
            currentShipBoard = targetedShips.removeFirst();
            currentProjectile = projectilesLeft.removeFirst();
            initialized2 = true;
        }
        if (!projectilesLeft.isEmpty()) { // still projectiles to throw
            if (!targetedShipLeftPerProjectile.isEmpty()) { // still ships to activate shields
                return new ActivateState(
                        currentProjectile.getActivatablePoints(targetedShipLeftPerProjectile.removeFirst()),
                        currentShipBoard); // Let the player activate shields
            } else {
                if (damagedShipsLeftToAddress.isEmpty()) {
                    damagedShipsLeftToAddress.addAll(targetedShips.stream().filter(s -> currentProjectile.fireAt(s)).toList()); // fire!
                }
                if (damagedShipsLeftToAddress.isEmpty()) { // no more damaged ships to process
                    currentProjectile = projectilesLeft.removeFirst();
                    targetedShipLeftPerProjectile.addAll(targetedShips);
                    return nextStep(); // to the next projectile or draw new card
                } else { // still damaged ships to process
                    currentShipBoard = damagedShipsLeftToAddress.removeFirst();
                    List<Set<Point>> shipPieces = currentShipBoard.getConnectedSets();
                    if (shipPieces.size() > 1) { // the ship broke
                        return new ChooseShipPieceState(currentShipBoard.getConnectedSets(), currentShipBoard);
                        // Let the player choose what part of the ship to keep
                    } else {
                        return nextStep(); // to the next damaged ship or projectile or draw new card
                    }
                }
            }
        }
        return new DrawCardState();
    }
}
