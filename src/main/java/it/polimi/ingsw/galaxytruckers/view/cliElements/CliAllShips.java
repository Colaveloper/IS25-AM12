package it.polimi.ingsw.galaxytruckers.view.cliElements;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.FourColors;
import it.polimi.ingsw.galaxytruckers.network.client.ConfigFactory;
import it.polimi.ingsw.galaxytruckers.view.DescriptionUtils;
import it.polimi.ingsw.galaxytruckers.view.model.ClientModel;
import it.polimi.ingsw.galaxytruckers.view.model.Component;
import javafx.beans.property.ObjectProperty;

import java.io.IOException;
import java.util.*;

public class CliAllShips extends CliElement {
    private final Map<FourColors, CliShipBoard> cliShipBoards; // ALL FINAL
    private final Map<FourColors, CliComponent> handsMap; // UPDATE WHEN HANDS CHANGES
    private final Map<FourColors, List<CliComponent>> stashedComponentsMap;

    // CONSTRUCTOR TO USE AFTER BUILDING PHASE
    public CliAllShips(ClientModel model) {
        super(model);
        cliShipBoards = new HashMap<>();
        model.getShips().forEach((color, _) -> {
            try {
                CliShipBoard cliShipBoard = new CliShipBoard(model, color);
                cliShipBoards.put(color, cliShipBoard);
                //cliShipBoard.addListener(this);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        handsMap = null;
        stashedComponentsMap = null;
    }

    // CONSTRUCTOR TO USE IN BUILDING PHASE
    public CliAllShips(ClientModel model, ConfigFactory config) {
        super(model);
        cliShipBoards = new HashMap<>();
        model.getShips().forEach((color, _) -> {
            try {
                CliShipBoard cliShipBoard = new CliShipBoard(model, color);
                cliShipBoards.put(color, cliShipBoard);
                //cliShipBoard.addListener(this);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        handsMap = new HashMap<>();
        model.getHand().forEach((color, componentProperty) -> {
            try {
                CliComponent hand = new CliComponent(model, componentProperty);
                handsMap.put(color, hand);
                //hand.addListener(this);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        if (config.isStashingAllowed()) {
            stashedComponentsMap = new HashMap<>();
            model.getStashed().forEach((color, propertyList) -> {
                try {
                    List<CliComponent> stashedComponents = new ArrayList<>();
                    for(ObjectProperty<Component> property : propertyList) {
                        CliComponent stashed = new CliComponent(model, property);
                        //stashed.addListener(this);
                        stashedComponents.add(stashed);
                    }
                    stashedComponentsMap.put(color, stashedComponents);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } else {stashedComponentsMap = null;}
    }

    @Override
    public List<String> getDescription() {
        List<String> description = new ArrayList<>();

        for (FourColors c : model.getPlayerToColor().values()) {

            List<String> singleShipDescription = new ArrayList<>(cliShipBoards.get(c).getDescription());
            List<String> handAndStashDescription = new ArrayList<>();
            List<String> singleStashedDescription = new ArrayList<>();

            if (handsMap != null) {
                List<String> hand = new ArrayList<>(handsMap.get(c).getDescription());
                hand.add(" ");
                handAndStashDescription = DescriptionUtils.borderAndTitle(hand, "hand");
            }

            if (stashedComponentsMap != null) {
                List<String> stashed = new ArrayList<>();
                int i = 1;
                for(CliComponent component : stashedComponentsMap.get(c)) {
                    singleStashedDescription.clear();
                    singleStashedDescription.addAll(component.getDescription());
                    singleStashedDescription.add("  "+i);
                    DescriptionUtils.sideBySide(stashed, singleStashedDescription);
                    i++;
                }
                DescriptionUtils.sideBySide(
                        handAndStashDescription,
                        DescriptionUtils.borderAndTitle(stashed, "stash")
                );
            }
            singleShipDescription.addAll(handAndStashDescription);
            DescriptionUtils.sideBySide(description, singleShipDescription);
        }

        return description;
    }
}
