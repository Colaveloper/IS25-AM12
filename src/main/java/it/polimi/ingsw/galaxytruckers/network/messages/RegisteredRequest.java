package it.polimi.ingsw.galaxytruckers.network.messages;

/**
 * Represents a request that should be performed only by registered clients, that
 * is clients that have successfully registered a nickname.
 */
public non-sealed abstract class RegisteredRequest extends Request {}
