package it.polimi.ingsw.galaxytruckers;

import javafx.application.Platform;

public class JavaFXInitializer {
    static {
        try {
            Platform.startup(() -> {}); // Ensures JavaFX is initialized once
        } catch (IllegalStateException e) {
            // JavaFX is already initialized, ignore this exception
        }
    }
}

// OLD BY OHIO

// Before all tests, initialize JavaFX platform
// This is needed because JUnit tests by default do not start a GUI environment
// and any component relying on a GUI like Image needs the JavaFX runtime to be
// properly initialized
//    //TODO: fix the warning this generates, can be ignored for now
//    @BeforeAll
//    static void initJavaFX() throws InterruptedException {
//        Thread thread = new Thread(() -> {
//            Platform.startup(() -> {});
//        });
//        thread.setDaemon(true);
//        thread.start();
//        Thread.sleep(1000); // Give JavaFX time to initialize
//    }