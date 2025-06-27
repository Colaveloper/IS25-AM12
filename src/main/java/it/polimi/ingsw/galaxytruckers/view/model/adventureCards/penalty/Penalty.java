package it.polimi.ingsw.galaxytruckers.view.model.adventureCards.penalty;

/**
 * Represents a penalty that can be applied to a player or ship during the game.
 * This is a sealed interface permitting specific penalty types such as CrewLoss,
 * FlightDaysLoss, GoodsLoss, and ProjectileThreat.
 */
public sealed interface Penalty permits CrewLoss,FlightDaysLoss,GoodsLoss,ProjectileThreat{}
