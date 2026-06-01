package oscp.fagogenisv.yakuzastafftools.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import oscp.fagogenisv.yakuzastafftools.YakuzaStaffToolsClient;

import java.util.List;

public final class SpyChatHud {
    private static final int TITLE_COLOR = 0xFFFFAA00;
    private static final int BG = 0x70000000;
    private static final int LINE_HEIGHT = 9;

    public void render(DrawContext context) {
        if (!YakuzaStaffToolsClient.CONFIG.enableMod || !YakuzaStaffToolsClient.CONFIG.enableSpyChatPanel) {
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer renderer = client.textRenderer;
        List<Text> messages = YakuzaStaffToolsClient.SPY_CHAT_MANAGER.getSpyMessages();

        int x = YakuzaStaffToolsClient.CONFIG.spyPanelX;
        int y = YakuzaStaffToolsClient.CONFIG.spyPanelY;
        int width = YakuzaStaffToolsClient.CONFIG.spyPanelWidth;
        int maxLines = YakuzaStaffToolsClient.CONFIG.spyPanelMaxLines;

        int height = 18 + maxLines * LINE_HEIGHT;

        context.fill(x - 3, y - 3, x + width, y + height, BG);
        context.drawText(renderer, "Spy Chat", x, y, TITLE_COLOR, true);

        int lineY = y + 13;
        int rendered = 0;

        for (Text message : messages) {
            if (rendered >= maxLines) {
                break;
            }

            List<OrderedText> wrapped = renderer.wrapLines(message, width - 8);

            for (OrderedText line : wrapped) {
                if (rendered >= maxLines) {
                    break;
                }

                context.drawText(renderer, line, x, lineY, 0xFFFFFFFF, true);
                lineY += LINE_HEIGHT;
                rendered++;
            }
        }
    }
}