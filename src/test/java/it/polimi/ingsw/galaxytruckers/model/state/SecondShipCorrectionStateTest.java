package it.polimi.ingsw.galaxytruckers.model.state;

import it.polimi.ingsw.galaxytruckers.model.Game;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.FourColors;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.SecondShipBoard;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.ShipBoard;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class SecondShipCorrectionStateTest {

    // fix this
    // hint, setGame does a whole bunch of stuff that you don't see
//    @Test
//    void tryStateTransitionWhenShipPiecesAreNotEmpty(){
//        SecondShipCorrectionState testState = new SecondShipCorrectionState();
//        Game game = new Game(Level.SECOND){
//            @Override
//            public Set<ShipBoard> getShipBoards(){
//                Set<ShipBoard> testShips = new HashSet<>();
//                testShips.add(new SecondShipBoard(FourColors.GREEN));
//                return testShips;
//            }
//        };
//        testState.setGame(game);
//        testState.shipPieces = new HashMap<>();
//        testState.shipPieces.put(new SecondShipBoard(FourColors.RED), List.of(Set.of(new Point(7,7))));
//        testState.tryStateTransition();
//        assertNull(game.getCurrentState());
//    }

}