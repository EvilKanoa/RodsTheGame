package kieronwiltshire.rods.gamemode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

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

import ca.kanoa.RodsTwo.RodsTwo;

public class ClassGui implements Listener {

	private Player buyer;
	private Inventory storeFront;

	public ClassGui(Player sender) {
		this.buyer = sender;
		populateStore();
		Bukkit.getPluginManager().registerEvents(this, RodsTwo.plugin);
	}

	private void populateStore() {
		storeFront = Bukkit.createInventory(buyer, roundUp(sizeWithPerms(ClassLoader.classes.keySet())), ChatMessages.guiTitle);
		for (Entry<String, ItemStack[]> cls : ClassLoader.classes.entrySet())
			if (buyer.hasPermission("rtg.class." + cls.getKey()))
				storeFront.addItem(generateItem(cls.getKey(), cls.getValue()));
	}

	private int sizeWithPerms(Set<String> set) {
		int i = 0;
		for (String str : set)
			if (buyer.hasPermission("rtg.class." + str.toLowerCase()))
				i++;
		return i;
	}

	private ItemStack generateItem(String name, ItemStack[] contants) {
		ItemStack raw = new ItemStack(Material.getMaterial(36), 1);
		ItemMeta meta = raw.getItemMeta();
		meta.setDisplayName(ChatMessages.L_PURPLE + name);
		List<String> list = new ArrayList<String>();
		for (ItemStack item : contants) {
			String format = ChatMessages.format;
			String itemName = item.getItemMeta().getDisplayName();
			String amount = "" + item.getAmount();
			list.add(format.replace("%%ITEM%%", itemName == null ? getMaterialName(item.getType()) : itemName).replace("%%AMOUNT%%", amount));
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
			if (event.getRawSlot() < storeFront.getSize()) {
				ItemStack is = event.getCurrentItem();
				for (Entry<String, ItemStack[]> item : ClassLoader.classes.entrySet())
					if (is != null && is.getItemMeta() != null && 
					is.getItemMeta().getDisplayName().equals(ChatMessages.L_PURPLE + item.getKey())) {
						ClassExecutor.choseClass(buyer, item.getKey());
						event.setCancelled(true);
					}
			}
			else if (event.isShiftClick())
				event.setCancelled(true);
		}
	}

	public static String getMaterialName(Material material) {
		StringBuilder materialName = new StringBuilder();
		for(String c : material.toString().toLowerCase().split("_")) {
			materialName.append(Character.toUpperCase(c.charAt(0))).append(c, 1, c.length()).append(" ");
		}
		return materialName.toString().trim();
	}

}
