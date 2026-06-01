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

        if (client == null || client.textRenderer == null) {
            return;
        }

        TextRenderer renderer = client.textRenderer;
        List<WatchdogAlert> alerts = YakuzaStaffToolsClient.WATCHDOG_MANAGER.getRecentAlerts();

        int width = 155;
        int height = 62;

        int x = YakuzaStaffToolsClient.CONFIG.watchdogHudX;
        int y = YakuzaStaffToolsClient.CONFIG.watchdogHudY;

        context.fill(x - 3, y - 3, x + width, y + height, BG);

        context.drawText(
                renderer,
                "Yakuza Watchdog (" + alerts.size() + ")",
                x,
                y,
                TITLE_COLOR,
                true
        );

        for (int i = 0; i < 5; i++) {
            int lineY = y + 11 + (i * LINE_HEIGHT);

            if (i < alerts.size()) {
                WatchdogAlert alert = alerts.get(i);

                String line = "[" + (i + 1) + "] " + alert.player();

                context.drawText(
                        renderer,
                        line,
                        x,
                        lineY,
                        TEXT_COLOR,
                        true
                );
            } else {
                context.drawText(
                        renderer,
                        "[" + (i + 1) + "] -",
                        x,
                        lineY,
                        EMPTY_COLOR,
                        true
                );
            }
        }
    }
}