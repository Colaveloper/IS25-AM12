package it.polimi.ingsw.galaxytruckers.adventureCards;

import it.polimi.ingsw.galaxytruckers.FlightBoard;
import it.polimi.ingsw.galaxytruckers.GameModel;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.PlayerAction;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.Projectile;
import it.polimi.ingsw.galaxytruckers.enumTypes.GoodsType;

import java.util.ArrayList;
import java.util.List;

public class StarDustCard extends AdventureCard {
    public StarDustCard(FlightBoard flightBoard) {
        super(flightBoard);
        cardStates = new ArrayList<>();
        cardStates.add(PlayerAction.START_CARD);
        cardStates.add(PlayerAction.END_CARD);

        this.name = "[STAR DUST]";
    }

    @Override
    public PlayerAction nextStep() {
        if (step == 0) {
            flightBoard.displaceShip(currentShipBoard, currentShipBoard.getExposedConnectorsNumber());
            passCardToNextPlayer();
        }
        step++;
        return cardStates.get(step);
    }





    //UNUSED METHODS--------------------------------------------------------

    @Override
    public List<Boolean> getPlanets() {
        return null;
    }

    @Override
    public int getFirePower() {
        return 0;
    }

    @Override
    public int getCredits() {
        return 0;
    }

    @Override
    public List<GoodsType> getGoods() {
        return null;
    }

    @Override
    public List<Integer> getProjectileDirections() {
        return null;
    }

    @Override
    public List<Projectile> getProjectilesType() {
        return null;
    }

    @Override
    public int getSacrifice() {
        return 0;
    }

    @Override
    public int getFlightDaysLost() {
        return 0;
    }

    @Override
    public void landOnPlanet(int i) {

    }
}
