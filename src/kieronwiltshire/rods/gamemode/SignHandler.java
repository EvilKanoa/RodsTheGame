package kieronwiltshire.rods.gamemode;

import java.util.Random;

import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import ca.kanoa.rodsthegame.classes.ClassExecutor;
import ca.kanoa.rodsthegame.classes.ClassLoader;
import ca.kanoa.rodsthegame.classes.PlayerClass;

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
						PlayerClass pc = null;
						int i = 0;
						while (pc == null) {
							PlayerClass temp = ClassLoader.classes.toArray(new PlayerClass[0])[classNum];
							if (p.hasPermission(temp.getPermission()) || p.hasPermission("rtg.class.all"))
								pc = temp;
							if (i++ > 30)
								return;
						}
						ClassExecutor.choseClass(p, pc.getName());
					}
					
					else
						ClassExecutor.choseClass(p, s.getLine(1));

				}	
			}
		}    
	}
}
