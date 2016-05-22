package de.prwh.ressourcetowers.events;

import java.util.Collections;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
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

				PS chunk = PS.valueOf(event.getPlayer().getLocation()).getChunk(true);
				Set<PS> chunks = Collections.singleton(chunk);

				event.setCancelled(true);

				fPlayer = MPlayer.get(event.getPlayer());
				if (fPlayer.hasFaction()) {
					Faction faction = fPlayer.getFaction();

					if (BoardColl.get().getFactionAt(chunk).equals(faction)) {
						event.getPlayer().sendMessage(ChatColor.RED + "[RessourceTowers]" + ChatColor.WHITE + " Tower already belongs to your Faction");
					} else {
						event.getPlayer().sendMessage(ChatColor.RED + "[RessourceTowers]" + ChatColor.WHITE + " Tower has been captured");
					}
					fPlayer.tryClaim(faction, chunks);
				}
			}
		}
	}
}
