package oscp.fagogenisv.yakuzastafftools.chat;

import oscp.fagogenisv.yakuzastafftools.YakuzaStaffToolsClient;
import oscp.fagogenisv.yakuzastafftools.util.TextUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class WatchdogManager {
    private static final int MAX_ALERTS = 5;

    /*
     * Matches:
     * [Watchdog] (skyblock) YakuzaGR failed Timer (Type B)
     * [Watchdog] (KitPvP) Player_Name failed Reach (Type A)
     */
    private static final Pattern WATCHDOG_PATTERN = Pattern.compile(
            "^\\s*\\[Watchdog]\\s*\\(([^)]+)\\)\\s+([A-Za-z0-9_]{3,16})\\s+failed\\s+(.+?)\\s+\\((?:Type\\s*)?([A-Za-z0-9]+)\\)\\s*$",
            Pattern.CASE_INSENSITIVE
    );

    private final LinkedList<WatchdogAlert> recentAlerts = new LinkedList<>();

    public void handleMessage(String message) {
        String clean = TextUtil.stripFormatting(message).trim();

        Matcher matcher = WATCHDOG_PATTERN.matcher(clean);

        if (!matcher.matches()) {
            return;
        }

        String world = matcher.group(1).trim();
        String player = matcher.group(2).trim();
        String cheat = matcher.group(3).trim();
        String type = matcher.group(4).trim();

        WatchdogAlert alert = new WatchdogAlert(world, player, cheat, type);
        addAlert(alert);

        YakuzaStaffToolsClient.LOGGER.info(
                "[YST] Watchdog alert captured: {} | {} | {} {}",
                player,
                world,
                cheat,
                type
        );
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
}