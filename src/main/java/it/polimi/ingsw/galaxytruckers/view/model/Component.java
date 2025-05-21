package it.polimi.ingsw.galaxytruckers.view.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.ingsw.galaxytruckers.model.enumTypes.GoodsType;
import it.polimi.ingsw.galaxytruckers.model.shipBuilding.CrewType;
import it.polimi.ingsw.galaxytruckers.view.enums.ComponentType;
import javafx.beans.property.*;

import java.io.File;
import java.io.IOException;
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

    //represents the index of a block of components disconnected from the rest
    //other components in the same block have the same value
    protected int disconnectedShipPart;
    private final IntegerProperty direction;
    private final ObjectProperty<CrewType> crewType;
    private final IntegerProperty stat;
    private final ObjectProperty<List<GoodsType>> cargo;
    private ComponentType type;

    public Component(int componentId) {
        this.componentId = componentId;
        disconnectedShipPart = 0;
        isSelectable = new SimpleBooleanProperty(false);
        direction = new SimpleIntegerProperty(0);
        stat = new SimpleIntegerProperty(0);
        crewType = new SimpleObjectProperty<>(null);
        cargo = new SimpleObjectProperty<>();
        node = rootNode.get(componentId);
    }

    public Component(ComponentType type) {
        this(switch (type) {
            case ComponentType.EMPTY_AREA -> 0;
            case ComponentType.EMPTY_SPACE -> 1;
            default -> throw new IllegalArgumentException(
                    "this constructor is to use only for special components"
            );
        });
        this.type = type;
    }

    public int getComponentId() {
        return componentId;
    }

    public JsonNode getNode() {
        return node;
    }

    public ComponentType getType() {return type;}

    public BooleanProperty isSelectableProperty() {
        return isSelectable;
    }

    public void setShipPart(int shipPart) {
        this.disconnectedShipPart = shipPart;
    }

    public int getDisconnectedShipIndex() {
        return disconnectedShipPart;
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
