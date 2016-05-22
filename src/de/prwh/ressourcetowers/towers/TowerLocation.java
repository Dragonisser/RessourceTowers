package de.prwh.ressourcetowers.towers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

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
		sender.sendMessage(map.entrySet().toString());
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
		RTMain.getLoggerMain().info("[RessourceTowers] Trying to save the tower list");

		try {
			try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
				oos.writeObject(map);
				RTMain.getLoggerMain().info("[RessourceTowers] Towerlist saved successfully");
			}
		} catch (IOException e) {
			RTMain.getLoggerMain().info("[RessourceTowers] Could not save towerlist to file", e);
		}

	}

	@SuppressWarnings("unchecked")
	public void loadTowerList() {
		RTMain.getLoggerMain().info("[RessourceTowers] Trying to load the tower list");
		if (file == null || !file.exists())
			try {
				file.createNewFile();
				return;
			} catch (IOException e1) {
				e1.printStackTrace();
			}

		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
			map = (HashMap<SerializableLocation, TowerInfo>) ois.readObject();
			RTMain.getLoggerMain().info("[RessourceTowers] Towerlist loaded successfully");
			RTMain.getLoggerMain().info("[RessourceTowers] Loaded " + map.size() + " towers");
		} catch (IOException | ClassNotFoundException | ClassCastException e) {
			RTMain.getLoggerMain().info("[RessourceTowers] Could not load towerlist from file", e);
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
