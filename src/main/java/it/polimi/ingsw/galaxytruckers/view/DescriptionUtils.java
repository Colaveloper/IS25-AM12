package it.polimi.ingsw.galaxytruckers.view;

import org.checkerframework.dataflow.qual.Pure;

import javax.annotation.CheckReturnValue;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class DescriptionUtils {
    private static final Pattern ANSI_ESCAPE = Pattern.compile("\u001B\\[[;\\d]*m");

    @Pure
    @CheckReturnValue
    public static List<String> sideBySide(List<String> a, List<String> b) {
        List<String> c = new ArrayList<>(a);

        // Find max length among a strings (treat empty strings normally)
        int leftWidth = 1;
        for (String s : a) {
            if (getRealWidth(s) > leftWidth) {
                leftWidth = getRealWidth(s);
            }
        }

        int maxSize = Math.max(a.size(), b.size());

        // Extend a list if needed
        while (a.size() < maxSize) {
            c.add("");
        }

        for (int i = 0; i < maxSize; i++) {
            String left = a.get(i);
            String right = (i < b.size()) ? b.get(i) : "";

            StringBuilder line = new StringBuilder(left);
            int padding = leftWidth - getRealWidth(left);
            line.append(" ".repeat(Math.max(0, padding)));

            // Add the space between left and right
            line.append(' ');
            line.append(right);

            c.set(i, line.toString());
        }
        return c;
    }

    @Pure
    @CheckReturnValue
    public static List<String> borderAndTitle(List<String> original, String title) {
        int contentWidth = 0;
        for (String line : original) {
            contentWidth = Math.max(contentWidth, getRealWidth(line));
        }

        int totalWidth = contentWidth + 2 + 2; // padding + border

        String titleWithSpace = " " + title + " ";
        int titleDisplayWidth = getRealWidth(titleWithSpace);

        // Calculate start position to center the title
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

        String bottomBorder = "╰" + "─".repeat(totalWidth - 2) +
                '╯';

        List<String> bordered = new ArrayList<>();
        bordered.add(topBorder.toString());

        for (String line : original) {
            StringBuilder borderedLine = new StringBuilder("│ ");
            borderedLine.append(line);
            int linePadding = contentWidth - getRealWidth(line);
            borderedLine.append(" ".repeat(Math.max(0, linePadding)));
            borderedLine.append(" │");
            bordered.add(borderedLine.toString());
        }

        bordered.add(bottomBorder);

        original.clear();
        original.addAll(bordered);
        return original;
    }

    private static int getRealWidth(String line) {
        return ANSI_ESCAPE.matcher(line).replaceAll("").length();
    }
}
