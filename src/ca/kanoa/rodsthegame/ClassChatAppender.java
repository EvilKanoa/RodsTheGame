package ca.kanoa.rodsthegame;

import kieronwiltshire.rods.gamemode.Main;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import ca.kanoa.batman.chat.ChatEvent;

public class ClassChatAppender implements Listener {

	@EventHandler(priority=EventPriority.LOW, ignoreCancelled=true)
	public void onChat(ChatEvent event) {
		String prePrefix;
		if (Main.lobbyBoolean)
			prePrefix = ChatColor.AQUA + "[" + ChatColor.GOLD + "Lobby" + ChatColor.AQUA + "] " + ChatColor.GREEN;
		else
			prePrefix = ChatColor.AQUA + "[" + 
					ChatColor.GOLD + 
					(Main.playerClasses.get(event.getPlayer().getName()) == "default" ? "Default" : Main.playerClasses.get(event.getPlayer().getName())) + 
					ChatColor.AQUA + "] " + 
					ChatColor.GREEN;
		event.setPrefix(prePrefix);
		event.setSuffix(ChatColor.WHITE + ": ");
	}
	
}
