package kieronwiltshire.rods.gamemode;

import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import ca.kanoa.batman.utils.SQL;

public class EndGameManager extends FixedRunnable {

	private Main plugin;
	public EndGameManager(Main plugin)
	{
		this.plugin = plugin;
	}


	//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
	//<    METHODS                -   INDEX   >
	//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
	//<gameEnd Method             - <Index = 1>
	//<getWinner Method           - <Index = 2>
	//<loop Method                - <Index = 3>



	//<Index = 1>
	public void gameEnd(){
		plugin.startEndGameTimer();
		Player p = getWinner();

		if (p != null) {
			String pName = p.getName();
			Bukkit.broadcastMessage(ChatMessages.winner.
					replace("%%PLAYER%%", pName).
					replace("%%KILLS%%", "" + plugin.count.get(pName)));
			SQL.addWin(pName);
		}

		for(Player a : Bukkit.getOnlinePlayers()) {
			a.setGameMode(GameMode.CREATIVE);
			plugin.gameEnded = true;
			for(Player s : Bukkit.getOnlinePlayers()) 
				s.showPlayer(a);
		}
	}





	//<Index = 2>
	public Player getWinner(){
		Entry<String, Integer> highest = null;
		for (Entry<String, Integer> e : plugin.count.entrySet()) {
			if (highest == null ? true : highest.getValue() < e.getValue())
				highest = e;
		}
		if (highest != null)
			return Bukkit.getServer().getPlayer(highest.getKey());
		else
			return null;
	}


	//<Index = 3>
	@Override
	public void loop() {
		if(Main.waitTimer == 0){
			this.cancel();
			plugin.resetHandle.pluginReset();
		}
		Main.waitTimer--;
	}


}
