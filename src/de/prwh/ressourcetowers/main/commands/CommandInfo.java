package de.prwh.ressourcetowers.main.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.prwh.ressourcetowers.main.RTMain;

public class CommandInfo implements CommandExecutor {

	private RTMain plugin;

	public CommandInfo(RTMain plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("rt")) {
			sender.sendMessage(ChatColor.RED + "[RessourceTowers]" + ChatColor.WHITE + " Version " + plugin.getDescription().getVersion() + " made by " + plugin.getDescription().getAuthors());
			return true;
		}
		return false;
	}
}
