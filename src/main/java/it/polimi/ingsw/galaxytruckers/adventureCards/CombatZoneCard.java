package it.polimi.ingsw.galaxytruckers.adventureCards;

import it.polimi.ingsw.galaxytruckers.FlightBoard;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.AdventureCard;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.Projectile;
import it.polimi.ingsw.galaxytruckers.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.state.*;
import javafx.scene.image.Image;

import java.awt.*;
import java.util.*;
import java.util.List;

public class CombatZoneCard extends AdventureCard {
    // Card data
    private final int flightDayLoss;
    private int crewLossLeft;
    private final List<Projectile> projectiles;

    // Lowest stats
    private Integer minCrewSize;
    private Integer minEnginePower;
    private Integer minFirePower;

    // Utility variables
    private Projectile currentProjectile;

    public CombatZoneCard(Image image, Level level, FlightBoard flightBoard, int flightDayLoss, int crewLoss, List<Projectile> projectiles) {
        super(image, level, flightBoard);
        this.flightDayLoss = flightDayLoss;
        this.crewLossLeft = crewLoss;
        this.projectiles = projectiles;
        this.currentPlayerIndex = 0;
    }

    @Override
    public GameState nextStep() {
        if (minCrewSize == null) { // the ship with the smallest crew loses flight-days
            minCrewSize = flightBoard.getOrderedShips()
                    .stream()
                    .mapToInt(ShipBoard::getCrewSize)
                    .min()
                    .orElseThrow(() -> new IllegalStateException("No players detected"));
            currentShipBoard = flightBoard.getOrderedShips()
                    .stream()
                    .filter(s -> s.getCrewSize() == minCrewSize)
                    .findFirst()
                    .orElseThrow();
            flightBoard.displaceShip(currentShipBoard, -flightDayLoss);
        }
        if (minEnginePower == null) { // establishing the ship with the weakest engines
            if (currentPlayerIndex < flightBoard.getOrderedShips().size()) { // engine activation
                currentShipBoard = flightBoard.getOrderedShips().get(currentPlayerIndex);
                currentPlayerIndex++;
                Set<Point> availablePositions = new HashSet<>(currentShipBoard.getEngines().keySet());
                availablePositions.retainAll(currentShipBoard.getActivatables().keySet());
                return new ActivateState(availablePositions, currentShipBoard);
            } else { // establishing the first ship with the weakest engines
                minEnginePower = flightBoard.getOrderedShips().stream()
                    .mapToInt(ShipBoard::getEnginePower)
                    .min()
                    .orElseThrow(() -> new IllegalStateException("No players detected"));
                currentShipBoard = flightBoard.getOrderedShips().stream()
                    .filter(s -> s.getCrewSize() == minEnginePower)
                    .findFirst()
                    .orElseThrow();
                currentPlayerIndex = 0;
            }
        }
        if (crewLossLeft>0) { // the ship with the weakest engines still has crew to lose
            crewLossLeft--; // TODO move this outside
            return new ChooseCrewToLoseState(currentShipBoard);
        }
        if (minFirePower == null) { // establishing the ship with the weakest cannons
            if (currentPlayerIndex < flightBoard.getOrderedShips().size()) { // engine activation
                currentShipBoard = flightBoard.getOrderedShips().get(currentPlayerIndex);
                currentPlayerIndex++;
                Set<Point> availablePositions = new HashSet<>(currentShipBoard.getCannons().keySet());
                availablePositions.retainAll(currentShipBoard.getActivatables().keySet());
                return new ActivateState(availablePositions, currentShipBoard);
            } else { // establishing the first ship with the weakest cannons
                minFirePower = flightBoard.getOrderedShips().stream()
                        .mapToInt(ShipBoard::getFirePower)
                        .min()
                        .orElseThrow(() -> new IllegalStateException("No players detected"));
                currentShipBoard = flightBoard.getOrderedShips().stream()
                        .filter(s -> s.getCrewSize() == minFirePower)
                        .findFirst()
                        .orElseThrow();
            }
        }
        // firing at the ship with the weakest cannons
        if (!projectiles.isEmpty()) { // still projectiles to throw
            if (currentProjectile == null) { // shields not yet activated
                currentProjectile = projectiles.removeFirst();
                return new ActivateState(currentProjectile.getActivatablePoints(currentShipBoard), currentShipBoard);
            } else { // fire!
                boolean hit = currentProjectile.fireAt(currentShipBoard);
                currentProjectile = null;
                if(hit && currentShipBoard.getConnectedSets().size() > 1) {
                    // a lost component broke the ship
                    return new ChooseShipPieceState(currentShipBoard.getConnectedSets(), currentShipBoard);
                } else {
                    return nextStep();
                }
            }
        }
        return new DrawCardState();
    }

    public ShipBoard getCurrentShipBoard() {
        return currentShipBoard;
    }

    public int getFlightDayLoss() {
        return flightDayLoss;
    }

    public int getCrewLossLeft() {
        return crewLossLeft;
    }

    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    public Integer getMinCrewSize() {
        return minCrewSize;
    }

    public Integer getMinEnginePower() {
        return minEnginePower;
    }

    public Integer getMinFirePower() {
        return minFirePower;
    }

    public Projectile getCurrentProjectile() {
        return currentProjectile;
    }
}
