package oscp.fagogenisv.yakuzastafftools.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import oscp.fagogenisv.yakuzastafftools.YakuzaStaffToolsClient;

@Mixin(InGameHud.class)
public abstract class HudRenderMixin {

    @Inject(method = "render", at = @At("TAIL"))
    private void yakuzastafftools$renderHud(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        YakuzaStaffToolsClient.WATCHDOG_HUD.render(context);
        YakuzaStaffToolsClient.STAFF_ONLINE_HUD.render(context);
        YakuzaStaffToolsClient.SPY_CHAT_HUD.render(context);
    }
}