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

	public ItemGui(Player buyer, String title, int rows) {
		this.buyer = buyer;
		this.storeFront = Bukkit.createInventory(buyer, rows, title);
		for (ItemStack im : populateStore())
			storeFront.addItem(im);
		Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
	}

	public static int roundUp(int n) {
		return ((n + 9 - 1) / 9) * 9;
	}

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
			if (event.getRawSlot() < storeFront.getSize()) {
				this.itemClicked(new ClickedItem(event.getCurrentItem(), event.getSlot(), event.isShiftClick()));
				event.setCancelled(true);
			}
			else if (event.isShiftClick())
				event.setCancelled(true);
		}
	}
}
