package ca.kanoa.rodsthegame.classes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kieronwiltshire.rods.gamemode.ChatMessages;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.permissions.Permission;
import org.bukkit.potion.PotionEffect;

import ca.kanoa.RodsTwo.RodsTwo;
import ca.kanoa.RodsTwo.Objects.Rod;
import ca.kanoa.rodsthegame.gui.ItemGui;

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
	 * @throws PlayerClassFormatException Thrown if a format exception occurs
	 */
	public static PlayerClass parseString(String fileStr, String name) throws PlayerClassFormatException {
		String[] lines = fileStr.split("\n");
		PlayerClass pClass = new PlayerClass(name);
		for (String str : lines) {
			/*
			 * Debug messages
			 */
			//Main.debug(name + ": " + str);
			int amount;
			String itemName;
			try {
				itemName = str.substring(str.indexOf('.') + 1, str.contains(":") ? str.indexOf(':') : str.length());
				try {
					amount = str.contains(":") ? Integer.parseInt(str.split(":")[1]) : 1;
				} catch (NumberFormatException e) {
					amount = 1;
				}
			} catch (Exception e){
				throw new PlayerClassFormatException("Error (" + e.toString() + ") loading " + name + "!");
			}

			switch (ItemType.getItemType(str)) {

			//LightningRod
			case ROD:
				Rod rod = RodsTwo.getRod(itemName);
				if (rod == null)
					throw new PlayerClassFormatException("Unknown rod while loading: " + 
							itemName + ", on line: " + str + ", in class " + 
							name);
				else
					pClass.addRod(rod, amount);
				break;

				//Armour Boots
			case BOOTS:
				Material boots = Material.getMaterial(itemName.toUpperCase());
				if (boots == null)
					throw new PlayerClassFormatException("Unknown material type for boots while loading: " + 
							itemName + ", on line: " + str + ", in class " + 
							name);
				else
					pClass.setBoots(new ItemStack(boots, amount));
				break;

				//Armour Chestplate
			case CHESTPLATE:
				Material chestplate = Material.getMaterial(itemName.toUpperCase());
				if (chestplate == null)
					throw new PlayerClassFormatException("Unknown material type for chestplate while loading: " + 
							itemName + ", on line: " + str + ", in class " + 
							name);
				else
					pClass.setChestplate(new ItemStack(chestplate, amount));
				break;

				//Armour Helmet
			case HELMET:
				Material helmet = Material.getMaterial(itemName.toUpperCase());
				if (helmet == null)
					throw new PlayerClassFormatException("Unknown material type for helmet while loading: " + 
							itemName + ", on line: " + str + ", in class " + 
							name);
				else
					pClass.setHelmet(new ItemStack(helmet, amount));
				break;

				//Item by Name
			case ITEM:
				Material item = Material.getMaterial(itemName.toUpperCase());
				if (item == null)
					throw new PlayerClassFormatException("Unknown material type while loading: " + 
							itemName + ", on line: " + str + ", in class " + 
							name);
				else
					pClass.addItem(new ItemStack(item, amount));
				break;

				//Item by ID
			case ITEMID:
				int itemID;
				try {
					itemID = Integer.parseInt(itemName);
				} catch (NumberFormatException e) {
					throw new PlayerClassFormatException("Unknown material type while loading: " + itemName + ", on line: " + str + ", in class " + name);
				}
				Material itemIDMaterial = Material.getMaterial(itemID);
				if (itemIDMaterial == null)
					throw new PlayerClassFormatException("Unknown item ID while loading: " + 
							itemName + ", on line: " + str + ", in class " + 
							name);
				else
					pClass.addItem(new ItemStack(itemIDMaterial, amount));
				break;

				//Armour Leggings
			case LEGGINGS:
				Material leggings = Material.getMaterial(itemName.toUpperCase());
				if (leggings == null)
					throw new PlayerClassFormatException("Unknown material type for leggings while loading: " + 
							itemName + ", on line: " + str + ", in class " + 
							name);
				else
					pClass.setLeggings(new ItemStack(leggings, amount));
				break;

				//Looks by ID or Name
			case LOOKS:
				Material looks;
				try {
					looks = Material.getMaterial(Integer.parseInt(itemName));
				} catch (NumberFormatException e) {
					looks = Material.getMaterial(itemName.toUpperCase());
				}
				if (looks == null)
					throw new PlayerClassFormatException("Unknown material for look while loading: " + 
							itemName + ", on line: " + str + ", in class " + 
							name);
				else
					pClass.setLook(looks);
				break;

				//Potion Effect [WIP]
			case POTIONEFFECT:
				break;

				//Unknown
			case UNKNOWN:
				throw new PlayerClassFormatException("Unknown item type while loading line: " + 
						str + ", in class: " + 
						name);
			default:
				throw new PlayerClassFormatException("Unknown item type while loading line: " + 
						str + ", in class: " + 
						name);

			}
		}
		return pClass;
	}

	public static enum ItemType {
		//Items
		ROD,
		ITEM,
		ITEMID,
		//Armour
		HELMET, 
		CHESTPLATE, 
		LEGGINGS, 
		BOOTS,
		//Other
		POTIONEFFECT, 
		LOOKS,
		UNKNOWN;

		/**
		 * Will attempt to figure out what type of item a string is
		 * @param item The string to check
		 * @return The ItemType the string is
		 */
		public static ItemType getItemType(String item) {
			if (item.toLowerCase().startsWith("rod"))
				return ItemType.ROD;
			else if (item.toLowerCase().startsWith("itemid"))
				return ItemType.ITEMID;
			else if (item.toLowerCase().startsWith("item"))
				return ItemType.ITEM;
			else if (item.toLowerCase().startsWith("helmet"))
				return ItemType.HELMET;
			else if (item.toLowerCase().startsWith("chestplate"))
				return ItemType.CHESTPLATE;
			else if (item.toLowerCase().startsWith("leggings"))
				return ItemType.LEGGINGS;
			else if (item.toLowerCase().startsWith("boots"))
				return ItemType.BOOTS;
			else if (item.toLowerCase().startsWith("potion") || item.toLowerCase().startsWith("effect"))
				return ItemType.POTIONEFFECT;
			else if (item.toLowerCase().startsWith("look"))
				return ItemType.LOOKS;
			else
				return  UNKNOWN;
		}
	}

	//PlayerClass Object

	private final String name;
	private final Permission permission;
	private List<ItemStack> items;
	private Material look;
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
		this.look = Material.PISTON_EXTENSION;
	}

	/**
	 * Will apply a class to a player by clearing their inventory they giving them all items/effects associated with this class
	 * @param player The player to apply the class to
	 */
	public void applyClass(Player player) {
		//Clear everything in the inventory currently
		//Remove potion effects
		for (PotionEffect effect : player.getActivePotionEffects())
			player.removePotionEffect(effect.getType());
		//Clear items and rods
		player.getInventory().clear();

		//Give the player his/her armour, items, rods, and potion effects
		//First we add the items and rods
		for (ItemStack item : this.getItems())
			player.getInventory().addItem(item);
		//Now we add the potion effects
		for (PotionEffect effect : this.getPotionEffects())
			player.addPotionEffect(effect);
		//Then we give them armour
		player.getInventory().setHelmet(this.getHelmet());
		player.getInventory().setChestplate(this.getChestplate());
		player.getInventory().setLeggings(this.getLeggings());
		player.getInventory().setBoots(this.getBoots());
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
	 * Gets a item that can be used as a button for this class in a gui/ui
	 * @return This classes button
	 */
	public ItemStack getGUIButton() {
		ItemStack button = new ItemStack(look, 1);
		ItemMeta meta = button.getItemMeta();
		meta.setDisplayName(ChatMessages.L_PURPLE + this.name);
		List<String> strList = new ArrayList<String>();
		String format = ChatMessages.format;
		for (ItemStack item : this.items) {
			String itemName = item.getItemMeta() == null || item.getItemMeta().getDisplayName() == null ? ItemGui.getMaterialName(item.getType()) : item.getItemMeta().getDisplayName();
			String amount = "" + item.getAmount();
			strList.add(ChatMessages.GOLD + format.replace("%%ITEM%%", itemName).replace("%%AMOUNT%%", amount));
		}
		for (PotionEffect effect : this.potionEffects) {
			String itemName = effect.getType().getName();
			int level = effect.getAmplifier() + 1;
			int duration = effect.getDuration() / 20;
			strList.add(ChatMessages.BLUE + format.replace("%%ITEM%%", itemName).replace("%%AMOUNT%%", duration + "*" + level));
		}
		if (this.helm != null)
			strList.add(ChatMessages.D_AQUA + "Helmet: " + ChatMessages.L_PURPLE + ItemGui.getMaterialName(this.helm.getType()));
		if (this.chestplate != null)
			strList.add(ChatMessages.D_AQUA + "Chestplate: " + ChatMessages.L_PURPLE + ItemGui.getMaterialName(this.chestplate.getType()));
		if (this.leggings != null)
			strList.add(ChatMessages.D_AQUA + "Leggings: " + ChatMessages.L_PURPLE + ItemGui.getMaterialName(this.leggings.getType()));
		if (this.boots != null)
			strList.add(ChatMessages.D_AQUA + "Boots: " + ChatMessages.L_PURPLE + ItemGui.getMaterialName(this.boots.getType()));
		meta.setLore(strList);
		button.setItemMeta(meta);
		return button;
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

	/**
	 * Gets the material that should represent this class in a inventory/GUI
	 * @return This classes look
	 */
	public Material getLook() {
		return look;
	}

	/**
	 * Sets the material that should represent this class in a inventory/GUI
	 * @param look This classes look
	 */
	public void setLook(Material look) {
		this.look = look;
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
