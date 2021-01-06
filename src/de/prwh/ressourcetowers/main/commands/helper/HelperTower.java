package de.prwh.ressourcetowers.main.commands.helper;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import de.prwh.ressourcetowers.towers.SerializableLocation;
import de.prwh.ressourcetowers.towers.TowerInfo;
import de.prwh.ressourcetowers.towers.TowerInfo.TowerType;
import de.prwh.ressourcetowers.towers.TowerLocation;
import net.prosavage.factionsx.manager.FactionManager;

public class HelperTower {

	public static boolean AddTower(Location loc, CommandSender sender, String towertype) {

		TowerLocation tLoc = TowerLocation.getInstance();
		sender.sendMessage(ChatColor.RED + "[RessourceTowers]" + ChatColor.WHITE + " Trying to add tower");

		try {
			TowerType type = null;
			String types = towertype;
			switch (types.toUpperCase()) {
			case "COAL":
				type = TowerType.COAL;
				break;
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
			case "EMERALD":
				type = TowerType.EMERALD;
				break;
			case "LAPIS":
				type = TowerType.LAPIS;
				break;
			case "QUARTZ":
				type = TowerType.QUARTZ;
				break;
			default:
				sender.sendMessage(ChatColor.RED + "[RessourceTowers]" + ChatColor.WHITE + " Invalid TowerType: "
						+ Arrays.asList(TowerType.values()));
			}

			if (!tLoc.chunkContainsTower(loc)) {
				tLoc.addTowerLocation(new SerializableLocation(loc),
						new TowerInfo(type, FactionManager.INSTANCE.getWilderness().getTag()));
				sender.sendMessage(ChatColor.RED + "[RessourceTowers] " + ChatColor.WHITE + type.getTowerName()
						+ " at x:" + loc.getBlockX() + " y:" + loc.getBlockY() + " z:" + loc.getBlockZ()
						+ " has been added");
				return true;
			} else {
				sender.sendMessage(
						ChatColor.RED + "[RessourceTowers]" + ChatColor.WHITE + " Chunk already contains a tower ");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public static void SpawnTower(Location loc, CommandSender sender) {
		Location locNew = loc;

		int x = loc.getBlockX();
		int z = loc.getBlockZ();

		for (int i = x - 2; i <= x + 2; i++) {
			for (int j = z - 2; j <= z + 2; j++) {

				locNew.setX(i);
				locNew.setZ(j);

				locNew.getBlock().setBlockData(Bukkit.createBlockData("minecraft:sea_lantern"));
			}
		}
		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = z - 1; j <= z + 1; j++) {

				locNew.setX(i);
				locNew.setZ(j);

				locNew.getBlock().setBlockData(Bukkit.createBlockData("minecraft:stone_bricks"));
			}
		}

		locNew.setX(x);
		locNew.setZ(z);

		locNew.getBlock().setBlockData(Bukkit.createBlockData("minecraft:obsidian"));
		for (int i = 0; i < 3; i++) {
			locNew.add(0, 1, 0).getBlock().setBlockData(Bukkit.createBlockData("minecraft:stone_bricks"));
		}
		locNew.add(0, 1, 0).getBlock().setBlockData(Bukkit.createBlockData("minecraft:obsidian"));
	}
}
