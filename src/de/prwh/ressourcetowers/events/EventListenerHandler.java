package de.prwh.ressourcetowers.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
		System.out.println(event.getBlock() + " " + event.getPlayer().getName());
		for (SerializableLocation loc : tlLoc.getMap().keySet()) {
			if (event.getBlock().getLocation().equals(loc.toLocation())) {
				System.out.println("Towerblock " + loc.toLocation());
				PS chunk_tower = PS.valueOf(loc.toLocation().getChunk());

				fPlayer = MPlayer.get(event.getPlayer());
				if (fPlayer.hasFaction()) {
					Faction faction = fPlayer.getFaction();
					Faction faction_none = FactionColl.get().getNone();
					Faction faction_tower = BoardColl.get().getFactionAt(chunk_tower);

					if (BoardColl.get().getFactionAt(chunk_tower).equals(faction)) {
						event.getPlayer().sendMessage(ChatColor.RED + "[RessourceTowers]" + ChatColor.WHITE + " Tower already belongs to your Faction");
					} else {
						if (BoardColl.get().getFactionAt(chunk_tower).equals(faction_none)) {
							BoardColl.get().setFactionAt(chunk_tower, faction);
							event.getPlayer().sendMessage(ChatColor.RED + "[RessourceTowers]" + ChatColor.WHITE + " Tower has been captured");
							Bukkit.broadcastMessage(ChatColor.RED + "[RessourceTowers]" + ChatColor.GREEN + " Faction '" + faction.getName() + "'" + ChatColor.WHITE + " has captured an "
									+ TowerLocation.getInstance().getTowerInfo(loc.toLocation()).getTowername());
						} else {
							BoardColl.get().setFactionAt(chunk_tower, faction);
							event.getPlayer()
									.sendMessage(ChatColor.RED + "[RessourceTowers]" + ChatColor.WHITE + " You have stolen a tower from the Faction " + ChatColor.GREEN + faction_tower.getName());
							Bukkit.broadcastMessage(ChatColor.RED + "[RessourceTowers]" + ChatColor.GREEN + " Faction '" + faction.getName() + "'" + ChatColor.WHITE + " has stolen an "
									+ TowerLocation.getInstance().getTowerInfo(loc.toLocation()).getTowername() + " from " + ChatColor.GREEN + " Faction " + faction_tower.getName());
						}
					}
				}
				event.setCancelled(true);
			} else if (event.getBlock().getChunk().getX() == loc.toLocation().getChunk().getX() && event.getBlock().getChunk().getZ() == loc.toLocation().getChunk().getZ()) {
				System.out.println("Chunkblock " + loc.toLocation());
				switch (event.getBlock().getType()) {
				case COAL_ORE:
					event.setCancelled(false);
					break;
				case IRON_ORE:
					event.setCancelled(false);
					break;
				case GOLD_ORE:
					event.setCancelled(false);
					break;
				case REDSTONE_ORE:
					event.setCancelled(false);
					break;
				case DIAMOND_ORE:
					event.setCancelled(false);
					break;
				case EMERALD_ORE:
					event.setCancelled(false);
					break;
				case LAPIS_ORE:
					event.setCancelled(false);
					break;
				case QUARTZ_ORE:
					event.setCancelled(false);
					break;
				default:
					event.setCancelled(true);
					break;
				}
			} else {
				System.out.println("Out of Towerchunk " + loc.toLocation());
			}
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		for (SerializableLocation loc : tlLoc.getMap().keySet()) {
			if (event.getBlock().getChunk().getX() == loc.toLocation().getChunk().getX() && event.getBlock().getChunk().getZ() == loc.toLocation().getChunk().getZ())
				event.setCancelled(true);
		}
	}

	@EventHandler
	public void onBlockDamage(BlockDamageEvent event) {
		for (SerializableLocation loc : tlLoc.getMap().keySet()) {
			if (event.getBlock().getLocation().equals(loc.toLocation())) {
				event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 20 * 40, 0));
			}
		}
	}
}
