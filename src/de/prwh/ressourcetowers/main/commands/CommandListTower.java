package de.prwh.ressourcetowers.main.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.prwh.ressourcetowers.towers.TowerLocation;

public class CommandListTower implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		TowerLocation tLoc = TowerLocation.getInstance();
		
		if (cmd.getName().equalsIgnoreCase("listTower")) {
			tLoc.listTowerLocations(sender);
			return true;
		}
		return false;
	}
}
