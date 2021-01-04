package de.prwh.ressourcetowers.main.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.prwh.ressourcetowers.towers.SerializableLocation;
import de.prwh.ressourcetowers.towers.TowerLocation;
import net.prosavage.factionsx.core.Faction;
import net.prosavage.factionsx.manager.FactionManager;
import net.prosavage.factionsx.manager.GridManager;
import net.prosavage.factionsx.persist.data.FLocation;

public class CommandRemoveTower implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = null;
		TowerLocation tLoc = TowerLocation.getInstance();
		if (sender instanceof Player) {
			player = (Player) sender;
		}

		if (cmd.getName().equalsIgnoreCase("removeTower")) {
			if (args.length == 0) {

				Location loc = player.getLocation().add(0, -1, 0).getBlock().getLocation();
				FLocation floc = new FLocation((long)loc.getX(), (long)loc.getZ(), loc.getWorld().getName());
				try {
					if (tLoc.locationContainsTower(loc)) {
						sender.sendMessage(ChatColor.RED + "[RessourceTowers] " + ChatColor.WHITE + tLoc.getTowerInfo(loc).getTowername() + " tower at x:" + loc.getBlockX() + " y:" + loc.getBlockY()
								+ " z:" + loc.getBlockZ() + " has been removed");
						tLoc.removeTowerLocation(loc);

						/*
						 * Unclaim faction chunk with tower in it
						 */
						Faction faction = FactionManager.INSTANCE.getWilderness();
						GridManager.INSTANCE.claim(faction, floc);

					} else {
						sender.sendMessage(ChatColor.RED + "[RessourceTowers]" + ChatColor.WHITE + " Location x:" + loc.getBlockX() + " y:" + loc.getBlockY() + " z:" + loc.getBlockZ()
								+ " does not contain a tower");
					}

					return true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (args.length == 1 && args[0] != null && args[0].toString().equals("all")) {
				try {
					/*
					 * Unclaim faction chunk with tower in it
					 */
					for (SerializableLocation loc : tLoc.getMap().keySet()) {
						FLocation floc = new FLocation((long)loc.getX(), (long)loc.getZ(), loc.getWorld().getName());
						Faction faction = FactionManager.INSTANCE.getWilderness();
						GridManager.INSTANCE.claim(faction, floc);
					}
					tLoc.removeAllTowers();

					sender.sendMessage(ChatColor.RED + "[RessourceTowers]" + ChatColor.WHITE + " All towers have been removed");
					return true;
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				sender.sendMessage(ChatColor.RED + "[RessourceTowers]" + ChatColor.WHITE + " /removetower , /removetower all");
			}
		}
		return false;
	}
}
