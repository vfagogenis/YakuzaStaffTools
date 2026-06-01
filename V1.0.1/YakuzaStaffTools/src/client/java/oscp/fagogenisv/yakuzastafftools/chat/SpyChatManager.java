package oscp.fagogenisv.yakuzastafftools.chat;

import net.minecraft.text.Text;
import oscp.fagogenisv.yakuzastafftools.util.TextUtil;

import java.util.*;

public final class SpyChatManager {
    private static final int MAX_MESSAGES = 50;

    private final LinkedList<Text> spyMessages = new LinkedList<>();

    public boolean handleMessage(Text message) {
        if (message == null) return false;

        String clean = TextUtil.stripFormatting(message.getString()).trim();
        if (!isSpyMessage(clean)) return false;

        spyMessages.addFirst(message.copy());

        while (spyMessages.size() > MAX_MESSAGES) {
            spyMessages.removeLast();
        }

        return true;
    }

    public List<Text> getSpyMessages() {
        return new ArrayList<>(spyMessages);
    }

    public void clear() {
        spyMessages.clear();
    }

    public boolean isSpyMessage(String message) {
        String lower = message.toLowerCase(Locale.ROOT);

        return lower.startsWith("spy>")
                || lower.startsWith("spy >")
                || lower.startsWith("spy:")
                || lower.contains("spy>");
    }
}