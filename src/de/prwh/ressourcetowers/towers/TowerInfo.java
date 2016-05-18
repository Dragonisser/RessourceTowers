package de.prwh.ressourcetowers.towers;

import net.minecraft.server.v1_8_R3.Blocks;
import net.minecraft.server.v1_8_R3.Item;
import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.Items;

public class TowerInfo {
	private ItemStack ressource;
	private String towername;

	public TowerInfo(String towername) {
		this.setTowername(towername);
		switch (towername.toUpperCase()) {
		case "IRON":
			setRessource(new ItemStack(Item.getItemOf(Blocks.IRON_ORE)));
			break;
		case "GOLD":
			setRessource(new ItemStack(Item.getItemOf(Blocks.GOLD_ORE)));
			break;
		case "REDSTONE":
			setRessource(new ItemStack(Items.REDSTONE, 4));
			break;
		case "LAPIS":
			setRessource(new ItemStack(Items.DYE, 4, 4));
			break;
		case "DIAMOND":
			setRessource(new ItemStack(Items.DIAMOND));
			break;
		default:
			break;
		}
	}

	public ItemStack getRessource() {
		return ressource;
	}

	private void setRessource(ItemStack ressource) {
		this.ressource = ressource;
	}

	public String getTowername() {
		return towername.toUpperCase();
	}

	private void setTowername(String towername) {
		this.towername = towername;
	}

	public String toString() {
		return towername + " " + ressource;

	}
}
