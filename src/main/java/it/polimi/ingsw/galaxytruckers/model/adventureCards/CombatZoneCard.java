package it.polimi.ingsw.galaxytruckers.model.adventureCards;

import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.*;
import com.google.common.annotations.VisibleForTesting;
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
import java.util.function.Supplier;

public class CombatZoneCard extends AdventureCard {
    // Card data
    private int flightDayLoss;
    private int crewLossLeft;
    private int goodsLossLeft;
    private int currentTask;
    private List<Projectile> projectiles;
    private List<Supplier<GameState>> actions; // evaluations and punishments

    // Lowest stats
    private Integer minCrewSize;
    private Integer minEnginePower;
    private Integer minFirePower;

    // Utility variables
    private Projectile currentProjectile;

    public CombatZoneCard(Image image, Level level, FlightBoard flightBoard, int flightDayLoss, int crewLoss, int goodsLoss, List<Projectile> projectiles, List<String> actions) {
        super(image, level, flightBoard);
        this.flightDayLoss = flightDayLoss;
        this.crewLossLeft = crewLoss;
        this.goodsLossLeft = goodsLoss;
        this.projectiles = new LinkedList<>(projectiles);
        this.currentPlayerIndex = 0;
        this.currentTask = 0;
        this.actions = actionParser(actions);
        this.actions.add(drawCard);
    }

    @Override
    public GameState nextStep() {
        return (GameState) actions.get(currentTask).get();
    }

    public Supplier<GameState> setCurrentShipToLeastCrewed = () -> {
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
        currentTask++;
        currentPlayerIndex = 0;
        return nextStep();
    };

    public Supplier<GameState> setCurrentShipToWeakestEngine = () -> {
        if (currentPlayerIndex < flightBoard.getOrderedShips().size()) { // engine activation
                currentShipBoard = flightBoard.getOrderedShips().get(currentPlayerIndex++);
                Set<Point> availablePositions = new HashSet<>(currentShipBoard.getEngines().keySet());
                availablePositions.retainAll(currentShipBoard.getActivatables().keySet());
                return new ActivateState(availablePositions, currentShipBoard);
        }

        // establishing the first ship with the weakest engines
        minEnginePower = flightBoard.getOrderedShips().stream()
            .mapToInt(ShipBoard::getEnginePower)
            .min()
            .orElseThrow(() -> new IllegalStateException("No players detected"));
        currentShipBoard = flightBoard.getOrderedShips().stream()
            .filter(s -> s.getEnginePower() == minEnginePower)
            .findFirst()
            .orElseThrow(() -> new IllegalStateException("No players detected"));

        currentTask++;
        currentPlayerIndex = 0;
        return nextStep();
    };

    public Supplier<GameState> setCurrentShipToWeakestCannons = () -> {
        if (currentPlayerIndex < flightBoard.getOrderedShips().size()) { // engine activation
            currentShipBoard = flightBoard.getOrderedShips().get(currentPlayerIndex);
            currentPlayerIndex++;
            Set<Point> availablePositions = new HashSet<>(currentShipBoard.getCannons().keySet());
            availablePositions.retainAll(currentShipBoard.getActivatables().keySet());
            return new ActivateState(availablePositions, currentShipBoard);
        }

        // establishing the first ship with the weakest cannons
        minFirePower = flightBoard.getOrderedShips().stream()
                .mapToInt(ShipBoard::getFirePower)
                .min()
                .orElseThrow(() -> new IllegalStateException("No players detected"));
        currentShipBoard = flightBoard.getOrderedShips().stream()
                .filter(s -> s.getFirePower() == minFirePower)
                .findFirst()
                .orElseThrow();

        currentTask++;
        currentPlayerIndex = 0;
        return nextStep();
    };

    public Supplier<GameState> currentShipLosesFlightDays = () -> {
        flightBoard.displaceShip(currentShipBoard, -flightDayLoss);
        currentTask++;
        currentPlayerIndex = 0;
        return nextStep();
    };

    public Supplier<GameState> currentShipLosesCrew = () -> {
        if (crewLossLeft>0) { // current ship still has crew to lose
            return new ChooseCrewToLoseState(currentShipBoard);
        }
        currentTask++;
        currentPlayerIndex = 0;
        return nextStep();
    };

    public Supplier<GameState> currentShipLosesGoods = () -> {
        if (goodsLossLeft>0) { // current ship still has crew to lose
            return new ChooseGoodToLoseState(currentShipBoard);
        }
        currentTask++;
        currentPlayerIndex = 0;
        return nextStep();
    };

    public Supplier<GameState> currentShipGetsShot = () -> {
        if (!projectiles.isEmpty()) { // still projectiles to throw
            if (currentProjectile == null) { // shields not yet activated
                currentProjectile = projectiles.getFirst();
                return new ActivateState(currentProjectile.getActivatablePoints(currentShipBoard), currentShipBoard);
            } else { // fire!
                boolean hit = currentProjectile.fireAt(currentShipBoard);
                List<Set<Point>> shipPieces = currentShipBoard.getConnectedSets();
                projectiles.removeFirst();
                currentProjectile = null;
                if(hit && shipPieces.size() > 1) {
                    // a lost component broke the ship
                    return new ChooseShipPieceState(shipPieces, currentShipBoard);
                } else {
                    return nextStep();
                }
            }
        }
        currentTask++;
        currentPlayerIndex = 0;
        return nextStep();
    };

    public Supplier<GameState> drawCard = DrawCardState::new;

    public void loseCrew() {
        crewLossLeft--;
    }

    public void loseGoods() {
        goodsLossLeft--;
    }

    @VisibleForTesting
    protected List<Supplier<GameState>> actionParser(List<String> actionStrings) {
        List<Supplier<GameState>> actions = new ArrayList<>();

        for (String action : actionStrings) {
            actions.add(switch (action) {
                case "min crew" ->
                    setCurrentShipToLeastCrewed;
                case "min cannons" ->
                    setCurrentShipToWeakestCannons;
                case "min engine" ->
                    setCurrentShipToWeakestEngine;
                case "loses flight days" ->
                    currentShipLosesFlightDays;
                case "loses crew" ->
                    currentShipLosesCrew;
                case "gets shot" ->
                    currentShipGetsShot;
                case "loses goods" ->
                    currentShipLosesGoods;
                default -> throw new IllegalArgumentException(
                        "Attempting to parse unknown combat action: " + action
                );
            });
        }
        return actions;
    }

    @VisibleForTesting
    protected ShipBoard getCurrentShipBoard() {
        return currentShipBoard;
    }

    @VisibleForTesting
    protected int getCrewLossLeft() {
        return crewLossLeft;
    }

    @VisibleForTesting
    protected int getGoodLossLeft() {
        return goodsLossLeft;
    }

    @VisibleForTesting
    protected List<Projectile> getProjectiles() {
        return projectiles;
    }

    @VisibleForTesting
    protected Integer getMinCrewSize() {
        return minCrewSize;
    }

    @VisibleForTesting
    protected Integer getMinEnginePower() {
        return minEnginePower;
    }

    @VisibleForTesting
    protected Integer getMinFirePower() {
        return minFirePower;
    }

    @VisibleForTesting
    protected Projectile getCurrentProjectile() {
        return currentProjectile;
    }
}
