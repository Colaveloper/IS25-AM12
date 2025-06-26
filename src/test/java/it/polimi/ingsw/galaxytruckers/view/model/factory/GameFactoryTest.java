package it.polimi.ingsw.galaxytruckers.view.model.factory;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.Level;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameFactoryTest {

    @Test
    void getFactory() {
        GameFactory testFactory = GameFactory.getFactory(Level.TEST);
        assertInstanceOf(TestFactory.class, testFactory);

        GameFactory secondFactory = GameFactory.getFactory(Level.SECOND);
        assertInstanceOf(SecondFactory.class, secondFactory);

        assertThrows(IllegalArgumentException.class, () -> GameFactory.getFactory(Level.FIRST));
    }
}