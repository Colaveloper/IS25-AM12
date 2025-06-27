package it.polimi.ingsw.galaxytruckers.client.view;

/**
 * Interface for reporting errors in the Galaxy Truckers game.
 * Implementations of this interface handle error reporting functionality
 * for GUI and CLI views.
 */
public interface ErrorReporter {
    /**
     * Reports an error message to the user.
     *
     * @param message The error message to be reported
     */
    public void reportError(String message);
}
