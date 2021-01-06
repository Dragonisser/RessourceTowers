package de.prwh.ressourcetowers.main.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.prwh.ressourcetowers.main.RTMain;
import de.prwh.ressourcetowers.towers.TowerHelper;

public class CommandHelper implements CommandExecutor {

	private RTMain main;

	public CommandHelper(RTMain main) {
		this.main = main;
	}
	
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		TowerHelper towerHelper = TowerHelper.getInstance();
		
		if(sender instanceof Player) {
			Player player = (Player)sender;

			if(args.length == 2 && args[0].equalsIgnoreCase("add")) {
				if(towerHelper.addTower(player, args[1])) {
					return true;
				}
			}
			if(args.length >= 1 && args.length < 3 && args[0].equalsIgnoreCase("remove")) {
				boolean all = false;
				if(args.length == 2 && args[1] != null && args[1].equalsIgnoreCase("all")) {
					all = true;
				}
				if(towerHelper.removeTower(player, all)) {
					return true;
				}
			}
			if(args.length >= 2 && args.length < 4 && args[0].equalsIgnoreCase("spawn")) {
				boolean centered = false;
				if(args.length == 3 && args[2] != null && Boolean.valueOf(args[2])) {
					centered = true;
				}
				
				if(towerHelper.spawnTower(player, args[1], centered)) {
					return true;
				}
			}
			if(args.length == 1 && args[0].equalsIgnoreCase("list")) {
				towerHelper.listTowerLocations(sender);
				return true;
			}
			if(args.length == 1 && args[0].equalsIgnoreCase("reload")) {
				main.reloadPlugin();
				sender.sendMessage("Reload RessourceTowers");
				return true;
			}
		}
		return false;
	}

}
