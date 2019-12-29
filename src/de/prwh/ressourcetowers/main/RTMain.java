package de.prwh.ressourcetowers.main;

import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import de.prwh.ressourcetowers.events.EventListenerHandler;
import de.prwh.ressourcetowers.main.commands.CommandAddTower;
import de.prwh.ressourcetowers.main.commands.CommandChunkTower;
import de.prwh.ressourcetowers.main.commands.CommandInfo;
import de.prwh.ressourcetowers.main.commands.CommandListTower;
import de.prwh.ressourcetowers.main.commands.CommandReloadTowerConfig;
import de.prwh.ressourcetowers.main.commands.CommandRemoveTower;
import de.prwh.ressourcetowers.main.commands.CommandSaveTowerList;
import de.prwh.ressourcetowers.main.commands.CommandSpawnTower;
import de.prwh.ressourcetowers.towers.TowerLocation;

public class RTMain extends JavaPlugin {

	BukkitScheduler autoSave = getServer().getScheduler();
	BukkitScheduler oreSpawn = getServer().getScheduler();

	private CreateConfig cfg = new CreateConfig();
	private TowerLocation tlh = TowerLocation.getInstance();
	public static final String PLUGINID = "ressourcetowers";
	private static final Logger log = LogManager.getLogManager().getLogger(PLUGINID.toUpperCase());

	public void onEnable() {

		if (getServer().getPluginManager().getPlugin("Factions") == null || getServer().getPluginManager().getPlugin("MassiveCore") == null) {
			getServer().getConsoleSender().sendMessage(ChatColor.RED + "[RessourceTowers] Plugin MassiveCore and Factions are Missing. Disabling RessourceTowers!");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}

		cfg.create(this);
		getServer().getPluginManager().registerEvents(new EventListenerHandler(), this);
		tlh.setFilePath(getDataFolder().getAbsolutePath());
		tlh.loadTowerList();
		startAutoSave();
		startOreSpawn();

		getCommand("rt").setExecutor(new CommandInfo(this));
		getCommand("addTower").setExecutor(new CommandAddTower());
		getCommand("spawnTower").setExecutor(new CommandSpawnTower());
		getCommand("listTower").setExecutor(new CommandListTower());
		getCommand("removeTower").setExecutor(new CommandRemoveTower());
		getCommand("chunkTower").setExecutor(new CommandChunkTower());
		getCommand("saveTowerList").setExecutor(new CommandSaveTowerList());
		getCommand("reloadTowerConfig").setExecutor(new CommandReloadTowerConfig(this, cfg));
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
		
		oreSpawn.cancelAllTasks();
		autoSave.cancelAllTasks();
		
		cfg.getConfig();
		
		startAutoSave();
		startOreSpawn();
	}

	public void onDisable() {
		if (getServer().getPluginManager().getPlugin("Factions") != null || getServer().getPluginManager().getPlugin("MassiveCore") != null) {
			tlh.saveTowerList();
		}
	}

	public static Logger getLoggerMain() {
		return log;
	}
}
