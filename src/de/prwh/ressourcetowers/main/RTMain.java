package de.prwh.ressourcetowers.main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import de.prwh.ressourcetowers.events.EventListenerHandler;
import de.prwh.ressourcetowers.towers.TowerInfo;
import de.prwh.ressourcetowers.towers.TowerLocation;

public class RTMain extends JavaPlugin {

	TowerLocation tlh = TowerLocation.getInstance();
	public static final String PLUGINID = "ressourcetowers";
	private static final Logger log = LogManager.getLogger(PLUGINID.toUpperCase());
	
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new EventListenerHandler(), this);
		tlh.loadTowerList();
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

			if (args[0] != null && args[1] != null && args[2] != null && args[3] != null) {
				System.out.println("Trying to add tower");
				try {
					Location loc = new Location(player.getWorld(), Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]));
					tLoc.addTowerLocation(loc, new TowerInfo(args[3]));
					sender.sendMessage(args[3].toUpperCase() + " tower at x:" + args[0] + " y:" + args[1] + " z:" + args[2] + " has been added");
					return true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} else if (cmd.getName().equalsIgnoreCase("listTower")) {
			tLoc.listTowerLocations();
		} else if (cmd.getName().equalsIgnoreCase("chunkTower")) {
			Location loc = player.getLocation();
			if (tLoc.chunkContainsTower(loc)) {
				sender.sendMessage(tLoc.getTowerInChunk(loc).toString());
			} else {
				sender.sendMessage("Chunk does not contain a tower");
			}

		} else if (cmd.getName().equalsIgnoreCase("removeTower")) {
			if (args.length == 3 && args[0] != null && args[1] != null && args[2] != null) {
				System.out.println("Trying to remove tower");
				try {
					Location loc = new Location(player.getWorld(), Double.parseDouble(args[0]), Double.parseDouble(args[1]), Double.parseDouble(args[2]));
					if (tLoc.locationContainsTower(loc)) {
						sender.sendMessage(
								tLoc.getTowerInfo(loc).getTowername() + " tower at x:" + args[0] + " y:" + args[1] + " z:" + args[2] + " has been removed");
						tLoc.removeTowerLocation(loc);
					} else {
						sender.sendMessage(" Location x:" + args[0] + " y:" + args[1] + " z:" + args[2] + " does not contain a tower");
					}

					return true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (args.length == 1 && args[0] != null && args[0].toString().equals("chunk")) {
				Location loc = player.getLocation();
				if (tLoc.chunkContainsTower(loc)) {
					Location locT = tLoc.getTowerInChunk(loc);
					sender.sendMessage(tLoc.getTowerInfo(locT).getTowername() + " tower in this chunk has been removed");
					tLoc.removeTowerLocation(locT);
				} else {
					sender.sendMessage("Chunk does not contain a tower");
				}
			}
		}
		return false;
	}
	
	public static Logger getLoggerMain() {
		return log;
	}
}
