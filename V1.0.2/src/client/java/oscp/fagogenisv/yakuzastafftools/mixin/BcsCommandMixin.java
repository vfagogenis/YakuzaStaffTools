package oscp.fagogenisv.yakuzastafftools.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import oscp.fagogenisv.yakuzastafftools.screen.BanCheatsheetScreen;
import oscp.fagogenisv.yakuzastafftools.screen.PunishmentGuiScreen;

@Mixin(ClientPlayNetworkHandler.class)
public abstract class BcsCommandMixin {

    @Inject(method = "sendChatCommand", at = @At("HEAD"), cancellable = true)
    private void yakuzastafftools$handleClientCommands(String command, CallbackInfo ci) {
        if (command == null || command.isBlank()) {
            return;
        }

        String clean = command.trim();
        String lower = clean.toLowerCase();

        if (lower.equals("bcs") || lower.equals("bancheatsheet")) {
            ci.cancel();

            MinecraftClient client = MinecraftClient.getInstance();
            client.execute(() -> {
                if (client.player != null) {
                    client.setScreen(new BanCheatsheetScreen(client.currentScreen));
                }
            });
            return;
        }

        if (lower.startsWith("bgui ") || lower.startsWith("punish ")) {
            ci.cancel();

            String[] parts = clean.split("\\s+", 2);

            if (parts.length < 2 || parts[1].isBlank()) {
                return;
            }

            String target = parts[1].trim();

            MinecraftClient client = MinecraftClient.getInstance();
            client.execute(() -> {
                if (client.player != null) {
                    client.setScreen(new PunishmentGuiScreen(client.currentScreen, target));
                }
            });
        }
    }
}