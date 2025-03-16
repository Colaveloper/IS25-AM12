package it.polimi.ingsw.galaxytruckers.adventureCards.utils;

public enum CardState {
    GRAB_GOODS,         //player places goods on his shipboard or none
    ACTIVATE_SHIELD,    //player rolls for small meteors and small cannons and chooses to activate
    ACTIVATE_CANNON,    //player rolls for big meteors and chooses to activate
    GET_BLASTED,        //player rolls for big cannon and loses pieces
    ACTIVATE_ENGINE,    //player chooses to activate engine
    SUBMIT_POWER,       //player chooses if he wants to stop activating stuff
    LOSE_GOODS,         //player chooses what goods to lose
    LOSE_RESIDENT,      //player chooses what residents to lose
    CHOOSE_PLANET,      //player chooses planet
    ASK_NEXT_PLAYER,    //player chooses if he wants to pass
    START_CARD,         //doesn't require input but single-action would end before doing automatic parts(stardust, epidemic)
    END_CARD            //flag to end card
}
