package oscp.fagogenisv.yakuzastafftools.keybinds;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import oscp.fagogenisv.yakuzastafftools.YakuzaStaffToolsClient;
import oscp.fagogenisv.yakuzastafftools.screen.YakuzaSettingsScreen;

import java.util.List;
import java.util.Locale;

public final class YakuzaKeybinds {
    private final boolean[] wasPressed = new boolean[5];

    private final int[] keys = {
            GLFW.GLFW_KEY_KP_1,
            GLFW.GLFW_KEY_KP_2,
            GLFW.GLFW_KEY_KP_3,
            GLFW.GLFW_KEY_KP_4,
            GLFW.GLFW_KEY_KP_5
    };

    private boolean settingsWasPressed = false;

    private String pendingSpecPlayer = null;
    private int pendingSpecTicks = 0;

    public void initialize() {
        for (int i = 0; i < wasPressed.length; i++) {
            wasPressed[i] = false;
        }

        settingsWasPressed = false;
        pendingSpecPlayer = null;
        pendingSpecTicks = 0;
    }

    public void tick(MinecraftClient client) {
        if (client == null || client.player == null || client.getWindow() == null) {
            return;
        }

        tickSettingsKey(client);

        if (!YakuzaStaffToolsClient.CONFIG.enableMod || !YakuzaStaffToolsClient.CONFIG.enableNumpadActions) {
            return;
        }

        /*
         * Safety:
         * Do not run moderation commands inside ReplayMod,
         * singleplayer, menus, or fake/non-server sessions.
         */
        if (!isAllowedServer(client)) {
            pendingSpecPlayer = null;
            pendingSpecTicks = 0;
            return;
        }

        tickPendingSpec(client);

        long handle = client.getWindow().getHandle();

        for (int i = 0; i < keys.length; i++) {
            boolean pressed = GLFW.glfwGetKey(handle, keys[i]) == GLFW.GLFW_PRESS;

            if (pressed && !wasPressed[i]) {
                runSlot(client, i);
            }

            wasPressed[i] = pressed;
        }
    }

    private void tickSettingsKey(MinecraftClient client) {
        long handle = client.getWindow().getHandle();

        boolean pressed = GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_RIGHT_SHIFT) == GLFW.GLFW_PRESS
                && GLFW.glfwGetKey(handle, GLFW.GLFW_KEY_Y) == GLFW.GLFW_PRESS;

        if (pressed && !settingsWasPressed) {
            client.setScreen(new YakuzaSettingsScreen(client.currentScreen));
        }

        settingsWasPressed = pressed;
    }

    private void runSlot(MinecraftClient client, int slot) {
        List<String> targets = YakuzaStaffToolsClient.WATCHDOG_MANAGER.getRecentTargets();

        if (slot >= targets.size()) {
            sendFeedback(client, "[YST] No Watchdog target in slot " + (slot + 1));
            return;
        }

        String player = targets.get(slot);

        if (player == null || player.isBlank()) {
            sendFeedback(client, "[YST] Empty Watchdog target in slot " + (slot + 1));
            return;
        }

        sendCommand(client, "moveto " + player);
        sendFeedback(client, "[YST] Moving to " + player + "...");

        pendingSpecPlayer = player;
        pendingSpecTicks = Math.max(20, YakuzaStaffToolsClient.CONFIG.moveToSpecDelayTicks);
    }

    private void tickPendingSpec(MinecraftClient client) {
        if (pendingSpecPlayer == null) {
            return;
        }

        if (client.player == null || client.getNetworkHandler() == null) {
            pendingSpecPlayer = null;
            pendingSpecTicks = 0;
            return;
        }

        if (!isAllowedServer(client)) {
            pendingSpecPlayer = null;
            pendingSpecTicks = 0;
            return;
        }

        if (pendingSpecTicks > 0) {
            pendingSpecTicks--;
            return;
        }

        sendCommand(client, "spec " + pendingSpecPlayer);
        sendFeedback(client, "[YST] Spectating " + pendingSpecPlayer);

        pendingSpecPlayer = null;
        pendingSpecTicks = 0;
    }

    private void sendCommand(MinecraftClient client, String commandWithoutSlash) {
        if (client.player == null || client.player.networkHandler == null) {
            return;
        }

        client.player.networkHandler.sendChatCommand(commandWithoutSlash);
    }

    private void sendFeedback(MinecraftClient client, String message) {
        if (client.player == null) {
            return;
        }

        client.player.sendMessage(Text.literal(message), false);
    }

    private boolean isAllowedServer(MinecraftClient client) {
        if (client == null || client.player == null || client.getNetworkHandler() == null) {
            return false;
        }

        ServerInfo serverInfo = client.getCurrentServerEntry();

        if (serverInfo == null || serverInfo.address == null || serverInfo.address.isBlank()) {
            return false;
        }

        String address = serverInfo.address.toLowerCase(Locale.ROOT);

        return address.contains("advancius")
                || address.contains("shinigami");
    }
}