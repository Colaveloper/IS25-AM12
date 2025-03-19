package it.polimi.ingsw.galaxytruckers.adventureCards;

import it.polimi.ingsw.galaxytruckers.adventureCards.utils.Projectile;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;

import java.util.ArrayList;
import java.util.List;

public class PiratesCard {
    private final int firePowerThreshold;
    // private final List<Integer> projectileDirections;
    // private final List<Projectile> projectileTypes;
    private final List<ShipBoard> defeatedPlayers;
    private final int creditPrize;
    private final int flightDaysLoss;
    private int defeatedPlayerIndex; // current defeated player

    public PiratesCard(int firePowerThreshold, int creditPrize, int flightDaysLoss) {
        this.firePowerThreshold = firePowerThreshold;
        this.creditPrize = creditPrize;
        this.flightDaysLoss = flightDaysLoss;
        this.defeatedPlayers = new ArrayList<>();
    }

    // void activate() {}

    void nextStep() {
        // crea stato di attivazione cannoni su
    }

    void choose(boolean choice) {

    }
}
