package ca.kanoa.rodsthegame.classes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permission;
import org.bukkit.potion.PotionEffect;

import ca.kanoa.RodsTwo.Objects.Rod;

public class PlayerClass {

	//Static methods
	
	/**
	 * Gives a player the specified class
	 * @param player The player to give the class to
	 * @param pClass The class to be given
	 */
	public static void applyClass(Player player, PlayerClass pClass) {
		//Clear everything in the inventory currently
		//Remove potion effects
		for (PotionEffect effect : player.getActivePotionEffects())
			player.removePotionEffect(effect.getType());
		//Clear items and rods
		player.getInventory().clear();
		
		//Give the player his/her armour, items, rods, and potion effects
		//First we add the items and rods
		for (ItemStack item : pClass.getItems())
			player.getInventory().addItem(item);
		//Now we add the potion effects
		for (PotionEffect effect : pClass.getPotionEffects())
			player.addPotionEffect(effect);
		//Then we give them armour
		player.getInventory().setHelmet(pClass.getHelmet());
		player.getInventory().setChestplate(pClass.getChestplate());
		player.getInventory().setLeggings(pClass.getLeggings());
		player.getInventory().setBoots(pClass.getBoots());
	}
	
	/**
	 * Reads a PlayerClass from a string retrieved from a file
	 * @param fileStr The string from the file
	 * @param name The name of the class
	 * @return The class read from the string
	 */
	public static PlayerClass parseString(String fileStr, String name) {
		String[] lines = fileStr.split("\n");
		PlayerClass pClass = new PlayerClass(name);
		return pClass;
	}
	
	//PlayerClass Object
	
	private final String name;
	private final Permission permission;
	private List<ItemStack> items;
	private ItemStack helm = null;
	private ItemStack chestplate = null;
	private ItemStack leggings = null;
	private ItemStack boots = null;
	private Set<PotionEffect> potionEffects;
	
	/**
	 * Creates a new class that a player can use
	 * @param name The name of the class
	 */
	public PlayerClass(String name) {
		this.name = name;
		this.permission = new Permission("rtg.class." + name, "The default permission for " + name + "class.");
		this.potionEffects = new HashSet<PotionEffect>();
		this.items = new ArrayList<ItemStack>();
	}
	
	/**
	 * Adds an item to this class
	 * @param item The item to be added
	 */
	public void addItem(ItemStack item) {
		items.add(item);
	}
	
	/**
	 * Adds a rod to this class
	 * @param rod The rod the be added
	 * @param amount The number to be included in this class
	 */
	public void addRod(Rod rod, int amount) {
		items.add(rod.getItem(amount));
	}
	
	/**
	 * Adds a potion effect to this class
	 * @param effect The potion effect to be added
	 */
	public void addPotionEffect(PotionEffect effect){
		potionEffects.add(effect);
	}
	
	/**
	 * Gets the potion effects associated with this class
	 * @return The PotionEffects associated with this class
	 */
	public Set<PotionEffect> getPotionEffects() {
		return this.potionEffects;
	}
	
	/**
	 * Gets the items (including rods) associated with this class
	 * @return The itemStacks associated with this class
	 */
	public List<ItemStack> getItems() {
		return this.items;
	}
	
	/**
	 * Gets the name of this clas
	 * @return This name of this class
	 */
	public String getName() {
		return this.name;
	}
	
	public Permission getPermission() {
		return this.permission;
	}
	
	//Armour contents below

	/**
	 * Gets the helmet associated with this class, null if none.
	 * @return The helmet associated with this class (null if none)
	 */
	public ItemStack getHelmet() {
		return helm;
	}

	/**
	 * Sets the helmet of this classes armour contents
	 * @param helm Helmet to be set in the armour contents of this class
	 */
	public void setHelmet(ItemStack helm) {
		this.helm = helm;
	}

	/**
	 * Gets the chestplate associated with this class, null if none.
	 * @return The chestplate associated with this class (null if none)
	 */
	public ItemStack getChestplate() {
		return chestplate;
	}

	/**
	 * Sets the chestplate of this classes armour contents
	 * @param helm Chestplate to be set in the armour contents of this class
	 */
	public void setChestplate(ItemStack chestplate) {
		this.chestplate = chestplate;
	}

	/**
	 * Gets the leggings associated with this class, null if none.
	 * @return The leggings associated with this class (null if none)
	 */
	public ItemStack getLeggings() {
		return leggings;
	}

	/**
	 * Sets the leggings of this classes armour contents
	 * @param helm Helmet to be set in the armour contents of this class
	 */
	public void setLeggings(ItemStack leggings) {
		this.leggings = leggings;
	}

	/**
	 * Gets the boots associated with this class, null if none.
	 * @return The boots associated with this class (null if none)
	 */
	public ItemStack getBoots() {
		return boots;
	}

	/**
	 * Sets the boots of this classes armour contents
	 * @param helm Boots to be set in the armour contents of this class
	 */
	public void setBoots(ItemStack boots) {
		this.boots = boots;
	}
	
}
