package org.casuistry.zdiscord;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.casuistry.zdiscord.commands.DiscordCommand;
import org.casuistry.zdiscord.factory.DataManager;
import org.casuistry.zdiscord.factory.UpdateChecker;
import org.casuistry.zdiscord.listeners.PlayerListener;

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

/*
 * zDiscord 1.4.0
 * An easily configurable /discord command for your Minecraft server
 * @author Casuistry
 */
public class zDiscord extends JavaPlugin {

	private static zDiscord instance;
	public static Logger logger;

	private DataManager dataManager;

	@Override
	public void onEnable() {
		logger = getLogger();
		logger.info("The plugin is now loading...");

		instance = this;

		this.getConfig().options().copyDefaults();
		saveDefaultConfig();

		registerCommands();
		registerListeners();

		this.dataManager = new DataManager(this);
		new UpdateChecker(this);

		logger.info("Created by Casuistry. Thank you for using the plugin! :)");
		logger.info("The plugin has started. Running version; " + getDescription().getVersion());
	}

	@Override
	public void onDisable() {
		instance = null;
	}

	private void registerCommands() {
		getCommand("discord").setExecutor(new DiscordCommand());
	}

	private void registerListeners() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new PlayerListener(), this);
	}

	public static zDiscord getInstance() {
		return instance;
	}

	public DataManager getDataManager() {
		return dataManager;
	}

}