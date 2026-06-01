package oscp.fagogenisv.yakuzastafftools.chat;

import oscp.fagogenisv.yakuzastafftools.YakuzaStaffToolsClient;
import oscp.fagogenisv.yakuzastafftools.util.TextUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class WatchdogManager {
    private static final int MAX_ALERTS = 5;

    /*
     * Main format:
     * [Watchdog] (skyblock) YakuzaGR failed Timer (Type B)
     */
    private static final Pattern FULL_PATTERN = Pattern.compile(
            ".*\\[Watchdog]\\s*\\(([^)]+)\\)\\s+([A-Za-z0-9_]{3,16})\\s+failed\\s+(.+?)\\s+\\((?:Type\\s*)?([A-Za-z0-9]+)\\).*",
            Pattern.CASE_INSENSITIVE
    );

    /*
     * Fallback:
     * Finds the username immediately before "failed".
     */
    private static final Pattern FALLBACK_PATTERN = Pattern.compile(
            ".*\\b([A-Za-z0-9_]{3,16})\\s+failed\\s+(.+)",
            Pattern.CASE_INSENSITIVE
    );

    private final LinkedList<WatchdogAlert> recentAlerts = new LinkedList<>();

    public void handleMessage(String message) {
        String clean = TextUtil.stripFormatting(message).trim();
        String lower = clean.toLowerCase(Locale.ROOT);

        if (!lower.contains("[watchdog]") || !lower.contains(" failed ")) {
            return;
        }

        Matcher fullMatcher = FULL_PATTERN.matcher(clean);

        if (fullMatcher.matches()) {
            String world = fullMatcher.group(1).trim();
            String player = fullMatcher.group(2).trim();
            String cheat = fullMatcher.group(3).trim();
            String type = fullMatcher.group(4).trim();

            addAlert(new WatchdogAlert(world, player, cheat, type));
            YakuzaStaffToolsClient.LOGGER.info("[YST] Watchdog captured: {} | {} | {} {}", player, world, cheat, type);
            return;
        }

        Matcher fallbackMatcher = FALLBACK_PATTERN.matcher(clean);

        if (fallbackMatcher.matches()) {
            String player = fallbackMatcher.group(1).trim();
            String cheatInfo = fallbackMatcher.group(2).trim();

            if (isIgnoredWord(player)) {
                return;
            }

            String cheat = cheatInfo;
            String type = "?";

            Matcher typeMatcher = Pattern.compile("(.+?)\\s+\\((?:Type\\s*)?([A-Za-z0-9]+)\\).*", Pattern.CASE_INSENSITIVE)
                    .matcher(cheatInfo);

            if (typeMatcher.matches()) {
                cheat = typeMatcher.group(1).trim();
                type = typeMatcher.group(2).trim();
            }

            addAlert(new WatchdogAlert("Unknown", player, cheat, type));
            YakuzaStaffToolsClient.LOGGER.info("[YST] Watchdog fallback captured: {} | {} {}", player, cheat, type);
        }
    }

    public List<WatchdogAlert> getRecentAlerts() {
        return new ArrayList<>(recentAlerts);
    }

    public List<String> getRecentTargets() {
        List<String> players = new ArrayList<>();

        for (WatchdogAlert alert : recentAlerts) {
            players.add(alert.player());
        }

        return players;
    }

    public void clear() {
        recentAlerts.clear();
    }

    private void addAlert(WatchdogAlert alert) {
        recentAlerts.removeIf(existing -> existing.player().equalsIgnoreCase(alert.player()));
        recentAlerts.addFirst(alert);

        while (recentAlerts.size() > MAX_ALERTS) {
            recentAlerts.removeLast();
        }
    }

    private boolean isIgnoredWord(String value) {
        String lower = value.toLowerCase(Locale.ROOT);

        return lower.equals("watchdog")
                || lower.equals("alerts")
                || lower.equals("enabled")
                || lower.equals("disabled")
                || lower.equals("permissions")
                || lower.equals("staff")
                || lower.equals("player")
                || lower.equals("failed");
    }
}