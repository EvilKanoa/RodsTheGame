package ca.kanoa.rodsthegame.gui;

import java.util.List;
import kieronwiltshire.rods.gamemode.Main;

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

public abstract class ItemGui implements Listener {

	public abstract List<ItemStack> populateStore();
	public abstract void itemClicked(ClickedItem item);

	private Player buyer;
	private Inventory storeFront;

	/**
	 * Easy way to show the player a chest like UI/GUI
	 * @param buyer Player to make the UI for
	 * @param title The title of the inventory UI
	 * @param rows The number of rows the inventory should have (will have 9 columns)
	 */
	public ItemGui(Player buyer, String title, int rows) {
		this.buyer = buyer;
		this.storeFront = Bukkit.createInventory(buyer, rows * 9, title);
		int i = 0;
		for (ItemStack im : populateStore()) {
			if (im != null)
				storeFront.setItem(i, im);
			i++;
		}
		Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
	}

	public static int roundUp(int n) {
		return ((n + 9 - 1) / 9) * 9;
	}

	/**
	 * Opens the inventory UI to the buyer
	 */
	public void show() {
		buyer.openInventory(storeFront);
	}

	public static String getMaterialName(Material material) {
		StringBuilder materialName = new StringBuilder();
		for(String c : material.toString().toLowerCase().split("_")) {
			materialName.append(Character.toUpperCase(c.charAt(0))).append(c, 1, c.length()).append(" ");
		}
		return materialName.toString().trim();
	}
	
	public Player getBuyer() {
		return this.buyer;
	}

	//Events
	@EventHandler (priority=EventPriority.MONITOR, ignoreCancelled=true)
	public void onInvClose(InventoryCloseEvent event) {
		if (event.getPlayer().getEntityId() == buyer.getEntityId())
			HandlerList.unregisterAll(this);
	}

	@EventHandler (priority=EventPriority.HIGH)
	public void onItemClicked(InventoryClickEvent event) {
		if (event.getWhoClicked().getName().equalsIgnoreCase(buyer.getName())) {
			if (event.getRawSlot() < storeFront.getSize() && event.getCurrentItem() != null) {
				this.itemClicked(new ClickedItem(event.getCurrentItem(), event.getSlot(), event.isShiftClick()));
				event.setCancelled(true);
			}
			else if (event.isShiftClick())
				event.setCancelled(true);
		}
	}
}
