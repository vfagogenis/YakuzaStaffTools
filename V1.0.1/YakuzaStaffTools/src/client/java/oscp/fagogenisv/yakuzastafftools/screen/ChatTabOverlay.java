package oscp.fagogenisv.yakuzastafftools.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import org.lwjgl.glfw.GLFW;
import oscp.fagogenisv.yakuzastafftools.YakuzaStaffToolsClient;
import oscp.fagogenisv.yakuzastafftools.chat.ChatTab;

public final class ChatTabOverlay {
    private static final int BG = 0xAA000000;
    private static final int ACTIVE = 0xAA7A0000;
    private static final int TEXT = 0xFFFFFFFF;
    private static boolean wasMousePressed;

    private ChatTabOverlay() {}

    public static void render(DrawContext context) {
        if (!YakuzaStaffToolsClient.CONFIG.enableMod || !YakuzaStaffToolsClient.CONFIG.enableChatTabs) return;
        MinecraftClient client = MinecraftClient.getInstance();
        if (!client.inGameHud.getChatHud().isChatFocused()) return;
        TextRenderer tr = client.textRenderer;
        int x = 4;
        int y = context.getScaledWindowHeight() - 58;
        for (ChatTab tab : ChatTab.values()) {
            int w = tr.getWidth(tab.displayName()) + 12;
            int color = YakuzaStaffToolsClient.CHAT_TAB_MANAGER.getActiveTab() == tab ? ACTIVE : BG;
            context.fill(x, y, x + w, y + 14, color);
            context.drawText(tr, tab.displayName(), x + 6, y + 3, TEXT, true);
            x += w + 3;
        }
    }

    public static void tickMouse(MinecraftClient client) {
        if (!YakuzaStaffToolsClient.CONFIG.enableMod || !YakuzaStaffToolsClient.CONFIG.enableChatTabs) return;
        if (client == null || client.getWindow() == null || !client.inGameHud.getChatHud().isChatFocused()) return;
        boolean pressed = GLFW.glfwGetMouseButton(client.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
        if (pressed && !wasMousePressed) handleClick(client);
        wasMousePressed = pressed;
    }

    private static void handleClick(MinecraftClient client) {
        double mouseX = client.mouse.getX() * client.getWindow().getScaledWidth() / client.getWindow().getWidth();
        double mouseY = client.mouse.getY() * client.getWindow().getScaledHeight() / client.getWindow().getHeight();
        TextRenderer tr = client.textRenderer;
        int x = 4;
        int y = client.getWindow().getScaledHeight() - 58;
        for (ChatTab tab : ChatTab.values()) {
            int w = tr.getWidth(tab.displayName()) + 12;
            if (mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + 14) {
                YakuzaStaffToolsClient.CHAT_TAB_MANAGER.setActiveTab(tab);
                return;
            }
            x += w + 3;
        }
    }
}
