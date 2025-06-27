package it.polimi.ingsw.galaxytruckers.server.controller.dto.components;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.galaxytruckers.shared.enums.GoodsType;
import it.polimi.ingsw.galaxytruckers.server.model.shipBuilding.CrewType;

import java.io.Serializable;
import java.util.Map;

/**
 * Represents the payload of a component in the Galaxy Truckers game.
 * This interface provides default methods to access various properties of a component,
 * such as crew type, number of residents, activation status, goods, and battery count.
 * <p>
 * Implementations of this interface are used for data transfer and serialization.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = CabinPayload.class, name = "cabin"),
        @JsonSubTypes.Type(value = ActivatablePayload.class, name = "activatable"),
        @JsonSubTypes.Type(value = CargoPayload.class, name = "cargoHold"),
        @JsonSubTypes.Type(value = BatteryPayload.class, name = "battery")
})
public interface ComponentPayload extends Serializable {
    //Cabin

    /**
     * Returns the type of crew associated with this component.
     *
     * @return the crew type, or null if not applicable
     */
    default CrewType crewType() {
        return null;
    }

    /**
     * Returns the number of residents in this component.
     *
     * @return the number of residents, or 0 if not applicable
     */
    default int numResidents() {
        return 0;
    }

    //Activatables

    /**
     * Returns whether this component is active.
     *
     * @return true if the component is active, false otherwise
     */
    default boolean active() {
        return false;
    }

    //Cargo Holds

    /**
     * Returns a map of goods types and their quantities stored in this component.
     *
     * @return a map where keys are goods types and values are their respective quantities, or null if not applicable
     */
    default Map<GoodsType, Integer> goods() {
        return null;
    }

    //Batteries

    /**
     * Returns the number of batteries associated with this component.
     *
     * @return the number of batteries, or 0 if not applicable
     */
    default int numBatteries() {
        return 0;
    }
}
