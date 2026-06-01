package oscp.fagogenisv.yakuzastafftools.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import oscp.fagogenisv.yakuzastafftools.YakuzaStaffToolsClient;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientTickMixin {

    @Unique
    private boolean yakuzastafftools$wasDisconnected = true;

    @Inject(method = "tick", at = @At("TAIL"))
    private void yakuzastafftools$tick(CallbackInfo ci) {
        MinecraftClient client = (MinecraftClient) (Object) this;

        yakuzastafftools$clearOnlyOnFreshJoin(client);
        YakuzaStaffToolsClient.KEYBINDS.tick(client);
    }

    @Unique
    private void yakuzastafftools$clearOnlyOnFreshJoin(MinecraftClient client) {
        boolean connected = client.player != null
                && client.world != null
                && client.getNetworkHandler() != null;

        if (!connected) {
            yakuzastafftools$wasDisconnected = true;
            return;
        }

        if (yakuzastafftools$wasDisconnected) {
            YakuzaStaffToolsClient.WATCHDOG_MANAGER.clear();
            YakuzaStaffToolsClient.SPY_CHAT_MANAGER.clear();
            YakuzaStaffToolsClient.STAFF_TRACKER.clear();

            ServerInfo serverInfo = client.getCurrentServerEntry();
            String server = serverInfo != null ? serverInfo.address : "singleplayer";

            YakuzaStaffToolsClient.LOGGER.info("YST session data cleared on fresh join: {}", server);

            yakuzastafftools$wasDisconnected = false;

            client.player.networkHandler.sendChatCommand("staff");
        }
    }
}