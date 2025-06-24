package it.polimi.ingsw.galaxytruckers.view.guiScreens;

import java.awt.*;

public abstract class GuiController {

    public void handlePointPress(Point currentPoint) {};

    public void goNext() {}

    public void placeShipOnFlightBoard(int position) {};

    public void requestRandComponent() {};

    public void rejectComponent() {};

    public void requestComponent(int id) {};

    public void grabStashedComponent(int i) {};

    public void stashComponent() {};

    public void rotateHandComponent() {};

    public void acquireForecast(int finalI) {}

    public void releaseForecast() {}

    public void drawCard() {}

    public void choosePlanet(int i) {}
}
