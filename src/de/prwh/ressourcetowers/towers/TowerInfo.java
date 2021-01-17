package de.prwh.ressourcetowers.towers;

import java.io.Serializable;

import net.prosavage.factionsx.core.Faction;
import net.prosavage.factionsx.manager.FactionManager;

public class TowerInfo implements Serializable {

	public static enum TowerType {
		COAL("Coal", "minecraft:coal_ore"), IRON("Iron", "minecraft:iron_ore"), GOLD("Gold", "minecraft:gold_ore"),
		REDSTONE("Redstone", "minecraft:redstone_ore[lit=false]"), DIAMOND("Diamond", "minecraft:diamond_ore"),
		EMERALD("Emerald", "minecraft:emerald_ore"), LAPIS("Lapis", "minecraft:lapis_ore"),
		QUARTZ("Quartz", "minecraft:nether_quartz_ore");

		private String resourceName;
		private String blockData;

		public String getTowerName() {
			return resourceName + "-tower";
		}
		
		public String getResourceName() {
			return resourceName;
		}

		public String getResourceBlockData() {
			return blockData;
		}

		private TowerType(String towername, String ressource) {
			this.resourceName = towername;
			this.blockData = ressource;
		}
	}

	private static final long serialVersionUID = 2401697897143815705L;
	private TowerType type;
	private long ownerFaction;

	public TowerInfo(TowerType type, long faction) {
		this.type = type;
		this.ownerFaction = faction;
	}

	public TowerType getType() {
		return type;
	}

	public String getRessource() {
		return type.getResourceBlockData();
	}

	public String getTowername() {
		return type.getTowerName();
	}

	public String toString() {
		return getTowername() + " - " + getOwnerFaction().getTag();

	}

	public Faction getOwnerFaction() {
		return FactionManager.INSTANCE.getFaction(ownerFaction);
	}

	public void setOwnerFaction(long faction) {
		this.ownerFaction = faction;
	}
}
