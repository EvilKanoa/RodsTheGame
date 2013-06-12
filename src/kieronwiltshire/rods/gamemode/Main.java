package kieronwiltshire.rods.gamemode;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;

import ca.kanoa.batman.utils.WorldLoader;
import ca.kanoa.rodsthegame.Copier;
import ca.kanoa.rodsthegame.ScoreboardHandler;
import ca.kanoa.rodsthegame.WorldControlHandler;
import ca.kanoa.rodsthegame.classes.ClassExecutor;
import ca.kanoa.rodsthegame.classes.ClassLoader;
import ca.kanoa.rodsthegame.classes.ClassesHandler;
import ca.kanoa.rodsthegame.store.StoreExecutor;
import ca.kanoa.rodsthegame.store.StoreParser;

public class Main extends JavaPlugin implements Listener
{


	//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	//<    METHODS             -   INDEX   >
	//-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
	//<OnEnable Method         - <Index = 1>
	//<ConfigSaver Method      - <Index = 2>
	//<copyLobby Method        - <Index = 3>
	//<checkMap Method         - <Index = 4>
	//<startLobbyTimer Method  - <Index = 5>
	//<startGameTimer Method   - <Index = 6>
	//<startResetTimer Method  - <Index = 7>	
	//<onDisable Method        - <Index = 8>	







	private File mapWorld;
	private File lobbyWorld;

	public static String selectedMap = "";

	public static int lobbyTimer;
	public static int gameTimer;
	public static int waitTimer = 15;

	public static int neededPlayers = 2;
	
	public boolean gameEnded = true;

	public static boolean lobbyBoolean;
	public boolean resetMode;

	public FileConfiguration config;
	public File configFile;


	//PUBLIC CLASSES
	public LobbyHandler lobby;
	public GameHandler game;
	public PlayerHandler playerHandle;
	public TeleportHandler teleportHandle;
	public ResetHandler resetHandle;
	public EndGameManager endGame;
	public CmdExecutor cmdExec;
	public ClassesHandler classesHandle;
	public SignHandler signHandle;
	public WorldControlHandler controlHandle;


	//HASH MAP
	public static HashMap<String, String> playerClasses = new HashMap<String, String>();;
	Set<String> spectators;
	
	private static Main plugin;


	//ON ENABLE <Index = 1>
	public void onEnable(){

		plugin = this;
		
		ClassLoader.loadFromFiles();
		StoreParser.loadFromFiles();
		
		lobby = new LobbyHandler(this);
		playerHandle = new PlayerHandler(this);
		teleportHandle = new TeleportHandler(this);
		game = new GameHandler(this);
		resetHandle = new ResetHandler(this);
		endGame = new EndGameManager(this);
		cmdExec = new CmdExecutor(this);
		classesHandle = new ClassesHandler();
		signHandle = new SignHandler(this);
		controlHandle = new WorldControlHandler(this);

		Bukkit.getPluginManager().registerEvents(playerHandle, this);
		Bukkit.getPluginManager().registerEvents(signHandle, this);
		Bukkit.getPluginManager().registerEvents(controlHandle, this);
		Bukkit.getPluginManager().registerEvents(this, this);

		getCommand("import").setExecutor(cmdExec);
		getCommand("forcestart").setExecutor(cmdExec);
		getCommand("forcerestart").setExecutor(cmdExec);
		getCommand("setlobby").setExecutor(cmdExec);
		getCommand("setspawn").setExecutor(cmdExec);
		getCommand("stoplobbytimer").setExecutor(cmdExec);
		getCommand("reloadclasses").setExecutor(cmdExec);
		getCommand("worldcheck").setExecutor(cmdExec);
		getCommand("map").setExecutor(cmdExec);
		getCommand("respawn").setExecutor(cmdExec);
		getCommand("worldteleport").setExecutor(cmdExec);
		getCommand("forceend").setExecutor(cmdExec);
		getCommand("forcesave").setExecutor(cmdExec);
		getCommand("admin").setExecutor(cmdExec);
		getCommand("class").setExecutor(new ClassExecutor());
		getCommand("store").setExecutor(new StoreExecutor());

		ScoreboardHandler.setupBoard();
		this.saveDefaultConfig();
		resetHandle.pluginReset();
		addTasks();

	}






	//CONFIG HANDLER <Index = 2>
	public void configSaver() {
		saveConfig();
		reloadConfig();
		getConfig();
	}







	//LOBBY COPIER <Index = 3>
	public void copyLobby(){
		lobbyWorld = new File("." + File.separator + "plugins" + File.separator + "RodsTheGame" + File.separator + "Maps" + File.separator + "lobby");
		if (!lobbyWorld.exists()) {
			getLogger().warning("[ERROR] Lobby world could not be found.");
			lobbyWorld.mkdirs();
		} 
		else {
			if (new File(lobbyWorld, "level.dat").exists()) {
				new File(lobbyWorld, "uid.dat").delete();
				if (!isCopied("lobby"))
					try {
						Copier.copyFolder(lobbyWorld, new File("." + File.separator + "lobby"));
					} catch (IOException e) {
						System.out.println("[ERROR] Lobby world couldn't be copied!");
					}
				if (Bukkit.getWorld("lobby") != null)
                    getLogger().severe("Lobby world is still loaded!");
                WorldLoader.load("lobby");
                for (LivingEntity e : Bukkit.getWorld("lobby").getEntitiesByClass(LivingEntity.class))
                	if (!(e instanceof Player))
                		e.setHealth(0);
			} 
			else {
				getLogger().warning("[ERROR] Lobby world could not be found.");
			}
		}
	}







	//MAP CHECKER <Index = 4>
	public void CheckMap(){
		mapWorld = new File("." + File.separator + "plugins" + File.separator + "RodsTheGame" + File.separator + "Maps" + File.separator + selectedMap);
		if (!mapWorld.exists()) {
			getLogger().warning("The world " + selectedMap + " was not found.");
			mapWorld.mkdirs();
			lobby.chooseMap();
		} 
		else {
			if (new File(mapWorld, "level.dat").exists()) {
				new File(mapWorld, "uid.dat").delete();
				if (!isCopied(selectedMap))
					try {
						Copier.copyFolder(mapWorld, new File("." + File.separator + selectedMap));
					} catch (IOException e) {
						System.out.println("[ERROR] Map couldn't be copied!");
					}
				if (Bukkit.getWorld(selectedMap) != null)
					getLogger().severe(selectedMap + " world is still loaded!");
                WorldLoader.load(selectedMap);
                for (LivingEntity e : Bukkit.getWorld(selectedMap).getEntitiesByClass(LivingEntity.class))
                	if (!(e instanceof Player))
                		e.setHealth(0);

			} 
			else {
				getLogger().warning("[ERROR] Map world could not be found.");
			}
		}
	}


	//START LOBBY TIMER <Index = 5>
	public void startLobbyTimer(){
		lobby.canceled = false;
	}


	//START GAME TIMER <Index = 6>
	public void startGameTimer(){
		game.canceled = false;
	}



	//START RESET TIMER <Index = 7>
	public void startResetTimer(){
		resetHandle.canceled = false;
	}
	
	public void startEndGameTimer(){
		endGame.canceled = false;
	}
	
	private void addTasks() {
		getServer().getScheduler().scheduleSyncRepeatingTask(this, endGame, 0L, 20L);
		getServer().getScheduler().scheduleSyncRepeatingTask(this, resetHandle, 0L, 20L);
		getServer().getScheduler().scheduleSyncRepeatingTask(this, game, 0L, 20L);
		getServer().getScheduler().scheduleSyncRepeatingTask(this, lobby, 0L, 20L);
		endGame.cancel();
		resetHandle.cancel();
		game.cancel();
		lobby.cancel();
	}
	
	
	//ON DISABLE <Index = 8>
	public void onDisable() {
		for (Player p : Bukkit.getOnlinePlayers())
			p.kickPlayer(ChatMessages.serverClosingKick);
		if (Bukkit.getWorld(Main.selectedMap) != null)
			WorldLoader.unload(Main.selectedMap);
		if (Bukkit.getWorld("lobby") != null)
			WorldLoader.unload("lobby");
	}


	public static boolean isCopied(String name) {
		File f = new File("." + File.separator + name);
		return f.exists() && f.isDirectory();
	}
	
	@EventHandler
	public void onPing(ServerListPingEvent event) {
		String mode;
		if (this.resetMode)
			mode = ChatMessages.pingResetMode;
		else if (Main.lobbyBoolean)
			mode = ChatMessages.pingLobbyMode;
		else
			mode = ChatMessages.pingGameMode;
		event.setMotd(ChatMessages.MOTD.replace("%%MODE%%", mode));
	}

    public static void debug(String msg) {
    	System.out.println(ChatColor.YELLOW + "" + '[' + ChatColor.AQUA + "Debug" + ChatColor.YELLOW + "] " + ChatColor.RED + msg);
    }






	public static Main getInstance() {
		return plugin;
	}

}
