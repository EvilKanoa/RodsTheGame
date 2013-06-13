package ca.kanoa.rodsthegame.store;

import java.util.ArrayList;
import java.util.List;

import kieronwiltshire.rods.gamemode.ChatMessages;
import kieronwiltshire.rods.gamemode.Main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ca.kanoa.batman.utils.SQL;
import ca.kanoa.rodsthegame.gui.ClickedItem;
import ca.kanoa.rodsthegame.gui.Item;
import ca.kanoa.rodsthegame.gui.ItemGui;

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

		final Player player = (Player) sender;

		if (args.length == 0) {
			//Open GUI
			new ItemGui(player, ChatMessages.storeTitle, ItemGui.roundUp(Buyable.items.size() + 1) / 9){

				@Override
				public List<ItemStack> populateStore() {
					List<ItemStack> list = new ArrayList<ItemStack>();
					for (Buyable b : Buyable.items) {
						String available;
						if (Buyable.hasPermissionFor(b, player))
							available = ChatColor.YELLOW + "Bought";
						else if (player.hasPermission("rtg.buy." + b.getName()) || player.hasPermission("rtg.buy.all"))
							available = ChatColor.GREEN + "Available";
						else
							available = ChatColor.RED + "Donar Only";
						list.add((new Item(ChatColor.LIGHT_PURPLE + b.getName(), new String[]
								{ChatColor.DARK_AQUA + "Cost: " + ChatColor.RED + b.getCost(), b.getDescription(), available}, 
								b.getLook())).getStack());
					}
					while (list.size() + 1 < getInventory().getSize())
						list.add(null);
					list.add((new Item("" + ChatColor.BOLD + ChatColor.UNDERLINE + ChatColor.RED + "Close", Material.TNT)).getStack());

					return list;
				}

				@Override
				public void itemClicked(ClickedItem item) {

					if (item.getName().toLowerCase().contains(ChatMessages.RED + "close")){
						close();
						return;
					}

					String cmd = "store " + item.getName().replace(ChatColor.LIGHT_PURPLE + "", "");
					getBuyer().performCommand(cmd);
					close();
				}}.show();
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

			if (Buyable.hasPermissionFor(item, player)) {
				player.sendMessage(ChatMessages.alreadyOwnClass);
				return true;
			}

			else if (SQL.getMoney(player.getName()) < item.getCost()) {
				player.sendMessage(ChatMessages.noMoneyToBuy);
				return true;
			}

			String cmd = Main.getInstance().getConfig().
					getString("permission-command").toLowerCase().
					replace("{player}", player.getName()).
					replace("%%player%%", player.getName()).
					replace("{node}", item.getNodeString()).
					replace("%%node%%", item.getNodeString()).
					trim();
			if (cmd.startsWith("/"))
				cmd = cmd.substring(1);

			try {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				SQL.removeMoney(player.getName(), item.getCost());
			}

			sender.sendMessage(ChatMessages.itemBought.
					replace("%%ITEM%%", item.getName()).
					replace("%%COST%%", item.getCost() + ""));

		}
		return true;
	}



}
