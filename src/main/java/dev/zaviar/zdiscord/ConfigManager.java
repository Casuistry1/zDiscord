package dev.zaviar.zdiscord;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.LinkedHashSet;

public final class ConfigManager {

    private static final ConfigManager instance = new ConfigManager();

    private final LinkedHashSet<TextComponent> components = new LinkedHashSet<>();
    private boolean soundEnabled;
    private String sound;
    private boolean presentOnJoin = false;

    public void initialise() {
        zDiscord.getInstance().reloadConfig();
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(zDiscord.getInstance().getDataFolder(), "config.yml"));

        this.components.clear();

        String textClick = config.getString("message.textClick", null);
        String textHover = config.getString("message.textHover", null);
        for (String textLine : config.getStringList("message.text")) {
            TextComponent component = new TextComponent(ChatColor.translateAlternateColorCodes('&', textLine));

            if (textClick != null) {
                component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, textClick));
            }
            if (textHover != null) {
                component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(textHover)));
            }
            this.components.add(component);
        }

        this.soundEnabled = config.getBoolean("sound.enabled", false);
        this.sound = config.getString("sound.value", null);
        this.presentOnJoin = config.getBoolean("message.presentOnJoin", false);
    }

    public void play(CommandSender commandSender) {
        for (TextComponent component : components) {
            commandSender.spigot().sendMessage(component);
        }

        // Play the sound if they are a player
        if (commandSender instanceof Player player && soundEnabled) {
            try {
                Sound sound = Sound.valueOf(this.sound);
                player.playSound(player.getLocation(), sound, 0.5f, 1f);
            } catch (Exception ignored) {
            }
        }
    }

    public boolean isPresentOnJoin() {
        return presentOnJoin;
    }

    public static ConfigManager getInstance() {
        return instance;
    }

}