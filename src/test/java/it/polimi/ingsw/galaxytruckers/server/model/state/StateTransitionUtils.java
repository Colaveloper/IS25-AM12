package it.polimi.ingsw.galaxytruckers.server.model.state;

import it.polimi.ingsw.galaxytruckers.server.model.Game;
import it.polimi.ingsw.galaxytruckers.server.model.state.GameState;
import org.checkerframework.dataflow.qual.AssertMethod;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class StateTransitionUtils {
    public static CountDownLatch setupLatch(Game game) {
        CountDownLatch latch = new CountDownLatch(1);
        game.setAfterEach(() -> {
            latch.countDown();
            game.shutdown();
            try {
                Thread.sleep(1050);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        return latch;
    }

    @AssertMethod
    public static void assertTransition(CountDownLatch latch, Game game, Class<? extends GameState> expectedState) {
        try {
            if (latch.await(400, TimeUnit.SECONDS))
                assertInstanceOf(expectedState, game.getCurrentState());
            else throw new IllegalStateException("Latch timed out");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @AssertMethod
    public static void assertNoTransition(CountDownLatch latch, Game game, GameState expectedState) {
        try {
            if (latch.await(50, TimeUnit.MILLISECONDS))
                throw new IllegalStateException("Latch did not time out");
            else
                assertEquals(expectedState, game.getCurrentState());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
