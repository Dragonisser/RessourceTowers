package de.prwh.ressourcetowers.towers;

import java.io.Serializable;

import org.bukkit.Material;

import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;

public class TowerInfo implements Serializable {

	public static enum TowerType {
		IRON("Iron", Material.IRON_ORE), GOLD("Gold", Material.GOLD_ORE), REDSTONE("Redstone", Material.REDSTONE_ORE), DIAMOND("Diamond", Material.DIAMOND_ORE), LAPIS("Lapis", Material.LAPIS_ORE);

		private String towername;
		private Material ressource;

		public String getTowerName() {
			return towername + "-tower";
		}

		public Material getTowerRessource() {
			return ressource;
		}

		private TowerType(String towername, Material ressource) {
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

	public Material getRessource() {
		return type.getTowerRessource();
	}

	public String getTowername() {
		return type.getTowerName();
	}

	public String toString() {
		return getTowername();

	}

	public Faction getOwnerFaction() {
		return FactionColl.get().getByName(ownerFaction);
	}

	public void setOwnerFaction(String faction) {
		this.ownerFaction = faction;
	}
}
