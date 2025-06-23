package it.polimi.ingsw.galaxytruckers.view;

import org.checkerframework.dataflow.qual.Pure;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Utility class for text formatting and manipulation in the Galaxy Truckers game.
 * This class provides methods for formatting text displays, such as placing text
 * side by side and adding borders with titles around text blocks.
 * It properly handles ANSI escape sequences when calculating text widths.
 */
public class DescriptionUtils {
    /** Pattern to match ANSI escape sequences in strings. */
    private static final Pattern ANSI_ESCAPE = Pattern.compile("\u001B\\[[;\\d]*m");

    /**
     * Places two lists of strings side by side with default spacing of 1.
     *
     * @param a The first list of strings (left side)
     * @param b The second list of strings (right side)
     * @return A new list of strings with content from both lists placed side by side
     */
    @Pure
    @CheckReturnValue
    public static List<String> sideBySide(List<String> a, List<String> b) {
        return sideBySide(a, b, 1); // default spacing = 1
    }

    /**
     * Places two lists of strings side by side with specified spacing between them.
     * If one list is longer than the other, empty strings are used to pad the shorter list.
     * ANSI escape sequences are handled correctly when calculating widths.
     *
     * @param a       The first list of strings (left side)
     * @param b       The second list of strings (right side)
     * @param spacing The number of space characters to insert between the two sides
     * @return A new list of strings with content from both lists placed side by side
     */
    @Pure
    @CheckReturnValue
    public static List<String> sideBySide(List<String> a, List<String> b, int spacing) {
        if (a.isEmpty()) return new ArrayList<>(b);
        if (b.isEmpty()) return new ArrayList<>(a);

        List<String> c = new ArrayList<>(a);
        int leftWidth = 1;

        for (String s : a) {
            leftWidth = Math.max(leftWidth, getRealWidth(s));
        }

        int maxSize = Math.max(a.size(), b.size());
        while (c.size() < maxSize) c.add("");

        String space = " ".repeat(Math.max(0, spacing));

        for (int i = 0; i < maxSize; i++) {
            String left = c.get(i);
            String right = (i < b.size()) ? b.get(i) : "";

            StringBuilder line = new StringBuilder(left);
            int padding = leftWidth - getRealWidth(left);
            line.append(" ".repeat(Math.max(0, padding))).append(space).append(right);
            c.set(i, line.toString());
        }

        return c;
    }

    /**
     * Adds a decorative border around a list of strings with a title at the top.
     * The content is centered within the border, and the title is centered in the top border.
     * ANSI escape sequences are handled correctly when calculating widths.
     *
     * @param original The list of strings to be bordered
     * @param title    The title to display in the top border
     * @return A new list of strings with the original content surrounded by a border and title
     */
    @Pure
    @CheckReturnValue
    public static List<String> borderAndTitle(List<String> original, String title) {
        int contentWidth = 0;
        for (String line : original) {
            contentWidth = Math.max(contentWidth, getRealWidth(line));
        }

        String titleWithSpace = " " + title + " ";
        int titleDisplayWidth = getRealWidth(titleWithSpace);

        // Ensure contentWidth is at least as wide as the title
        contentWidth = Math.max(contentWidth, titleDisplayWidth);

        int totalWidth = contentWidth + 4; // 2 padding + 2 border

        // Calculate connect position to center the title
        int titleStart = Math.max(0, (totalWidth - 2 - titleDisplayWidth) / 2);

        StringBuilder topBorder = new StringBuilder("╭");
        int i = 0;
        while (i < titleStart) {
            topBorder.append('─');
            i++;
        }
        topBorder.append(titleWithSpace);
        while (i < totalWidth - 2 - titleDisplayWidth) {
            topBorder.append('─');
            i++;
        }
        topBorder.append('╮');

        String bottomBorder = "╰" + "─".repeat(totalWidth - 2) + '╯';

        List<String> bordered = new ArrayList<>();
        bordered.add(topBorder.toString());

        for (String line : original) {
            int realLineWidth = getRealWidth(line);
            int totalPadding = contentWidth - realLineWidth;
            int leftPadding = totalPadding / 2;
            int rightPadding = totalPadding - leftPadding;

            String borderedLine =
                    "│ " + " ".repeat(leftPadding) + line + " ".repeat(rightPadding) + " │";
            bordered.add(borderedLine);
        }

        bordered.add(bottomBorder);
        return bordered;
    }

    /**
     * Calculates the actual display width of a string by removing ANSI escape sequences.
     * This is necessary because ANSI escape sequences don't occupy display space but are part of the string length.
     *
     * @param line The string to calculate the real width for
     * @return The display width of the string (excluding ANSI escape sequences)
     */
    private static int getRealWidth(String line) {
        return ANSI_ESCAPE.matcher(line).replaceAll("").length();
    }
}
