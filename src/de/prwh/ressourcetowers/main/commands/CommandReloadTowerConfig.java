package de.prwh.ressourcetowers.main.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import de.prwh.ressourcetowers.main.CreateConfig;
import de.prwh.ressourcetowers.main.RTMain;

public class CommandReloadTowerConfig implements CommandExecutor {

	private CreateConfig config;
	private RTMain main;

	public CommandReloadTowerConfig(RTMain main, CreateConfig config) {
		this.config = config;
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if (cmd.getName().equalsIgnoreCase("reloadTowerConfig")) {
			config.reloadConfig();
			main.restartScheduler();
			return true;
		}
		return false;
	}
}
