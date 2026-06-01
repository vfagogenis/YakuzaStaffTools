package oscp.fagogenisv.yakuzastafftools.util;

import oscp.fagogenisv.yakuzastafftools.YakuzaStaffToolsClient;
import oscp.fagogenisv.yakuzastafftools.staff.StaffMember;

public final class RankUtil {
    private RankUtil() {
    }

    public static String getRankForPlayer(String username) {
        if (username == null || username.isBlank()) {
            return "Unknown";
        }

        for (StaffMember member : YakuzaStaffToolsClient.STAFF_TRACKER.getOnlineStaffMembers()) {
            if (member.username().equalsIgnoreCase(username)) {
                return member.rank();
            }
        }

        return "Player";
    }
}

