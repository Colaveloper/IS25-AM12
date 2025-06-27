package it.polimi.ingsw.galaxytruckers.server.controller.events.types;

import java.util.List;
import java.util.Optional;

/**
 * Event containing details about the forecast deck for a specific player.
 *
 * @param playerName the name of the player receiving the forecast details
 * @param forecastDeckIds the list of forecast deck card IDs
 */
public record ForecastDetailsEvent(String playerName, List<Integer> forecastDeckIds) implements LobbyEvent {

    @Override
    public Optional<String> getReceiverName() {
        return Optional.of(playerName);
    }
}
