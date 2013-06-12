package ca.kanoa.rodsthegame.store;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;

public class Buyable {

	public static List<Buyable> items;
	
	private final Permission node;
	private final String nodeStr;
	private String className;
	private String description;
	private int cost;
	private Material look;

	public Buyable(String node, String className, String description, int cost, Material look) {
		this.node = new Permission(node);
		this.nodeStr = node;
		this.setName(className);
		this.setDescription(description);
		this.setCost(cost);
		this.setLook(look);
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
	
	public String getNodeString() {
		return nodeStr;
	}

	public static Buyable parseString(String fileStr) throws BuyableFormatException {
		String[] lines = fileStr.split("\n");
		String perm = null;
		String name = null;
		String desc = null;
		int mat = -1;
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
					price = Integer.parseInt(str.toLowerCase().replace("cost:", "").replace("xp", "").replace("exp", "").replace(" ", "").trim());
				} catch (NumberFormatException e) {
					try {
						throw new BuyableFormatException(str + " is not a number!");
					} catch (BuyableFormatException e1) {
						e1.printStackTrace();
					}
				}
			
			else if (str.toLowerCase().startsWith("look:"))
				try {
					mat = Integer.parseInt(str.toLowerCase().replace("look:", "").replace("mat", "").replace("material", "").replace(" ", "").trim());
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
		if (perm == null || name == null || desc == null || price == -1 || mat == -1)
			try {
				throw new BuyableFormatException("all lines");
			} catch (BuyableFormatException e) {
				e.printStackTrace();
				throw new BuyableFormatException("all lines");
			}
		return new Buyable(perm, name, desc, price, Material.getMaterial(mat));
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
		return player.hasPermission(item.getNodeString());
	}
	
	public static Buyable getBuyable(String name) {
		for (Buyable item : items)
			if (item.getName().equalsIgnoreCase(name))
				return item;
		return null;
	}
	
	public static int getCount(Player player) {
		int i = 0;
		for (Buyable b : items)
			if (!Buyable.hasPermissionFor(b, player))
				i++;
		return i;
	}

	public Material getLook() {
		return look;
	}

	public void setLook(Material look) {
		this.look = look;
	}

}
