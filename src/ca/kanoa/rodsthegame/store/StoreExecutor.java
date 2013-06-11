package ca.kanoa.rodsthegame.store;

import kieronwiltshire.rods.gamemode.ChatMessages;
import kieronwiltshire.rods.gamemode.Main;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StoreExecutor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if (!(sender instanceof Player)) {
			sender.sendMessage("Player only command!");
			return true;
		}
		else if (!sender.hasPermission("rtg.store")) {
			sender.sendMessage(ChatMessages.noCmdPerms);
			return false;
		}
		
		Player player = (Player) sender;
		
		if (args.length == 0) {
			//Open GUI
		}
		else {
			StringBuilder sb = new StringBuilder();
			for (String s : args)
				sb.append(s + " ");
			String itemName = sb.toString().trim();
			
			if (!Buyable.isForSale(itemName)) {
				player.sendMessage(ChatMessages.itemNotfound);
				return true;
			}
			
			Buyable item = Buyable.getBuyable(itemName);
			String cmd = Main.getInstance().getConfig().
					getString("permission-command").toLowerCase().
					replace("{player}", player.getName()).
					replace("%%player%%", player.getName()).
					replace("{node}", item.getNode().toString()).
					replace("%%node%%", item.getNode().toString()).
					trim();
			if (cmd.startsWith("/"))
				cmd = cmd.substring(1);
			
			try {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				//remove sql money: SQL.removeMoney(item.getCost());
			}
			
			sender.sendMessage(ChatMessages.itemBought.
					replace("%%ITEM%%", item.getName()).
					replace("%%COST%%", item.getCost() + ""));
			
		}
		return true;
	}



}
