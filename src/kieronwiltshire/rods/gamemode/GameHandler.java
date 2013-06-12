package kieronwiltshire.rods.gamemode;

import org.bukkit.Bukkit;

import ca.kanoa.rodsthegame.FixedRunnable;

public class GameHandler extends FixedRunnable{

	private Main plugin;
	public GameHandler(Main plugin)
	{
		this.plugin = plugin;
	}
	
	public boolean enabled = false;
	
	
	
	//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
	//<    METHODS              -   INDEX   >
	//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
	//<stopTimer Method         - <Index = 1>
	//<loop Method              - <Index = 2>
	
	
	
	//<Index = 1>
	public void stopTimer(){
		this.cancel();
	}
	
	
	//<Index = 2>
	@Override
	public void loop() {
		if(Main.gameTimer == 3600)
			Bukkit.broadcastMessage(ChatMessages.sixtyMinGame);
		if(Main.gameTimer == 2700)
			Bukkit.broadcastMessage(ChatMessages.fortyFiveMinGame);
		if(Main.gameTimer == 1800)
			Bukkit.broadcastMessage(ChatMessages.thirtyMinGame);
		if(Main.gameTimer == 1200)
			Bukkit.broadcastMessage(ChatMessages.twentyMinGame);
		if(Main.gameTimer == 600)
			Bukkit.broadcastMessage(ChatMessages.tenMinGame);
		if(Main.gameTimer == 300)
			Bukkit.broadcastMessage(ChatMessages.fiveMinGame);
		if(Main.gameTimer == 120)
			Bukkit.broadcastMessage(ChatMessages.twoMinGame);
		if(Main.gameTimer == 60)
			Bukkit.broadcastMessage(ChatMessages.oneMinGame);				
		if(Main.gameTimer == 30)
			Bukkit.broadcastMessage(ChatMessages.thirtySecGame);	
		if(Main.gameTimer == 20)
			Bukkit.broadcastMessage(ChatMessages.twentySecGame);			
		if(Main.gameTimer == 10)
			Bukkit.broadcastMessage(ChatMessages.tenSecGame);					
		if(Main.gameTimer == 5)
			Bukkit.broadcastMessage(ChatMessages.fiveSecGame);	
		if(Main.gameTimer == 4)
			Bukkit.broadcastMessage(ChatMessages.fourSecGame);	
		if(Main.gameTimer == 3)
			Bukkit.broadcastMessage(ChatMessages.threeSecGame);	
		if(Main.gameTimer == 2)
			Bukkit.broadcastMessage(ChatMessages.twoSecGame);	
		if(Main.gameTimer == 1)
			Bukkit.broadcastMessage(ChatMessages.oneSecGame);	
		if(Main.gameTimer == 0){
			
			stopTimer();
			plugin.endGame.gameEnd();
			
		}	
		Main.gameTimer--;
	}

}
