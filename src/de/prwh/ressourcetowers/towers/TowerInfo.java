package de.prwh.ressourcetowers.towers;

import java.io.Serializable;

import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.Items;

public class TowerInfo implements Serializable {

	public static enum TowerType {
		IRON("Iron", new ItemStack(Items.IRON_INGOT, 1)), GOLD("Gold", new ItemStack(Items.GOLD_INGOT, 1)), REDSTONE("Redstone",
				new ItemStack(Items.REDSTONE, 4)), DIAMOND("Diamond", new ItemStack(Items.DIAMOND, 1)), LAPIS("Lapis", new ItemStack(Items.DYE, 4, 4));

		private String towername;
		private ItemStack ressource;

		public String getTowerName() {
			return towername  + "-tower";
		}

		public ItemStack getTowerRessource() {
			return ressource;
		}

		private TowerType(String towername, ItemStack ressource) {
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

	public ItemStack getRessource() {
		return type.getTowerRessource();
	}

	public String getTowername() {
		return type.getTowerName();
	}
	
	public String toString() {
		return getTowername();
		
	}

}
