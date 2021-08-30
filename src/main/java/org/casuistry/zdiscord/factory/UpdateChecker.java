package org.casuistry.zdiscord.factory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.casuistry.zdiscord.zDiscord;

import net.md_5.bungee.api.ChatColor;

/*
 * This class is responsible for checking if an update
 * is available and notifying server operators to update to the latest release.
 */
public class UpdateChecker {

	private int resourceId = 63241;
	public static boolean usingLatest = true;

	public UpdateChecker(final zDiscord main) {
		getVersion(new Consumer<String>() {
			public void accept(String version) {
				if (!main.getDescription().getVersion().equalsIgnoreCase(version)) {
					usingLatest = false;
				}
				main.getLogger().info(getUpdateMessage());
			}
		});
	}

	public void getVersion(final Consumer<String> consumer) {
		Bukkit.getScheduler().runTaskAsynchronously(zDiscord.getInstance(), () -> {
			try (InputStream inputStream = new URL(
					"https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId).openStream();
					Scanner scanner = new Scanner(inputStream)) {
				if (scanner.hasNext()) {
					consumer.accept(scanner.next());
				}
			} catch (IOException exception) {
				zDiscord.logger.warning("Failed to check for updates.");
			}
		});
	}

	public static String getUpdateMessage() {
		if (usingLatest) {
			return ChatColor.GOLD + "You are using the latest version of zDiscord!";
		}
		return ChatColor.RED
				+ "zDiscord is outdated! Please download the latest release at: https://www.spigotmc.org/resources/https://www.spigotmc.org/resources/63241/";
	}

}