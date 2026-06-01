package oscp.fagogenisv.yakuzastafftools.chat;

public enum ChatTab {
    YAKUZA("Yakuza"),
    VANILLA("Vanilla"),
    WATCHDOG("Watchdog"),
    SPY("Spy");

    private final String displayName;

    ChatTab(String displayName) {
        this.displayName = displayName;
    }

    public String displayName() {
        return displayName;
    }
}