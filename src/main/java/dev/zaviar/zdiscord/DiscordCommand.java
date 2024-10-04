package dev.zaviar.zdiscord;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DiscordCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1 && args[0].equalsIgnoreCase("reload") && sender.isOp()) {
            sender.sendMessage(ChatColor.GRAY + "Reloading zDiscord...");
            ConfigManager.getInstance().initialise();
            sender.sendMessage(ChatColor.GREEN + "zDiscord has been reloaded!");
            return true;
        }

        ConfigManager.getInstance().play(sender);
        return true;
    }
}