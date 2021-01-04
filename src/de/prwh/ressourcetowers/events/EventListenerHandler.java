package de.prwh.ressourcetowers.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import de.prwh.ressourcetowers.main.RTPermissions;
import de.prwh.ressourcetowers.towers.SerializableLocation;
import de.prwh.ressourcetowers.towers.TowerInfo;
import de.prwh.ressourcetowers.towers.TowerLocation;
import net.prosavage.factionsx.core.FPlayer;
import net.prosavage.factionsx.core.Faction;
import net.prosavage.factionsx.manager.FactionManager;
import net.prosavage.factionsx.manager.GridManager;
import net.prosavage.factionsx.manager.PlayerManager;
import net.prosavage.factionsx.persist.data.FLocation;


public class EventListenerHandler implements Listener {

	TowerLocation tlLoc = TowerLocation.getInstance();

	@EventHandler
	public void onBlockBreak(BlockBreakEvent event) {
		// System.out.println(event.getBlock() + " " + event.getPlayer().getName());

		FPlayer fPlayer = PlayerManager.INSTANCE.getFPlayer(event.getPlayer());
		Location loca = event.getPlayer().getLocation();
		FLocation fLoc = new FLocation((long)loca.getX(), (long)loca.getZ(), loca.getWorld().getName());
		System.out.println("Location: " + fLoc);
		System.out.println("Current Faction: " + GridManager.INSTANCE.getFactionAt(fLoc));
		GridManager.INSTANCE.claim(fPlayer.getFaction(), fLoc);
		System.out.println("Current Faction: " + GridManager.INSTANCE.getFactionAt(fLoc));
		
		
		for (SerializableLocation loc : tlLoc.getMap().keySet()) {
			TowerInfo info = tlLoc.getMap().get(loc);
			
			Faction faction_tower = info.getOwnerFaction();
			Faction faction_none = FactionManager.INSTANCE.getWilderness();
			Faction faction = fPlayer.getFaction();
			Faction faction_safezone = FactionManager.INSTANCE.getFaction(FactionManager.SAFEZONE_ID);

			if (event.getBlock().getLocation().equals(loc.toLocation())) {
				// System.out.println("Towerblock " + loc.toLocation());
				// PS chunk_tower = PS.valueOf(loc.toLocation().getChunk());
				FLocation fLocation = new FLocation((long)loc.getX(), (long)loc.getZ(), loc.getWorld().getName());

				if (fPlayer.hasFaction()) {
					if (faction_tower.equals(faction)) {
						event.getPlayer().sendMessage(ChatColor.RED + "[RessourceTowers]" + ChatColor.WHITE
								+ " Tower already belongs to your Faction");
					} else {
						if (faction_tower.equals(faction_none)) {
							// BoardColl.get().setFactionAt(chunk_tower, faction);
							System.out.println("Location: " + fLocation);
							System.out.println("Current Faction: " + GridManager.INSTANCE.getFactionAt(fLocation));
							System.out.println("Player Faction: " + faction);
							GridManager.INSTANCE.claim(faction_safezone, fLocation);
							System.out.println("Set Faction: " + GridManager.INSTANCE.getFactionAt(fLocation));
							info.setOwnerFaction(faction.getTag());
							//tlLoc.updateTowerLocation(loc, info);
							event.getPlayer().sendMessage(
									ChatColor.RED + "[RessourceTowers]" + ChatColor.WHITE + " Tower has been captured");
							Bukkit.broadcastMessage(ChatColor.RED + "[RessourceTowers]" + ChatColor.GREEN + " Faction '"
									+ faction.getTag() + "'" + ChatColor.WHITE + " has captured an "
									+ TowerLocation.getInstance().getTowerInfo(loc.toLocation()).getTowername());
						} else {
							GridManager.INSTANCE.claim(faction, fLocation);
							info.setOwnerFaction(faction.getTag());
							//tlLoc.updateTowerLocation(loc, info);
							event.getPlayer()
									.sendMessage(ChatColor.RED + "[RessourceTowers]" + ChatColor.WHITE
											+ " You have stolen a tower from the Faction " + ChatColor.GREEN + "'"
											+ faction_tower.getTag() + "'");
							Bukkit.broadcastMessage(ChatColor.RED + "[RessourceTowers]" + ChatColor.GREEN + " Faction '"
									+ faction.getTag() + "'" + ChatColor.WHITE + " has stolen an "
									+ TowerLocation.getInstance().getTowerInfo(loc.toLocation()).getTowername()
									+ " from " + ChatColor.GREEN + "Faction '" + faction_tower.getTag() + "'");
						}
					}
				} else {
					event.getPlayer().sendMessage(
							ChatColor.RED + "[RessourceTowers]" + ChatColor.WHITE + " You don't belong to a Faction!");
				}
				event.setCancelled(true);
			} else if (event.getBlock().getChunk().getX() == loc.toLocation().getChunk().getX()
					&& event.getBlock().getChunk().getZ() == loc.toLocation().getChunk().getZ()) {
				// System.out.println("Chunkblock " + loc.toLocation());
				if (!event.getPlayer().hasPermission(RTPermissions.EditTower.getPermissionName())) {
					if (fPlayer.hasFaction()) {
						if (fPlayer.getFaction().equals(faction_tower)) {
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
							case NETHER_QUARTZ_ORE:
								event.setCancelled(false);
								break;
							default:
								event.setCancelled(true);
								break;
							}
						} else {
							event.setCancelled(true);
						}
					} else {
						event.setCancelled(true);
					}
				}

			} else {
				// System.out.println("Out of Towerchunk " + loc.toLocation());
			}
		}
	}

	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event) {
		if (!event.getPlayer().hasPermission(RTPermissions.EditTower.getPermissionName())) {
			for (SerializableLocation loc : tlLoc.getMap().keySet()) {
				if (event.getBlock().getChunk().getX() == loc.toLocation().getChunk().getX()
						&& event.getBlock().getChunk().getZ() == loc.toLocation().getChunk().getZ())
					event.setCancelled(true);
			}
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
