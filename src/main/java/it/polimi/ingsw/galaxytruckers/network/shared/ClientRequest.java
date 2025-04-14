package it.polimi.ingsw.galaxytruckers.network.shared;

import java.io.Serializable;

public record ClientRequest(String nickname, String methodName, Object... args) implements Serializable {}