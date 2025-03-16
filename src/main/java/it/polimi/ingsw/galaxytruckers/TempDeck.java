package it.polimi.ingsw.galaxytruckers;//TEMPORARY CLASS, TESTING ONLY

import it.polimi.ingsw.galaxytruckers.adventureCards.*;
import it.polimi.ingsw.galaxytruckers.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.Projectile;
import it.polimi.ingsw.galaxytruckers.enumTypes.GoodsType;

import javax.smartcardio.Card;
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

    public TempDeck(){
        //default constructor
    }

    @Override
    public AdventureCard drawCard() {
        CardType[] values = CardType.values(); //get all enum values
        Random rand = new Random();
        CardType randomCard = values[rand.nextInt(values.length)]; //pick random one
        AdventureCard returnCard;

        List<Integer> projectileDirections = new ArrayList<>();
        List<Projectile> projectilesType = new ArrayList<>();
        List<GoodsType> goodsList = new ArrayList<>();
        
        //TODO: generate values for cards from JSON
        switch (randomCard){
            case ABANDONED_SHIP:
                returnCard = new AbandonedShipCard(4,3,1);
                break;
            case ABANDONED_STATION:
                goodsList.clear();
                goodsList.add(GoodsType.YELLOW);
                goodsList.add(GoodsType.GREEN);
                returnCard = new AbandonedStationCard(goodsList, 5, 1);
                break;
            case COMBAT_ZONE:
                //0 from below, 1 left, 2 above, 3 right
                projectileDirections.clear();
                projectileDirections.add(0);
                projectileDirections.add(0);

                projectilesType.clear();
                projectilesType.add(Projectile.SMALL_CANNON);
                projectilesType.add(Projectile.BIG_CANNON);

                returnCard = new CombatZoneCard(3,2,projectileDirections,projectilesType);
                break;
            case EPIDEMIC:
                returnCard = new EpidemicCard();
                break;
            case METEOR_SWARM:
                //0 from below, 1 left, 2 above, 3 right
                projectileDirections.clear();
                projectileDirections.add(1);
                projectileDirections.add(2);
                projectileDirections.add(3);

                projectilesType.clear();
                projectilesType.add(Projectile.SMALL_METEOR);
                projectilesType.add(Projectile.BIG_METEOR);
                projectilesType.add(Projectile.SMALL_METEOR);

                returnCard = new MeteorSwarmCard(projectileDirections,projectilesType);
                break;
            case OPEN_SPACE:
                returnCard = new OpenSpaceCard(4);
                break;
            case PIRATES:
                projectileDirections.clear();
                projectileDirections.add(2);
                projectileDirections.add(2);
                projectileDirections.add(2);
                projectileDirections.add(1);
                projectileDirections.add(3);

                projectilesType.clear();
                projectilesType.add(Projectile.BIG_CANNON);
                projectilesType.add(Projectile.SMALL_CANNON);
                projectilesType.add(Projectile.BIG_CANNON);
                projectilesType.add(Projectile.SMALL_CANNON);
                projectilesType.add(Projectile.SMALL_CANNON);

                returnCard = new PiratesCard(10,projectileDirections,projectilesType,2,12);
                break;
            case PLANETS:
                goodsList.clear();
                goodsList.add(GoodsType.RED);
                goodsList.add(GoodsType.RED);
                goodsList.add(GoodsType.RED);
                goodsList.add(GoodsType.BLUE);
                goodsList.add(GoodsType.BLUE);
                goodsList.add(GoodsType.YELLOW);

                returnCard = new PlanetsCard(3,goodsList,2);
                break;
            case SABOTAGE:
                returnCard = new SabotageCard();
                break;
            case SLAVERS:
                returnCard = new SlaversCard(7,8,4,2);
                break;
            case SMUGGLERS:
                goodsList.clear();
                goodsList.add(GoodsType.RED);
                goodsList.add(GoodsType.GREEN);
                goodsList.add(GoodsType.YELLOW);

                returnCard = new SmugglersCard(goodsList,1,4,2);
                break;
            case STARDUST:
                returnCard = new StarDustCard();
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
    public List<AdventureCard> peekForecastDeck(int id) {
        return null;
    }
    @Override
    public AdventureCard getCurrentCard() {
        return null;
    }


}