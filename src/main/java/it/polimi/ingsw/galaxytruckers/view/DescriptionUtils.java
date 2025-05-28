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
        if (a.isEmpty()) return new ArrayList<>(b);
        if (b.isEmpty()) return new ArrayList<>(a);

        List<String> c = new ArrayList<>(a);

        int leftWidth = 1;
        for (String s : a) {
            leftWidth = Math.max(leftWidth, getRealWidth(s));
        }

        int maxSize = Math.max(a.size(), b.size());

        while (c.size() < maxSize) {
            c.add("");
        }

        for (int i = 0; i < maxSize; i++) {
            String left = c.get(i);
            String right = (i < b.size()) ? b.get(i) : "";

            StringBuilder line = new StringBuilder(left);
            int padding = leftWidth - getRealWidth(left);
            line.append(" ".repeat(Math.max(0, padding)));

            line.append(' ').append(right);
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

        String titleWithSpace = " " + title + " ";
        int titleDisplayWidth = getRealWidth(titleWithSpace);

        // Ensure contentWidth is at least as wide as the title
        contentWidth = Math.max(contentWidth, titleDisplayWidth);

        int totalWidth = contentWidth + 4; // 2 padding + 2 border

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

    private static int getRealWidth(String line) {
        return ANSI_ESCAPE.matcher(line).replaceAll("").length();
    }
}
