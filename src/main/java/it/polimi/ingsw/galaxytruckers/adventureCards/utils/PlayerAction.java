package it.polimi.ingsw.galaxytruckers.adventureCards.utils;

public enum PlayerAction {
    MANAGE_GOODS,       //player moves goods on/to/off his shipboard or submits
    ACTIVATE_SHIELDS,    //player chooses shields to activate and chooses battery to use // TODO: atomize
    ACTIVATE_CANNONS,    //player chooses double cannons to activate or to submit
    ROLL_DICE,          //player presses enter to roll the dice
    ACTIVATE_ENGINES,    //player chooses to activate engine
    LOSE_GOODS,         //player chooses what goods to lose
    LOSE_RESIDENTS,      //player chooses what residents to lose
    CHOOSE_PLANET,      //player chooses planet
    ASK_IF_PASS,        //player chooses if he wants to pass
    START_CARD,         //doesn't require input but single-action would end before doing automatic parts(stardust, epidemic)
    END_CARD            //flag to end card
//  SUBMIT_POWER,       //player chooses if he wants to stop activating stuff
}
