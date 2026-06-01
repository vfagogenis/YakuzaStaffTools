package oscp.fagogenisv.yakuzastafftools;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import oscp.fagogenisv.yakuzastafftools.chat.ChatTabManager;
import oscp.fagogenisv.yakuzastafftools.chat.SpyChatManager;
import oscp.fagogenisv.yakuzastafftools.chat.WatchdogManager;
import oscp.fagogenisv.yakuzastafftools.config.YakuzaStaffConfig;
import oscp.fagogenisv.yakuzastafftools.hud.ChatTabsHud;
import oscp.fagogenisv.yakuzastafftools.hud.SpyChatHud;
import oscp.fagogenisv.yakuzastafftools.hud.StaffOnlineHud;
import oscp.fagogenisv.yakuzastafftools.hud.WatchdogHud;
import oscp.fagogenisv.yakuzastafftools.keybinds.YakuzaKeybinds;
import oscp.fagogenisv.yakuzastafftools.staff.StaffTracker;

public final class YakuzaStaffToolsClient implements ClientModInitializer {
    public static final String MOD_ID = "yakuzastafftools";
    public static final Logger LOGGER = LoggerFactory.getLogger("Yakuza Staff Tools");

    public static final YakuzaStaffConfig CONFIG = new YakuzaStaffConfig();

    public static final WatchdogManager WATCHDOG_MANAGER = new WatchdogManager();
    public static final SpyChatManager SPY_CHAT_MANAGER = new SpyChatManager();
    public static final ChatTabManager CHAT_TAB_MANAGER = new ChatTabManager();
    public static final StaffTracker STAFF_TRACKER = new StaffTracker();

    public static final WatchdogHud WATCHDOG_HUD = new WatchdogHud();
    public static final StaffOnlineHud STAFF_ONLINE_HUD = new StaffOnlineHud();
    public static final SpyChatHud SPY_CHAT_HUD = new SpyChatHud();
    public static final ChatTabsHud CHAT_TABS_HUD = new ChatTabsHud();

    public static final YakuzaKeybinds KEYBINDS = new YakuzaKeybinds();

    @Override
    public void onInitializeClient() {
        CONFIG.load();

        WATCHDOG_MANAGER.clear();
        SPY_CHAT_MANAGER.clear();
        CHAT_TAB_MANAGER.clear();
        STAFF_TRACKER.clear();

        KEYBINDS.initialize();

        LOGGER.info("Yakuza Staff Tools loaded.");
    }
}