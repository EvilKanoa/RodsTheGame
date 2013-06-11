package ca.kanoa.rodsthegame.gui;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Item {

	private ItemStack item;

	public Item(String title, String[] lore, Material material) {
		item = new ItemStack(material, 1);
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(title);
		im.setLore(Arrays.asList(lore));
		item.setItemMeta(im);
	}

	public Item(String title, Material material) {
		this(title, new String[]{}, material);
	}

	public Item(String title, String[] lore, int material) {
		this(title, lore, Material.getMaterial(material));
	}
	
	public Item(String title, int material) {
		this(title, new String[]{}, material);
	}
	
	public Item(int material) {
		this(Material.getMaterial(material));
	}
	
	public Item(Material material) {
		this(ItemGui.getMaterialName(material), material);
	}
	
	/**
	 * Generates a ItemStack for the specified options
	 * @return The ItemStack with the specified options
	 */
	public ItemStack getStack() {
		return this.item;
	}

}
