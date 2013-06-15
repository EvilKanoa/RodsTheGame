package ca.kanoa.rodsthegame;

import kieronwiltshire.rods.gamemode.Main;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
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
		String map = Main.selectedMap;
		FileConfiguration config = Main.getInstance().getConfig();
		World world = Bukkit.getWorld(map == null ? "nullteehee" : map);
		if (map != null && world != null) {
			Weather.setWeather(Weather.parseString(config.getString("maps." + map + ".weather")), world);
			world.setTime(config.getLong("maps." + map + ".time"));
		}
		if (Bukkit.getWorld("lobby") != null)
			for (LivingEntity e : Bukkit.getWorld("lobby").getEntitiesByClass(LivingEntity.class)) {
				if (!(e instanceof Player)) {
					e.setHealth(0);
					Bukkit.getWorld("lobby").setTime(6000);
				}
			}

	}

	@EventHandler(ignoreCancelled=true)
	public void onPickup(PlayerPickupItemEvent event) {
		if(!event.getPlayer().hasPermission("rtg.puke"))
			event.setCancelled(true);
	}

	@EventHandler(ignoreCancelled=true)
	public void onDrop(PlayerDropItemEvent event) {
		if(!event.getPlayer().hasPermission("rtg.puke"))
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
		System.out.print("World [" + event.getWorld().getName() + "] unloading was " + result);
	}
	
	public enum Weather {
		
		SUNNY, RAINING, STORMING;
		
		public static void setWeather(Weather weather, World world) {
			switch (weather) {
			case SUNNY: 
				world.setStorm(false);
				world.setThundering(false);
				break;
			case RAINING:
				world.setStorm(true);
				world.setThundering(false);
				break;
			case STORMING:
				world.setStorm(true);
				world.setThundering(true);
				world.setThunderDuration(2400);
				break;
			}
		}
		
		public static Weather parseString(String str) {
			str = str.toLowerCase().trim();
			if (str.contains("sunny"))
				return SUNNY;
			else if (str.contains("raining"))
				return RAINING;
			else if (str.contains("storming"))
				return STORMING;
			else
				return null;
		}
		
	}

}
