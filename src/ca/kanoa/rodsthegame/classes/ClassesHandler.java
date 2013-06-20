package ca.kanoa.rodsthegame.classes;

import kieronwiltshire.rods.gamemode.Main;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class ClassesHandler {



	//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	//<    METHODS             -   INDEX   >
	//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	//<customClasses Method    - <Index = 1>


	//<Index = 1>
	@SuppressWarnings("deprecation")
	public static void customClasses(Player player){
		String pClass = "null/unknown";
		try {
			pClass = Main.playerClasses.get(player.getName());
			ClassLoader.getClass(pClass).applyClass(player);
			player.updateInventory();
		} catch (NullPointerException e) {
			Bukkit.getLogger().severe("Error while attempting to give class " + pClass + " to player " + player.getName());
		}

	}


}
