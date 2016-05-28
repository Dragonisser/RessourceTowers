package de.prwh.ressourcetowers.towers;

import java.io.Serializable;

import org.bukkit.Material;

public class TowerInfo implements Serializable {

	public static enum TowerType {
		IRON("Iron", Material.IRON_ORE), GOLD("Gold", Material.GOLD_ORE), REDSTONE("Redstone", Material.REDSTONE_ORE), DIAMOND("Diamond",
				Material.DIAMOND_ORE), LAPIS("Lapis", Material.LAPIS_ORE);

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

	public TowerInfo(TowerType type) {
		this.type = type;
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

}
