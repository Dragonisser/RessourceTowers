package de.prwh.ressourcetowers.main.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.prwh.ressourcetowers.main.commands.helper.HelperTower;

public class CommandSpawnTower implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = null;

		if (sender instanceof Player) {
			player = (Player) sender;
		}
		if (cmd.getName().equalsIgnoreCase("spawnTower")) {
			if (args.length > 0 && args[0] != null) {

				Location towerLoc = player.getLocation().add(0, 3, 0).getBlock().getLocation();
				Location playerLoc = player.getLocation().add(0, -1, 0).getBlock().getLocation();
				
				HelperTower.AddTower(towerLoc, sender, args[0]);
				HelperTower.SpawnTower(playerLoc, sender);
			}
			return true;
		}
		return false;
	}

}
