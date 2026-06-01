package oscp.fagogenisv.yakuzastafftools.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import oscp.fagogenisv.yakuzastafftools.YakuzaStaffToolsClient;
import oscp.fagogenisv.yakuzastafftools.screen.YakuzaSettingsScreen;
import oscp.fagogenisv.yakuzastafftools.util.RankUtil;

@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenMixin extends Screen {
    private static final Identifier YAKUZA_LOGO =
            Identifier.of(YakuzaStaffToolsClient.MOD_ID, "textures/gui/yakuza_logo.png");

    protected GameMenuScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void yakuzastafftools$addButton(CallbackInfo ci) {
        int x = this.width / 2 - 102;
        int y = this.height - 52;

        this.addDrawableChild(
                ButtonWidget.builder(Text.literal("Yakuza Staff Tools"), button -> {
                            if (this.client != null) {
                                this.client.setScreen(new YakuzaSettingsScreen(this));
                            }
                        })
                        .dimensions(x, y, 204, 20)
                        .build()
        );
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void yakuzastafftools$renderLogoPanel(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.player == null) {
            return;
        }

        String username = client.player.getName().getString();
        String rank = RankUtil.getRankForPlayer(username);

        int panelCenterX = this.width / 4;
        int panelCenterY = this.height / 2;

        int logoSize = 96;
        int logoX = panelCenterX - logoSize / 2;
        int logoY = panelCenterY - 90;

        context.drawTexture(
                RenderPipelines.GUI_TEXTURED,
                YAKUZA_LOGO,
                logoX,
                logoY,
                0.0F,
                0.0F,
                logoSize,
                logoSize,
                logoSize,
                logoSize
        );

        MutableText title = Text.literal("Yakuza Staff Tools")
                .setStyle(Style.EMPTY.withColor(Formatting.RED).withBold(true));

        context.drawCenteredTextWithShadow(
                this.textRenderer,
                title,
                panelCenterX,
                logoY + logoSize + 10,
                0xFFFFFFFF
        );

        MutableText rankText = Text.literal("[")
                .setStyle(Style.EMPTY.withColor(Formatting.DARK_RED).withBold(true))
                .append(Text.literal(rank).setStyle(Style.EMPTY.withColor(getRankFormatting(rank)).withBold(true)))
                .append(Text.literal("] ").setStyle(Style.EMPTY.withColor(Formatting.DARK_RED).withBold(true)))
                .append(Text.literal(username).setStyle(Style.EMPTY.withColor(Formatting.RED).withBold(true)));

        context.drawCenteredTextWithShadow(
                this.textRenderer,
                rankText,
                panelCenterX,
                logoY + logoSize + 24,
                0xFFFFFFFF
        );
    }

    private Formatting getRankFormatting(String rank) {
        String lower = rank.toLowerCase();

        if (lower.contains("owner")) {
            return Formatting.DARK_RED;
        }

        if (lower.contains("admin")) {
            return Formatting.RED;
        }

        if (lower.contains("dev")) {
            return Formatting.AQUA;
        }

        if (lower.contains("builder")) {
            return Formatting.GREEN;
        }

        if (lower.contains("jrmod") || lower.contains("jr.mod")) {
            return Formatting.LIGHT_PURPLE;
        }

        if (lower.contains("mod")) {
            return Formatting.DARK_PURPLE;
        }

        if (lower.contains("media")) {
            return Formatting.BLUE;
        }

        if (lower.contains("helper")) {
            return Formatting.YELLOW;
        }

        return Formatting.GRAY;
    }
}