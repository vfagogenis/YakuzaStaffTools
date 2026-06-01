package oscp.fagogenisv.yakuzastafftools.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import oscp.fagogenisv.yakuzastafftools.cheatsheet.BanRule;

public final class PunishmentConfirmScreen extends Screen {
    private static final int BG = 0xDD000000;
    private static final int TITLE_COLOR = 0xFFFF5555;
    private static final int TEXT_COLOR = 0xFFFFFFFF;
    private static final int COMMAND_COLOR = 0xFFFFAA00;

    private final Screen parent;
    private final String target;
    private final BanRule rule;
    private final int offenseLevel;
    private final String command;

    public PunishmentConfirmScreen(Screen parent, String target, BanRule rule, int offenseLevel, String command) {
        super(Text.literal("Confirm Punishment"));
        this.parent = parent;
        this.target = target;
        this.rule = rule;
        this.offenseLevel = offenseLevel;
        this.command = command;
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int y = this.height - 42;

        addDrawableChild(ButtonWidget.builder(Text.literal("CONFIRM"), button -> {
                    sendCommand();
                    close();
                })
                .dimensions(centerX - 105, y, 100, 20)
                .build());

        addDrawableChild(ButtonWidget.builder(Text.literal("Cancel"), button -> close())
                .dimensions(centerX + 5, y, 100, 20)
                .build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.fill(0, 0, this.width, this.height, BG);

        int centerX = this.width / 2;
        int y = 45;

        context.drawCenteredTextWithShadow(
                this.textRenderer,
                Text.literal("Confirm Punishment"),
                centerX,
                20,
                TITLE_COLOR
        );

        context.drawCenteredTextWithShadow(
                this.textRenderer,
                Text.literal("Target: " + target),
                centerX,
                y,
                TEXT_COLOR
        );

        context.drawCenteredTextWithShadow(
                this.textRenderer,
                Text.literal("Rule: " + rule.rule()),
                centerX,
                y + 18,
                TEXT_COLOR
        );

        context.drawCenteredTextWithShadow(
                this.textRenderer,
                Text.literal("Offense Level: " + offenseLevel),
                centerX,
                y + 36,
                TEXT_COLOR
        );

        context.drawCenteredTextWithShadow(
                this.textRenderer,
                Text.literal("Command: /" + command),
                centerX,
                y + 62,
                COMMAND_COLOR
        );

        context.drawCenteredTextWithShadow(
                this.textRenderer,
                Text.literal("Only confirm if you have valid evidence."),
                centerX,
                y + 92,
                0xFFFFAA00
        );

        super.render(context, mouseX, mouseY, delta);
    }

    private void sendCommand() {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player == null || client.player.networkHandler == null) {
            return;
        }

        client.player.networkHandler.sendChatCommand(command);
    }

    @Override
    public void close() {
        if (this.client != null) {
            this.client.setScreen(parent);
        }
    }
}