package oscp.fagogenisv.yakuzastafftools.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.screen.ChatScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import oscp.fagogenisv.yakuzastafftools.YakuzaStaffToolsClient;

@Mixin(ChatHud.class)
public abstract class ChatHudMixin {

    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void yakuzastafftools$hideVanillaChat(
            DrawContext context,
            TextRenderer textRenderer,
            int width,
            int height,
            int tick,
            boolean focused,
            boolean hidden,
            CallbackInfo ci
    ) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.currentScreen instanceof ChatScreen
                && YakuzaStaffToolsClient.CONFIG.enableMod
                && YakuzaStaffToolsClient.CONFIG.enableChatTabs) {
            ci.cancel();
        }
    }
}