package org.casuistry.zdiscord.commands;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.casuistry.zdiscord.zDiscord;
import org.casuistry.zdiscord.factory.DataManager;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

/*
 * This class is responsible for 
 * handling the /(z)discord command.
 */
public class DiscordCommand implements CommandExecutor {

	@SuppressWarnings("deprecation") // Deprecated to allow support for 1.8.8-1.15.2
	public boolean onCommand(CommandSender sender, Command cmd, String lab, String[] args) {
		DataManager dm = zDiscord.getInstance().getDataManager();
		if (args.length == 1 && args[0].equalsIgnoreCase("reload") && sender.isOp()) {
			sender.sendMessage(ChatColor.GRAY + "Reloading zDiscord, a full restart is recommended...");
			dm.reload();
			sender.sendMessage(ChatColor.GREEN + "zDiscord has been reloaded!");
			return true;
		}

		if (dm.TEXT == null) {
			return false;
		}

		if (!(sender instanceof Player)) {
			for (String textLine : dm.TEXT) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', textLine));
			}
			return true;
		}
		Player player = (Player) sender;

		for (String textLine : dm.TEXT) {
			TextComponent chat = new TextComponent(ChatColor.translateAlternateColorCodes('&', textLine));
			if (dm.TEXT_HOVER != null) {
				chat.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
						new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', dm.TEXT_HOVER)).create()));
			}

			if (dm.TEXT_CLICK != null) {
				chat.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, dm.TEXT_CLICK));
			}
			player.spigot().sendMessage(chat);
		}

		if (dm.SOUND_ENABLED && dm.SOUND_VALUE != null) {
			Sound sound = null;
			try {
				sound = Sound.valueOf(dm.SOUND_VALUE);
			} catch (IllegalArgumentException e) {
			}

			if (sound != null) {
				player.playSound(player.getLocation(), sound, 0.4f, 1);
			}
		}
		return true;
	}
}