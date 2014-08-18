package kieronwiltshire.rods.gamemode;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import ca.kanoa.rodsthegame.FixedRunnable;
import ca.kanoa.rodsthegame.ScoreboardHandler;

public class LobbyHandler extends FixedRunnable{

	private Main plugin;
	public LobbyHandler(Main plugin)
	{
		this.plugin = plugin;
	}




	//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
	//<    METHODS              -   INDEX   >
	//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
	//<chooseMap Method         - <Index = 1>
	//<stopTimer Method         - <Index = 2>
	//<loop Method              - <Index = 3>





	//<Index = 1>
	public void chooseMap() {
		try {
			if (Main.mapIndex >= Main.maps.size()) {
				Main.mapIndex = 0;
			}
			Main.selectedMap = Main.maps.get(Main.mapIndex++);
			plugin.CheckMap();
		} catch (NullPointerException e) {
			System.out.print("No maps currently defined! use the [/import <map>] command.");
		}
	}




	//<Index = 2>
	public void stopTimer(){
		this.cancel();
	}






	//<Index = 3>
	@Override
	public void loop() {
		if(Main.lobbyTimer == 3600)
			Bukkit.broadcastMessage(ChatMessages.sixtyMinLobby);
		if(Main.lobbyTimer == 2700)
			Bukkit.broadcastMessage(ChatMessages.fortyFiveMinLobby);
		if(Main.lobbyTimer == 1800)
			Bukkit.broadcastMessage(ChatMessages.thirtyMinLobby);
		if(Main.lobbyTimer == 1200)
			Bukkit.broadcastMessage(ChatMessages.twentyMinLobby);
		if(Main.lobbyTimer == 600)
			Bukkit.broadcastMessage(ChatMessages.tenMinLobby);
		if(Main.lobbyTimer == 300)
			Bukkit.broadcastMessage(ChatMessages.fiveMinLobby);
		if(Main.lobbyTimer == 120)
			Bukkit.broadcastMessage(ChatMessages.twoMinLobby);
		if(Main.lobbyTimer == 60)
			Bukkit.broadcastMessage(ChatMessages.oneMinLobby);				
		if(Main.lobbyTimer == 30)
			Bukkit.broadcastMessage(ChatMessages.thirtySecLobby);	
		if(Main.lobbyTimer == 20)
			Bukkit.broadcastMessage(ChatMessages.twentySecLobby);			
		if(Main.lobbyTimer == 10)
			Bukkit.broadcastMessage(ChatMessages.tenSecLobby);					
		if(Main.lobbyTimer == 5)
			Bukkit.broadcastMessage(ChatMessages.fiveSecLobby);	
		if(Main.lobbyTimer == 4)
			Bukkit.broadcastMessage(ChatMessages.fourSecLobby);	
		if(Main.lobbyTimer == 3)
			Bukkit.broadcastMessage(ChatMessages.threeSecLobby);	
		if(Main.lobbyTimer == 2)
			Bukkit.broadcastMessage(ChatMessages.twoSecLobby);	
		if(Main.lobbyTimer == 1)
			Bukkit.broadcastMessage(ChatMessages.oneSecLobby);	
		if(Main.lobbyTimer == 0){
			int online = 0;
			for (Player p : Bukkit.getOnlinePlayers()) {
				if (p.isOnline()) {
					online++;
				}
			}
			
			if (online >= Main.neededPlayers) {
				Main.lobbyBoolean = false;
				plugin.startGameTimer();
				plugin.teleportHandle.teleportAllToMap();
				plugin.game.enabled = true;
				stopTimer();
				for (Player player : Bukkit.getOnlinePlayers())
					ScoreboardHandler.sendBoard(player);
			}
			else {
				Main.lobbyTimer = 302;
				Bukkit.broadcastMessage(ChatMessages.tooFewPlayers.replace("%%AMOUNT%%", "" + Main.neededPlayers));
			}

		}	
		Main.lobbyTimer--;
	}


}
