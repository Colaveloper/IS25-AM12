package it.polimi.ingsw.galaxytruckers;//TEMPORARY CLASS, TESTING ONLY

import it.polimi.ingsw.galaxytruckers.adventureCards.*;
import it.polimi.ingsw.galaxytruckers.adventureCards.AdventureCardDeprecated;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.ProjectileDeprecated;
import it.polimi.ingsw.galaxytruckers.enumTypes.GoodsType;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

enum CardType{
    ABANDONED_SHIP,
    ABANDONED_STATION,
    COMBAT_ZONE,
    EPIDEMIC,
    METEOR_SWARM,
    OPEN_SPACE,
    PIRATES,
    PLANETS,
    SABOTAGE,
    SLAVERS,
    SMUGGLERS,
    STARDUST
}

public class TempDeck extends Deck{

    FlightBoard flightBoard;
    public TempDeck(FlightBoard flightBoard){
        this.flightBoard = flightBoard;
        //default constructor
    }

    @Override
    public AdventureCardDeprecated drawCard() {
        CardType[] values = CardType.values(); //get all enum values
        Random rand = new Random();
        CardType randomCard = values[rand.nextInt(values.length)]; //pick random one
        AdventureCardDeprecated returnCard;

        List<Integer> projectileDirections = new ArrayList<>();
        List<ProjectileDeprecated> projectilesType = new ArrayList<>();
        List<GoodsType> goodsList = new ArrayList<>();
        
        //TODO: generate values for cards from JSON
        randomCard = CardType.PIRATES;
        switch (randomCard){
            case ABANDONED_SHIP:
                returnCard = new AbandonedShipCard(flightBoard,4,3,1);
                break;
            case ABANDONED_STATION:
                goodsList.clear();
                goodsList.add(GoodsType.YELLOW);
                goodsList.add(GoodsType.GREEN);
                returnCard = new AbandonedStationCard(flightBoard, goodsList, 5, 1);
                break;
            case COMBAT_ZONE:
                //0 from below, 1 left, 2 above, 3 right
                projectileDirections.clear();
                projectileDirections.add(0);
                projectileDirections.add(0);

                projectilesType.clear();
                projectilesType.add(ProjectileDeprecated.LIGHT_FIRE);
                projectilesType.add(ProjectileDeprecated.HEAVY_FIRE);

                returnCard = new CombatZoneCard(flightBoard, 3,2,projectileDirections,projectilesType);
                break;
            case EPIDEMIC:
                returnCard = new EpidemicCard(flightBoard);
                break;
            case METEOR_SWARM:
                //0 from below, 1 left, 2 above, 3 right
                projectileDirections.clear();
                projectileDirections.add(1);
                projectileDirections.add(2);
                projectileDirections.add(3);

                projectilesType.clear();
                projectilesType.add(ProjectileDeprecated.SMALL_METEOR);
                projectilesType.add(ProjectileDeprecated.LARGE_METEOR);
                projectilesType.add(ProjectileDeprecated.SMALL_METEOR);

                returnCard = new MeteorSwarmCard(flightBoard, projectileDirections,projectilesType);
                break;
            case OPEN_SPACE:
                returnCard = new OpenSpaceCard(flightBoard, 4);
                break;
            case PIRATES:
                projectileDirections.clear();
                projectileDirections.add(2);
                projectileDirections.add(2);
                projectileDirections.add(2);
                projectileDirections.add(1);
                projectileDirections.add(3);

                projectilesType.clear();
                projectilesType.add(ProjectileDeprecated.HEAVY_FIRE);
                projectilesType.add(ProjectileDeprecated.LIGHT_FIRE);
                projectilesType.add(ProjectileDeprecated.HEAVY_FIRE);
                projectilesType.add(ProjectileDeprecated.LIGHT_FIRE);
                projectilesType.add(ProjectileDeprecated.LIGHT_FIRE);

                returnCard = new PiratesCardDeprecated(flightBoard, 10,2,12, projectileDirections,projectilesType);
                break;
            case PLANETS:
                goodsList.clear();
                goodsList.add(GoodsType.RED);
                goodsList.add(GoodsType.RED);
                goodsList.add(GoodsType.RED);
                goodsList.add(GoodsType.BLUE);
                goodsList.add(GoodsType.BLUE);
                goodsList.add(GoodsType.YELLOW);

                returnCard = new PlanetsCard(flightBoard, 3,goodsList,2);
                break;
            case SABOTAGE:
                returnCard = new SabotageCard(flightBoard);
                break;
            case SLAVERS:
                returnCard = new SlaversCard(flightBoard, 7,8,4,2);
                break;
            case SMUGGLERS:
                goodsList.clear();
                goodsList.add(GoodsType.RED);
                goodsList.add(GoodsType.GREEN);
                goodsList.add(GoodsType.YELLOW);

                returnCard = new SmugglersCard(flightBoard, goodsList,1,4,2);
                break;
            case STARDUST:
                returnCard = new StarDustCard(flightBoard);
                break;
            default:
                //TODO: this is bad, please fix
                returnCard = null;
        }
        return returnCard;
    }

    @Override
    public void initMasterDeck() {

    }
    @Override
    public List<AdventureCardDeprecated> peekForecastDeck(int id) {
        return null;
    }
    @Override
    public AdventureCardDeprecated getCurrentCard() {
        return null;
    }


}