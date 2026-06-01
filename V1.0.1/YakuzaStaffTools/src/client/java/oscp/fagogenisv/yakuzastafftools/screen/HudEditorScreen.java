package oscp.fagogenisv.yakuzastafftools.screen;

import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;
import oscp.fagogenisv.yakuzastafftools.YakuzaStaffToolsClient;

public final class HudEditorScreen extends Screen {
    private final Screen parent;

    private String dragging = null;
    private int dragOffsetX;
    private int dragOffsetY;
    private boolean wasDraggingLastFrame = false;

    private static final int STAFF_W = 165;
    private static final int STAFF_H = 70;

    private static final int SPY_H = 70;

    private static final int WATCHDOG_W = 145;
    private static final int WATCHDOG_H = 60;

    public HudEditorScreen(Screen parent) {
        super(Text.literal("HUD Editor"));
        this.parent = parent;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        updateDragging(mouseX, mouseY);

        context.fill(0, 0, this.width, this.height, 0xAA000000);

        drawBox(
                context,
                "Staff Online",
                YakuzaStaffToolsClient.CONFIG.staffHudX,
                YakuzaStaffToolsClient.CONFIG.staffHudY,
                STAFF_W,
                STAFF_H,
                0xFF55FFFF
        );

        drawBox(
                context,
                "Spy Chat",
                YakuzaStaffToolsClient.CONFIG.spyPanelX,
                YakuzaStaffToolsClient.CONFIG.spyPanelY,
                YakuzaStaffToolsClient.CONFIG.spyPanelWidth,
                SPY_H,
                0xFFFFAA00
        );

        drawBox(
                context,
                "Yakuza Watchdog",
                YakuzaStaffToolsClient.CONFIG.watchdogHudX,
                YakuzaStaffToolsClient.CONFIG.watchdogHudY,
                WATCHDOG_W,
                WATCHDOG_H,
                0xFFFF5555
        );

        context.drawCenteredTextWithShadow(
                this.textRenderer,
                "Drag HUD boxes. Release mouse to save. ESC to exit.",
                this.width / 2,
                20,
                0xFFFFFFFF
        );

        super.render(context, mouseX, mouseY, delta);
    }

    private void drawBox(DrawContext context, String title, int x, int y, int w, int h, int color) {
        context.fill(x - 3, y - 3, x + w, y + h, 0x90000000);
        context.drawText(this.textRenderer, title, x, y, color, true);
        context.drawText(this.textRenderer, "Drag me", x, y + 14, 0xFFFFFFFF, true);
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        int mouseX = (int) click.x();
        int mouseY = (int) click.y();

        if (inside(mouseX, mouseY, YakuzaStaffToolsClient.CONFIG.staffHudX, YakuzaStaffToolsClient.CONFIG.staffHudY, STAFF_W, STAFF_H)) {
            startDrag("staff", mouseX, mouseY, YakuzaStaffToolsClient.CONFIG.staffHudX, YakuzaStaffToolsClient.CONFIG.staffHudY);
            return true;
        }

        if (inside(mouseX, mouseY, YakuzaStaffToolsClient.CONFIG.spyPanelX, YakuzaStaffToolsClient.CONFIG.spyPanelY, YakuzaStaffToolsClient.CONFIG.spyPanelWidth, SPY_H)) {
            startDrag("spy", mouseX, mouseY, YakuzaStaffToolsClient.CONFIG.spyPanelX, YakuzaStaffToolsClient.CONFIG.spyPanelY);
            return true;
        }

        if (inside(mouseX, mouseY, YakuzaStaffToolsClient.CONFIG.watchdogHudX, YakuzaStaffToolsClient.CONFIG.watchdogHudY, WATCHDOG_W, WATCHDOG_H)) {
            startDrag("watchdog", mouseX, mouseY, YakuzaStaffToolsClient.CONFIG.watchdogHudX, YakuzaStaffToolsClient.CONFIG.watchdogHudY);
            return true;
        }

        return false;
    }

    private void startDrag(String id, int mouseX, int mouseY, int hudX, int hudY) {
        dragging = id;
        dragOffsetX = mouseX - hudX;
        dragOffsetY = mouseY - hudY;
        wasDraggingLastFrame = true;
    }

    private void updateDragging(int mouseX, int mouseY) {
        if (dragging == null || this.client == null) {
            return;
        }

        long windowHandle = this.client.getWindow().getHandle();
        boolean leftMouseDown = GLFW.glfwGetMouseButton(windowHandle, GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;

        if (!leftMouseDown) {
            if (wasDraggingLastFrame) {
                YakuzaStaffToolsClient.CONFIG.save();
            }

            dragging = null;
            wasDraggingLastFrame = false;
            return;
        }

        int newX = Math.max(2, mouseX - dragOffsetX);
        int newY = Math.max(2, mouseY - dragOffsetY);

        switch (dragging) {
            case "staff" -> {
                YakuzaStaffToolsClient.CONFIG.staffHudX = newX;
                YakuzaStaffToolsClient.CONFIG.staffHudY = newY;
            }
            case "spy" -> {
                YakuzaStaffToolsClient.CONFIG.spyPanelX = newX;
                YakuzaStaffToolsClient.CONFIG.spyPanelY = newY;
            }
            case "watchdog" -> {
                YakuzaStaffToolsClient.CONFIG.watchdogHudX = newX;
                YakuzaStaffToolsClient.CONFIG.watchdogHudY = newY;
            }
        }

        wasDraggingLastFrame = true;
    }

    private boolean inside(int mouseX, int mouseY, int x, int y, int w, int h) {
        return mouseX >= x - 3
                && mouseX <= x + w
                && mouseY >= y - 3
                && mouseY <= y + h;
    }

    @Override
    public void close() {
        YakuzaStaffToolsClient.CONFIG.save();

        if (this.client != null) {
            this.client.setScreen(parent);
        }
    }
}