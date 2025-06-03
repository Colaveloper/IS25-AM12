package it.polimi.ingsw.galaxytruckers.utils;

import it.polimi.ingsw.galaxytruckers.view.enums.Highlights;

import java.lang.*;

public class Logger {
    private static boolean debug = true;
//
//    public static void setSilent(boolean flag) {
//        Logger.debug = flag;
//    }

    public static void println(int n, Object... params) {

        if (!debug) {
            return;
        }

        StringBuilder res = new StringBuilder();

        for (int i = 0; i < params.length; i++) {
            res.append(params[i].toString());
        }

        System.out.println(Highlights.getSomeColors(n).getLast().getHighlight() + res.toString() + Highlights.RESET.getHighlight());
    }
}