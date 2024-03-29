package kieronwiltshire.rods.gamemode;

import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

import ca.kanoa.batman.utils.Healer;
import ca.kanoa.rodsthegame.classes.ClassesHandler;

public class TeleportHandler {

	private Main plugin;
	public TeleportHandler(Main plugin)
	{
		this.plugin = plugin;
	}



	//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
	//<    METHODS                -   INDEX   >
	//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
	//<teleportAllToMap Method    - <Index = 1>
	//<teleportSingleToMap Method - <Index = 2>
	//<teleportServerLobby Method - <Index = 3>






	//<Index = 1>
	public void teleportAllToMap(){
		try{

			double x = plugin.getConfig().getInt("maps." + Main.selectedMap + ".lobby.x");
			double y = plugin.getConfig().getInt("maps." + Main.selectedMap + ".lobby.y");
			double z = plugin.getConfig().getInt("maps." + Main.selectedMap + ".lobby.z");
			float yaw = plugin.getConfig().getInt("maps." + Main.selectedMap + ".lobby.yaw");
			float pitch = plugin.getConfig().getInt("maps." + Main.selectedMap + ".lobby.pitch");

			Bukkit.broadcastMessage(ChatMessages.selectedMapIs.replace("%%MAP%%", Main.selectedMap));

			try {
				World world = Bukkit.getServer().getWorld(Main.selectedMap);
				Location spawn = new Location(world, x, y, z, yaw, pitch);

				for(Player a : Bukkit.getOnlinePlayers())
					a.teleport(spawn);	

			} catch (NullPointerException n) {
				System.out.println("Map not found. Finding default spawn location.");
				Location spawn = Bukkit.getServer().getWorld("world").getSpawnLocation();	
				for(Player a : Bukkit.getOnlinePlayers())
					a.teleport(spawn);	
			}

		} catch (NullPointerException e) {
			System.out.println("Lobby spawn not found in config. teleporting to");
			System.out.println("default spawn area...");

			try {
				Location spawn = Bukkit.getServer().getWorld("lobby").getSpawnLocation();	
				for(Player a : Bukkit.getOnlinePlayers())
					a.teleport(spawn);	
			} catch (NullPointerException p){
				System.out.println("Lobby world could not be found.");
			}
		}

	}






	//<Index = 2>
	public void teleportRandomSpawn(Player player){

		if(Main.playerClasses.get(player.getName()).equalsIgnoreCase("default")){
			player.sendMessage(ChatMessages.prefix + "You must choose a class first!");
		}
		else{



			int maxSpawnLimit = plugin.getConfig().getInt("max-spawns");
			Random randomNum = new Random();
			player.setHealth(20);
			player.setFoodLevel(Main.lobbyBoolean ? 20 : 16);

			try {

				World world = Bukkit.getServer().getWorld(Main.selectedMap);
				Location loc = null;
				int i = 0;
				while (loc == null) {
					int getSpawn = randomNum.nextInt(maxSpawnLimit);
					int x = plugin.getConfig().getInt("maps." + Main.selectedMap + ".spawns." + getSpawn + ".x");
					int y = plugin.getConfig().getInt("maps." + Main.selectedMap + ".spawns." + getSpawn + ".y");
					int z = plugin.getConfig().getInt("maps." + Main.selectedMap + ".spawns." + getSpawn + ".z");
					int yaw = plugin.getConfig().getInt("maps." + Main.selectedMap + ".spawns." + getSpawn + ".yaw");
					int pitch = plugin.getConfig().getInt("maps." + Main.selectedMap + ".spawns." + getSpawn + ".pitch");
					if ((world.getBlockAt(x, y, z).getType() == Material.AIR && 
							world.getBlockAt(x, y + 1, z).getType() == Material.AIR &&
							world.getBlockAt(x, y - 1, z).getType() != Material.AIR && 
							world.getBlockAt(x, y - 1, z).getType() != Material.FIRE) ||
							i > 30)
						loc = new Location(world, x + 0.5f, y, z + 0.5f, yaw, pitch);
					i++;
				}


				player.teleport(loc);	
				ClassesHandler.customClasses(player);

			} catch (NullPointerException e) {
				plugin.getLogger().info("Unknown error occured whilst teleport player.");
			}
		}
	}

	//<Index = 3>
	public void teleportServerLobby(Player p) {
		try{
			p.setHealth(20);
			p.setFoodLevel(Main.lobbyBoolean ? 20 : 16);

			double x = plugin.getConfig().getInt("lobby.x");
			double y = plugin.getConfig().getInt("lobby.y");
			double z = plugin.getConfig().getInt("lobby.z");
			float yaw = plugin.getConfig().getInt("lobby.yaw");
			float pitch = plugin.getConfig().getInt("lobby.pitch");

			World world = Bukkit.getServer().getWorld("lobby");

			Location spawn = new Location(world, x, y, z, yaw, pitch);
			p.teleport(spawn);
			Healer.simulateRespawn(p);
			for (PotionEffect effect : p.getActivePotionEffects())
				p.removePotionEffect(effect.getType());

		}catch (NullPointerException e){

			System.out.println("Lobby spawn not found in config. teleporting to: ");
			System.out.println("default spawn area...");

		}
	}

	//<Index = 4>
	public void teleportToMapLobby(Player p) {
		try{

			double x = plugin.getConfig().getInt("maps." + Main.selectedMap + ".lobby.x");
			double y = plugin.getConfig().getInt("maps." + Main.selectedMap + ".lobby.y");
			double z = plugin.getConfig().getInt("maps." + Main.selectedMap + ".lobby.z");
			float yaw = plugin.getConfig().getInt("maps." + Main.selectedMap + ".lobby.yaw");
			float pitch = plugin.getConfig().getInt("maps." + Main.selectedMap + ".lobby.pitch");

			World world = Bukkit.getServer().getWorld(Main.selectedMap);

			Location spawn = new Location(world, x, y, z, yaw, pitch);
			p.teleport(spawn);

			p.setHealth(20);
			p.setFoodLevel(Main.lobbyBoolean ? 20 : 16);

		} catch (NullPointerException e){
			System.out.println("Map spawn not found in config. teleporting to:");
			System.out.println("default spawn area...");

			Location spawn = Bukkit.getServer().getWorld(Main.selectedMap).getSpawnLocation();
			p.teleport(spawn);
		}

	}


}
