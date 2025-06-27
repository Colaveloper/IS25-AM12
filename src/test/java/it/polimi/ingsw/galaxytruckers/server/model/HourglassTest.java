package it.polimi.ingsw.galaxytruckers.server.model;

import it.polimi.ingsw.galaxytruckers.server.model.Hourglass;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class HourglassTest {
    Hourglass hourglass;

    @BeforeEach
    void setUp() {
        hourglass = new Hourglass(2);
    }

    @Test
    void flipThrowsWhenRunning() {
        hourglass.flip(() -> {});
        assertTrue(hourglass.getIsRunning());
        assertTrue(hourglass.getMissingTime() > 0);
        assertThrows(IllegalStateException.class, () -> hourglass.flip(() -> {}));
    }

    @Test
    void flipHourgalsThrowsWithNoMoreFlips() throws InterruptedException {
        hourglass.setDuration(1);
        for (int i = 0; i < 2; i++) {
            CountDownLatch latch = new CountDownLatch(1);
            hourglass.flip(latch::countDown);
            if (!latch.await(3000, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Latch timed out");
            }
        }
        assertFalse(hourglass.getIsRunning());
        assertEquals(0, hourglass.getFlipsLeft());
        assertThrows(IllegalStateException.class, () -> hourglass.flip(() -> {}));
    }
}