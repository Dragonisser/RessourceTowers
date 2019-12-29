package de.prwh.ressourcetowers.main.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.prwh.ressourcetowers.main.commands.helper.HelperTower;

public class CommandAddTower implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = null;

		if (sender instanceof Player) {
			player = (Player) sender;
		}
		if (cmd.getName().equalsIgnoreCase("addTower")) {
			if (args.length > 0 && args[0] != null) {

				Location loc = player.getLocation().add(0, -1, 0).getBlock().getLocation();
				HelperTower.AddTower(loc, sender, args[0]);

			} else {
				sender.sendMessage(
						ChatColor.RED + "[RessourceTowers]" + ChatColor.WHITE + " Missing argument TowerType");
			}
			return true;
		}
		return false;
	}
}
