package de.prwh.ressourcetowers.main;

import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import de.prwh.ressourcetowers.events.EventListenerHandler;
import de.prwh.ressourcetowers.main.commands.CommandHelper;
import de.prwh.ressourcetowers.towers.TowerHelper;

public class RTMain extends JavaPlugin {

	BukkitScheduler autoSave = getServer().getScheduler();
	BukkitScheduler oreSpawn = getServer().getScheduler();

	private CreateConfig cfg = new CreateConfig();
	private TowerHelper tlh = TowerHelper.getInstance();
	public static final String PLUGINID = "ressourcetowers";
	private static final Logger log = LogManager.getLogManager().getLogger(PLUGINID.toUpperCase());

	public void onEnable() {

		if (getServer().getPluginManager().getPlugin("FactionsX") == null) {
			getServer().getConsoleSender().sendMessage(ChatColor.RED
					+ "[RessourceTowers] Plugin FactionsX is Missing!");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		cfg.create(this);
		getServer().getPluginManager().registerEvents(new EventListenerHandler(), this);
		tlh.setFilePath(getDataFolder().getAbsolutePath());
		tlh.loadTowerList();
		startAutoSave();
		startOreSpawn();

		getCommand("rt").setExecutor(new CommandHelper(this));
	}

	private void startAutoSave() {
		autoSave.scheduleSyncRepeatingTask(this, new Runnable() {

			@Override
			public void run() {
				tlh.saveTowerList();
			}
		}, 0, cfg.getConfig().getInt("autoSaveTime") * 60 * 20);
	}

	private void startOreSpawn() {
		int time = cfg.getConfig().getBoolean("hardMode") ? 5 : 1;

		oreSpawn.scheduleSyncRepeatingTask(this, new Runnable() {

			@Override
			public void run() {
				if (!tlh.getMap().isEmpty())
					tlh.spawnOres();
			}
		}, 0, (cfg.getConfig().getInt("oreSpawnTime") * 60 * 20) * time);
	}

	public void restartScheduler() {

		oreSpawn.cancelTasks(this);
		autoSave.cancelTasks(this);
		
		startAutoSave();
		startOreSpawn();
	}
	
	public void reloadPlugin() {
		cfg.reloadConfig();
		restartScheduler();
	}

	public void onDisable() {
		if (getServer().getPluginManager().getPlugin("FactionsX") != null) {
			tlh.saveTowerList();
		}
	}

	public static Logger getLoggerMain() {
		return log;
	}
	
	public FileConfiguration getConfig() {
		return cfg.getConfig();
	}
	
	public static void sendToConsole(String message) {
		RTMain.getPlugin(RTMain.class).getServer().getConsoleSender().sendMessage(ChatColor.RED + "[RessourceTowers] " + ChatColor.GREEN + message);
	}
}
