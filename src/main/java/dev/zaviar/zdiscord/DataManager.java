package dev.zaviar.zdiscord;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

/*
 * This class is responsible for loading and storing values from
 * the configuration file to be used again, rather than reading each time.
 */
public class DataManager {

    public boolean PRESENT_ON_JOIN = false;
    public List<String> TEXT;
    public String TEXT_HOVER;
    public String TEXT_CLICK;
    public boolean SOUND_ENABLED = false;
    public String SOUND_VALUE;

    public DataManager(zDiscord main) {
        loadConfiguration(main);
    }

    public void reload() {
        zDiscord.getInstance().getLogger().info("Reloading the plugin...");
        zDiscord.getInstance().reloadConfig();
        loadConfiguration(zDiscord.getInstance());
        zDiscord.getInstance().getLogger().info("The plugin has been reloaded.");
    }

    // TODO Cleanup
    private void loadConfiguration(zDiscord main) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(main.getDataFolder(), "config.yml"));

        if (config.get("message.presentOnJoin") != null) {
            PRESENT_ON_JOIN = config.getBoolean("message.presentOnJoin");
        }

        if (config.get("message.text") != null) {
            TEXT = config.getStringList("message.text");
        } else {
            TEXT = null;
        }

        if (config.get("message.textHover") != null) {
            TEXT_HOVER = config.getString("message.textHover");
        } else {
            TEXT_HOVER = null;
        }

        if (config.get("message.textClick") != null) {
            TEXT_CLICK = config.getString("message.textClick");
        } else {
            TEXT_CLICK = null;
        }

        if (config.get("sound.enabled") != null) {
            SOUND_ENABLED = config.getBoolean("sound.enabled");
        }

        if (config.get("sound.value") != null) {
            SOUND_VALUE = config.getString("sound.value");
        } else {
            SOUND_VALUE = null;
        }
    }

}