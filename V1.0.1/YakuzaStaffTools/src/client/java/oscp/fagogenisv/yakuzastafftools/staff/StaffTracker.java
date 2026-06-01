package oscp.fagogenisv.yakuzastafftools.staff;

import oscp.fagogenisv.yakuzastafftools.util.TextUtil;

import java.util.*;
import java.util.regex.*;

public final class StaffTracker {
    private static final Pattern STAFF_HEADER = Pattern.compile("^Online\\s+Staff\\s+\\((\\d+)\\):?$", Pattern.CASE_INSENSITIVE);
    private static final Pattern STAFF_LINE = Pattern.compile("^\\[(.+?)]\\s+([A-Za-z0-9_]{1,16})\\s+-\\s+(.+)$");
    private static final Pattern STAFF_NETWORK_PATTERN = Pattern.compile("^SC>\\s*([A-Za-z0-9_]{1,16})\\s+has\\s+(joined|left)\\s+the\\s+network!$", Pattern.CASE_INSENSITIVE);

    private final Map<String, StaffMember> onlineStaff = new LinkedHashMap<>();
    private boolean readingStaffList = false;
    private int expectedStaffLines = 0;
    private int readStaffLines = 0;

    public void handleMessage(String message) {
        String clean = TextUtil.stripFormatting(message).trim();

        handleStaffNetworkEvent(clean);

        Matcher header = STAFF_HEADER.matcher(clean);
        if (header.matches()) {
            onlineStaff.clear();
            readingStaffList = true;
            expectedStaffLines = parseInt(header.group(1));
            readStaffLines = 0;
            return;
        }

        if (!readingStaffList) return;

        Matcher line = STAFF_LINE.matcher(clean);
        if (line.matches()) {
            String rank = line.group(1).trim();
            String username = line.group(2).trim();
            String server = line.group(3).trim();

            onlineStaff.put(username.toLowerCase(Locale.ROOT), new StaffMember(rank, username, server));
            readStaffLines++;

            if (expectedStaffLines > 0 && readStaffLines >= expectedStaffLines) {
                readingStaffList = false;
            }
        }
    }

    public List<StaffMember> getOnlineStaffMembers() {
        return new ArrayList<>(onlineStaff.values());
    }

    public List<String> getOnlineStaff() {
        List<String> names = new ArrayList<>();
        for (StaffMember member : onlineStaff.values()) {
            names.add(member.username());
        }
        return names;
    }

    public void clear() {
        onlineStaff.clear();
        readingStaffList = false;
        expectedStaffLines = 0;
        readStaffLines = 0;
    }

    private void handleStaffNetworkEvent(String clean) {
        Matcher matcher = STAFF_NETWORK_PATTERN.matcher(clean);
        if (!matcher.matches()) return;

        String username = matcher.group(1);
        String action = matcher.group(2).toLowerCase(Locale.ROOT);

        if (action.equals("left")) {
            onlineStaff.remove(username.toLowerCase(Locale.ROOT));
        }
    }

    private int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ignored) {
            return 0;
        }
    }
}