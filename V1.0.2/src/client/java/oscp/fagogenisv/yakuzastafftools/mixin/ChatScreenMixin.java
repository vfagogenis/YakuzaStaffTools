package oscp.fagogenisv.yakuzastafftools.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import oscp.fagogenisv.yakuzastafftools.YakuzaStaffToolsClient;
import oscp.fagogenisv.yakuzastafftools.chat.ChatTab;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin extends Screen {

    protected ChatScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void yakuzastafftools$initChatTabs(CallbackInfo ci) {
        if (!YakuzaStaffToolsClient.CONFIG.enableMod || !YakuzaStaffToolsClient.CONFIG.enableChatTabs) {
            return;
        }

        int x = 6;
        int panelY = this.height - 170;
        int tabsY = panelY - 24;
        int tabX = x;

        for (ChatTab tab : ChatTab.values()) {
            int tabWidth = this.textRenderer.getWidth(tab.displayName()) + 22;

            this.addDrawableChild(
                    ButtonWidget.builder(Text.literal(tab.displayName()), button -> {
                                YakuzaStaffToolsClient.CHAT_TAB_MANAGER.setActiveTab(tab);
                            })
                            .dimensions(tabX, tabsY, tabWidth, 20)
                            .build()
            );

            tabX += tabWidth + 4;
        }
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void yakuzastafftools$renderChatTabs(
            DrawContext context,
            int mouseX,
            int mouseY,
            float delta,
            CallbackInfo ci
    ) {
        if (YakuzaStaffToolsClient.CONFIG.enableMod && YakuzaStaffToolsClient.CONFIG.enableChatTabs) {
            YakuzaStaffToolsClient.CHAT_TABS_HUD.render(context);
        }
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (YakuzaStaffToolsClient.CONFIG.enableMod && YakuzaStaffToolsClient.CONFIG.enableChatTabs) {
            int panelX = 6;
            int panelY = this.height - 170;
            int panelWidth = 430;
            int panelHeight = 120;

            boolean insidePanel = mouseX >= panelX - 4
                    && mouseX <= panelX + panelWidth
                    && mouseY >= panelY - 4
                    && mouseY <= panelY + panelHeight;

            if (insidePanel) {
                if (verticalAmount > 0) {
                    YakuzaStaffToolsClient.CHAT_TAB_MANAGER.scroll(3);
                } else if (verticalAmount < 0) {
                    YakuzaStaffToolsClient.CHAT_TAB_MANAGER.scroll(-3);
                }

                return true;
            }
        }

        return false;
    }
}