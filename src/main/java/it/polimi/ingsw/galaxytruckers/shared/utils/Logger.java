package it.polimi.ingsw.galaxytruckers.shared.utils;

import it.polimi.ingsw.galaxytruckers.client.view.cli.CliHighlights;

import java.lang.*;

/**
 * Utility class for logging messages with customizable formatting.
 * This logger supports colored console output based on a specified color index.
 * Debug mode can be toggled to enable or disable logging output.
 */
public class Logger {
    /** Flag to control whether debug messages are displayed */
    private static boolean debug = false;

    public static void setEnable(boolean flag) {
        Logger.debug = flag;
    }

    /**
     * Prints a formatted message to the console with color highlighting.
     * If debug mode is disabled, no output will be produced.
     *
     * @param n The color index to use from CliHighlights
     * @param params Variable number of objects to print; their string representations will be concatenated
     */
    public static void println(int n, Object... params) {

        if (!debug) {
            return;
        }

        StringBuilder res = new StringBuilder();

        for (int i = 0; i < params.length; i++) {
            res.append(params[i].toString());
        }

        System.out.println(CliHighlights.getSomeColors(n).getLast().getHighlight() + res.toString() + CliHighlights.RESET.getHighlight());
    }
}