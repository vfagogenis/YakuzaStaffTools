package oscp.fagogenisv.yakuzastafftools.staff;

public final class StaffMember {
    private final String rank;
    private final String username;
    private final String server;

    public StaffMember(String rank, String username, String server) {
        this.rank = rank;
        this.username = username;
        this.server = server;
    }

    public String rank() {
        return rank;
    }

    public String username() {
        return username;
    }

    public String server() {
        return server;
    }

    public String compactLine() {
        return "[" + rank + "] " + username;
    }
}