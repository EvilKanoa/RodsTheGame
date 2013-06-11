package ca.kanoa.rodsthegame.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.inventory.ItemStack;

public class ClickedItem {

	private ItemStack item;
	private int slot;
	private boolean shiftClicked;
	
	public ClickedItem(ItemStack item, int slot, boolean shifted) {
		this.item = item;
		this.slot = slot;
		this.shiftClicked = shifted;
	}
	
	public ItemStack getItem() {
		return this.item;
	}
	
	public int getSlot() {
		return this.slot;
	}
	
	public boolean wasShiftClick() {
		return this.shiftClicked;
	}
	
	public String getName() {
		if (item.getItemMeta().getDisplayName() != null)
			return item.getItemMeta().getDisplayName();
		else
			return ItemGui.getMaterialName(item.getType());
	}
	
	public List<String> getLore() {
		if (item.getItemMeta().getLore() != null)
			return item.getItemMeta().getLore();
		else
			return new ArrayList<String>();
	}
	
}
