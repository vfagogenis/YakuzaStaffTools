package oscp.fagogenisv.yakuzastafftools.mixin;

import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import oscp.fagogenisv.yakuzastafftools.YakuzaStaffToolsClient;
import oscp.fagogenisv.yakuzastafftools.chat.ChatTab;

@Mixin(ChatHud.class)
public abstract class ChatCaptureMixin {

    @Inject(method = "addMessage(Lnet/minecraft/text/Text;)V", at = @At("HEAD"), cancellable = true)
    private void yakuzastafftools$captureChat(Text message, CallbackInfo ci) {
        if (message == null) {
            return;
        }

        if (!YakuzaStaffToolsClient.CONFIG.enableMod) {
            return;
        }

        String text = message.getString();

        // Store every message first.
        // This powers:
        // Vanilla = everything
        // Yakuza = everything except Spy
        // Watchdog = only Watchdog
        // Spy = only Spy
        YakuzaStaffToolsClient.CHAT_TAB_MANAGER.add(message);

        YakuzaStaffToolsClient.WATCHDOG_MANAGER.handleMessage(text);
        YakuzaStaffToolsClient.STAFF_TRACKER.handleMessage(text);

        boolean isSpy = YakuzaStaffToolsClient.SPY_CHAT_MANAGER.handleMessage(message);

        if (!isSpy) {
            return;
        }

        /*
         * Spy handling:
         *
         * Vanilla tab:
         * - Spy must remain visible.
         *
         * Yakuza tab:
         * - Spy must be hidden from normal chat flow.
         *
         * Spy tab:
         * - Spy is shown through the Spy tab / Spy HUD.
         */
        boolean vanillaTabActive =
                YakuzaStaffToolsClient.CONFIG.enableChatTabs
                        && YakuzaStaffToolsClient.CHAT_TAB_MANAGER.getActiveTab() == ChatTab.VANILLA;

        if (!vanillaTabActive
                && YakuzaStaffToolsClient.CONFIG.hideSpyFromVanillaChat) {
            ci.cancel();
        }
    }
}