package ca.kanoa.rodsthegame;

import kieronwiltshire.rods.gamemode.Main;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class WorldControlHandler implements Listener, Runnable {

	private Main plugin;

	public WorldControlHandler(Main main) {
		this.plugin = main;
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 100L, 200L);
	}

	@EventHandler
	public void mobSpawn(CreatureSpawnEvent event) {
		if (event.getSpawnReason() != SpawnReason.CUSTOM)
			event.setCancelled(true);
	}

	@Override
	public void run() {
		for (World world : Bukkit.getWorlds()) {
			world.setStorm(false);
			world.setThundering(false);
			world.setTime(6000);
		}
		if (Bukkit.getWorld("lobby") != null)
			for (LivingEntity e : Bukkit.getWorld("lobby").getEntitiesByClass(LivingEntity.class)) {
				if (!(e instanceof Player)) {
					e.setHealth(0);
				}
			}

	}

	@EventHandler
	public void onPickup(PlayerPickupItemEvent event) {
		if(event.getPlayer().hasPermission("rtg.puke"))
			event.setCancelled(false);
		else
			event.setCancelled(true);
	}

	@EventHandler
	public void onDrop(PlayerDropItemEvent event) {
		if(event.getPlayer().hasPermission("rtg.puke"))
			event.setCancelled(false);
		else
			event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
	public void onWorldUnload(WorldUnloadEvent event)
	{
		String result;
		if(event.isCancelled())
			result = "cancelled by another plugin!";
		else
			result = "not cancelled!";
		System.out.print("World ["+event.getWorld().getName()+"] unloading was "+result);
	}

}
