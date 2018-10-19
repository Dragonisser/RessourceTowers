package de.prwh.ressourcetowers.main.commands;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.prwh.ressourcetowers.towers.SerializableLocation;
import de.prwh.ressourcetowers.towers.TowerInfo;
import de.prwh.ressourcetowers.towers.TowerInfo.TowerType;
import de.prwh.ressourcetowers.towers.TowerLocation;

public class CommandAddTower implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
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
					} else {
						sender.sendMessage(ChatColor.RED + "[RessourceTowers]" + ChatColor.WHITE + " Chunk already contains a tower ");
					}
					return true;

				} catch (Exception e) {

					sender.sendMessage(ChatColor.RED + "[RessourceTowers]" + ChatColor.WHITE + " Invalid TowerType: " + Arrays.asList(TowerType.values()));
					e.printStackTrace();
				}

			} else {
				sender.sendMessage(ChatColor.RED + "[RessourceTowers]" + ChatColor.WHITE + " Missing argument TowerType");
			}
		}
		return false;
	}
}
