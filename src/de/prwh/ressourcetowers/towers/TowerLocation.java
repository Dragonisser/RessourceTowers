package de.prwh.ressourcetowers.towers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.CommandSender;

import de.prwh.ressourcetowers.main.RTMain;
import net.prosavage.factionsx.manager.FactionManager;

public class TowerLocation implements Serializable {

	private static final long serialVersionUID = 8216077333009256406L;
	private static File file;
	private static TowerLocation instance;

	private HashMap<SerializableLocation, TowerInfo> map = new HashMap<>();

	private TowerLocation() {
	}

	public HashMap<SerializableLocation, TowerInfo> getMap() {
		return map;
	}

	public void addTowerLocation(SerializableLocation loc, TowerInfo tower) {
		map.put(loc, tower);
	}

	public void updateTowerLocation(SerializableLocation loc, TowerInfo tower) {
		addTowerLocation(loc, tower);
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

	public void listTowerLocations(CommandSender sender) {
		sender.sendMessage(ChatColor.RED + "[RessourceTowers] " + ChatColor.WHITE + map.entrySet().toString());
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

	public boolean chunkContainsTower(Location loc) {
		if (map.isEmpty())
			return false;
		Chunk chunk = loc.getChunk();

		for (SerializableLocation locM : map.keySet()) {
			if (chunk.equals(locM.toLocation().getChunk())) {
				return true;
			}
		}
		return false;
	}

	public SerializableLocation getTowerInChunk(Location loc) {

		if (chunkContainsTower(loc)) {
			Chunk chunk = loc.getChunk();

			for (SerializableLocation locM : map.keySet()) {
				if (chunk == locM.toLocation().getChunk()) {
					return locM;
				}
			}
		}
		return null;
	}

	public void removeAllTowers() {
		map.clear();
	}

	public static TowerLocation getInstance() {
		if (instance == null)
			instance = new TowerLocation();
		return instance;
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

	public void spawnOres() {
		int xMin, xMax;
		int zMin, zMax;
		Location loc;
		TowerInfo info;
		BlockData blockData;
		Random rand = new Random();

		for (SerializableLocation sLoc : map.keySet()) {

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
}
