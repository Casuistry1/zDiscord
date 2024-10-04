package dev.zaviar.zdiscord;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Objects;

/* -------------
 * LICENSE
 * zDiscord: Copyright (C)
 * -------------
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */
public class zDiscord extends JavaPlugin implements Listener, CommandExecutor {

    private final LinkedHashSet<TextComponent> components = new LinkedHashSet<>();
    private boolean soundEnabled;
    private String sound;
    private boolean presentOnJoin = false;

    @Override
    public void onEnable() {
        this.getConfig().options().copyDefaults();
        saveDefaultConfig();

        initialise();

        Objects.requireNonNull(getCommand("discord")).setExecutor(this);
        Bukkit.getPluginManager().registerEvents(this, this);

        getLogger().info("The plugin has started!");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender.isOp() && args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            sender.sendMessage(ChatColor.GRAY + "Reloading zDiscord...");
            initialise();
            sender.sendMessage(ChatColor.GREEN + "zDiscord has been reloaded!");
        } else {
            play(sender);
        }
        return true;
    }

    // Present to the player if the config option is enabled
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (this.presentOnJoin) {
            play(event.getPlayer());
        }
    }

    private void initialise() {
        reloadConfig();
        YamlConfiguration config = YamlConfiguration.loadConfiguration(new File(getDataFolder(), "config.yml"));

        this.components.clear();

        String textClick = config.getString("message.textClick", null);
        String textHover = config.getString("message.textHover", null);
        for (String textLine : config.getStringList("message.text")) {
            TextComponent component = new TextComponent(ChatColor.translateAlternateColorCodes('&', textLine));

            if (textClick != null) {
                component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, textClick));
            }
            if (textHover != null) {
                component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.translateAlternateColorCodes('&', textHover))));
            }
            this.components.add(component);
        }

        this.soundEnabled = config.getBoolean("sound.enabled", false);
        this.sound = config.getString("sound.value", null);
        this.presentOnJoin = config.getBoolean("message.presentOnJoin", false);
    }

    private void play(CommandSender commandSender) {
        for (TextComponent component : components) {
            commandSender.spigot().sendMessage(component);
        }

        // Play the sound if they are a player
        if (soundEnabled && commandSender instanceof Player player) {
            try {
                Sound sound = Sound.valueOf(this.sound);
                player.playSound(player.getLocation(), sound, 0.5f, 1f);
            } catch (Exception ignored) {
            }
        }
    }

}