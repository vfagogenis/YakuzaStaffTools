package oscp.fagogenisv.yakuzastafftools.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import oscp.fagogenisv.yakuzastafftools.YakuzaStaffToolsClient;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class YakuzaStaffConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public boolean enableMod = true;
    public boolean enableWatchdogHud = true;
    public boolean enableStaffHud = true;
    public boolean enableSpyChatPanel = true;
    public boolean enableChatTabs = true;
    public boolean enableNumpadActions = true;
    public boolean hideSpyFromVanillaChat = true;
    public boolean onlyWorkOnTargetNetworks = false;

    public int watchdogHudX = 6;
    public int watchdogHudY = 90;

    public int staffHudX = 6;
    public int staffHudY = 6;

    public int spyPanelX = 220;
    public int spyPanelY = 24;
    public int spyPanelWidth = 260;
    public int spyPanelMaxLines = 10;

    public int moveToSpecDelayTicks = 80;

    public List<String> staffUsernames = new ArrayList<>(Arrays.asList("Owner", "Admin", "Moderator"));
    public List<String> allowedServers = new ArrayList<>(Arrays.asList("advancius.net", "shinigamivalley"));

    public void load() {
        Path path = getConfigPath();

        if (!Files.exists(path)) {
            save();
            return;
        }

        try (Reader reader = Files.newBufferedReader(path)) {
            YakuzaStaffConfig loaded = GSON.fromJson(reader, YakuzaStaffConfig.class);

            if (loaded != null) {
                copyFrom(loaded);
            }
        } catch (IOException | RuntimeException exception) {
            YakuzaStaffToolsClient.LOGGER.warn("Failed to load Yakuza Staff Tools config, using defaults.", exception);
            save();
        }
    }

    public void save() {
        Path path = getConfigPath();

        try {
            Files.createDirectories(path.getParent());

            try (Writer writer = Files.newBufferedWriter(path)) {
                GSON.toJson(this, writer);
            }
        } catch (IOException exception) {
            YakuzaStaffToolsClient.LOGGER.warn("Failed to save Yakuza Staff Tools config.", exception);
        }
    }

    public Path getConfigPath() {
        return FabricLoader.getInstance().getConfigDir().resolve("yakuzastafftools.json");
    }

    private void copyFrom(YakuzaStaffConfig loaded) {
        enableMod = loaded.enableMod;
        enableWatchdogHud = loaded.enableWatchdogHud;
        enableStaffHud = loaded.enableStaffHud;
        enableSpyChatPanel = loaded.enableSpyChatPanel;
        enableChatTabs = loaded.enableChatTabs;
        enableNumpadActions = loaded.enableNumpadActions;
        hideSpyFromVanillaChat = loaded.hideSpyFromVanillaChat;
        onlyWorkOnTargetNetworks = loaded.onlyWorkOnTargetNetworks;

        watchdogHudX = loaded.watchdogHudX;
        watchdogHudY = loaded.watchdogHudY;

        staffHudX = loaded.staffHudX;
        staffHudY = loaded.staffHudY;

        spyPanelX = loaded.spyPanelX;
        spyPanelY = loaded.spyPanelY;
        spyPanelWidth = loaded.spyPanelWidth;
        spyPanelMaxLines = loaded.spyPanelMaxLines;

        moveToSpecDelayTicks = loaded.moveToSpecDelayTicks;

        staffUsernames = loaded.staffUsernames == null ? new ArrayList<>() : new ArrayList<>(loaded.staffUsernames);
        allowedServers = loaded.allowedServers == null ? new ArrayList<>() : new ArrayList<>(loaded.allowedServers);
    }
}