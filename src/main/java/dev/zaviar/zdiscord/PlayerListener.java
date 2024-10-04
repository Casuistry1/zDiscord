package dev.zaviar.zdiscord;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

/*
 * Responsible for sending discord message upon join.
 * This class also notifies updates.
 */
public class PlayerListener implements Listener {

    @SuppressWarnings("deprecation")
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerJoinEvent e) {
        final Player player = e.getPlayer();

        DataManager dm = zDiscord.getInstance().getDataManager();

        if (dm.PRESENT_ON_JOIN) {
            if (dm.TEXT != null) {

                for (String textLine : dm.TEXT) {
                    TextComponent chat = new TextComponent(ChatColor.translateAlternateColorCodes('&', textLine));
                    if (dm.TEXT_HOVER != null) {
                        chat.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', dm.TEXT_HOVER))
                                        .create()));
                    }

                    if (dm.TEXT_CLICK != null) {
                        chat.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, dm.TEXT_CLICK));
                    }
                    player.spigot().sendMessage(chat);
                }
            }
        }
    }

}