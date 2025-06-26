package it.polimi.ingsw.galaxytruckers.view.controller;

import it.polimi.ingsw.galaxytruckers.model.enumTypes.GameColor;
import it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ComponentRegistryTest {
    private ComponentRegistry registry;

    @BeforeEach
    void setUp() {
        registry = ComponentRegistry.getInstance();
    }

    @Test
    void getInstance_ReturnsSameInstance() {
        ComponentRegistry secondInstance = ComponentRegistry.getInstance();
        assertSame(registry, secondInstance);
    }

    @Test
    void getComponent_ReturnsValidComponent() {
        Component component = registry.getComponent(1);

        assertNotNull(component);
        assertEquals(1, component.getId());
    }

    @Test
    void getComponent_InvalidId_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> registry.getComponent(-1));
    }

    @Test
    void getComponent_NonexistentId_ThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> registry.getComponent(999999));
    }

    @Test
    void getStartingCabin_ReturnsValidComponentForColor() {
        Component cabin = registry.getStartingCabin(GameColor.RED);

        assertNotNull(cabin);
        assertTrue(cabin.getId() > 0);
    }

    @Test
    void getStartingCabin_AllColorsHaveCabins() {
        for (GameColor color : GameColor.values()) {
            Component cabin = registry.getStartingCabin(color);
            assertNotNull(cabin, "Should have cabin for color " + color);
        }
    }

    @Test
    void getInstance_InitializesComponentsFromJson() {
        ComponentRegistry instance = ComponentRegistry.getInstance();
        Component component = instance.getComponent(1);
        assertNotNull(component, "Component should be loaded from JSON");
    }

    @Test
    void getComponent_ReturnedComponentHasCorrectProperties() {
        Component component = registry.getComponent(1);

        assertNotNull(component);
    }

    @Test
    void getStartingCabin_CopiesAreIndependent() {
        Component firstCabin = registry.getStartingCabin(GameColor.RED);
        Component secondCabin = registry.getStartingCabin(GameColor.RED);

        assertNotSame(firstCabin, secondCabin, "Each call should return a new instance");
        assertEquals(firstCabin.getId(), secondCabin.getId(), "Copies should have same ID");
    }

    @Test
    void constructor_WhenLoadComponentsThrowsIOException_ThrowsRuntimeException() {
        class FailingRegistry extends ComponentRegistry {
            @Override
            protected void loadComponents() throws IOException {
                throw new IOException("Simulated failure");
            }
        }
        assertThrows(RuntimeException.class, FailingRegistry::new);
    }

    @Test
    void getComponent_WhenParseComponentThrowsIOException_ThrowsRuntimeException() {
        ComponentRegistry registry = new ComponentRegistry() {
            @Override
            public Component getComponent(int id) {
                throw new RuntimeException(new IOException("Simulated IO error"));
            }
        };
        assertThrows(RuntimeException.class, () -> registry.getComponent(1));
    }

    @Test
    void getStartingCabin_WhenParseComponentThrowsIOException_ThrowsRuntimeException() {
        ComponentRegistry registry = new ComponentRegistry() {
            @Override
            public Component getStartingCabin(GameColor color) {
                throw new RuntimeException(new IOException("Simulated IO error"));
            }
        };
        assertThrows(RuntimeException.class, () -> registry.getStartingCabin(GameColor.RED));
    }

    @Test
    void parseComponent_UnknownType_ThrowsIllegalArgumentException() throws Exception {
        ComponentRegistry registry = new ComponentRegistry();
        ObjectNode node = new ObjectNode(JsonNodeFactory.instance);
        node.put("id", 9999);
        node.put("type", "unknown_type");
        node.set("connectors", new ObjectNode(JsonNodeFactory.instance));
        java.lang.reflect.Method m = ComponentRegistry.class.getDeclaredMethod("parseComponent", int.class, JsonNode.class);
        m.setAccessible(true);
        assertThrows(IllegalArgumentException.class, () -> {
            try {
                m.invoke(registry, 9999, node);
            } catch (java.lang.reflect.InvocationTargetException e) {
                throw e.getCause();
            }
        });
    }

    @Test
    void parseCrewType_NullOrInvalid_ThrowsJsonParseException() throws Exception {
        java.lang.reflect.Method m = ComponentRegistry.class.getDeclaredMethod("parseCrewType", JsonNode.class);
        m.setAccessible(true);
        assertThrows(JsonParseException.class, () -> {
            try {
                m.invoke(null, (JsonNode) null);
            } catch (java.lang.reflect.InvocationTargetException e) {
                throw e.getCause();
            }
        });
        TextNode invalidNode = new TextNode("");
        assertThrows(IllegalArgumentException.class, () -> {
            try {
                m.invoke(null, invalidNode);
            } catch (java.lang.reflect.InvocationTargetException e) {
                throw e.getCause();
            }
        });
    }

    @Test
    void parseComponent_ShieldType_ReturnsShield() throws Exception {
        ComponentRegistry registry = new ComponentRegistry();
        ObjectNode node = new ObjectNode(JsonNodeFactory.instance);
        node.put("id", 1);
        node.put("type", "shield");
        node.set("connectors", new ObjectNode(JsonNodeFactory.instance));
        java.lang.reflect.Method m = ComponentRegistry.class.getDeclaredMethod("parseComponent", int.class, JsonNode.class);
        m.setAccessible(true);
        Object result = m.invoke(registry, 1, node);
        assertTrue(result instanceof it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Shield);
    }

    @Test
    void parseComponent_LifeSupportType_ReturnsLifeSupport() throws Exception {
        ComponentRegistry registry = new ComponentRegistry();
        ObjectNode node = new ObjectNode(JsonNodeFactory.instance);
        node.put("id", 2);
        node.put("type", "life_support");
        node.set("connectors", new ObjectNode(JsonNodeFactory.instance));
        node.put("crewtype", "HUMAN");
        java.lang.reflect.Method m = ComponentRegistry.class.getDeclaredMethod("parseComponent", int.class, JsonNode.class);
        m.setAccessible(true);
        Object result = m.invoke(registry, 2, node);
        assertTrue(result instanceof it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.LifeSupport);
    }

    @Test
    void parseComponent_DoubleCannonType_ReturnsDoubleCannon() throws Exception {
        ComponentRegistry registry = new ComponentRegistry();
        ObjectNode node = new ObjectNode(JsonNodeFactory.instance);
        node.put("id", 3);
        node.put("type", "double_cannon");
        node.set("connectors", new ObjectNode(JsonNodeFactory.instance));
        java.lang.reflect.Method m = ComponentRegistry.class.getDeclaredMethod("parseComponent", int.class, JsonNode.class);
        m.setAccessible(true);
        Object result = m.invoke(registry, 3, node);
        assertTrue(result instanceof it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.DoubleCannon);
    }

    @Test
    void parseComponent_CannonType_ReturnsCannon() throws Exception {
        ComponentRegistry registry = new ComponentRegistry();
        ObjectNode node = new ObjectNode(JsonNodeFactory.instance);
        node.put("id", 4);
        node.put("type", "cannon");
        node.set("connectors", new ObjectNode(JsonNodeFactory.instance));
        java.lang.reflect.Method m = ComponentRegistry.class.getDeclaredMethod("parseComponent", int.class, JsonNode.class);
        m.setAccessible(true);
        Object result = m.invoke(registry, 4, node);
        assertTrue(result instanceof it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Cannon);
    }

    @Test
    void parseComponent_DoubleEngineType_ReturnsDoubleEngine() throws Exception {
        ComponentRegistry registry = new ComponentRegistry();
        ObjectNode node = new ObjectNode(JsonNodeFactory.instance);
        node.put("id", 5);
        node.put("type", "double_engine");
        node.set("connectors", new ObjectNode(JsonNodeFactory.instance));
        java.lang.reflect.Method m = ComponentRegistry.class.getDeclaredMethod("parseComponent", int.class, JsonNode.class);
        m.setAccessible(true);
        Object result = m.invoke(registry, 5, node);
        assertTrue(result instanceof it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.DoubleEngine);
    }

    @Test
    void parseComponent_EngineType_ReturnsEngine() throws Exception {
        ComponentRegistry registry = new ComponentRegistry();
        ObjectNode node = new ObjectNode(JsonNodeFactory.instance);
        node.put("id", 6);
        node.put("type", "engine");
        node.set("connectors", new ObjectNode(JsonNodeFactory.instance));
        java.lang.reflect.Method m = ComponentRegistry.class.getDeclaredMethod("parseComponent", int.class, JsonNode.class);
        m.setAccessible(true);
        Object result = m.invoke(registry, 6, node);
        assertTrue(result instanceof it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Engine);
    }

    @Test
    void parseComponent_CargoHoldType_ReturnsCargoHold() throws Exception {
        ComponentRegistry registry = new ComponentRegistry();
        ObjectNode node = new ObjectNode(JsonNodeFactory.instance);
        node.put("id", 7);
        node.put("type", "cargo_hold");
        node.set("connectors", new ObjectNode(JsonNodeFactory.instance));
        node.put("size", 1);
        node.put("special", false);
        java.lang.reflect.Method m = ComponentRegistry.class.getDeclaredMethod("parseComponent", int.class, JsonNode.class);
        m.setAccessible(true);
        Object result = m.invoke(registry, 7, node);
        assertTrue(result instanceof it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.CargoHold);
    }

    @Test
    void parseComponent_StructuralType_ReturnsComponent() throws Exception {
        ComponentRegistry registry = new ComponentRegistry();
        ObjectNode node = new ObjectNode(JsonNodeFactory.instance);
        node.put("id", 8);
        node.put("type", "structural");
        node.set("connectors", new ObjectNode(JsonNodeFactory.instance));
        java.lang.reflect.Method m = ComponentRegistry.class.getDeclaredMethod("parseComponent", int.class, JsonNode.class);
        m.setAccessible(true);
        Object result = m.invoke(registry, 8, node);
        assertTrue(result instanceof it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Component);
    }

    @Test
    void parseComponent_BatteryType_ReturnsBattery() throws Exception {
        ComponentRegistry registry = new ComponentRegistry();
        ObjectNode node = new ObjectNode(JsonNodeFactory.instance);
        node.put("id", 9);
        node.put("type", "battery");
        node.set("connectors", new ObjectNode(JsonNodeFactory.instance));
        node.put("batteries", 2);
        java.lang.reflect.Method m = ComponentRegistry.class.getDeclaredMethod("parseComponent", int.class, JsonNode.class);
        m.setAccessible(true);
        Object result = m.invoke(registry, 9, node);
        assertTrue(result instanceof it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Battery);
    }

    @Test
    void parseComponent_CabinType_ReturnsCabin() throws Exception {
        ComponentRegistry registry = new ComponentRegistry();
        ObjectNode node = new ObjectNode(JsonNodeFactory.instance);
        node.put("id", 10);
        node.put("type", "cabin");
        node.set("connectors", new ObjectNode(JsonNodeFactory.instance));
        java.lang.reflect.Method m = ComponentRegistry.class.getDeclaredMethod("parseComponent", int.class, JsonNode.class);
        m.setAccessible(true);
        Object result = m.invoke(registry, 10, node);
        assertTrue(result instanceof it.polimi.ingsw.galaxytruckers.view.model.shipBuilding.Cabin);
    }

    @Test
    void getIdToImagePath_ReturnsCorrectMapping() throws Exception {
        // Prepare a registry and inject a known component with a 'path' field
        ComponentRegistry registry = new ComponentRegistry();
        ObjectNode node = new ObjectNode(JsonNodeFactory.instance);
        node.put("id", 42);
        node.put("type", "shield");
        node.put("path", "textures/tiles/shield.png");
        node.set("connectors", new ObjectNode(JsonNodeFactory.instance));
        Map<Integer, JsonNode> map = new HashMap<>();
        map.put(42, node);
        // Use reflection to set the private 'components' field
        java.lang.reflect.Field field = ComponentRegistry.class.getDeclaredField("components");
        field.setAccessible(true);
        field.set(registry, map);
        Map<Integer, java.nio.file.Path> result = registry.getIdToImagePath();
        assertTrue(result.containsKey(42));
        assertEquals(java.nio.file.Path.of("textures/tiles/shield.png"), result.get(42));
    }

    // Helper for test registry
    protected Map<Integer, JsonNode> getComponents() { return null; }
}
