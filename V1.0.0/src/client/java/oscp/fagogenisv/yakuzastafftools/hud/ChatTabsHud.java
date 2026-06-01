package oscp.fagogenisv.yakuzastafftools.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.OrderedText;
import oscp.fagogenisv.yakuzastafftools.YakuzaStaffToolsClient;
import oscp.fagogenisv.yakuzastafftools.chat.CapturedChatMessage;
import oscp.fagogenisv.yakuzastafftools.chat.ChatTab;

import java.util.List;

public final class ChatTabsHud {
    private static final int BG = 0x66000000;
    private static final int TEXT = 0xFFFFFFFF;
    private static final int TITLE = 0xFFFFAA00;
    private static final int LINE_HEIGHT = 10;

    public void render(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer renderer = client.textRenderer;

        int x = 6;
        int panelWidth = 430;
        int panelHeight = 120;
        int panelY = context.getScaledWindowHeight() - 170;

        context.fill(x - 4, panelY - 4, x + panelWidth, panelY + panelHeight, BG);

        ChatTab active = YakuzaStaffToolsClient.CHAT_TAB_MANAGER.getActiveTab();
        context.drawText(renderer, active.displayName() + " Chat", x, panelY, TITLE, true);

        List<CapturedChatMessage> messages = YakuzaStaffToolsClient.CHAT_TAB_MANAGER.getVisibleMessages();
        int scroll = YakuzaStaffToolsClient.CHAT_TAB_MANAGER.getScrollOffset();

        int lineY = panelY + panelHeight - LINE_HEIGHT;
        int renderedLines = 0;
        int maxLines = 10;

        for (int i = scroll; i < messages.size(); i++) {
            if (renderedLines >= maxLines) {
                break;
            }

            CapturedChatMessage message = messages.get(i);
            List<OrderedText> wrapped = renderer.wrapLines(message.message(), panelWidth - 8);

            for (OrderedText line : wrapped) {
                if (renderedLines >= maxLines) {
                    break;
                }

                context.drawText(renderer, line, x, lineY, TEXT, true);
                lineY -= LINE_HEIGHT;
                renderedLines++;
            }
        }
    }
}