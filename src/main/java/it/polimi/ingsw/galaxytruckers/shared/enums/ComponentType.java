package it.polimi.ingsw.galaxytruckers.shared.enums;

import java.util.List;

/**
 * Enum representing the types of ship components in the game.
 * <p>
 * Each component type is associated with a list of symbols, which can be used for CLI representation.
 * The symbols may represent different rotations or visual states of the component.
 * </p>
 */
public enum ComponentType {
    SHIELD(
            List.of("╮", "╯", "╰", "╭")),
    LIFE_SUPPORT(
            List.of("Ѫ")),
    DOUBLE_CANNON(
            List.of("▲", "▶", "▼", "◀")),
    CANNON(
            List.of("△", "▷", "▽", "◁")),
    ENGINE(
            List.of("↓", "←", "↑", "→")),
    DOUBLE_ENGINE(
            List.of("⇓", "⇐", "⇑", "⇒")),
    CARGO_HOLD(
            List.of("■")),
    SPECIAL_CARGO_HOLD(
            List.of("○")),
    STRUCTURAL(
            List.of(" ")),
    BATTERY(
            List.of("⭍")),
    CABIN(
            List.of("⌂")),
    EMPTY_AREA(                 //yet to be placed
            List.of("")),
    EMPTY_SPACE(                //not of the ship
            List.of(""));

    private final List<String> symbols;

    /**
     * Constructs a ComponentType with the given list of symbols.
     *
     * @param symbol the list of symbols for this component type
     */
    ComponentType(List<String> symbol) {
        this.symbols = symbol;
    }

    /**
     * Returns the symbol for the given rotation.
     *
     * @param rotation the rotation index
     * @return the symbol corresponding to the rotation
     */
    public String getSymbol(int rotation) {
        return symbols.get(rotation % symbols.size());
    }
}
