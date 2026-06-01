package oscp.fagogenisv.yakuzastafftools.mixin;

import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import oscp.fagogenisv.yakuzastafftools.screen.YakuzaSettingsScreen;

@Mixin(GameMenuScreen.class)
public abstract class GameMenuScreenMixin extends Screen {

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
}