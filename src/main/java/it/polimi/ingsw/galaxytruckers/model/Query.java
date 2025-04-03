package it.polimi.ingsw.galaxytruckers.model;

public record Query<Integer, PlayerAction>(Integer playerID, PlayerAction expectedAction) {}
