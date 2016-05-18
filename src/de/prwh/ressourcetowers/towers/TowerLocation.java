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

import de.prwh.ressourcetowers.main.RTMain;

public class TowerLocation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8216077333009256406L;
	private static File file = new File("C:/Users/Simon/Desktop/mc/world/data/towerlist.dat");
	private static TowerLocation instance;
	private HashMap<Location, TowerInfo> map = new HashMap<>();

	private TowerLocation() {
	}

	public void addTowerLocation(Location loc, TowerInfo tower) {
		map.put(loc, tower);
	}

	public void removeTowerLocation(Location loc) {
		map.remove(loc);
	}

	public Location getTowerLocations() {
		for (Location locM : map.keySet()) {
			return locM;
		}
		return null;
	}

	public void listTowerLocations() {
		System.out.println(map.keySet());
	}

	public TowerInfo getTowerInfo(Location loc) {
		if (locationContainsTower(loc))
			return map.get(loc);
		return null;
	}

	public boolean locationContainsTower(Location loc) {
		if (map.containsKey(loc))
			return true;
		return false;
	}

	public boolean chunkContainsTower(Location loc) {
		if (map.isEmpty())
			return false;
		Chunk chunk = loc.getChunk();

		for (Location locM : map.keySet()) {
			if (chunk.equals(locM.getChunk())) {
				return true;
			}
		}
		return false;
	}

	public Location getTowerInChunk(Location loc) {

		if (chunkContainsTower(loc)) {
			Chunk chunk = loc.getChunk();

			for (Location locM : map.keySet()) {
				if (chunk == locM.getChunk()) {
					return locM;
				}
			}
		}
		return null;
	}

	public static TowerLocation getInstance() {
		if (instance == null)
			instance = new TowerLocation();
		return instance;
	}

	public void saveTowerList() {
		RTMain.getLoggerMain().info("[RessourceTowers] Trying to save the tower list");

		try {

			if (!file.exists())
				file.createNewFile();

			try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
				oos.writeObject(map);
			}
		} catch (IOException e) {
			throw new RuntimeException("[RessourceTowers] Could not save towerlist to file", e);
		}

	}

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
			map = (HashMap<Location, TowerInfo>) ois.readObject();
		} catch (IOException | ClassNotFoundException | ClassCastException e) {
			throw new RuntimeException("[RessourceTowers] Could not load towerlist from file", e);
		}
	}
}
