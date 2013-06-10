package kieronwiltshire.rods.gamemode;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class SignHandler implements Listener{

	private Main plugin;
	public SignHandler(Main plugin)
	{
		this.plugin = plugin;
	}

	@EventHandler
	public void hitSign(PlayerInteractEvent event){
		Player p = event.getPlayer();
		if ((event.getAction() == Action.RIGHT_CLICK_BLOCK) && (event.getClickedBlock() != null)) {
			if ((event.getClickedBlock().getType() == Material.WALL_SIGN) || (event.getClickedBlock().getType() == Material.SIGN_POST) || (event.getClickedBlock().getType() == Material.SIGN)) {
				Sign s = (Sign) event.getClickedBlock().getState();

				if(s.getLine(1).equalsIgnoreCase("random spawn")){
					plugin.teleportHandle.teleportRandomSpawn(p);
				}

				//CLASS INDEX!
				//<ranger> <Index = 1>
				//<tank> <Index = 2>
				//<attacker> <Index = 3>
				//<defender> <Index = 4>
				//<ninja> <Index = 5>




				else if(s.getLine(0).equalsIgnoreCase("[class]")){

					if (s.getLine(1).equalsIgnoreCase("random")) {
						int classNum = (new Random()).nextInt(ClassLoader.classes.size());
						String str = ClassLoader.classes.keySet().toArray(new String[0])[classNum];
						plugin.playerClasses.put(p.getName(), str);
						p.sendMessage(ChatMessages.chosenClass + str);
					}
					
					else
						for (String str : ClassLoader.classes.keySet())
							if (s.getLine(1).equalsIgnoreCase(str)) {
								if (p.hasPermission("rtg.class." + str) || p.hasPermission("rtg.class.all")) {
									plugin.playerClasses.put(p.getName(), str);
									p.sendMessage(ChatMessages.chosenClass + str);
								}
								else
									p.sendMessage(ChatMessages.noClassPermission);
							}

				}	
			}
		}    
	}
}
