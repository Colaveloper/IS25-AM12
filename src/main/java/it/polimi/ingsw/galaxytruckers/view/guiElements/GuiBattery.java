package it.polimi.ingsw.galaxytruckers.view.guiElements;

import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Battery;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class GuiBattery extends GuiComponent {

    public GuiBattery(Battery battery) {
        super(battery);
        updateContentBox(new ArrayList<>(Collections.nCopies(
                battery.getNumBatteries(),
                Color.GREEN
        )));
    }

    public void notifyBatteryUpdate() {
        // todo: implement and use
    }
}
