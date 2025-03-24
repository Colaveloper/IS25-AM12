package it.polimi.ingsw.galaxytruckers.adventureCards;

import it.polimi.ingsw.galaxytruckers.FlightBoard;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.BigMeteor;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.Projectile;
import it.polimi.ingsw.galaxytruckers.adventureCards.utils.SmallMeteor;
import it.polimi.ingsw.galaxytruckers.enumTypes.Colors;
import it.polimi.ingsw.galaxytruckers.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.shipBuilding.ShipBoard;
import javafx.scene.image.Image;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CombatZoneTest extends AdventureCardTestInitializer{
    CombatZoneCard combatZoneCard;
    List<ShipBoard> ships;
    Map<ShipBoard, Integer> shipPlaces;
    ShipBoard testShip;
    int testDisplacement;
    int flightDaysLoss;
    int crewLoss;
    List<Projectile> projectiles;

    @BeforeEach
    void setUp() {
        super.setUp();
        flightDaysLoss = 2;
        crewLoss = 2;
        projectiles = new ArrayList<>(List.of(
                new SmallMeteor(()->6,0),
                new BigMeteor(()->5, 2)
        ));
        combatZoneCard = new CombatZoneCard(null, Level.SECOND, flightBoard, 2, 2, projectiles);
    }
}
