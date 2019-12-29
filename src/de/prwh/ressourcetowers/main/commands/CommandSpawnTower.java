package de.prwh.ressourcetowers.main.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.prwh.ressourcetowers.main.commands.helper.HelperTower;

public class CommandSpawnTower implements CommandExecutor {

	@Override
	/***
	 * Spawns a predefined tower at the player location or at the center of the chunk
	 * 
	 * @args TowerType @see de.prwh.ressourcetowers.towers.TowerInfo
	 * @args boolean True or False to auto-center the spawned tower
	 */
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = null;
		
		//Command only allowed by player, not by console
		if (sender instanceof Player) {
			player = (Player) sender;
		}
		//Checks if command its typed correctly
		if (cmd.getName().equalsIgnoreCase("spawnTower")) {
			//Checks if the command it supplied with arguments(@see de.prwh.ressourcetowers.towers.TowerInfo), sends back message if they are missing
			if (args.length > 0 && args[0] != null) {

				Location towerLoc = player.getLocation().add(0, 3, 0).getBlock().getLocation();
				Location playerLoc = player.getLocation().add(0, -1, 0).getBlock().getLocation();
				Location centerTowerLoc = towerLoc.getChunk().getBlock(8, towerLoc.getBlockY(), 8).getLocation();
				Location centerBlockLoc = playerLoc.getChunk().getBlock(8, playerLoc.getBlockY(), 8).getLocation();
				
				//Another check for the second argument(boolean auto-centered). Depending if the argument True/False is supplied it auto-centers it or not.
				if (args.length > 1 && args[1] != null && Boolean.valueOf(args[1])) {
					//Checks if the actual placement of the tower(correct TowerType, no existing Tower in chunk, etc) worked before attempting to place the physical form
					if (HelperTower.AddTower(centerTowerLoc, sender, args[0])) {
						HelperTower.SpawnTower(centerBlockLoc, sender);
					}
				} else {
					if (HelperTower.AddTower(towerLoc, sender, args[0])) {
						HelperTower.SpawnTower(playerLoc, sender);
					}
				}
			} else {
				sender.sendMessage(ChatColor.RED + "[RessourceTowers]" + ChatColor.WHITE + " Missing argument TowerType");
			}
			return true;
		}
		return false;
	}

}
