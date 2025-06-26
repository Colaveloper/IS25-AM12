package it.polimi.ingsw.galaxytruckers.network.messages;

import java.io.Serializable;

/**
 * Represents a message that can be sent over the network via sockets.
 * This interface is a marker interface for all types of messages in the game.
 * It extends Serializable to allow messages to be serialized for network transmission.
 */
public sealed interface Message extends Serializable permits EventMessage, Ping, Request, Response {
}
