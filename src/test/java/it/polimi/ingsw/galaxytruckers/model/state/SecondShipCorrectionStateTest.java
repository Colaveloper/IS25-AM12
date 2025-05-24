package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.FourColors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SecondShipCorrectionStateTest {
    @Test
    void tryStateTransitionWhenShipPiecesAreNotEmpty() throws IOException {
        SecondShipCorrectionState testState = new SecondShipCorrectionState(){
            @Override
            public void setGame(Game game){
                this.game = game;
            }
        };
        Game game = new Game(Level.SECOND){
            @Override
            public Set<ShipBoard> getShipBoards(){
                //testShips.add(new SecondShipBoard(FourColors.GREEN));
                return new HashSet<>();
            }
        };
        testState.setGame(game);
        testState.shipPieces = new HashMap<>();
        testState.shipPieces.put(new SecondShipBoard(FourColors.RED), List.of(Set.of(new Point(7,7))));
        testState.tryStateTransition();
        assertNull(game.getCurrentState());
    }
}