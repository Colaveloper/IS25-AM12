package it.polimi.ingsw.galaxytruckers.model;

import it.polimi.ingsw.galaxytruckers.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.SurrenderCause;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.Component;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import it.polimi.ingsw.galaxytruckers.model.state.AdventureState;
import it.polimi.ingsw.galaxytruckers.model.state.DeclareFirePowerState;
import it.polimi.ingsw.galaxytruckers.serverController.dto.DtoConverter;
import it.polimi.ingsw.galaxytruckers.serverController.dto.GameSnapshot;
import it.polimi.ingsw.galaxytruckers.serverController.dto.LobbyDetailsDTO;
import it.polimi.ingsw.galaxytruckers.serverController.dto.states.SimpleStateDTO;
import it.polimi.ingsw.galaxytruckers.serverController.dto.states.StateDTOType;
import it.polimi.ingsw.galaxytruckers.serverController.events.EventListener;
import it.polimi.ingsw.galaxytruckers.serverController.events.types.*;
import it.polimi.ingsw.galaxytruckers.serverController.lobby.Player;
import it.polimi.ingsw.galaxytruckers.view.Direction;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

import static org.mockito.Mockito.*;

class GameEventListenerTest {
    interface ControllerEventListener extends EventListener<LobbyEvent> {
    }

    GameEventListener listener;
    Player p1;
    Player p2;
    ShipBoard s1;
    ShipBoard s2;

    LobbyDetailsDTO lobbyDetailsDTO = new LobbyDetailsDTO(UUID.randomUUID(), Map.of(), Level.SECOND, 2);
    Supplier<LobbyDetailsDTO> supplier = () -> lobbyDetailsDTO;
    ControllerEventListener controllerListener = mock(ControllerEventListener.class);

    Game game;
    Point point = new Point(7,7);
    GoodsType goodsType = GoodsType.RED;
    CrewType crewType = CrewType.HUMAN;
    AdventureCard adventureCard = new AdventureCard(game, Level.TEST, 0) {
        @Override
        public AdventureState getNextState() {
            return null;
        }
    };
    Component component = new Component();

    @BeforeEach
    void setUp() {
        listener = new GameEventListener(controllerListener,  supplier);
        p1 = Player.addPlayer("p1");
        p2 = Player.addPlayer("p2");
        game = new Game(Level.SECOND, 1, listener);
        s1 = game.addShipBoard(GameColor.RED);
        p1.setShipBoard(s1);
        s2 = game.addShipBoard(GameColor.BLUE);
        p2.setShipBoard(s2);
        game.setCurrentState(new DeclareFirePowerState(s1));
        clearInvocations(controllerListener);
    }

    @AfterEach
    void tearDown() {
        Player.clear();
    }

    @Test
    void requestSnapshotWithNoCard() {
        listener.requestSnapshot(game, s1);
        verify(controllerListener).notifyEvent(new GameSnapshotEvent(
                p1.getNickname(),
                lobbyDetailsDTO,
                new GameSnapshot(
                        DtoConverter.getFlightBoard(game.getFlightBoard()),
                        DtoConverter.getComplexState(game.getCurrentState()),
                        Map.of(p1.getNickname(),DtoConverter.getShipBoard(s1),
                                p2.getNickname(), DtoConverter.getShipBoard(s2)),
                        -1
                )
        ));
    }

    @Test
    void requestSnapshotWithCard() {
        game.getDeck().drawCard();
        listener.requestSnapshot(game, s1);
        verify(controllerListener).notifyEvent(new GameSnapshotEvent(
                p1.getNickname(),
                lobbyDetailsDTO,
                new GameSnapshot(
                        DtoConverter.getFlightBoard(game.getFlightBoard()),
                        DtoConverter.getComplexState(game.getCurrentState()),
                        Map.of(p1.getNickname(),DtoConverter.getShipBoard(s1),
                                p2.getNickname(), DtoConverter.getShipBoard(s2)),
                        game.getDeck().getCurrentCard().getId()
                )
        ));
    }

    @Test
    void notifyActivateComponentEvent() {
        listener.notifyActivateComponentEvent(s1,point,true);
        verify(controllerListener).notifyEvent(new ActivateComponentEvent(p1.getNickname(),point,true));
    }

    @Test
    void notifyFlightBoardUpdateEvent() {
        listener.notifyFlightBoardUpdateEvent(s1,0);
        verify(controllerListener).notifyEvent(new FlightBoardUpdateEvent(p1.getNickname(),0));
    }

    @Test
    void notifyFlipHourglassEvent() {
        listener.notifyFlipHourglassEvent(s1);
        verify(controllerListener).notifyEvent(new FlipHourglassEvent(p1.getNickname()));
    }

    @Test
    void notifyForecastDetailsEvent() {
        listener.notifyForecastDetailsEvent(s1, List.of(adventureCard));
        verify(controllerListener).notifyEvent(new ForecastDetailsEvent(p1.getNickname(),List.of(adventureCard.getId())));
    }

    @Test
    void notifyGameEndEvent() {
        listener.notifyGameEndEvent(Map.of(s1,0));
        verify(controllerListener).notifyEvent(new GameEndEvent(Map.of(p1.getNickname(),0)));
    }

    @Test
    void notifyGameStateUpdateEvent() {
        listener.notifyGameStateUpdateEvent(new DeclareFirePowerState(s1));
        verify(controllerListener).notifyEvent(new GameStateUpdateEvent(new SimpleStateDTO(p1.getNickname(), StateDTOType.DECLARE_FIRE_POWER)));
    }

    @Test
    void notifyGoodsUpdateEvent() {
        listener.notifyGoodsUpdateEvent(s1, point, goodsType, true);
        verify(controllerListener).notifyEvent(new GoodsUpdateEvent(p1.getNickname(), point, goodsType, true));
    }

    @Test
    void notifyGrabPlacedComponentEvent() {
        listener.notifyGrabPlacedComponentEvent(s1);
        verify(controllerListener).notifyEvent(new GrabPlacedComponentEvent(p1.getNickname()));
    }

    @Test
    void notifyGrabStashedComponentEvent() {
        listener.notifyGrabStashedComponentEvent(s1,0);
        verify(controllerListener).notifyEvent(new GrabStashedComponentEvent(p1.getNickname(),0));
    }

    @Test
    void notifyHourglassEndEvent() {
        listener.notifyHourglassEndEvent();
        verify(controllerListener).notifyEvent(new HourglassEndEvent());
    }

    @Test
    void notifyCabinInitializationEvent() {
        listener.notifyCabinInitializationEvent(s1, point, crewType);
        verify(controllerListener).notifyEvent(new InitializeCabinEvent(p1.getNickname(), point,crewType));
    }

    @Test
    void notifyLoseCrewEvent() {
        listener.notifyLoseCrewEvent(s1, point);
        verify(controllerListener).notifyEvent(new LoseCrewEvent(p1.getNickname(), point));
    }

    @Test
    void notifyNewCardEvent() {
        listener.notifyNewCardEvent(adventureCard);
        verify(controllerListener).notifyEvent(new NewCardEvent(adventureCard.getId()));
    }

    @Test
    void notifyPeekForecastEvent() {
        listener.notifyPeekForecastEvent(s1, 0);
        verify(controllerListener).notifyEvent(new PeekForecastEvent(p1.getNickname(), 0));
    }

    @Test
    void notifyPlaceComponentEvent() {
        listener.notifyPlaceComponentEvent(s1, Direction.UP, point);
        verify(controllerListener).notifyEvent(new PlaceComponentEvent(p1.getNickname(), point, Direction.UP));
    }

    @Test
    void notifyPlanetChoiceEvent() {
        listener.notifyPlanetChoiceEvent(s1, 0,s2);
        verify(controllerListener).notifyEvent(new PlanetChoiceEvent(p1.getNickname(), 0, p2.getNickname()));
    }

    @Test
    void notifyRejectComponentEvent() {
        listener.notifyRejectComponentEvent(s1);
        verify(controllerListener).notifyEvent(new RejectComponentEvent(p1.getNickname()));
    }

    @Test
    void notifyReleaseForecastEvent() {
        listener.notifyReleaseForecastEvent(s1,0);
        verify(controllerListener).notifyEvent(new ReleaseForecastEvent(p1.getNickname(), 0));
    }

    @Test
    void notifyRemoveComponentEvent() {
        listener.notifyRemoveComponentEvent(s1, point);
        verify(controllerListener).notifyEvent(new RemoveComponentEvent(p1.getNickname(), point));
    }

    @Test
    void notifyRequestFaceDownComponentEvent() {
        listener.notifyRequestFaceDownComponentEvent(s1,component);
        verify(controllerListener).notifyEvent(new RequestFaceDownComponentEvent(p1.getNickname(), component.getId()));
    }

    @Test
    void notifyRequestFaceUpComponentEvent() {
        listener.notifyRequestFaceUpComponentEvent(s1,component);
        verify(controllerListener).notifyEvent(new RequestFaceUpComponentEvent(p1.getNickname(), component.getId()));
    }

    @Test
    void notifyShipNotConnectedEvent() {
        listener.notifyShipNotConnectedEvent(s1, List.of(Set.of(point)));
        verify(controllerListener).notifyEvent(new ShipNotConnectedEvent(p1.getNickname(), List.of(Set.of(point))));
    }

    @Test
    void notifyShipPieceRemovalEvent() {
        listener.notifyShipPieceRemovalEvent(s1, 0);
        verify(controllerListener).notifyEvent(new ShipPieceRemoveEvent(p1.getNickname(), 0));
    }

    @Test
    void notifyGrabCreditsEvent() {
        listener.notifyGrabCreditsEvent(s1,0);
        verify(controllerListener).notifyEvent(new GrabCreditsEvent(p1.getNickname(), 0));
    }

    @Test
    void notifyStashComponentEvent() {
        listener.notifyStashComponentEvent(s1);
        verify(controllerListener).notifyEvent(new StashComponentEvent(p1.getNickname()));
    }

    @Test
    void notifySurrenderEvent() {
        listener.notifySurrenderEvent(List.of(s1,s2));
        verify(controllerListener).notifyEvent(new SurrenderEvent(List.of(p1.getNickname(),p2.getNickname())));
    }

    @Test
    void notifySurrenderRequestEvent() {
        listener.notifySurrenderRequestEvent(s1, SurrenderCause.REQUEST);
        verify(controllerListener).notifyEvent(new SurrenderRequestEvent(p1.getNickname(), SurrenderCause.REQUEST));
    }

    @Test
    void notifyUseBatteryEvent() {
        listener.notifyUseBatteryEvent(s1,point);
        verify(controllerListener).notifyEvent(new UseBatteryEvent(p1.getNickname(), point));
    }

    @Test
    void notifyValidateShipEvent() {
        listener.notifyValidateShipEvent(s1);
        verify(controllerListener).notifyEvent(new ValidateShipEvent(p1.getNickname()));
    }

    @Test
    void notifyCurrentPlayerUpdateEvent() {
        listener.notifyCurrentPlayerUpdateEvent(s1);
        verify(controllerListener).notifyEvent(new CurrentPlayerUpdateEvent(p1.getNickname()));
    }
}