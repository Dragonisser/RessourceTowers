package de.prwh.ressourcetowers.main.commands.helper;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import com.massivecraft.factions.entity.FactionColl;

import de.prwh.ressourcetowers.towers.SerializableLocation;
import de.prwh.ressourcetowers.towers.TowerInfo;
import de.prwh.ressourcetowers.towers.TowerInfo.TowerType;
import de.prwh.ressourcetowers.towers.TowerLocation;

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
						new TowerInfo(type, FactionColl.get().getNone().getName()));
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

				locNew.getBlock().setType(Material.SEA_LANTERN);
			}
		}
		for (int i = x - 1; i <= x + 1; i++) {
			for (int j = z - 1; j <= z + 1; j++) {

				locNew.setX(i);
				locNew.setZ(j);

				locNew.getBlock().setType(Material.SMOOTH_BRICK);
			}
		}

		locNew.setX(x);
		locNew.setZ(z);

		locNew.getBlock().setType(Material.OBSIDIAN);
		for (int i = 0; i < 3; i++) {
			locNew.add(0, 1, 0).getBlock().setType(Material.SMOOTH_BRICK);
		}
		locNew.add(0, 1, 0).getBlock().setType(Material.OBSIDIAN);
	}
}
