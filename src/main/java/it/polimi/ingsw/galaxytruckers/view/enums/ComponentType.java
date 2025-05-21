package it.polimi.ingsw.galaxytruckers.view.enums;

import java.util.List;

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

    ComponentType(List<String> symbol) {
        this.symbols = symbol;
    }

    public String getSymbol(int rotation) {
        return symbols.get(rotation% symbols.size());
    }
}
