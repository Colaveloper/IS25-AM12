package it.polimi.ingsw.galaxytruckers.view.guiElements;

import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.ShipBoard;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class GuiStatBox extends PurpleVBox {

    private final ShipBoard shipBoard;
    private final GridPane grid;

    public GuiStatBox(ShipBoard shipBoard) {
        this.shipBoard = shipBoard;
        this.grid = new GridPane();
        setupContainer();
        buildGrid();
    }

    private void setupContainer() {
        this.setSpacing(0);
        this.setPadding(new Insets(10));
        this.setBackground(new Background(new BackgroundFill(
                Color.rgb(10, 20, 50, 0.7),
                new CornerRadii(8),
                Insets.EMPTY
        )));
        // allow grid to fill horizontally
        this.setFillWidth(true);
        grid.setMaxWidth(Double.MAX_VALUE);
        this.getChildren().add(grid);
    }

    private void buildGrid() {
        // clear old content and constraints
        grid.getChildren().clear();
        grid.getColumnConstraints().clear();

        grid.setHgap(15);
        grid.setVgap(8);
        grid.setPadding(new Insets(10, 15, 10, 20));

        // define six equal-width columns
        for (int i = 0; i < 6; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setHgrow(Priority.ALWAYS);
            cc.setPercentWidth(100.0 / 6);
            grid.getColumnConstraints().add(cc);
        }

        // header row labels
        String[] headers = {"Cannons", "Engines", "Batteries", "Crew", "Credits", "Losses"};
        for (int col = 0; col < headers.length; col++) {
            Label lbl = createLabel(headers[col], 12);
            grid.add(lbl, col, 0);
        }

        // stat values row
        int fire = shipBoard.getFirePower() / 2;
        String lossVal = shipBoard.getLosses() != 0 ? String.valueOf(shipBoard.getLosses()) : "-";
        String[] values = {String.valueOf(fire),
                String.valueOf(shipBoard.getEnginePower()),
                String.valueOf(shipBoard.getNumBatteries()),
                String.valueOf(shipBoard.getCrewSize()),
                String.valueOf(shipBoard.getCredits()),
                lossVal};
        for (int col = 0; col < values.length; col++) {
            Label valLbl = createLabel(values[col], 11);
            grid.add(valLbl, col, 1);
        }
    }

    private Label createLabel(String text, double fontSize) {
        Label lbl = new Label(text);
        lbl.setFont(new Font("Arial", fontSize));
        lbl.setTextFill(Color.WHITE);
        GridPane.setHgrow(lbl, Priority.ALWAYS);
        lbl.setMaxWidth(Double.MAX_VALUE);
        lbl.setAlignment(javafx.geometry.Pos.CENTER);
        return lbl;
    }

    /**
     * Refreshes the stat grid when underlying ShipBoard changes.
     */
    public void notifyChange() {
        Platform.runLater(this::buildGrid);
    }
}
