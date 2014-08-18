package ca.kanoa.rodsthegame.classes;

import java.util.ArrayList;
import java.util.List;

import kieronwiltshire.rods.gamemode.ChatMessages;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import ca.kanoa.rodsthegame.gui.ItemGui;
import ca.kanoa.rodstwo.RodsTwo;

public class ClassGui implements Listener {

	private Player buyer;
	private Inventory storeFront;
	public static int itemID = 36;

	public ClassGui(Player sender) {
		this.buyer = sender;
		populateStore();
		Bukkit.getPluginManager().registerEvents(this, RodsTwo.getInstance());
	}

	private void populateStore() {
		storeFront = Bukkit.createInventory(buyer, roundUp(sizeWithPerms(ClassLoader.classes)), ChatMessages.guiTitle);
		for (PlayerClass pc : ClassLoader.classes) {
			if (buyer.hasPermission(pc.getPermission()) || buyer.hasPermission("rtg.class.all")) {
				storeFront.addItem(pc.getGUIButton());
			}
		}
	}

	private int sizeWithPerms(List<?> set) {
		int i = 0;
		for (Object obj : set)
			if (obj instanceof PlayerClass)
				if (buyer.hasPermission(((PlayerClass) obj).getPermission()) || buyer.hasPermission("rtg.class.all"))
					i++;
		return i;
	}

	/**
	 * Attempts to generate a ItemStack for a (old style) class
	 * @param name The name of the class
	 * @param contants The contents of the class
	 * @return An ItemSTack to represent the class in a GUI
	 * @deprecated
	 */
	@SuppressWarnings("unused")
	private ItemStack generateItem(String name, ItemStack[] contants) {
		ItemStack raw = new ItemStack(Material.getMaterial(itemID), 1);
		ItemMeta meta = raw.getItemMeta();
		meta.setDisplayName(ChatMessages.L_PURPLE + name);
		List<String> list = new ArrayList<String>();
		for (ItemStack item : contants) {
			String format = ChatMessages.format;
			String itemName = item.getItemMeta().getDisplayName();
			String amount = "" + item.getAmount();
			list.add(format.replace("%%ITEM%%", itemName == null ? ItemGui.getMaterialName(item.getType()) : itemName).replace("%%AMOUNT%%", amount));
		}
		meta.setLore(list);
		raw.setItemMeta(meta);
		return raw;
	}

	public void show() {
		buyer.openInventory(storeFront);
	}

	private int roundUp(int n) {
		return ((n + 9 - 1) / 9) * 9;
	}

	//Events
	@EventHandler (priority=EventPriority.MONITOR)
	public void onInvClose(InventoryCloseEvent event) {
		if (event.getPlayer().getEntityId() == buyer.getEntityId()) {
			HandlerList.unregisterAll(this);
		}
	}

	@EventHandler (priority=EventPriority.HIGH)
	public void onItemClicked(InventoryClickEvent event) {
		if (event.getWhoClicked().getName().equalsIgnoreCase(buyer.getName())) {
			try {
				if (event.getRawSlot() < storeFront.getSize()) {
					ItemStack is = event.getCurrentItem();
					for (PlayerClass pc : ClassLoader.classes)
						if (is.getItemMeta().getDisplayName().equals(pc.getGUIButton().getItemMeta().getDisplayName())) {
							ClassExecutor.choseClass(buyer, pc.getName());
							close();
						}
					event.setCancelled(true);

				}
				else if (event.isShiftClick())
					event.setCancelled(true);
			} catch (NullPointerException e) {
				event.setCancelled(true);
			}
		}
	}

	private void close() {
		buyer.closeInventory();
	}

}
