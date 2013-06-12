package ca.kanoa.rodsthegame.store;

import org.bukkit.permissions.Permission;

public class Buyable {
	
	private final Permission node;
	private String className;
	private String description;
	
	public Buyable(Permission node, String className, String description) {
		this.node = node;
		this.setClassName(className);
		this.setDescription(description);
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
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
		for (String str : lines) {
			if (str.toLowerCase().startsWith("node:")) 
				perm = str.replace("node:", "").trim().toLowerCase();
			
			else if (str.toLowerCase().startsWith("name:")) 
				name = str.replace("name:", "").trim();
				
			else if (str.toLowerCase().startsWith("description:")) 
				desc = str.replace("description:", "").trim();
				
			else
				try {
					throw new BuyableFormatException(str);
				} catch (BuyableFormatException e) {
					e.printStackTrace();
				}
		}
		if (perm == null || name == null || desc == null)
			try {
				throw new BuyableFormatException("all lines");
			} catch (BuyableFormatException e) {
				e.printStackTrace();
				throw new BuyableFormatException("all lines");
			}
		return new Buyable(new Permission(perm), name, desc);
	}
	
}