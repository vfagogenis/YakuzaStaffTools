package oscp.fagogenisv.yakuzastafftools.hud;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import oscp.fagogenisv.yakuzastafftools.YakuzaStaffToolsClient;
import oscp.fagogenisv.yakuzastafftools.chat.WatchdogAlert;

import java.util.List;

public final class WatchdogHud {
    private static final int TITLE_COLOR = 0xFFFF5555;
    private static final int TEXT_COLOR = 0xFFFFFFFF;
    private static final int EMPTY_COLOR = 0xFFAAAAAA;
    private static final int BG = 0x70000000;
    private static final int LINE_HEIGHT = 9;

    public void render(DrawContext context) {
        if (!YakuzaStaffToolsClient.CONFIG.enableMod || !YakuzaStaffToolsClient.CONFIG.enableWatchdogHud) {
            return;
        }

        MinecraftClient client = MinecraftClient.getInstance();
        TextRenderer renderer = client.textRenderer;
        List<WatchdogAlert> alerts = YakuzaStaffToolsClient.WATCHDOG_MANAGER.getRecentAlerts();

        int width = 135;
        int height = 58;

        int x = YakuzaStaffToolsClient.CONFIG.watchdogHudX;
        int y = YakuzaStaffToolsClient.CONFIG.watchdogHudY;

        context.fill(x - 3, y - 3, x + width, y + height, BG);
        context.drawText(renderer, "Yakuza Watchdog", x, y, TITLE_COLOR, true);

        for (int i = 0; i < 5; i++) {
            String line = i < alerts.size()
                    ? "[" + (i + 1) + "] " + alerts.get(i).player()
                    : "[" + (i + 1) + "] -";

            context.drawText(
                    renderer,
                    line,
                    x,
                    y + 10 + (i * LINE_HEIGHT),
                    i < alerts.size() ? TEXT_COLOR : EMPTY_COLOR,
                    true
            );
        }
    }
}