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

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import com.massivecraft.factions.entity.FactionColl;

import de.prwh.ressourcetowers.main.RTMain;

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
		map.remove(loc);
		map.put(loc, tower);
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
		RTMain.getPlugin(RTMain.class).getServer().getConsoleSender().sendMessage(ChatColor.RED + "[RessourceTowers] " + ChatColor.GREEN + "Trying to save the tower list");

		try {
			if (file == null || !file.exists()) {

			}
			try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
				oos.writeObject(map);
				RTMain.getPlugin(RTMain.class).getServer().getConsoleSender().sendMessage(ChatColor.RED + "[RessourceTowers] " + ChatColor.GREEN + "Towerlist saved successfully");
			}
		} catch (IOException e) {
			RTMain.getLoggerMain().log(Level.SEVERE, "[RessourceTowers] Could not save towerlist to file", e);
		}

	}

	@SuppressWarnings("unchecked")
	public void loadTowerList() {
		RTMain.getPlugin(RTMain.class).getServer().getConsoleSender().sendMessage(ChatColor.RED + "[RessourceTowers] " + ChatColor.GREEN + "Trying to load the tower list");
		if (file == null || !file.exists())
			try {
				file.createNewFile();
				return;
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
			map = (HashMap<SerializableLocation, TowerInfo>) ois.readObject();
			String nominator = map.size() == 1 ? " tower" : " towers";
			RTMain.getPlugin(RTMain.class).getServer().getConsoleSender().sendMessage(ChatColor.RED + "[RessourceTowers] " + ChatColor.GREEN + "Towerlist loaded successfully");
			RTMain.getPlugin(RTMain.class).getServer().getConsoleSender().sendMessage(ChatColor.RED + "[RessourceTowers] " + ChatColor.GREEN + "Loaded " + map.size() + nominator);
		} catch (IOException | ClassNotFoundException | ClassCastException e) {
			RTMain.getLoggerMain().log(Level.SEVERE, "[RessourceTowers] Could not load towerlist to file", e);
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
		Material stack;
		Random rand = new Random();

		for (SerializableLocation sLoc : map.keySet()) {

			info = getTowerInfo(sLoc.toLocation());

			if (info.getOwnerFaction().equals(FactionColl.get().getNone()))
				return;

			loc = sLoc.toLocation();

			stack = info.getRessource();
			xMin = loc.getBlockX() - 1;
			xMax = loc.getBlockX() + 1;
			zMin = loc.getBlockZ() - 1;
			zMax = loc.getBlockZ() + 1;

			while (true) {
				if (!loc.subtract(0, 1, 0).getBlock().getType().equals(Material.SMOOTH_BRICK))
					break;
			}

			int x = xMin + rand.nextInt(xMax - xMin + 1);
			int z = zMin + rand.nextInt(zMax - zMin + 1);
			if (x == loc.getBlockX() && z == loc.getBlockZ())
				return;

			loc.setX(x);
			loc.setZ(z);
			loc.setY(loc.getBlockY() + 1);

			loc.getBlock().setType(stack);
		}
	}
}
