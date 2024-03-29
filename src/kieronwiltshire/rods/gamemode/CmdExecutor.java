package kieronwiltshire.rods.gamemode;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import ca.kanoa.batman.utils.Healer;
import ca.kanoa.batman.utils.InventoryClear;
import ca.kanoa.batman.utils.SQL;
import ca.kanoa.rodsthegame.Copier;
import ca.kanoa.rodsthegame.classes.ClassLoader;
import ca.kanoa.rodsthegame.gui.ClickedItem;
import ca.kanoa.rodsthegame.gui.Item;
import ca.kanoa.rodsthegame.gui.ItemGui;
import ca.kanoa.rodsthegame.store.StoreParser;

public class CmdExecutor implements CommandExecutor{

	private Main plugin;
	public CmdExecutor(Main plugin)
	{
		this.plugin = plugin;
	}


	public static boolean isInt(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}


	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

		if(cmd.getName().equalsIgnoreCase("import")){
			if(args.length == 1){
				File worldFile = new File("./plugins/RodsTheGame/Maps/" + args[0]);
				if(worldFile.exists()){
					if(plugin.getConfig().contains("maps." + args[0])){
						sender.sendMessage(ChatMessages.prefix + "The world " + args[0] + " is already imported!");
					}
					else{
						plugin.getConfig().set("maps." + args[0] + ".spawns", "");
						plugin.getConfig().set("maps." + args[0] + ".lobby", "");
						sender.sendMessage(ChatMessages.prefix + "The world " + args[0] + " has been imported!");
						plugin.configSaver();
					}	
				}
				else
				{
					sender.sendMessage(ChatMessages.RED + "The world you specified doesn't exist!");
				}
			}
			else
			{
				sender.sendMessage(ChatMessages.RED + "Correct Syntax: /import <world>");
			}
		}

		else if(cmd.getName().equalsIgnoreCase("forcestart")) {
			Main.lobbyTimer = 5;
			if(plugin.game.enabled == false) Bukkit.broadcastMessage(ChatMessages.L_PURPLE + "Match force-started by a staff member!");
		}
		
		else if (cmd.getName().equalsIgnoreCase("money")) {
			switch (args.length) {
			case 0:
				return false;
			case 1: 
				return false;
			case 2: 
				return false;
			case 3:
				String name = args[1];
				int amount;
				try {
					amount = Integer.parseInt(args[2]);
				} catch (NumberFormatException e) {
					sender.sendMessage("Thats not a number!");
					return false;
				}
				if (args[0].equalsIgnoreCase("set")) {
					SQL.removeMoney(name, SQL.getMoney(name));
					SQL.addMoney(name, amount);
					sender.sendMessage(ChatMessages.prefix + name + " now has " + amount + " exp!");
					return true;
				}
				else if (args[0].equalsIgnoreCase("add")) {
					sender.sendMessage(ChatMessages.prefix + name + " now has " + (amount + SQL.getMoney(name)) + " exp!");
					SQL.addMoney(name, amount);
					return true;
				}
				else
					return false;
			}
			return false;
		}

		else if(cmd.getName().equalsIgnoreCase("stoplobbytimer")){
			Main.lobbyTimer = Integer.MAX_VALUE;
			if(plugin.game.enabled == false) Bukkit.broadcastMessage(ChatMessages.L_PURPLE + "Lobby timer stopped by a staff member!");
		}

		else if(cmd.getName().equalsIgnoreCase("forcerestart")){
			plugin.resetHandle.pluginReset();
		}

		else if(cmd.getName().equalsIgnoreCase("forceend")){
			if(Main.lobbyBoolean == false && plugin.resetMode == false){
				Bukkit.broadcastMessage(ChatMessages.prefix + "Game force ended by a staff member!");
				Main.gameTimer = 5;
			}
			else 
				sender.sendMessage(ChatMessages.prefix + "You cannot end a game that hasn't started!");	
		}

		else if(cmd.getName().equalsIgnoreCase("reloadclasses")) {
			sender.sendMessage(ChatMessages.reloadClasses);
			ClassLoader.loadFromFiles();
		}
		
		else if(cmd.getName().equalsIgnoreCase("reloadstore")) {
			sender.sendMessage(ChatMessages.reloadStore);
			StoreParser.loadFromFiles();
		}

		else if(cmd.getName().equalsIgnoreCase("forcesave")) {
			if (Main.selectedMap != null && !Main.selectedMap.isEmpty())
				try {
					sender.sendMessage(ChatMessages.forceSave.replace("%%MAP%%", Main.selectedMap));
					Copier.deleteDirectory(new File(plugin.getDataFolder(), "Maps" + File.separator + Main.selectedMap));
					Bukkit.getWorld(Main.selectedMap).save();
					Bukkit.getWorld(Main.selectedMap).setAutoSave(false);
					Copier.copyFolder(new File("." + File.separator + Main.selectedMap), new File(plugin.getDataFolder(), "Maps" + File.separator + Main.selectedMap));
					sender.sendMessage(ChatMessages.saved);
				} catch (Exception e) {
					sender.sendMessage(ChatColor.RED + "ERROR");
					sender.sendMessage(e.getMessage());
					e.printStackTrace();
				}

		}

		else if(cmd.getName().equalsIgnoreCase("worldteleport")) {
			if(sender instanceof Player){
				if(args.length == 1){
					Player p = (Player) sender;
					World world = Bukkit.getServer().getWorld(args[0]);
					if(world != null){
						if(plugin.getConfig().contains("maps." + world.getName() + "lobby.x")){
							plugin.teleportHandle.teleportToMapLobby(p);
						}	
						else {
							Location loc = world.getSpawnLocation();
							p.teleport(loc);
						}
					}
				}
				else {
					sender.sendMessage(ChatMessages.prefix + "Correct Syntax: < /worldteleport [world] >");
				}
			}
			else 
				sender.sendMessage("The console cannot teleport silly!");	
		}

		else if (cmd.getName().equalsIgnoreCase("map"))
			if (Main.lobbyBoolean)
				sender.sendMessage(ChatMessages.selectedMapIs.replace("%%MAP%%", "Lobby"));
			else
				sender.sendMessage(ChatMessages.selectedMapIs.replace("%%MAP%%", Main.selectedMap == null ? "null" : Main.selectedMap));

		else if(sender instanceof Player){

			Player player = (Player) sender;

			double x = player.getLocation().getBlockX();
			double y = player.getLocation().getBlockY();
			double z = player.getLocation().getBlockZ();
			float yaw = player.getLocation().getYaw();
			float pitch = player.getLocation().getPitch();

			if(cmd.getName().equalsIgnoreCase("setlobby")){
				if(args.length == 0){
					if(player.getWorld().getName() == "lobby"){
						plugin.getConfig().set("lobby.x", x);
						plugin.getConfig().set("lobby.y", y);
						plugin.getConfig().set("lobby.z", z);
						plugin.getConfig().set("lobby.yaw", yaw);
						plugin.getConfig().set("lobby.pitch", pitch);
						plugin.configSaver();
						player.sendMessage(ChatMessages.mainLobbySet);
					}
					else{
						if(plugin.getConfig().contains("maps." + player.getWorld().getName())){
							plugin.getConfig().set("maps." + player.getWorld().getName() + ".lobby.x", x);
							plugin.getConfig().set("maps." + player.getWorld().getName() + ".lobby.y", y);
							plugin.getConfig().set("maps." + player.getWorld().getName() + ".lobby.z", z);
							plugin.getConfig().set("maps." + player.getWorld().getName() + ".lobby.yaw", yaw);
							plugin.getConfig().set("maps." + player.getWorld().getName() + ".lobby.pitch", pitch);
							plugin.configSaver();
							player.sendMessage(ChatMessages.worldLobbySet);
						}
						else 
							player.sendMessage(ChatMessages.prefix + "This world isn't defined yet! Please use the /import command first.");
					}
				}
			}

			else if(cmd.getName().equalsIgnoreCase("setspawn")){
				if(args.length == 1){
					if(plugin.getConfig().contains("maps." + player.getWorld().getName())){
						if(isInt(args[0])){
							int num = Integer.parseInt(args[0]);

							int maxSpawnLimit = plugin.getConfig().getInt("max-spawns");

							if(!(num > maxSpawnLimit)){
								plugin.getConfig().set("maps." + player.getWorld().getName() + ".spawns." + num + ".x", x);
								plugin.getConfig().set("maps." + player.getWorld().getName() + ".spawns." + num + ".y", y);
								plugin.getConfig().set("maps." + player.getWorld().getName() + ".spawns." + num + ".z", z);
								plugin.getConfig().set("maps." + player.getWorld().getName() + ".spawns." + num + ".yaw", yaw);
								plugin.getConfig().set("maps." + player.getWorld().getName() + ".spawns." + num + ".pitch", pitch);
								plugin.configSaver();
								player.sendMessage(ChatMessages.prefix + "Spawn " + num + " has been set to your location.");
							}
							else 
								player.sendMessage(ChatMessages.prefix + "Max spawn limit: " + maxSpawnLimit);
						}
						else 
							player.sendMessage(ChatMessages.prefix + "/setspawn <integer>");
					}
					else 
						player.sendMessage(ChatMessages.prefix + "This world isn't defined yet! Please use the /import command first.");
				}
				else 
					player.sendMessage(ChatMessages.prefix + "/setspawn <integer>");
			}
			else if (cmd.getName().equalsIgnoreCase("worldcheck"))
				player.sendMessage(ChatMessages.currWorld.replace("%%WORLD%%", player.getWorld().getName()));

			else if (cmd.getName().equalsIgnoreCase("respawn")) {
				double x1 = plugin.getConfig().getInt("maps." + Main.selectedMap + ".lobby.x");
				double y1 = plugin.getConfig().getInt("maps." + Main.selectedMap + ".lobby.y");
				double z1 = plugin.getConfig().getInt("maps." + Main.selectedMap + ".lobby.z");
				float yaw1 = plugin.getConfig().getInt("maps." + Main.selectedMap + ".lobby.yaw");
				float pitch1 = plugin.getConfig().getInt("maps." + Main.selectedMap + ".lobby.pitch");

				World world = Bukkit.getServer().getWorld(Main.selectedMap);

				Location spawn = new Location(world, x1, y1, z1, yaw1, pitch1);

				if (Main.lobbyBoolean) {
					player.sendMessage(ChatMessages.noLobbyRespawn);
					return true;
				}
				if(args.length == 0){

					player.teleport(spawn);
					InventoryClear.clear(player);
					Healer.simulateRespawn(player);

					for (PotionEffect effect : player.getActivePotionEffects())
						player.removePotionEffect(effect.getType());

					player.sendMessage(ChatMessages.selectedMapIs.replace("%%MAP%%", Main.selectedMap));

				}	
				else if(args.length == 1 && sender.hasPermission("rtg.respawn.other")){
					Player targetPlayer = Bukkit.getServer().getPlayer(args[0]);
					if(targetPlayer != null){

						targetPlayer.teleport(spawn);
						InventoryClear.clear(targetPlayer);
						Healer.simulateRespawn(targetPlayer);

						for (PotionEffect effect : targetPlayer.getActivePotionEffects())
							targetPlayer.removePotionEffect(effect.getType());

						targetPlayer.sendMessage(ChatMessages.selectedMapIs.replace("%%MAP%%", Main.selectedMap));

					}

				}
			}
			else if(cmd.getName().equalsIgnoreCase("admin")) {
				new ItemGui(player, ChatColor.RED + "Admin Panel", 1){

					@Override
					public List<ItemStack> populateStore() {
						Item stopLobbyTimer = new Item(ChatColor.BLUE + "Stop Lobby Timer", new String[]{"Stops the current lobby timer.", ChatColor.RED + "/stoplobbytimer"}, Material.ANVIL);
						Item forceStart = new Item(ChatColor.BLUE + "Force Start", new String[]{"Will force a game in lobby to start.", ChatColor.RED + "/force-start"}, Material.DIAMOND_PICKAXE);
						Item forceRestart = new Item(ChatColor.BLUE + "Force Restart", new String[]{"Will force a game to be restarted.", ChatColor.RED + "/force-restart"}, Material.WORKBENCH);
						Item forceEnd = new Item(ChatColor.BLUE + "Force End", new String[]{"Sets the games timer to 5 seconds.", ChatColor.RED + "/force-end"}, Material.TNT);
						return Arrays.asList(new ItemStack[]{null, stopLobbyTimer.getStack(), null, forceStart.getStack(), null, forceRestart.getStack(), null, forceEnd.getStack(), null});
					}

					@Override
					public void itemClicked(ClickedItem item) {
						try {
							String cmd = item.getLore().get(1).replace(ChatColor.RED.toString(), "").substring(1);
							getBuyer().performCommand(cmd);
						} catch (Exception e){}
					}}.show();
			}

		}
		else 
			sender.sendMessage(ChatMessages.RED + "You must be a player to operate this command.");	

		return true;
	}

}
