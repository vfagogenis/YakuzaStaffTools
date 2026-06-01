package oscp.fagogenisv.yakuzastafftools.chat;

import net.minecraft.text.Text;
import oscp.fagogenisv.yakuzastafftools.YakuzaStaffToolsClient;
import oscp.fagogenisv.yakuzastafftools.util.TextUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public final class ChatTabManager {
    private static final int MAX_MESSAGES = 250;

    private final LinkedList<CapturedChatMessage> messages = new LinkedList<>();
    private ChatTab activeTab = ChatTab.YAKUZA;
    private int scrollOffset = 0;

    public void add(Text message) {
        if (message == null) {
            return;
        }

        ChatTab type = classify(message);

        messages.addFirst(new CapturedChatMessage(message, type));

        while (messages.size() > MAX_MESSAGES) {
            messages.removeLast();
        }
    }

    public List<CapturedChatMessage> getVisibleMessages() {
        List<CapturedChatMessage> visible = new ArrayList<>();

        for (CapturedChatMessage captured : messages) {
            if (shouldShow(captured)) {
                visible.add(captured);
            }
        }

        return visible;
    }

    public ChatTab getActiveTab() {
        return activeTab;
    }

    public void setActiveTab(ChatTab activeTab) {
        this.activeTab = activeTab;
        this.scrollOffset = 0;
    }

    public int getScrollOffset() {
        return scrollOffset;
    }

    public void scroll(int amount) {
        int max = Math.max(0, getVisibleMessages().size() - 1);

        scrollOffset += amount;

        if (scrollOffset < 0) {
            scrollOffset = 0;
        }

        if (scrollOffset > max) {
            scrollOffset = max;
        }
    }

    public void clear() {
        messages.clear();
        activeTab = ChatTab.YAKUZA;
        scrollOffset = 0;
    }

    private ChatTab classify(Text message) {
        String clean = TextUtil.stripFormatting(message.getString()).trim();
        String lower = clean.toLowerCase(Locale.ROOT);

        if (YakuzaStaffToolsClient.SPY_CHAT_MANAGER.isSpyMessage(clean)) {
            return ChatTab.SPY;
        }

        if (lower.startsWith("[watchdog]") && lower.contains(" failed ")) {
            return ChatTab.WATCHDOG;
        }

        // Normal chat, server messages, staff messages, system messages, etc.
        return ChatTab.VANILLA;
    }

    private boolean shouldShow(CapturedChatMessage message) {
        return switch (activeTab) {
            // Vanilla tab shows literally everything captured.
            case VANILLA -> true;

            // Yakuza tab shows normal chat + watchdog + server messages, but no spy.
            case YAKUZA -> message.type() != ChatTab.SPY;

            // Watchdog tab only watchdog alerts.
            case WATCHDOG -> message.type() == ChatTab.WATCHDOG;

            // Spy tab only spy commands/messages.
            case SPY -> message.type() == ChatTab.SPY;
        };
    }
}