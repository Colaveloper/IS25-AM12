//package it.polimi.ingsw.galaxytruckers.model.adventureCards;
//
//import it.polimi.ingsw.galaxytruckers.model.FlightBoard;
//import it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles.BigMeteor;
//import it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles.Projectile;
//import it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles.SmallMeteor;
//import it.polimi.ingsw.galaxytruckers.model.enumTypes.Colors;
//import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
//import it.polimi.ingsw.galaxytruckers.model.state.*;
//import it.polimi.ingsw.galaxytruckers.model.shipBuilding.SecondShipBoard;
//import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
//import javafx.scene.image.Image;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Nested;
//import org.junit.jupiter.api.Test;
//
//import java.awt.*;
//import java.util.*;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class CombatZoneTest {
//    CombatZoneCard combatZoneCard;
//    Map<ShipBoard, Integer> shipPlaces;
//    FlightBoard flightBoard;
//    ShipBoard ship1;
//    ShipBoard ship2;
//    ShipBoard testShip;
//    List<ShipBoard> ships;
//    int testDisplacement;
//    int flightDaysLoss;
//    int crewLoss;
//    int goodLoss;
//    List<Projectile> projectiles;
//    int crewSize1;
//    int crewSize2;
//    int firePower1;
//    int firePower2;
//    int enginePower1;
//    int enginePower2;
//    int position1;
//    int position2;
//    Projectile damagingProjectile;
//    boolean doesBreak;
//    List<String> actions;
//
//    @BeforeEach
//    void setUp() {
//
//        shipPlaces = new HashMap<>();
//
//        // To have full coverage, because of lazy streams
//        // the ship on the back needs to always have the worst stats!
//        crewSize1 = 2;
//        crewSize2 = 1;
//        firePower1 = 5;
//        firePower2 = 3;
//        enginePower1 = 7;
//        enginePower2 = 4;
//        position1 = 9;
//        position2 = 10;
//        doesBreak = true;
//
//        ship1 = new SecondShipBoard(Colors.RED) {
//            @Override
//            public int getCrewSize() {
//                return crewSize1;
//            }
//            @Override
//            public int getFirePower() {
//                return firePower1;
//            }
//            @Override
//            public int getEnginePower() {
//                return enginePower1;
//            }
//            @Override
//            public List<Set<Point>> getConnectedSets() {
//                return List.of(Set.of());
//            }
//        };
//        ship2 = new SecondShipBoard(Colors.BLUE) {
//            @Override
//            public int getCrewSize() {
//                return crewSize2;
//            }
//            @Override
//            public int getFirePower() {
//                return firePower2;
//            }
//            @Override
//            public int getEnginePower() {
//                return enginePower2;
//            }
//            @Override
//            public List<Set<Point>> getConnectedSets() {
//                if (doesBreak) {
//                    doesBreak = false;
//                    return List.of(Set.of(), Set.of());
//                }
//                return List.of(Set.of());}
//        };
//        ships = new ArrayList<>(List.of(ship1, ship2));
//        shipPlaces.put(ship1, position1);
//        shipPlaces.put(ship2, position2);
//
//        flightBoard = new FlightBoard(null) {
//
//            @Override
//            public Map<ShipBoard, Integer> getShipToPlace() {
//                return shipPlaces;
//            }
//
//            @Override
//            public List<ShipBoard> getOrderedShips() {
//                return new ArrayList<>(List.of(ship1, ship2));
//            }
//
//            @Override
//            public void displaceShip(ShipBoard shipBoard, int displacement) {
//                testShip = shipBoard;
//                testDisplacement = displacement;
//            }
//
//            @Override
//            public boolean placeShipOnFlightBoard(ShipBoard shipBoard, int startingPosition) {
//                return true;
//            }
//
//            @Override
//            protected int getLoopLength() {
//                return 0;
//            }
//        };
//
//        crewLoss = 0;
//        flightDaysLoss = 0;
//        goodLoss = 0;
//        projectiles = new ArrayList<>();
//    }
//
//    @Test
//    void actionParserThrowsExceptionForUnknownAction() {
//        actions = List.of("slurping ramen");
//        assertThrows(IllegalArgumentException.class, () ->
//                new CombatZoneCard(null,
//                flightDaysLoss, crewLoss, goodLoss, projectiles, actions));
//    }
//
//    @Nested
//    class firstCardInstance {
//        @BeforeEach
//        void setUp() {
//            flightDaysLoss = 2;
//            crewLoss = 2;
//            projectiles = new ArrayList<>(List.of(
//                    new SmallMeteor(()->6,0),
//                    new BigMeteor(()->1, 3),
//                    new BigMeteor(()->5, 2)
//            ));
//            damagingProjectile = new SmallMeteor(()->1,0) {
//                @Override
//                public boolean fireAt(ShipBoard shipBoard) {
//                    return true;
//                }
//            };
//            actions = Arrays.asList(
//                    "min crew",
//                    "loses flight days",
//                    "min engine",
//                    "loses crew",
//                    "min cannons",
//                    "gets shot"
//            );
//            combatZoneCard = new CombatZoneCard(null,
//                    flightDaysLoss, crewLoss, goodLoss, projectiles, actions);
//            combatZoneCard.initialize(flightBoard);
//        }
//
//
//        @Test
//        void shipWithSmallestCrewLosesFlightDaysThenActivateState() {
//            GameState testState = combatZoneCard.nextStep();
//
//            assertEquals(Math.min(crewSize1, crewSize2), combatZoneCard.getMinCrewSize());
//            assertEquals(testShip, ship2);
//            assertEquals(testDisplacement, -flightDaysLoss);
//            assertInstanceOf(ActivateState.class, testState);
//
//            // rest unchanged
//            assertEquals(projectiles, combatZoneCard.getProjectiles());
//            assertEquals(crewSize1, ship1.getCrewSize());
//            assertEquals(crewSize2, ship2.getCrewSize());
//            assertEquals(shipPlaces, flightBoard.getShipToPlace());
//        }
//
//        @Test
//        void nextStepReturnsActivateStateForEngines() {
//            for (ShipBoard ship : ships) {
//                GameState testState = combatZoneCard.nextStep();
//                assertInstanceOf(ActivateState.class, testState);
//                assertEquals(ship, combatZoneCard.getCurrentShipBoard());
//
//                // rest unchanged
//                assertEquals(projectiles, combatZoneCard.getProjectiles());
//                assertEquals(crewSize1, ship1.getCrewSize());
//                assertEquals(crewSize2, ship2.getCrewSize());
//                assertEquals(shipPlaces, flightBoard.getShipToPlace());
//            }
//            GameState testState = combatZoneCard.nextStep();
//        }
//
//        @Test
//        void nextStepReturnsLoseCrewStateAfterCannonActivation() {
//            for (ShipBoard ship : ships) {
//                combatZoneCard.nextStep();
//            }
//            assertInstanceOf(ChooseCrewToLoseState.class, combatZoneCard.nextStep());
//        }
//
//        @Test
//        void loseCrewDiminishesCrewLossLeft() {
//            for (int i = crewLoss; i > 0; i-- ) {
//                combatZoneCard.loseCrew();
//                crewLoss--;
//                assertEquals(crewLoss, combatZoneCard.getCrewLossLeft());
//            }
//        }
//
//        @Test
//        void minEnginePowerGetsCalculated() {
//            for (ShipBoard ship : ships) { // none activated cannons
//                combatZoneCard.nextStep();
//            }
//            combatZoneCard.nextStep();
//            assertEquals(Math.min(enginePower1, enginePower2), combatZoneCard.getMinEnginePower());
//        }
//
//        @Test
//        void nextStepReturnsActivateStateForCannons() {
//            for (ShipBoard ship : ships) { // engine activation
//                combatZoneCard.nextStep();
//            }
//            combatZoneCard.nextStep();
//            for (int i = crewLoss; i > 0; i-- ) {
//                combatZoneCard.loseCrew();
//            }
//            for (ShipBoard ship : ships) {
//                GameState testState = combatZoneCard.nextStep();
//                assertInstanceOf(ActivateState.class, testState);
//                assertEquals(ship, combatZoneCard.getCurrentShipBoard());
//            }
//        }
//
//        @Test
//        void minFirePowerGetsCalculated() {
//            for (ShipBoard ship : ships) { // engine activation
//                combatZoneCard.nextStep();
//            }
//            combatZoneCard.nextStep();
//            for (int i = crewLoss; i > 0; i-- ) { // losing crew
//                combatZoneCard.loseCrew();
//            }
//            for (ShipBoard ship : ships) { // cannon activation
//                combatZoneCard.nextStep();
//            }
//            combatZoneCard.nextStep();
//            assertEquals(Math.min(firePower1, firePower2), combatZoneCard.getMinFirePower());
//            assertEquals(Math.min(firePower1, firePower2), combatZoneCard.getCurrentShipBoard().getFirePower());
//        }
//
//        @Test
//        void nextStepReturnsActivateStateForEachProjectile() {
//            for (ShipBoard ship : ships) { // engine activation
//                combatZoneCard.nextStep();
//            }
//            combatZoneCard.nextStep();
//            for (int i = crewLoss; i > 0; i-- ) { // losing crew
//                combatZoneCard.loseCrew();
//            }
//            for (ShipBoard ship : ships) { // cannon activation
//                combatZoneCard.nextStep();
//            }
//            for (Projectile projectile : projectiles) {
//                GameState testState = combatZoneCard.nextStep();
//                assertInstanceOf(ActivateState.class, testState);
//                assertEquals(projectile, combatZoneCard.getCurrentProjectile());
//            }
//        }
//
//        @Test
//        void nextStepReturnsDrawCardStateAfterProjectiles() {
//            for (ShipBoard ship : ships) { // engine activation
//                combatZoneCard.nextStep();
//            }
//            combatZoneCard.nextStep();
//            for (int i = crewLoss; i > 0; i-- ) { // losing crew
//                combatZoneCard.loseCrew();
//            }
//            for (ShipBoard ship : ships) { // cannon activation
//                combatZoneCard.nextStep();
//            }
//            for (Projectile projectile : projectiles) {
//                combatZoneCard.nextStep();
//            }
//            GameState testState = combatZoneCard.nextStep();
//            assertInstanceOf(DrawCardState.class, testState);
//        }
//
////        @Test
////        void nextStepReturnsChooseShipPieceStateIfBroken() {
////
////            projectiles = new ArrayList<>(List.of(damagingProjectile, damagingProjectile));
////            // only the first damaging projectile separates the ship in pieces
////
////            combatZoneCard = new CombatZoneCard(Level.SECOND, flightBoard, flightDaysLoss, crewLoss,goodLoss, projectiles, actions);
////
////            for (ShipBoard ship : ships) { // engine activation
////                combatZoneCard.nextStep();
////            }
////            combatZoneCard.nextStep();
////            for (int i = crewLoss; i > 0; i-- ) { // losing crew
////                combatZoneCard.loseCrew();
////            }
////            for (ShipBoard ship : ships) { // cannon activation
////                combatZoneCard.nextStep();
////            }
////            combatZoneCard.nextStep(); // shield activation
////            GameState testState = combatZoneCard.nextStep();
////            assertInstanceOf(ChooseShipPieceState.class, testState);
////            combatZoneCard.nextStep(); // useless activation
////            testState = combatZoneCard.nextStep(); // ship did not broke, all meteor finished
////            assertInstanceOf(DrawCardState.class, testState);
////        }
//    }
//
//    @Nested
//    class secondCardInstance {
//        // testing only the remaining methods
//        @BeforeEach
//        void setUp() {
//            goodLoss = 2;
//            actions = Arrays.asList(
//                    "min engine",
//                    "loses goods"
//            );
//            combatZoneCard = new CombatZoneCard(null,
//                    flightDaysLoss, crewLoss, goodLoss, projectiles, actions);
//            combatZoneCard.initialize(flightBoard);
//        }
//
//        @Test
//        void nextStepReturnsLoseGoodStateAfterEngineActivation() {
//            for (ShipBoard ship : ships) { // activating all engines
//                combatZoneCard.nextStep();
//            }
//            GameState testState = combatZoneCard.nextStep();
//            assertInstanceOf(ChooseGoodToLoseState.class, testState);
//        }
//
//        @Test
//        void loseCrewDiminishesGoodLossLeft() {
//            for (int i = goodLoss; i > 0; i-- ) {
//                combatZoneCard.loseGoods();
//                goodLoss--;
//                assertEquals(goodLoss, combatZoneCard.getGoodLossLeft());
//            }
//        }
//
//        @Test
//        void nextStepReturnsDrawCardStateAfterLosingCrew() {
//            for (ShipBoard ship : ships) { // activating all engines
//                combatZoneCard.nextStep();
//            }
//            for (int i = goodLoss; i > 0; i-- ) { // losing all goods
//                combatZoneCard.loseGoods();
//            }
//            GameState testState = combatZoneCard.nextStep();
//            assertInstanceOf(DrawCardState.class, testState);
//        }
//    }
//}
