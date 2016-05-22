package de.prwh.ressourcetowers.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.FactionColl;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;

import de.prwh.ressourcetowers.towers.SerializableLocation;
import de.prwh.ressourcetowers.towers.TowerLocation;

public class EventListenerHandler implements Listener {

	TowerLocation tlLoc = TowerLocation.getInstance();
	MPlayer fPlayer;

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {

		for (SerializableLocation loc : tlLoc.getMap().keySet()) {

			if (event.getBlock().getLocation().equals(loc.toLocation())) {
				PS chunk_tower = PS.valueOf(loc.toLocation().getChunk());

				event.setCancelled(true);

				fPlayer = MPlayer.get(event.getPlayer());
				if (fPlayer.hasFaction()) {
					Faction faction = fPlayer.getFaction();
					Faction faction_none = FactionColl.get().getNone();
					Faction faction_tower = BoardColl.get().getFactionAt(chunk_tower);

					if (BoardColl.get().getFactionAt(chunk_tower).equals(faction)) {
						event.getPlayer().sendMessage(ChatColor.RED + "[RessourceTowers]" + ChatColor.WHITE + " Tower already belongs to your Faction");
					} else {
						if (BoardColl.get().getFactionAt(chunk_tower).equals(faction_none)) {
							event.getPlayer().sendMessage(ChatColor.RED + "[RessourceTowers]" + ChatColor.WHITE + " Tower has been captured");
							Bukkit.broadcastMessage(ChatColor.RED + "[RessourceTowers] " + ChatColor.GREEN + " Faction " + faction.getName() + ChatColor.WHITE
									+ " has captured an " + TowerLocation.getInstance().getTowerInfo(loc.toLocation()).getTowername());
						} else {
							event.getPlayer().sendMessage(ChatColor.RED + "[RessourceTowers]" + ChatColor.WHITE + " You have stolen a tower from the Faction "
									+ ChatColor.GREEN + faction_tower.getName());
							Bukkit.broadcastMessage(ChatColor.RED + "[RessourceTowers] " + ChatColor.GREEN + " Faction " + faction.getName() + ChatColor.WHITE
									+ " has stolen an " + TowerLocation.getInstance().getTowerInfo(loc.toLocation()).getTowername() + " from " + ChatColor.GREEN
									+ " Faction " + faction_tower.getName());
						}
						BoardColl.get().setFactionAt(chunk_tower, faction);
					}
				}
			}
		}
	}
}
