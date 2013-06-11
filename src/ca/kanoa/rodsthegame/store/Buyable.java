package ca.kanoa.rodsthegame.store;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class Buyable {

	public static List<Buyable> items;
	
	private final Permission node;
	private String className;
	private String description;
	private int cost;

	public Buyable(Permission node, String className, String description, int cost) {
		this.node = node;
		this.setName(className);
		this.setDescription(description);
		this.setCost(cost);
	}

	public String getName() {
		return className;
	}

	public void setName(String name) {
		this.className = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Permission getNode() {
		return node;
	}

	public static Buyable parseString(String fileStr) throws BuyableFormatException {
		String[] lines = fileStr.split("\n");
		String perm = null;
		String name = null;
		String desc = null;
		int price = -1;
		for (String str : lines) {
			if (str.toLowerCase().startsWith("node:")) 
				perm = str.replace("node:", "").trim().toLowerCase();

			else if (str.toLowerCase().startsWith("name:")) 
				name = str.replace("name:", "").trim();

			else if (str.toLowerCase().startsWith("description:")) 
				desc = str.replace("description:", "").trim();

			else if (str.toLowerCase().startsWith("cost:"))
				try {
					price = Integer.parseInt(str.replace("cost:", "").replace("xp", "").replace("exp", "").replace(" ", "").trim());
				} catch (NumberFormatException e) {
					try {
						throw new BuyableFormatException(str + " is not a number!");
					} catch (BuyableFormatException e1) {
						e1.printStackTrace();
					}
				}

			else
				try {
					throw new BuyableFormatException(str);
				} catch (BuyableFormatException e) {
					e.printStackTrace();
				}
		}
		if (perm == null || name == null || desc == null || price == -1)
			try {
				throw new BuyableFormatException("all lines");
			} catch (BuyableFormatException e) {
				e.printStackTrace();
				throw new BuyableFormatException("all lines");
			}
		return new Buyable(new Permission(perm), name, desc, price);
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}
	
	public static boolean isForSale(String name) {
		for (Buyable b : items)
			if (b.getName().equalsIgnoreCase(name))
				return true;
		return false;
	}
	
	public static boolean hasPermissionFor(Buyable item, Player player) {
		return player.hasPermission(item.getNode());
	}
	
	public static Buyable getBuyable(String name) {
		for (Buyable item : items)
			if (item.getName().equalsIgnoreCase(name))
				return item;
		return null;
	}

}
