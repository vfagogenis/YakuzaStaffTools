package oscp.fagogenisv.yakuzastafftools.cheatsheet;

import java.util.ArrayList;
import java.util.List;

public final class BanCheatsheetData {
    private BanCheatsheetData() {
    }

    public static List<BanRule> getRules() {
        List<BanRule> rules = new ArrayList<>();

        /*
         * GENERAL RULES — BAN
         */

        add(rules, "General Rules (Ban)", "AFK Clicking/Farming",
                "15 days", "30 days", "60 days", "Permanent",
                "Use for AFK clicking, farming, macros, or similar unfair automation.");

        add(rules, "General Rules (Ban)", "Alternate Account Exploitation",
                "Warn", "7 days IP", "15 days IP", "30 days IP",
                "Use when alternate accounts are abused for advantage. Be careful with shared IPs.");

        add(rules, "General Rules (Ban)", "Alternate Account Limit",
                "Kick/Warn", "15 days IP", "30 days IP", "60 days IP",
                "Use when account limit rules are broken.");

        add(rules, "General Rules (Ban)", "Alt Teams",
                "7 days", "15 days", "30 days", "",
                "Use when alt accounts are used for teaming advantage.");

        add(rules, "General Rules (Ban)", "Asking/Sharing Personal Information",
                "Warn", "30 days", "Permanent", "",
                "Use when players ask for or share personal/private information.");

        add(rules, "General Rules (Ban)", "Ban/Mute Evasion",
                "Double Length", "Double Length", "Double Length", "Double Length",
                "Use when a player avoids an active punishment.");

        add(rules, "General Rules (Ban)", "Compromised Accounts",
                "Permanent", "", "", "",
                "Use when an account is compromised or unsafe.");

        add(rules, "General Rules (Ban)", "Discrimination",
                "15 days", "30 days", "60 days", "Permanent",
                "Use for discriminatory behavior or language.");

        add(rules, "General Rules (Ban)", "Discriminatory or Sensitive Builds / Symbols",
                "30 days", "60 days", "Permanent", "",
                "Use for offensive, discriminatory, or sensitive builds/symbols.");

        add(rules, "General Rules (Ban)", "Disorderly Conduct",
                "Warn", "1 day", "7 days", "15 days",
                "Use for disruptive conduct that affects the server environment.");

        add(rules, "General Rules (Ban)", "Exploitation of Bugs/Glitches",
                "30 days", "60 days", "Permanent", "",
                "Use when a player abuses bugs, glitches, dupes, or unintended mechanics.");

        add(rules, "General Rules (Ban)", "Ghosting/Teaming",
                "Warn", "1 day", "7 days", "15 days",
                "Use for ghosting, teaming, or unfair cooperation.");

        add(rules, "General Rules (Ban)", "Griefing/Stealing/Killing",
                "7 days", "15 days", "30 days", "",
                "Use for griefing, stealing, or killing where rules forbid it.");

        add(rules, "General Rules (Ban)", "Griefing/Stealing/Killing - In Boss Events",
                "Warn", "1 day", "7 days", "",
                "Use when the incident happens inside boss/event areas.");

        add(rules, "General Rules (Ban)", "Harassment",
                "Warn", "30 days", "60 days", "Permanent",
                "Use for repeated or serious harassment.");

        add(rules, "General Rules (Ban)", "Inappropriate Builds",
                "1 day", "7 days", "30 days", "",
                "Use for inappropriate structures/builds.");

        add(rules, "General Rules (Ban)", "Inappropriate Skins",
                "6 hours", "7 days", "30 days", "",
                "Use for inappropriate player skins.");

        add(rules, "General Rules (Ban)", "Inappropriate Titles",
                "Warn", "7 days", "15 days", "30 days",
                "Use for inappropriate titles, tags, or visible profile text.");

        add(rules, "General Rules (Ban)", "Inappropriate Usernames",
                "Permanent", "", "", "",
                "Use when a username is inappropriate and requires removal from the network.");

        add(rules, "General Rules (Ban)", "IRL Trading",
                "Warn", "30 days", "60 days", "Permanent",
                "Use for real-money trading or similar economy abuse.");

        add(rules, "General Rules (Ban)", "Lag Machine Attempts",
                "7 days", "15 days", "30 days", "",
                "Use for attempts to create lag machines or performance abuse.");

        add(rules, "General Rules (Ban)", "Trapping/TPA Killing",
                "7 days", "15 days", "30 days", "",
                "Use for trapping or killing through teleport abuse.");

        add(rules, "General Rules (Ban)", "Lying to Staff/Forging Evidence",
                "30 days", "60 days", "Permanent", "",
                "Use when a player lies to staff or creates fake evidence.");

        add(rules, "General Rules (Ban)", "Network Threats/Server Damage",
                "Permanent IP", "", "", "",
                "Use for serious threats against the network or attempts to damage the server.");

        add(rules, "General Rules (Ban)", "Nudity/Sexual Themes",
                "15 day mute", "30 days", "60 days", "Permanent",
                "Use for sexual content, nudity, or explicit themes.");

        add(rules, "General Rules (Ban)", "Profile Boosting",
                "Warn", "7 days", "15 days", "30 days",
                "Use for boosting or abusing profile progression.");

        add(rules, "General Rules (Ban)", "Scamming",
                "7 days", "15 days", "30 days", "",
                "Use for scams, dishonest trades, or deception.");

        add(rules, "General Rules (Ban)", "Sensitive Topics",
                "15 day mute", "30 days", "60 days", "Permanent",
                "Use for sensitive, harmful, or inappropriate topics.");

        add(rules, "General Rules (Ban)", "Threats",
                "Permanent IP", "", "", "",
                "Use for severe threats. Escalate carefully.");

        add(rules, "General Rules (Ban)", "Unapproved Modifications",
                "15 days", "30 days", "60 days", "Permanent",
                "Use for illegal mods, hacked clients, unfair gameplay modifications, cheats, macros, or automation.");

        /*
         * CHAT RULES — MUTE
         */

        add(rules, "Chat Rules (Mute)", "Advertising",
                "Warn", "30 min", "1 day", "7 days",
                "Use for advertising in chat.");

        add(rules, "Chat Rules (Mute)", "Advertising on Auction House",
                "Warn", "6 hour AH-Ban", "1 day AH-Ban", "7 days AH-Ban",
                "Use for advertising through auction house systems.");

        add(rules, "Chat Rules (Mute)", "Asking for Staff/Operator Permission",
                "Warn", "30 min", "1 day", "7 days",
                "Use when players ask for staff/operator permissions.");

        add(rules, "Chat Rules (Mute)", "Asking/Sharing Personal Information - Minor",
                "30 min", "7 days", "30 days", "",
                "Use for lower-severity personal information cases.");

        add(rules, "Chat Rules (Mute)", "Asking/Sharing Personal Information - Severe",
                "30 min", "1 day", "7 days", "30 days",
                "Use for more serious personal information cases.");

        add(rules, "Chat Rules (Mute)", "Drama/Escalation by Involvement",
                "Warn", "30 min", "1 day", "7 days",
                "Use when players continue or escalate drama.");

        add(rules, "Chat Rules (Mute)", "English Only/Custom Fonts",
                "Warn", "30 min", "1 day", "7 days",
                "Use for non-English chat where English-only rules apply, or unreadable custom fonts.");

        add(rules, "Chat Rules (Mute)", "External Links",
                "Warn", "30 min", "1 day", "7 days",
                "Use for unauthorized links.");

        add(rules, "Chat Rules (Mute)", "Impersonating Staff or Partner",
                "Warn", "30 min", "1 day", "7 days",
                "Use for pretending to be staff, partner, or official representative.");

        add(rules, "Chat Rules (Mute)", "Inappropriate Topics",
                "Warn", "30 min", "1 day", "7 days",
                "Use for inappropriate chat topics.");

        add(rules, "Chat Rules (Mute)", "Respect All Players",
                "Warn", "30 min", "1 day", "7 days",
                "Use for disrespectful behavior.");

        add(rules, "Chat Rules (Mute)", "Spam",
                "Warn", "30 min", "1 day", "7 days",
                "Use for spam, flood, repeated messages, or disruptive chat behavior.");

        add(rules, "Chat Rules (Mute)", "Swearing",
                "Warn", "30 min", "1 day", "7 days",
                "Use for excessive swearing or inappropriate language.");

        /*
         * WARNINGS
         */

        add(rules, "Important Warning", "Bedrock IP Punishments",
                "DO NOT IP BAN BEDROCK PLAYERS",
                "DO NOT IP MUTE BEDROCK PLAYERS",
                "", "",
                "Bedrock players may share/proxy IPs. Avoid IP punishments unless explicitly approved by senior staff.");

        return rules;
    }

    private static void add(
            List<BanRule> rules,
            String category,
            String rule,
            String first,
            String second,
            String third,
            String fourth,
            String note
    ) {
        rules.add(new BanRule(
                category,
                rule,
                "Screenshot / video / logs / staff observation depending on the case.",
                first,
                second,
                third,
                fourth,
                "",
                note
        ));
    }
}