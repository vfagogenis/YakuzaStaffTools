package oscp.fagogenisv.yakuzastafftools.screen;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import oscp.fagogenisv.yakuzastafftools.YakuzaStaffToolsClient;
import oscp.fagogenisv.yakuzastafftools.screen.HudEditorScreen;

public final class YakuzaSettingsScreen extends Screen {
    private final Screen parent;

    public YakuzaSettingsScreen(Screen parent) {
        super(Text.literal("Yakuza Staff Tools"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int x = this.width / 2 - 100;
        int y = this.height / 2 - 80;

        addDrawableChild(toggleButton(x, y, "Mod", YakuzaStaffToolsClient.CONFIG.enableMod, button -> {
            YakuzaStaffToolsClient.CONFIG.enableMod = !YakuzaStaffToolsClient.CONFIG.enableMod;
            saveAndReload();
        }));

        addDrawableChild(toggleButton(x, y + 24, "Staff HUD", YakuzaStaffToolsClient.CONFIG.enableStaffHud, button -> {
            YakuzaStaffToolsClient.CONFIG.enableStaffHud = !YakuzaStaffToolsClient.CONFIG.enableStaffHud;
            saveAndReload();
        }));

        addDrawableChild(toggleButton(x, y + 48, "Spy HUD", YakuzaStaffToolsClient.CONFIG.enableSpyChatPanel, button -> {
            YakuzaStaffToolsClient.CONFIG.enableSpyChatPanel = !YakuzaStaffToolsClient.CONFIG.enableSpyChatPanel;
            saveAndReload();
        }));

        addDrawableChild(toggleButton(x, y + 72, "Watchdog HUD", YakuzaStaffToolsClient.CONFIG.enableWatchdogHud, button -> {
            YakuzaStaffToolsClient.CONFIG.enableWatchdogHud = !YakuzaStaffToolsClient.CONFIG.enableWatchdogHud;
            saveAndReload();
        }));

        addDrawableChild(toggleButton(x, y + 96, "Numpad Actions", YakuzaStaffToolsClient.CONFIG.enableNumpadActions, button -> {
            YakuzaStaffToolsClient.CONFIG.enableNumpadActions = !YakuzaStaffToolsClient.CONFIG.enableNumpadActions;
            saveAndReload();
        }));

        addDrawableChild(ButtonWidget.builder(Text.literal("Edit HUD Positions"), button -> {
            if (this.client != null) {
                this.client.setScreen(new HudEditorScreen(this));
            }
        }).dimensions(x, y + 120, 200, 20).build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Done"), button -> close())
                .dimensions(x, y + 150, 200, 20)
                .build());
    }

    private ButtonWidget toggleButton(int x, int y, String label, boolean enabled, ButtonWidget.PressAction action) {
        return ButtonWidget.builder(Text.literal(label + ": " + (enabled ? "ON" : "OFF")), action)
                .dimensions(x, y, 200, 20)
                .build();
    }

    private void saveAndReload() {
        YakuzaStaffToolsClient.CONFIG.save();

        if (this.client != null) {
            this.client.setScreen(new YakuzaSettingsScreen(parent));
        }
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(parent);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, this.width, this.height, 0xAA000000);

        context.drawCenteredTextWithShadow(
                this.textRenderer,
                this.title,
                this.width / 2,
                30,
                0xFFFFAA00
        );

        super.render(context, mouseX, mouseY, delta);
    }
}