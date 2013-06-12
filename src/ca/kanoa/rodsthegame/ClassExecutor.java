package ca.kanoa.rodsthegame;

import kieronwiltshire.rods.gamemode.ChatMessages;
import kieronwiltshire.rods.gamemode.Main;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClassExecutor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("Player only command!");
			return true;
		}
		else if (Main.lobbyBoolean) {
			sender.sendMessage(ChatMessages.prefix + "Cannot use " + ChatMessages.D_RED + "/class" + ChatMessages.L_PURPLE + " in lobby!");
			return true;
		}
			
		
		Player player = (Player) sender;
		
		if (args.length == 0 && sender.hasPermission("rtg.class.gui")) {
			new ClassGui(player).show();
		}
		
		else if (args.length >= 1 && sender.hasPermission("rtg.class.cmd")) {
			StringBuilder sb = new StringBuilder(args[0]);
			for (int i = 1; i < args.length; i++)
				sb.append(" " + args[i]);
			if (!choseClass(player, sb.toString()))
				player.sendMessage(ChatMessages.classNotFound);
		}
		
		else {
			sender.sendMessage(args.length == 0 ? ChatMessages.noClassGuiPerms : ChatMessages.noClassCmdPerms);
		}
		
		return true;
	}


	
	/**
	 * Tries to give a player a class
	 * @param player The player to try to give this class too
	 * @param classStr The name of the class to give to the player
	 * @return if the class was found
	 */
	public static boolean choseClass(Player player, String classStr) {
		if (player.hasPermission("rtg.class." + classStr) || player.hasPermission("rtg.class.all")) {
			for (String str : ClassLoader.classes.keySet())
				if (str.equalsIgnoreCase(classStr)) {
					Main.playerClasses.put(player.getName(), str);
					player.sendMessage(ChatMessages.chosenClass + str);
					return true;
				}
		}
		else {
			player.sendMessage(ChatMessages.noClassPermission);
			return true;
		}
		return false;
					
	}

}
