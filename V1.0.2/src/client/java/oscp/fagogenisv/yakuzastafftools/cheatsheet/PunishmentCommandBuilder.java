package oscp.fagogenisv.yakuzastafftools.cheatsheet;

public final class PunishmentCommandBuilder {
    private PunishmentCommandBuilder() {
    }

    public static String buildCommand(String player, BanRule rule, int level) {
        String reason = sanitizeReason(rule.rule());

        boolean muteRule = rule.category().toLowerCase().contains("chat")
                || rule.rule().toLowerCase().contains("spam")
                || rule.rule().toLowerCase().contains("toxicity")
                || rule.rule().toLowerCase().contains("harassment")
                || rule.rule().toLowerCase().contains("advertising");

        if (muteRule) {
            return buildMuteCommand(player, reason, level);
        }

        return buildBanCommand(player, reason, level);
    }

    private static String buildBanCommand(String player, String reason, int level) {
        return switch (level) {
            case 1 -> "warn " + player + " " + reason;
            case 2 -> "tempban " + player + " 1d " + reason;
            case 3 -> "tempban " + player + " 7d " + reason;
            case 4 -> "tempban " + player + " 30d " + reason;
            case 5 -> "ban " + player + " " + reason;
            default -> "warn " + player + " " + reason;
        };
    }

    private static String buildMuteCommand(String player, String reason, int level) {
        return switch (level) {
            case 1 -> "warn " + player + " " + reason;
            case 2 -> "tempmute " + player + " 30m " + reason;
            case 3 -> "tempmute " + player + " 2h " + reason;
            case 4 -> "tempmute " + player + " 1d " + reason;
            case 5 -> "mute " + player + " " + reason;
            default -> "warn " + player + " " + reason;
        };
    }

    private static String sanitizeReason(String reason) {
        return reason
                .replace("/", "")
                .replace("\n", " ")
                .replace("\r", " ")
                .trim();
    }
}