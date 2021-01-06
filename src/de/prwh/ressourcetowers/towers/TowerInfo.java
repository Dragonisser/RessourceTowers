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

		private String towername;
		private String ressource;

		public String getTowerName() {
			return towername + "-tower";
		}

		public String getTowerRessource() {
			return ressource;
		}

		private TowerType(String towername, String ressource) {
			this.towername = towername;
			this.ressource = ressource;
		}
	}

	private static final long serialVersionUID = 2401697897143815705L;
	private TowerType type;
	private String ownerFaction;

	public TowerInfo(TowerType type, String faction) {
		this.type = type;
		this.ownerFaction = faction;
	}

	public TowerType getType() {
		return type;
	}

	public String getRessource() {
		return type.getTowerRessource();
	}

	public String getTowername() {
		return type.getTowerName();
	}

	public String toString() {
		return getTowername();

	}

	public Faction getOwnerFaction() {
		return FactionManager.INSTANCE.getFaction(ownerFaction);
	}

	public void setOwnerFaction(String faction) {
		this.ownerFaction = faction;
	}
}
