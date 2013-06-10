package kieronwiltshire.rods.gamemode;

import java.util.HashSet;

import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import ca.kanoa.batman.utils.WorldLoader;

public class ResetHandler extends FixedRunnable {

	private Main plugin;
	public ResetHandler(Main plugin)
	{
		this.plugin = plugin;
	}
	
	
	
	//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
	//<    METHODS              -   INDEX   >
	//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-
	//<pluginReset Method       - <Index = 1>
	//<stopTimer Method         - <Index = 2>
	//<loop Method              - <Index = 3>

	
	
	public int resetTimer;
	
	
	
	//<Index = 1>
	public void pluginReset(){
		
		ChatMessages.msg(ChatMessages.stageZero);
		
		plugin.endGame.cancel();
		plugin.resetHandle.cancel();
		plugin.game.cancel();
		plugin.resetHandle.cancel();
		
		Main.waitTimer = 15;
		resetTimer = 8;
		
		plugin.resetMode = true;
		for(Player a : Bukkit.getOnlinePlayers()) 
			a.kickPlayer(ChatMessages.kickMessage);
		
		ChatMessages.msg(ChatMessages.stageOne);
		if (Bukkit.getWorld("lobby") != null) {
			for (LivingEntity e : Bukkit.getWorld("lobby").getEntitiesByClass(LivingEntity.class))
				e.setHealth(0);
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
				public void run() {
					ChatMessages.msg(ChatMessages.stageThree);
					WorldLoader.unload("lobby");
				}}, 60);
		}
		
		ChatMessages.msg(ChatMessages.stageTwo);
		if (Bukkit.getWorld(Main.selectedMap) != null) {
			for (LivingEntity e : Bukkit.getWorld(Main.selectedMap).getEntitiesByClass(LivingEntity.class))
				e.setHealth(0);
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
				public void run() {
					ChatMessages.msg(ChatMessages.stageFour);
					WorldLoader.unload(Main.selectedMap);
				}}, 120);
		}
				
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			public void run() {
				
				ChatMessages.msg(ChatMessages.stageFive);
				
				plugin.game.enabled = false;
				
				plugin.copyLobby();
				plugin.lobby.chooseMap();
				plugin.getConfig();
				plugin.spectators = new HashSet<String>();
				Main.lobbyTimer = plugin.getConfig().getInt("lobby-timer");
				Main.gameTimer = plugin.getConfig().getInt("game-timer");
				ScoreboardHandler.reset();			
				if(plugin.spectators != null) 
					plugin.spectators.clear();
				plugin.startResetTimer();
				plugin.lobbyBoolean = true;
				
				
				
			}}, 300);	
		
	}
	
	
	
	
	
	
	
	
	//<Index = 2>
	public void stopTimer() {
		this.cancel();
	}
	
	
	

	//<Index = 3>
	@Override
	public void loop() {
		resetTimer--;
		if(resetTimer == 0){ 
			ChatMessages.msg(ChatMessages.stageSix);
			plugin.startLobbyTimer(); 
			stopTimer();
			ChatMessages.msg(ChatMessages.resetDone);
			plugin.resetMode = false; 
		}
	}
}	
