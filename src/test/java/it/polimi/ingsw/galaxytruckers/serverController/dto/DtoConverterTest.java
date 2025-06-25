package it.polimi.ingsw.galaxytruckers.serverController.dto;

import it.polimi.ingsw.galaxytruckers.model.*;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles.BigFire;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.projectiles.Projectile;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.*;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.model.state.*;
import it.polimi.ingsw.galaxytruckers.serverController.dto.components.ActivatablePayload;
import it.polimi.ingsw.galaxytruckers.serverController.dto.components.BatteryPayload;
import it.polimi.ingsw.galaxytruckers.serverController.dto.components.CabinPayload;
import it.polimi.ingsw.galaxytruckers.serverController.dto.components.CargoPayload;
import it.polimi.ingsw.galaxytruckers.serverController.dto.states.*;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Lobby;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DtoConverterTest {
    Player p1;
    Player p2;
    ShipBoard s1;
    ShipBoard s2;
    GameState state;
    Game game;

    @BeforeEach
    void setUp() {
        game = new GameStub(Level.SECOND);
        p1 = Player.addPlayer("p1");
        s1 = game.addShipBoard(GameColor.RED);
        p1.setShipBoard(s1);
        p2 = Player.addPlayer("p2");
        s2 = game.addShipBoard(GameColor.RED);
        p2.setShipBoard(s2);
        game.getFlightBoard().placeShipOnFlightBoard(s1);
        game.getFlightBoard().placeShipOnFlightBoard(s2);
        s1.initializeCabin(new Point(7,7), CrewType.HUMAN);
        s2.initializeCabin(new Point(7,7), CrewType.HUMAN);
    }

    @AfterEach
    void tearDown() {
        Player.clear();
    }

    @Nested
    class GetComplexStateTests {
        @Test
        void getAddGoodsState() {
            Map<GoodsType, Integer> goods = Map.of(GoodsType.RED,1);
            state = new AddGoodsState(goods, s1);
            assertEquals(
                    new AddGoodsDTO(
                            p1.getNickname(),
                            goods
                    ),
                    DtoConverter.getComplexState(state)
            );
        }

        @Test
        void getChoosePlanetState() {
            state = new ChoosePlanetState((_,_) -> {}, 1);
            state.setGame(game);
            state.choosePlanet(s1, 0);
            ComplexStateDTO res = DtoConverter.getComplexState(state);
            assertInstanceOf(ComplexChoosePlanetDTO.class, res);
            ComplexChoosePlanetDTO complexChoosePlanetDTO = (ComplexChoosePlanetDTO) res;
            assertEquals(
                    new ChoosePlanetDTO(
                    p2.getNickname(),
                    1),
                    complexChoosePlanetDTO.baseData()
            );
            assertArrayEquals(
                    new String[] {p1.getNickname()},
                    complexChoosePlanetDTO.choices()
            );
        }

        @Test
        void getChooseShipPieceState() {
            state = new ChooseShipPieceState(List.of(), s1);
            assertEquals(
                    new ChooseShipPieceDTO(
                            p1.getNickname(),
                            List.of()
                    ),
                    DtoConverter.getComplexState(state)
            );
        }

        @Test
        void getDeclareEnginePowerState() {
            state = new DeclareEnginePowerState(s1);
            assertEquals(
                    new SimpleStateDTO(
                            p1.getNickname(),
                            StateDTOType.DECLARE_ENGINE_POWER
                    ),
                    DtoConverter.getComplexState(state)
            );
        }

        @Test
        void getDeclareFirePowerState() {
            state = new DeclareFirePowerState(s1);
            assertEquals(
                    new SimpleStateDTO(
                            p1.getNickname(),
                            StateDTOType.DECLARE_FIRE_POWER
                    ),
                    DtoConverter.getComplexState(state)
            );
        }

        @Test
        void getDrawCardState() {
            state = new DrawCardState();
            state.setGame(game);
            assertEquals(
                    new ComplexDrawCardDTO(
                            p1.getNickname(),
                            false
                    ),
                    DtoConverter.getComplexState(state)
            );
        }

        @Test
        void getGrabRewardState() {
            state = new GrabRewardState(s1, () -> {});
            assertEquals(
                    new SimpleStateDTO(
                            p1.getNickname(),
                            StateDTOType.GRAB_REWARD
                    ),
                    DtoConverter.getComplexState(state)
            );
        }

        @Test
        void getHandleProjectileState() {
            Projectile projectile = new BigFire(() -> 0, Direction.UP);
            state = new HandleProjectileState(s1, projectile);
            assertEquals(
                    new HandleProjectileDTO(
                            p1.getNickname(),
                            projectile.getProjectileType(),
                            projectile.getDiceRoll(),
                            projectile.getDirection(),
                            projectile.getActivatablePoints(s1)
                    ),
                    DtoConverter.getComplexState(state)
            );
        }

        @Test
        void getRemoveCrewState() {
            state = new RemoveCrewState(1, s1);
            assertEquals(
                    new RemoveCrewDTO(
                            p1.getNickname(),
                            1
                    ),
                    DtoConverter.getComplexState(state)
            );
        }

        @Test
        void getRemoveGoodsState() {
            state = new RemoveGoodsState(1, s1);
            assertEquals(
                    new RemoveGoodsDTO(
                            p1.getNickname(),
                            1
                    ),
                    DtoConverter.getComplexState(state)
            );
        }

        @Test
        void getSecondShipBuildingState() {
            state = new SecondShipBuildingState();
            state.setGame(game);
            SecondShipBuildingState secondShipBuildingState = (SecondShipBuildingState) state;
            secondShipBuildingState.getHourglass().stop();
            game.getFlightBoard().removeShips(Set.of(s1, s2));
            state.placeShipOnFlightBoard(s1);
            state.acquireForecast(s2, 0);
            assertEquals(
                    new SecondShipBuildingDTO(
                            new BuildingDataDTO(
                                    secondShipBuildingState.getComponentBank().getNumCovered(),
                                    secondShipBuildingState.getComponentBank().getUncoveredIds(),
                                    Set.of(p1.getNickname())
                            ),
                            new HourglassDTO(
                                    secondShipBuildingState.getHourglass().getMissingTime(),
                                    secondShipBuildingState.getHourglass().getFlipsLeft(),
                                    secondShipBuildingState.getHourglass().getIsRunning()
                            ),
                            Map.of(p2.getNickname(), 0)
                    ),
                    DtoConverter.getComplexState(state)
            );
        }

        @Test
        void getShipCorrectionState() {
            state = new ShipCorrectionState(true);
            s1.addWeldedComponent(new Component(Map.of(
                    Direction.UP, Connector.NONE,
                    Direction.LEFT, Connector.NONE,
                    Direction.DOWN, Connector.NONE,
                    Direction.RIGHT, Connector.NONE
            )), new Point(6,7), Direction.UP);
            s2.addWeldedComponent(new Component(), new Point(8,8), Direction.UP);
            state.setGame(game);
            assertEquals(
                    new ShipCorrectionDTO(
                            Set.of(p2.getNickname()),
                            Map.of(p2.getNickname(), ((ShipCorrectionState) state).getShipPieces(s2)),
                            ((ShipCorrectionState) state).getShouldDiscard()
                    ),
                    DtoConverter.getComplexState(state)
            );
        }

        @Test
        void getShipInitializationState() {
            state = new ShipInitializationState();
            s1.addWeldedComponent(new LifeSupport(CrewType.PURPLE), new Point(8,7), Direction.UP);
            s1.addWeldedComponent(new Cabin(), new Point(9,7), Direction.UP);
            state.setGame(game);
            assertEquals(
                    new ShipInitializationDTO(
                            Map.of(p1.getNickname(), Map.of(CrewType.PURPLE, Set.of(new Point(9,7))))
                    ),
                    DtoConverter.getComplexState(state)
            );
        }

        @Test
        void getTestShipBuildingState() {
            state = new TestShipBuildingState();
            state.setGame(game);
            TestShipBuildingState secondShipBuildingState = (TestShipBuildingState) state;
            game.getFlightBoard().removeShips(Set.of(s1, s2));
            state.placeShipOnFlightBoard(s1);
            assertEquals(
                    new TestShipBuildingDTO(
                            new BuildingDataDTO(
                                    secondShipBuildingState.getComponentBank().getNumCovered(),
                                    secondShipBuildingState.getComponentBank().getUncoveredIds(),
                                    Set.of(p1.getNickname())
                            )
                    ),
                    DtoConverter.getComplexState(state)
            );
        }

        @Test
        void getInvalidGameStateThrows() {
            assertThrows(RuntimeException.class,() ->  DtoConverter.getComplexState(new GameState() {
                @Override
                public void skip(ShipBoard shipBoard) {

                }
            }));
        }
    }

    @Nested
    class GetStateTests {
        @Test
        void getAddGoodsState() {
            Map<GoodsType, Integer> goods = Map.of(GoodsType.RED,1);
            state = new AddGoodsState(goods, s1);
            assertEquals(
                    new AddGoodsDTO(
                            p1.getNickname(),
                            goods
                    ),
                    DtoConverter.getState(state)
            );
        }

        @Test
        void getChoosePlanetState() {
            state = new ChoosePlanetState((_,_) -> {}, 1);
            state.setGame(game);
            assertEquals(
                    new ChoosePlanetDTO(
                            p1.getNickname(),
                            1
                    ),
                    DtoConverter.getState(state)
            );
        }

        @Test
        void getChooseShipPieceState() {
            state = new ChooseShipPieceState(List.of(), s1);
            assertEquals(
                    new ChooseShipPieceDTO(
                            p1.getNickname(),
                            List.of()
                    ),
                    DtoConverter.getState(state)
            );
        }

        @Test
        void getDeclareEnginePowerState() {
            state = new DeclareEnginePowerState(s1);
            assertEquals(
                    new SimpleStateDTO(
                            p1.getNickname(),
                            StateDTOType.DECLARE_ENGINE_POWER
                    ),
                    DtoConverter.getState(state)
            );
        }

        @Test
        void getDeclareFirePowerState() {
            state = new DeclareFirePowerState(s1);
            assertEquals(
                    new SimpleStateDTO(
                            p1.getNickname(),
                            StateDTOType.DECLARE_FIRE_POWER
                    ),
                    DtoConverter.getState(state)
            );
        }

        @Test
        void getDrawCardState() {
            state = new DrawCardState();
            state.setGame(game);
            assertEquals(
                    new SimpleStateDTO(
                            p1.getNickname(),
                            StateDTOType.DRAW_CARD
                    ),
                    DtoConverter.getState(state)
            );
        }

        @Test
        void getGrabRewardState() {
            state = new GrabRewardState(s1, () -> {});
            assertEquals(
                    new SimpleStateDTO(
                            p1.getNickname(),
                            StateDTOType.GRAB_REWARD
                    ),
                    DtoConverter.getState(state)
            );
        }

        @Test
        void getHandleProjectileState() {
            Projectile projectile = new BigFire(() -> 0, Direction.UP);
            state = new HandleProjectileState(s1, projectile);
            assertEquals(
                    new HandleProjectileDTO(
                            p1.getNickname(),
                            projectile.getProjectileType(),
                            projectile.getDiceRoll(),
                            projectile.getDirection(),
                            projectile.getActivatablePoints(s1)
                    ),
                    DtoConverter.getState(state)
            );
        }

        @Test
        void getRemoveCrewState() {
            state = new RemoveCrewState(1, s1);
            assertEquals(
                    new RemoveCrewDTO(
                            p1.getNickname(),
                            1
                    ),
                    DtoConverter.getState(state)
            );
        }

        @Test
        void getRemoveGoodsState() {
            state = new RemoveGoodsState(1, s1);
            assertEquals(
                    new RemoveGoodsDTO(
                            p1.getNickname(),
                            1
                    ),
                    DtoConverter.getState(state)
            );
        }

        @Test
        void getSecondShipBuildingState() {
            state = new SecondShipBuildingState();
            assertEquals(
                    new ShipBuildingDTO(),
                    DtoConverter.getState(state)
            );
        }

        @Test
        void getShipCorrectionState() {
            state = new ShipCorrectionState(true);
            s1.addWeldedComponent(new Component(Map.of(
                    Direction.UP, Connector.NONE,
                    Direction.LEFT, Connector.NONE,
                    Direction.DOWN, Connector.NONE,
                    Direction.RIGHT, Connector.NONE
            )), new Point(6,7), Direction.UP);
            s2.addWeldedComponent(new Component(), new Point(8,8), Direction.UP);
            state.setGame(game);
            assertEquals(
                    new ShipCorrectionDTO(
                            Set.of(p2.getNickname()),
                            Map.of(p2.getNickname(), ((ShipCorrectionState) state).getShipPieces(s2)),
                            ((ShipCorrectionState) state).getShouldDiscard()
                    ),
                    DtoConverter.getState(state)
            );
        }

        @Test
        void getShipInitializationState() {
            state = new ShipInitializationState();
            s1.addWeldedComponent(new LifeSupport(CrewType.PURPLE), new Point(8,7), Direction.UP);
            s1.addWeldedComponent(new Cabin(), new Point(9,7), Direction.UP);
            state.setGame(game);
            assertEquals(
                    new ShipInitializationDTO(
                            Map.of(p1.getNickname(), Map.of(CrewType.PURPLE, Set.of(new Point(9,7))))
                    ),
                    DtoConverter.getState(state)
            );
        }

        @Test
        void getTestShipBuildingState() {
            state = new TestShipBuildingState();
            assertEquals(
                    new ShipBuildingDTO(),
                    DtoConverter.getState(state)
            );
        }

        @Test
        void getInvalidGameStateThrows() {
            assertThrows(RuntimeException.class,() ->  DtoConverter.getState(new GameState() {
                @Override
                public void skip(ShipBoard shipBoard) {

                }
            }));
        }
    }

    @Nested
    class LobbyDtoTests {
        Lobby lobby;

        @BeforeEach
        void setUp() {
            lobby = new Lobby(new GameModel(), p1, Level.SECOND, 2, (_) -> {});
        }

        @Test
        void getActiveLobby() {
            assertEquals(
                    new ActiveLobbyDTO(
                            lobby.getId(),
                            lobby.getLevel(),
                            2,
                            List.of(p1.getNickname()),
                            p1.getNickname()
                    ),
                    DtoConverter.getActiveLobby(lobby));
        }

        @Test
        void getLobbyDetails() {
            assertEquals(
                    new LobbyDetailsDTO(
                            lobby.getId(),
                            Map.of(p1.getNickname(), lobby.getPlayerColors().get(p1)),
                            lobby.getLevel(),
                            2
                    ),
                    DtoConverter.getLobbyDetails(lobby)
            );
        }
    }

    @Test
    void getFlightBoard() {
        assertEquals(
                new FlightBoardDTO(
                        Map.of(p1.getNickname(), game.getFlightBoard().getShipToPlace().get(s1),
                                p2.getNickname(), game.getFlightBoard().getShipToPlace().get(s2))
                ),
                DtoConverter.getFlightBoard(game.getFlightBoard())
        );
    }

    @Test
    void getShipBoard() {
        assertEquals(
                new ShipBoardDTO(
                        Map.of(new Point(7,7), DtoConverter.getComponent(s1.getComponentMap().get(new Point(7,7)))),
                        -1,
                        null,
                        List.of(),
                        0,
                        0
                ),
                DtoConverter.getShipBoard(s1)
        );
    }

    @Test
    void getShipBoardDuringBuilding() {
        SecondShipBuildingState secondShipBuildingState = new SecondShipBuildingState();
        secondShipBuildingState.setGame(game);
        secondShipBuildingState.getHourglass().stop();
        secondShipBuildingState.requestRandComponent(s1);
        s1.stashComponent();
        secondShipBuildingState.requestRandComponent(s1);
        s1.placeComponent(new Point(7,6), Direction.UP);
        assertEquals(
                new ShipBoardDTO(
                        Map.of(
                                new Point(7,7), DtoConverter.getComponent(s1.getComponentMap().get(new Point(7,7)))
                        ),
                        s1.getLastComponent().orElseThrow().getId(),
                        new Point(7,6),
                        s1.getStashedComponents().stream().map(Component::getId).toList(),
                        0,
                        0
                ),
                DtoConverter.getShipBoard(s1)
        );
    }

    @Nested
    class ComponentDtoMethods {

        @Test
        void getActivatableComponent() {
            Activatable activatable = new DoubleCannon();
//            activatable.activate(Mockito.mock(ActivatableVisitor.class));
            ComponentDTO dto = DtoConverter.getComponent(activatable);
            assertEquals(activatable.getId(), dto.id());
            assertEquals(activatable.getOrientation(), dto.orientation());
            assertEquals(
                    new ActivatablePayload(
                            activatable.isActive()
                    ),
                    dto.payload()
            );
        }

        @Test
        void getBatteryComponent() {
            Battery battery = new Battery(3);
            ComponentDTO dto = DtoConverter.getComponent(battery);
            assertEquals(battery.getId(), dto.id());
            assertEquals(battery.getOrientation(), dto.orientation());
            assertEquals(
                    new BatteryPayload(3),
                    dto.payload()
            );
        }

        @Test
        void getCabinComponent() {
            Cabin cabin = new Cabin();
            cabin.initialize(CrewType.HUMAN);
            ComponentDTO dto = DtoConverter.getComponent(cabin);
            assertEquals(cabin.getId(), dto.id());
            assertEquals(cabin.getOrientation(), dto.orientation());
            assertEquals(
                    new CabinPayload(cabin.getCrewType(), cabin.getNumResidents()),
                    dto.payload()
            );
        }

        @Test
        void getCargoHoldComponent() {
            CargoHold cargoHold = new CargoHold(3);
            cargoHold.addGoods(GoodsType.YELLOW, 1);
            cargoHold.addGoods(GoodsType.BLUE, 1);
            ComponentDTO dto = DtoConverter.getComponent(cargoHold);
            assertEquals(cargoHold.getId(), dto.id());
            assertEquals(cargoHold.getOrientation(), dto.orientation());
            assertEquals(
                    new CargoPayload(
                            Map.of(GoodsType.YELLOW, 1, GoodsType.BLUE, 1)
                    ),
                    dto.payload()
            );
        }

        @Test
        void getOtherComponent() {
            Component component = new Component(Map.of(
                    Direction.UP, Connector.UNIVERSAL,
                    Direction.RIGHT, Connector.UNIVERSAL,
                    Direction.DOWN, Connector.UNIVERSAL,
                    Direction.LEFT, Connector.UNIVERSAL
            ));
            ComponentDTO dto = DtoConverter.getComponent(component);
            assertEquals(component.getId(), dto.id());
            assertEquals(component.getOrientation(), dto.orientation());
            assertNull(dto.payload());
        }

        @Test
        void checkAllTypes() {
            Component component = new Cannon();
            assertNull(DtoConverter.getComponent(component).payload());
            component = new Engine();
            assertNull(DtoConverter.getComponent(component).payload());
            component = new LifeSupport(CrewType.PURPLE);
            assertNull(DtoConverter.getComponent(component).payload());
        }
    }
}