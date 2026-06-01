package oscp.fagogenisv.yakuzastafftools.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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

        if (yakuzastafftools$isRealAllowedServer(client)) {
            YakuzaStaffToolsClient.KEYBINDS.tick(client);
        }
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

            YakuzaStaffToolsClient.LOGGER.info("YST session data cleared on fresh join.");

            yakuzastafftools$wasDisconnected = false;

            if (yakuzastafftools$isRealAllowedServer(client)) {
                client.player.networkHandler.sendChatCommand("staff");
            }
        }
    }

    @Unique
    private boolean yakuzastafftools$isRealAllowedServer(MinecraftClient client) {
        if (client == null || client.player == null || client.getNetworkHandler() == null) {
            return false;
        }

        ServerInfo serverInfo = client.getCurrentServerEntry();

        if (serverInfo == null || serverInfo.address == null) {
            return false;
        }

        String address = serverInfo.address.toLowerCase();

        return address.contains("advancius")
                || address.contains("shinigami");
    }
}