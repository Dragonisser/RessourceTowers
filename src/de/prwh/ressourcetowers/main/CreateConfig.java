package de.prwh.ressourcetowers.main;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class CreateConfig {

	private File config_file;
	private FileConfiguration config;

	public FileConfiguration getConfig() {
		return config;
	}

	public void reloadConfig() {
		RTMain.sendToConsole("Trying to reload the config");

		config = YamlConfiguration.loadConfiguration(config_file);
		if (config != null) {
			RTMain.sendToConsole("Config reloaded successfully");
		} else {
			RTMain.sendToConsole("Failed to reload Config");
		}
	}

	public void create(RTMain plugin) {

		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
		}
		config_file = new File(plugin.getDataFolder() + File.separator + "config.yml");
		if (!config_file.exists()) {
			try {
				config_file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		config = YamlConfiguration.loadConfiguration(config_file);
		config.addDefault("autoSaveTime", 10);
		config.addDefault("oreSpawnTime", 5);
		config.addDefault("hardMode", false);
		config.addDefault("secureTowerPlacement", true);
		config.options().copyDefaults(true);

		try {
			config.save(config_file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
