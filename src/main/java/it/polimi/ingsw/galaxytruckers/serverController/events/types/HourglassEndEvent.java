package it.polimi.ingsw.galaxytruckers.serverController.events.types;

/**
 * Event that represents the end of an hourglass timer.
 * This event is dispatched when the time tracked by an hourglass has expired,
 * signaling that a time-limited phase or action has ended.
 * This event contains no additional parameters as it only communicates the expiration of time.
 */
public record HourglassEndEvent() implements LobbyEvent {
}
