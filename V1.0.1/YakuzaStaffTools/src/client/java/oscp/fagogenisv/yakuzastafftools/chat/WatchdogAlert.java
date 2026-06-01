package oscp.fagogenisv.yakuzastafftools.chat;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public final class WatchdogAlert {
    private static final DateTimeFormatter TIME_FORMAT = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final String world;
    private final String player;
    private final String cheat;
    private final String type;
    private final String time;

    public WatchdogAlert(String world, String player, String cheat, String type) {
        this.world = world;
        this.player = player;
        this.cheat = cheat;
        this.type = type;
        this.time = LocalTime.now().format(TIME_FORMAT);
    }

    public String world() {
        return world;
    }

    public String player() {
        return player;
    }

    public String cheat() {
        return cheat;
    }

    public String type() {
        return type;
    }

    public String time() {
        return time;
    }

    public String shortLine(int index) {
        return "[" + index + "] " + player + " | " + world + " | " + cheat + " " + type;
    }
}