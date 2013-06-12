package kieronwiltshire.rods.gamemode;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.scheduler.BukkitRunnable;

import ca.kanoa.RodsTwo.Objects.PlayerUseRodEvent;
import ca.kanoa.batman.utils.Healer;
import ca.kanoa.batman.utils.InventoryClear;
import ca.kanoa.batman.utils.SQL;
import ca.kanoa.rodsthegame.ScoreboardHandler;

public class PlayerHandler extends BukkitRunnable implements Listener{

	private Main plugin;
	public PlayerHandler(Main plugin) {
		this.plugin = plugin;
		Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 20l, 20l);
	}



	//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	//<    METHODS             -   INDEX   >
	//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	//<onPlayerJoin Method      - <Index = 1>
	//<onPlayerQuit Method      - <Index = 2>
	//<onPlayerDeath Method     - <Index = 3>
	//<onPlayerRespawn Method   - <Index = 4>





	//<Index = 1>
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event){
		Player p = event.getPlayer();
		String pName = p.getName();

		event.setJoinMessage(ChatMessages.WHITE + pName + ChatMessages.joinMessage);
		
		if (!ScoreboardHandler.isOnBoard(p)) {
			ScoreboardHandler.initPlayer(p);
			ScoreboardHandler.sendBoard(p);
		}
		else
			if (Main.lobbyBoolean)
				ScoreboardHandler.hide(p);
			else
				ScoreboardHandler.sendBoard(p);
		
		Main.playerClasses.put(pName, "default");
		p.setGameMode(GameMode.SURVIVAL);
		InventoryClear.clear(p);
		p.sendMessage(" ");
		p.sendMessage(ChatMessages.welcomeMessage);
		p.sendMessage(ChatMessages.welcomeMessage2);
		p.sendMessage(" ");
		p.sendMessage(ChatMessages.alphaTestMessage1);
		p.sendMessage(ChatMessages.alphaTestMessage2);
		p.sendMessage(" ");
		Healer.simulateRespawn(p);
		if(Main.lobbyBoolean == true){
			plugin.teleportHandle.teleportServerLobby(p);
		}
		else {
			plugin.teleportHandle.teleportToMapLobby(p);
			p.sendMessage(ChatMessages.selectedMapIs.replace("%%MAP%%", Main.selectedMap));
		}

	}


	@EventHandler(priority=EventPriority.HIGH)
	public void onJoinWhileReseting(AsyncPlayerPreLoginEvent event) {
		if (plugin.resetMode) {
			event.setKickMessage(ChatMessages.resetMessage);
			event.setLoginResult(Result.KICK_OTHER);
		}
	}


	//<Index = 2>
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event){
		Player p = event.getPlayer();
		final String pName = p.getName();
		
		event.setQuitMessage(ChatMessages.WHITE + pName + ChatMessages.quitMessage);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			@Override
			public void run() {
				if(Main.lobbyBoolean == false && Bukkit.getOnlinePlayers().length < 2){
					Main.gameTimer = 5;
				}
			}}, 100);
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), new Runnable(){
			@Override
			public void run() {
				if (!Bukkit.getOfflinePlayer(pName).isOnline())
					ScoreboardHandler.removePlayer(pName);
			}}, 300L);
	}

	@EventHandler(priority=EventPriority.MONITOR)
	public void onRodUse(PlayerUseRodEvent event) {
		SQL.addRodUsed(event.getPlayer().getName());
	}



	//<Index = 3>
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event){
		Player att = event.getEntity().getKiller();
		String pName = event.getEntity().getName();
		if(att instanceof Player && att.getEntityId() != event.getEntity().getEntityId()){
			String aName = att.getName();
			SQL.addKill(aName);
			SQL.addMoney(aName, SQL.getMoney(pName) > SQL.getMoney(aName) ? 10 : 5);
			ScoreboardHandler.addKill(aName);
		}
		SQL.addDeath(pName);
		event.getDrops().clear();
	}

	@EventHandler
	public void onPlayerHungerLoss(FoodLevelChangeEvent e){
		e.setCancelled(true);
		if (e.getEntity() instanceof Player)
			((Player)e.getEntity()).setFoodLevel(Main.lobbyBoolean ? 20 : 16);
	}



	//<Index = 4>
	@EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent event){

		Player p = event.getPlayer();

		double x = plugin.getConfig().getInt("maps." + Main.selectedMap + ".lobby.x");
		double y = plugin.getConfig().getInt("maps." + Main.selectedMap + ".lobby.y");
		double z = plugin.getConfig().getInt("maps." + Main.selectedMap + ".lobby.z");
		float yaw = plugin.getConfig().getInt("maps." + Main.selectedMap + ".lobby.yaw");
		float pitch = plugin.getConfig().getInt("maps." + Main.selectedMap + ".lobby.pitch");

		World world = Bukkit.getServer().getWorld(Main.selectedMap);

		Location spawn = new Location(world, x, y, z, yaw, pitch);
		event.setRespawnLocation(spawn);

		p.teleport(spawn);
		InventoryClear.clear(p);
		Healer.simulateRespawn(p);

		p.sendMessage(ChatMessages.selectedMapIs.replace("%%MAP%%", Main.selectedMap));

	}

	@EventHandler
	public void onPlayerAttack(EntityDamageByEntityEvent event){
		Entity att = event.getDamager();
		Entity ent = event.getEntity();

		if(ent instanceof Player){
			if(att instanceof Player){
				if(plugin.spectators.contains(((Player) att).getName())) event.setCancelled(true);
				if(Main.lobbyBoolean) event.setCancelled(true);
			}
		}
		else{

		}
	}

	@EventHandler(priority=EventPriority.MONITOR)
	public void NoBlockBreak(BlockBreakEvent event){
		if(event.getPlayer().hasPermission("rtg.break"))
			event.setCancelled(false);
		else
			event.setCancelled(true);
	}

	@EventHandler(priority=EventPriority.MONITOR)
	public void NoBlockPlace(BlockPlaceEvent event){
		if(event.getPlayer().hasPermission("rtg.place"))
			event.setCancelled(false);
		else
			event.setCancelled(true);
	}

	@EventHandler
	public void onPlayerDamage(EntityDamageEvent event){
		Entity ent = event.getEntity();

		if(ent instanceof Player){
			if(Main.lobbyBoolean) event.setCancelled(true);
		}
		else {

		}
	}



	@Override
	public void run() {
		for (Player p : Bukkit.getOnlinePlayers())
			p.setFoodLevel(Main.lobbyBoolean ? 20 : 16);
	}

}
