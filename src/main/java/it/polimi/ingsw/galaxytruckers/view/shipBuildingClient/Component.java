package it.polimi.ingsw.galaxytruckers.view.shipBuildingClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.StatType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import javafx.beans.property.*;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.List;

public class Component {
    private static final String jsonPath = "src/main/resources/tiles.json";
    private static final File jsonFile = new File(jsonPath);
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static JsonNode rootNode;
    static {
        try {
            rootNode = objectMapper.readTree(jsonFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private final int componentId;
    private final JsonNode node;
    private final BooleanProperty isSelectable;
    private final IntegerProperty direction;
    private final ObjectProperty<CrewType> crewType;
    private final IntegerProperty stat;
    private final ObjectProperty<List<GoodsType>> cargo;

    public Component(int componentId) {
        this.componentId = componentId;
        isSelectable = new SimpleBooleanProperty(false);
        direction = new SimpleIntegerProperty(0);
        stat = new SimpleIntegerProperty(0);
        crewType = new SimpleObjectProperty<>(null);
        cargo = new SimpleObjectProperty<>();
        node = rootNode.get(componentId);
    }

    public int getComponentId() {
        return componentId;
    }

    public JsonNode getNode() {
        return node;
    }

    public BooleanProperty isSelectableProperty() {
        return isSelectable;
    }

    public IntegerProperty directionProperty() {
        return direction;
    }

    public ObjectProperty<CrewType> crewTypeProperty() {
        return crewType;
    }

    public IntegerProperty statProperty() {
        return stat;
    }

    public ObjectProperty<List<GoodsType>> cargoProperty() {
        return cargo;
    }

    public void setStat(int stat) {
        this.stat.set(stat);
    }

    public void setDirection(int direction) {
        this.direction.set(direction);
    }

    public void rotateLeft() {
        direction.setValue((direction.get()+3)%4);
    }

    public void setGoods(List<GoodsType> goods) {this.cargo.set(goods);}

    public void setCrewType(CrewType crewType) {
        this.crewType.set(crewType);
        switch (crewType) {
            case CrewType.HUMAN:
                stat.set(2);
                break;
            case CrewType.PURPLE, CrewType.BROWN:
                stat.set(1);
                break;
        }
    }

}
