package de.prwh.ressourcetowers.towers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.prwh.ressourcetowers.main.RTMain;
import de.prwh.ressourcetowers.towers.TowerInfo.TowerType;
import net.prosavage.factionsx.core.Faction;
import net.prosavage.factionsx.manager.FactionManager;
import net.prosavage.factionsx.manager.GridManager;
import net.prosavage.factionsx.persist.data.FLocation;

public class TowerHelper implements Serializable {

	private static final long serialVersionUID = 8216077333009256406L;
	private static File file;
	private static TowerHelper instance;
	private static RTMain rtMain;
	private HashMap<SerializableLocation, TowerInfo> map = new HashMap<>();
	
	public static TowerHelper getInstance() {
		if (instance == null)
			instance = new TowerHelper();
		return instance;
	}

	public void addTowerLocation(SerializableLocation loc, TowerInfo tower) {
		map.put(loc, tower);
	}

	public void updateTowerLocation(SerializableLocation loc, TowerInfo tower) {
		addTowerLocation(loc, tower);
	}
	
	public void removeTowerLocation(Chunk chunk) {
		removeTowerLocation(getTowerInChunk(chunk).toLocation());
	}

	public void removeTowerLocation(Location loc) {
		map.remove(new SerializableLocation(loc));
	}

	public SerializableLocation getTowerLocations() {
		for (SerializableLocation locM : map.keySet()) {
			return locM;
		}
		return null;
	}
	
	public HashMap<SerializableLocation, TowerInfo> getMap() {
		return map;
	}

	public void listTowerLocations(CommandSender sender) {
		sender.sendMessage(ChatColor.RED + "[RessourceTowers] " + ChatColor.WHITE + map.entrySet().toString());
	}

	public TowerInfo getTowerInfo(Chunk chunk) {
		return getTowerInfo(getTowerInChunk(chunk).toLocation());
	}
	
	public TowerInfo getTowerInfo(Location loc) {
		if (locationContainsTower(loc))
			return map.get(new SerializableLocation(loc));
		return null;
	}

	public boolean locationContainsTower(Location loc) {
		if (map.containsKey(new SerializableLocation(loc))) {
			return true;
		}
		return false;
	}

	public boolean chunkContainsTower(Chunk chunk) {
		if (map.isEmpty()) {
			return false;
		}

		for (SerializableLocation locM : map.keySet()) {
			if (chunk.equals(locM.toLocation().getChunk())) {
				return true;
			}
		}
		return false;
	}

	
	public SerializableLocation getTowerInChunk(Chunk chunk) {
		if (chunkContainsTower(chunk)) {
			for (SerializableLocation locM : map.keySet()) {
				if (chunk.equals(locM.toLocation().getChunk())) {
					return locM;
				}
			}
		}
		return null;
	}

	public void removeAllTowers() {
		map.clear();
	}

	public void spawnOres() {
		int xMin, xMax;
		int zMin, zMax;
		Location loc;
		TowerInfo info;
		BlockData blockData;
		Random rand = new Random();

		for (SerializableLocation sLoc : getMap().keySet()) {

			info = getTowerInfo(sLoc.toLocation());

			if (info.getOwnerFaction().equals(FactionManager.INSTANCE.getWilderness()))
				return;

			loc = sLoc.toLocation();

			blockData = Bukkit.createBlockData(info.getRessource());
			xMin = loc.getBlockX() - 1;
			xMax = loc.getBlockX() + 1;
			zMin = loc.getBlockZ() - 1;
			zMax = loc.getBlockZ() + 1;


			while (true) {
				if (loc.subtract(0, 1, 0).getBlock().getBlockData().equals(Bukkit.createBlockData("minecraft:obsidian"))) {
					break;
				}	
			}

			int x = xMin + rand.nextInt(xMax - xMin + 1);
			int z = zMin + rand.nextInt(zMax - zMin + 1);
			if (x == loc.getBlockX() && z == loc.getBlockZ())
				return;

			loc.setX(x);
			loc.setZ(z);
			loc.setY(loc.getBlockY() + 1);

			loc.getBlock().setBlockData(blockData);
		}
	}
	
	public boolean addTower(Player player, String towerType) {
		Location loc = player.getLocation().subtract(0,1,0).getBlock().getLocation();
		return addTower(player, loc, towerType);
	}
	
	public boolean addTower(Player player, Location loc, String towerType) {
		try {
			TowerType type = null;
			String types = towerType;
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
				player.sendMessage(ChatColor.RED + "[RessourceTowers]" + ChatColor.WHITE + " Invalid TowerType: "
						+ Arrays.asList(TowerType.values()));
				return false;
			}

			if (!chunkContainsTower(loc.getChunk())) {
				addTowerLocation(new SerializableLocation(loc),
						new TowerInfo(type, FactionManager.INSTANCE.getWilderness().getTag()));
				player.sendMessage(ChatColor.RED + "[RessourceTowers] " + ChatColor.WHITE + type.getTowerName()
						+ " at x:" + loc.getBlockX() + " y:" + loc.getBlockY() + " z:" + loc.getBlockZ()
						+ " has been added");
				return true;
			} else {
				player.sendMessage(
						ChatColor.RED + "[RessourceTowers]" + ChatColor.WHITE + " Chunk already contains a tower ");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean removeTower(Player player, boolean all) {
		if (all) {
			try {
				/*
				 * Unclaim faction chunk with tower in it
				 */
				for (SerializableLocation loc : getMap().keySet()) {
					Chunk chunk = loc.toLocation().getChunk();
					FLocation fLoc = new FLocation(chunk.getX(), chunk.getZ(), chunk.getWorld().getName());
					Faction faction = FactionManager.INSTANCE.getWilderness();
					GridManager.INSTANCE.unclaim(faction, fLoc);
				}
				removeAllTowers();

				player.sendMessage(ChatColor.RED + "[RessourceTowers]" + ChatColor.WHITE + " All towers have been removed");
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}	
		} else {
			Chunk chunk = player.getLocation().getChunk();
			FLocation fLoc = new FLocation(chunk.getX(), chunk.getZ(), chunk.getWorld().getName());
			try {
				if (chunkContainsTower(chunk)) {
					player.sendMessage(ChatColor.RED + "[RessourceTowers] " + ChatColor.WHITE + getTowerInfo(chunk).getTowername() + " tower has been removed");
					removeTowerLocation(chunk);

					/*
					 * Unclaim faction chunk with tower in it
					 */
					Faction faction = FactionManager.INSTANCE.getWilderness();
					GridManager.INSTANCE.unclaim(faction, fLoc);
				} else {
					player.sendMessage(ChatColor.RED + "[RessourceTowers]" + ChatColor.WHITE + " Chunk does not contain a tower");
				}

				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public boolean spawnTower(Player player, String towertype, boolean centered) {
		
		Location towerLoc = player.getLocation().add(0, 3, 0).getBlock().getLocation();
		Location playerLoc = player.getLocation().add(0, -1, 0).getBlock().getLocation();
		Location centerTowerLoc = towerLoc.getChunk().getBlock(8, towerLoc.getBlockY(), 8).getLocation();
		Location centerBlockLoc = playerLoc.getChunk().getBlock(8, playerLoc.getBlockY(), 8).getLocation();
		rtMain = RTMain.getPlugin(RTMain.class);
		
		if(!centered && rtMain.getConfig().getBoolean("secureTowerPlacement")) {
			int blockX = towerLoc.getBlockX() & 0xF;
			int blockZ = towerLoc.getBlockZ() & 0xF;
			
			if(blockX >= 15 || blockX <= 2 || blockZ >= 15 || blockZ <= 2) {
				player.sendMessage(ChatColor.RED + "[RessourceTowers]" + ChatColor.WHITE + " Tower to close to chunk edge. Stay at least 2 blocks away or disable 'secureTowerPlacement' in config.");
				return true;
			}
		} 
		if(!addTower(player, centered ? centerTowerLoc : towerLoc, towertype)) {
			return false;
		} else {
			Location loc = centered ? centerBlockLoc : playerLoc;
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
			return true;
		}
	}
	
	public void saveTowerList() {
		RTMain.sendToConsole("Trying to save the Towerlist");

		try {
			if (file == null || !file.exists()) {

			}
			try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
				oos.writeObject(map);
				RTMain.sendToConsole("Towerlist saved successfully");
			}
		} catch (IOException e) {
			RTMain.getLoggerMain().log(Level.SEVERE, "[RessourceTowers] Could not save Towerlist to file", e);
		}

	}

	@SuppressWarnings("unchecked")
	public void loadTowerList() {
		RTMain.sendToConsole("Trying to load the Towerlist");
		if (file == null || !file.exists())
			try {
				file.createNewFile();
				return;
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
			map = (HashMap<SerializableLocation, TowerInfo>) ois.readObject();
			RTMain.sendToConsole("Towerlist loaded successfully");
			RTMain.sendToConsole("Loaded " + map.size() + (map.size() == 1 ? " tower" : " towers"));
		} catch (IOException | ClassNotFoundException | ClassCastException e) {
			RTMain.getLoggerMain().log(Level.SEVERE, "[RessourceTowers] Could not load Towerlist from file", e);
		}
	}

	public void setFilePath(String path) {

		try {
			file = new File(path + "/towerlist.dat");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
