package de.prwh.ressourcetowers.main;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.massivecore.ps.PS;

import de.prwh.ressourcetowers.events.EventListenerHandler;
import de.prwh.ressourcetowers.towers.SerializableLocation;
import de.prwh.ressourcetowers.towers.TowerInfo;
import de.prwh.ressourcetowers.towers.TowerInfo.TowerType;
import de.prwh.ressourcetowers.towers.TowerLocation;

public class RTMain extends JavaPlugin {

	BukkitScheduler autoSave = getServer().getScheduler();
	private File config_file;
	private FileConfiguration config;
	TowerLocation tlh = TowerLocation.getInstance();
	public static final String PLUGINID = "ressourcetowers";
	private static final Logger log = LogManager.getLogger(PLUGINID.toUpperCase());

	public void onEnable() {
		
		if (getServer().getPluginManager().getPlugin("Factions") == null && getServer().getPluginManager().getPlugin("MassiveCore") == null) {
			RTMain.getLoggerMain().info("[RessourceTowers] Plugin MassiveCore and Factions are Missing. Disabling RessourceTowers!");
			getServer().getPluginManager().disablePlugin(this);
		}	

		if (!getDataFolder().exists()) {
			getDataFolder().mkdir();
		}
		config_file = new File(getDataFolder() + File.separator + "config.yml");
		if (!config_file.exists()) {
			try {
				config_file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		config = YamlConfiguration.loadConfiguration(config_file);
		config.addDefault("#How many minutes until the towerlist gets autosaved", null);
		config.addDefault("autoSaveTime", 5);
		config.options().copyDefaults(true);

		try {
			config.save(config_file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		getServer().getPluginManager().registerEvents(new EventListenerHandler(), this);
		tlh.setFilePath(getDataFolder().getAbsolutePath());
		tlh.loadTowerList();
		startAutoSave();

	}

	private void startAutoSave() {
		autoSave.scheduleSyncRepeatingTask(this, new Runnable() {

			@Override
			public void run() {
				tlh.saveTowerList();
			}
		}, 0, config.getInt("autoSaveTime") * 60 * 20);

	}

	public void onDisable() {
		tlh.saveTowerList();

	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		Player player = null;
		TowerLocation tLoc = TowerLocation.getInstance();
		if (sender instanceof Player) {
			player = (Player) sender;
		}

		if (cmd.getName().equalsIgnoreCase("addTower")) {
			if (args.length > 0 && args[0] != null) {

				Location loc = player.getLocation().add(0, -1, 0).getBlock().getLocation();

				sender.sendMessage(ChatColor.RED + "[RessourceTowers]" + ChatColor.WHITE + " Trying to add tower");

				try {
					TowerType type = null;
					String types = args[0];
					switch (types.toUpperCase()) {
					case "IRON":
						type = TowerType.IRON;
						break;
					case "GOLD":
						type = TowerType.GOLD;
						break;
					case "REDSTONE":
						type = TowerType.REDSTONE;
						break;
					case "DIAMOND":
						type = TowerType.DIAMOND;
						break;
					case "LAPIS":
						type = TowerType.LAPIS;
						break;
					}

					if (!tLoc.chunkContainsTower(loc)) {
						tLoc.addTowerLocation(new SerializableLocation(loc), new TowerInfo(type));
						sender.sendMessage(ChatColor.RED + "[RessourceTowers] " + ChatColor.WHITE + type.getTowerName() + " at x:" + loc.getBlockX() + " y:"
								+ loc.getBlockY() + " z:" + loc.getBlockZ() + " has been added");
						return true;
					} else {
						sender.sendMessage(ChatColor.RED + "[RessourceTowers]" + ChatColor.WHITE + " Chunk already contains a tower ");
					}

				} catch (Exception e) {

					sender.sendMessage(ChatColor.RED + "[RessourceTowers]" + ChatColor.WHITE + " Invalid TowerType: " + Arrays.asList(TowerType.values()));
					e.printStackTrace();
				}

			} else {
				sender.sendMessage(ChatColor.RED + "[RessourceTowers]" + ChatColor.WHITE + " Missing argument TowerType");
			}

		} else if (cmd.getName().equalsIgnoreCase("listTower")) {
			tLoc.listTowerLocations(sender);
		} else if (cmd.getName().equalsIgnoreCase("chunkTower")) {
			Location loc = (Location) player.getLocation();
			if (tLoc.chunkContainsTower(loc)) {
				sender.sendMessage(tLoc.getTowerInChunk(loc).toString());
				sender.sendMessage(ChatColor.RED + "[RessourceTowers]" + ChatColor.WHITE + " Chunk does contain a tower");
			} else {
				sender.sendMessage(ChatColor.RED + "[RessourceTowers]" + ChatColor.WHITE + " Chunk does not contain a tower");
			}

		} else if (cmd.getName().equalsIgnoreCase("removeTower")) {
			if (args.length == 0) {
				sender.sendMessage(ChatColor.RED + "[RessourceTowers]" + ChatColor.WHITE + " Trying to remove tower");

				Location loc = player.getLocation().add(0, -1, 0).getBlock().getLocation();
				try {
					if (tLoc.locationContainsTower(loc)) {
						sender.sendMessage(ChatColor.RED + "[RessourceTowers] " + ChatColor.WHITE + tLoc.getTowerInfo(loc).getTowername() + " tower at x:"
								+ loc.getBlockX() + " y:" + loc.getBlockY() + " z:" + loc.getBlockZ() + " has been removed");
						tLoc.removeTowerLocation(loc);

						/*
						 * Unclaim faction chunk with tower in it
						 */
						Faction faction = FactionColl.get().getNone();

						PS chunk = PS.valueOf(loc);
						BoardColl.get().setFactionAt(chunk, faction);

					} else {
						sender.sendMessage(ChatColor.RED + "[RessourceTowers]" + ChatColor.WHITE + " Location x:" + loc.getBlockX() + " y:" + loc.getBlockY()
								+ " z:" + loc.getBlockZ() + " does not contain a tower");
					}

					return true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (args.length == 1 && args[0] != null && args[0].toString().equals("all")) {
				try {
					/*
					 * Unclaim faction chunk with tower in it
					 */
					for (SerializableLocation loc : tlh.getMap().keySet()) {
						Faction faction = FactionColl.get().getNone();

						PS chunk = PS.valueOf(loc.toLocation());
						BoardColl.get().setFactionAt(chunk, faction);
					}
					tlh.removeAllTowers();

					sender.sendMessage(ChatColor.RED + "[RessourceTowers]" + ChatColor.WHITE + " All towers have been removed");
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				sender.sendMessage(ChatColor.RED + "[RessourceTowers]" + ChatColor.WHITE + " /removetower , /removetower all");
			}
		}
		return false;
	}

	public static Logger getLoggerMain() {
		return log;
	}
}
