package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.*;
import it.polimi.ingsw.galaxytruckers.model.adventureCards.AdventureCard;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DrawCardStateTest {
    DrawCardState testState;
    ShipBoard ship1;
    ShipBoard ship2;
    Game game;
    FlightBoard flightBoard;
    Deck deck;
    AdventureCard adventureCard;

    @BeforeEach
    void setup() throws IOException{
        game = new Game(Level.SECOND);
        ship1 = new SecondShipBoard(GameColor.RED);
        ship2 = new SecondShipBoard(GameColor.BLUE);
        flightBoard = new SecondFlightBoard(Set.of(ship1, ship2)){
            @Override
            public List<ShipBoard> getOrderedShips(){
                return List.of(ship1, ship2);
            }
        };
        game.setFlightBoard(flightBoard);
        testState = new DrawCardState();
        testState.setGame(game);
    }

    @Test
    void drawCardThrowsExceptionWhenOutOfTurn(){
        assertThrows(IllegalStateException.class, () -> testState.drawCard(ship2));
    }

    @Test
    void drawCardEndsGameIfNoCardsRemain() throws IOException{
        deck = new SecondDeck(game){
            @Override
            public boolean tryDrawCard(){
                return false;
            }
        };
        game.setDeck(deck);
        testState.drawCard(ship1);
        assertEquals(EndGameState.class, game.getCurrentState().getClass());
    }

    @Test
    void drawCardDrawsCardAndChangesState() throws IOException{
        adventureCard = new AdventureCard(game, Level.SECOND, 1) {
            @Override
            public void initialize(){
                // mock
            }
            @Override
            public AdventureState getNextState(){
                return new AdventureState();
            }
        };
        deck = new SecondDeck(game){
            @Override
            public boolean tryDrawCard(){
                return true;
            }
            @Override
            public AdventureCard getCurrentCard(){
                return adventureCard;
            }
        };
        game.setDeck(deck);
        testState.drawCard(ship1);
        assertEquals(AdventureState.class, game.getCurrentState().getClass());
    }

}