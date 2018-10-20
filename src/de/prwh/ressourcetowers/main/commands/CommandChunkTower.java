package de.prwh.ressourcetowers.main.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.prwh.ressourcetowers.towers.TowerLocation;

public class CommandChunkTower implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = null;
		TowerLocation tLoc = TowerLocation.getInstance();
		if (sender instanceof Player) {
			player = (Player) sender;
		}

		if (cmd.getName().equalsIgnoreCase("chunkTower")) {
			Location loc = (Location) player.getLocation();
			if (tLoc.chunkContainsTower(loc)) {
				sender.sendMessage(ChatColor.RED + "[RessourceTowers]" + ChatColor.WHITE + " " + tLoc.getTowerInChunk(loc).toString());
				sender.sendMessage(ChatColor.RED + "[RessourceTowers]" + ChatColor.WHITE + " Chunk does contain a tower");
			} else {
				sender.sendMessage(ChatColor.RED + "[RessourceTowers]" + ChatColor.WHITE + " Chunk does not contain a tower");
			}
			return true;
		}
		return false;
	}
}
