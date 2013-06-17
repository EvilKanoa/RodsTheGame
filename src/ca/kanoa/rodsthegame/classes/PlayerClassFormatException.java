package ca.kanoa.rodsthegame.classes;

import org.bukkit.ChatColor;

public class PlayerClassFormatException extends Exception {

	private static final long serialVersionUID = 7460433923761040777L;
	
	public PlayerClassFormatException(String exception) {
		super(ChatColor.RED + exception);
	}

}
