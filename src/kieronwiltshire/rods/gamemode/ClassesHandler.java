package kieronwiltshire.rods.gamemode;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class ClassesHandler {

	private Main plugin;
	public ClassesHandler(Main plugin)
	{
		this.plugin = plugin;
	}



	//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	//<    METHODS             -   INDEX   >
	//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	//<customClasses Method    - <Index = 1>


	//<Index = 1>
	@SuppressWarnings("deprecation")
	public void customClasses(Player player){
		String pName = player.getName();
		String pClass = plugin.playerClasses.get(pName);

		for (ItemStack is : ClassLoader.classes.get(pClass))
			player.getInventory().addItem(is);
		player.updateInventory();


	}


}
