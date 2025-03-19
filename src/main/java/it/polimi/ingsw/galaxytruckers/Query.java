package it.polimi.ingsw.galaxytruckers;

public record Query<Integer, PlayerAction>(Integer playerID, PlayerAction expectedAction) {}
