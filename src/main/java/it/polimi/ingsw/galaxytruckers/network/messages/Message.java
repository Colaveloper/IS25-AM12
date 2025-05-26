package it.polimi.ingsw.galaxytruckers.network.messages;

import java.io.Serializable;

public sealed interface Message extends Serializable permits EventMessage, Ping, Request, Response {
}
