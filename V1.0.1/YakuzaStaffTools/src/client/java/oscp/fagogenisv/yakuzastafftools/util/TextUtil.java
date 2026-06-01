package oscp.fagogenisv.yakuzastafftools.util;

import java.util.regex.Pattern;

public final class TextUtil {
    private static final Pattern FORMATTING = Pattern.compile("(?i)\u00a7[0-9A-FK-OR]");

    private TextUtil() {}

    public static String stripFormatting(String value) {
        return FORMATTING.matcher(safeString(value)).replaceAll("");
    }

    public static String safeString(String value) {
        return value == null ? "" : value;
    }

    public static String limit(String value, int maxLength) {
        String safe = safeString(value);
        if (maxLength < 0 || safe.length() <= maxLength) return safe;
        return safe.substring(0, maxLength);
    }
}
