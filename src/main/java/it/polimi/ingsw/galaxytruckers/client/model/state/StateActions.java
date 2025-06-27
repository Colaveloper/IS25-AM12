package it.polimi.ingsw.galaxytruckers.client.model.state;

/**
 * Enumeration of all possible actions that players can take during different game states
 * in the Galaxy Truckers game. Each action represents a specific interaction with the game
 * that is available in one or more game states.
 */
public enum StateActions {
    GRAB_PLACED_COMPONENT,
    ACTIVATE_COMPONENT,
    SPEND_BATTERIES,
    GRAB_REWARD,
    CHOOSE_SHIP_PIECE,
    DRAW_CARD,
    LOSE_CREW,
    LOSE_GOOD,
    ADD_GOOD,
    REMOVE_GOOD,
    GO_NEXT,
    CHOOSE_PLANET,
    REQUEST_RAND_COMPONENT,
    REQUEST_COMPONENT,
    REJECT_COMPONENT,
    STASH_COMPONENT,
    GRAB_STASHED_COMPONENT,
    PLACE_COMPONENT,
    ROTATE_COMPONENT,
    FLIP_HOURGLASS,
    PLACE_SHIP_ON_FLIGHTBOARD,
    PLACE_SHIP_FOR_TEST,
    FINISH_BUILDING,
    ACQUIRE_FORECAST,
    RELEASE_FORECAST,
    REMOVE_COMPONENT,
    INITIALIZE_CABIN,
    GIVE_UP,
}
