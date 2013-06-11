package kieronwiltshire.rods.gamemode;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class ChatMessages {

	
	public static void msg(String m) {
		Bukkit.broadcastMessage(m);
	}
	
	
	
	//CHAT COLOURS
	public static ChatColor AQUA = ChatColor.AQUA;
	public static ChatColor BLACK = ChatColor.BLACK;
	public static ChatColor BLUE = ChatColor.BLUE;
	public static ChatColor D_AQUA = ChatColor.DARK_AQUA;
	public static ChatColor D_BLUE = ChatColor.DARK_BLUE;
	public static ChatColor D_GRAY = ChatColor.DARK_GRAY;
	public static ChatColor D_GREEN = ChatColor.DARK_GREEN;
	public static ChatColor D_PURPLE = ChatColor.DARK_PURPLE;
	public static ChatColor D_RED = ChatColor.DARK_RED;
	public static ChatColor GOLD = ChatColor.GOLD;
	public static ChatColor GRAY = ChatColor.GRAY;
	public static ChatColor GREEN = ChatColor.GREEN;
	public static ChatColor L_PURPLE = ChatColor.LIGHT_PURPLE;
	public static ChatColor RED = ChatColor.RED;
	public static ChatColor WHITE = ChatColor.WHITE;
	public static ChatColor YELLOW = ChatColor.YELLOW;

	public static String AQUAstr = "AQUA";
	public static String BLACKstr = "BLACK";
	public static String BLUEstr = "BLUE";
	public static String D_AQUAstr = "DARKAQUA";
	public static String D_BLUEstr = "DARKBLUE";
	public static String D_GRAYstr = "DARKGRAY";
	public static String D_GREENstr = "DARKGREEN";
	public static String D_PURPLEstr = "DARKPURPLE";
	public static String D_REDstr = "DARKRED";
	public static String GOLDstr = "GOLD";
	public static String GRAYstr = "GRAY";
	public static String GREENstr = "GREEN";
	public static String L_PURPLEstr = "LIGHTPURPLE";
	public static String REDstr = "RED";
	public static String WHITEstr = "WHITE";
	public static String YELLOWstr = "YELLOW";
	
	public static String prefix = D_GRAY + "[" + D_PURPLE + "StickGames" + D_GRAY + "]" + " " + L_PURPLE;
	public static String resetMessage = prefix + "Server is currently in" + WHITE + " reset " + L_PURPLE + "mode.";
	public static String kickMessage = prefix + "Server is " + WHITE + "restarting, " + L_PURPLE + "rejoin.";
	public static String welcomeMessage = L_PURPLE + "Welcome to Minecraft Cluster!";
	public static String welcomeMessage2 = WHITE + "=-The Ultimate Stick Games-=";
	public static String winner = prefix + "Deathmatch Winner: " + RED + "%%PLAYER%%" + L_PURPLE + " with " + YELLOW + "%%KILLS%%" + L_PURPLE + " kills!";
	public static String joinMessage = L_PURPLE + " joined the game!";
	public static String quitMessage = L_PURPLE + " left the game!";
	public static String mainLobbySet = prefix + "The main lobby spawn has been set!";
	public static String worldLobbySet = prefix + "The world lobby spawn has been set!";
	public static String chosenClass = prefix + "You have chosen the class: " + D_PURPLE;
	public static String alphaTestMessage1 = RED + "SERVER IS CURRENTLY IN ALPHA MODE!";
	public static String alphaTestMessage2 = RED + "PLEASE REPORT ALL BUGS/GLITCHES!";
	public static String cannotEndGame = prefix + "You cannot end a game that isn't running!";
	
	
	
	
	
	//LOBBY TIMER TODO
	public static String sixtyMinLobby = prefix + "60 minute(s) until lobby ends.";
	public static String fortyFiveMinLobby = prefix + "45 minute(s) until lobby ends.";
	public static String thirtyMinLobby = prefix + "30 minute(s) until lobby ends.";
	public static String twentyMinLobby = prefix + "20 minute(s) until lobby ends.";
	public static String tenMinLobby = prefix + "10 minute(s) until lobby ends.";
	public static String fiveMinLobby = prefix + "5 minute(s) until lobby ends.";
	public static String twoMinLobby = prefix + "2 minute(s) until lobby ends.";
	public static String oneMinLobby = prefix + "1 minute(s) until lobby ends.";
	public static String thirtySecLobby = prefix + "30 second(s) until lobby ends.";
	public static String twentySecLobby = prefix + "20 second(s) until lobby ends.";
	public static String tenSecLobby = prefix + "10 second(s) until lobby ends.";
	public static String fiveSecLobby = prefix + "5 second(s) until lobby ends.";
	public static String fourSecLobby = prefix + "4 second(s) until lobby ends.";
	public static String threeSecLobby = prefix + "3 second(s) until lobby ends.";
	public static String twoSecLobby = prefix + "2 second(s) until lobby ends.";
	public static String oneSecLobby = prefix + "1 second(s) until lobby ends.";
	
	
	
	
	
	//GAME TIMER TODO
	public static String sixtyMinGame = prefix + "60 minute(s) until game ends.";
	public static String fortyFiveMinGame = prefix + "45 minute(s) until game ends.";
	public static String thirtyMinGame = prefix + "30 minute(s) until game ends.";
	public static String twentyMinGame = prefix + "20 minute(s) until game ends.";
	public static String tenMinGame = prefix + "10 minute(s) until game ends.";
	public static String fiveMinGame = prefix + "5 minute(s) until game ends.";
	public static String twoMinGame = prefix + "2 minute(s) until game ends.";
	public static String oneMinGame = prefix + "1 minute(s) until game ends.";
	public static String thirtySecGame = prefix + "30 second(s) until game ends.";
	public static String twentySecGame = prefix + "20 second(s) until game ends.";
	public static String tenSecGame = prefix + "10 second(s) until game ends.";
	public static String fiveSecGame = prefix + "5 second(s) until game ends.";
	public static String fourSecGame = prefix + "4 second(s) until game ends.";
	public static String threeSecGame = prefix + "3 second(s) until game ends.";
	public static String twoSecGame = prefix + "2 second(s) until game ends.";
	public static String oneSecGame = prefix + "1 second(s) until game ends.";
	
	
	
	
	
	//OTHER MESSAGES TODO FROM KANOA
	public static String tooFewPlayers = prefix + "There needs to be %%AMOUNT%% or more players online to start the game!";
	public static String reloadClasses = prefix + "Reloading classes from files.";
	public static String noClassPermission = prefix + "You don't have permission for that class!";
	public static String serverClosingKick = prefix + "Server shutting down, rejoin!";
	public static String selectedMapIs = prefix + "The selected map is: " + D_PURPLE + "%%MAP%%";
	public static String resetDone = prefix + "Server reset complete, player may rejoin now.";
	public static String currWorld = prefix + "Current world: " + D_PURPLE + "%%WORLD%%";
	public static String MOTD = YELLOW + "Sticks!  %%MODE%%";
	public static String pingResetMode = RED + "Restarting...";
	public static String pingLobbyMode = GREEN + "In Lobby.";
	public static String pingGameMode = L_PURPLE + "In Game.";
	public static String noLobbyRespawn = prefix + "You can't respawn in the lobby!";
	public static String forceSave = prefix + "Force saving %%MAP%%...";
	public static String saved = prefix + "World saved";
	public static String noOnlineWin = prefix + "There is no-one online to win!";
	public static String scoreboardTitle = L_PURPLE + "Score";
	public static String noCmdPerms = prefix + "You don't have permission to use that command!";
	public static String noClassGuiPerms = prefix + "You don't have permission to use the GUI!";
	public static String classNotFound = prefix + "That class does not exist! (yet)";
	public static String guiTitle = "Classes";
	public static String itemNotfound = prefix + "Unknown item!";
	public static String itemBought = prefix + "You just bought the %%ITEM%% for %%COST%% XP!";
	
	
	
	
	//RESET STAGES
	public static String stageZero = prefix + "Entering server reset stage " + RED + "zero" + ".";
	public static String stageOne = prefix + "Entering server reset stage " + RED + "one" + ".";
	public static String stageTwo = prefix + "Entering server reset stage " + RED + "two" + ".";
	public static String stageThree = prefix + "Entering server reset stage " + RED + "three" + ".";
	public static String stageFour = prefix + "Entering server reset stage " + RED + "four" + ".";
	public static String stageFive = prefix + "Entering server reset stage " + RED + "five" + ".";
	public static String stageSix = prefix + "Entering server reset stage " + RED + "six" + ".";
	
	
	
	
	//STORE
	public static String format = GOLD + "%%ITEM%%" + YELLOW + " X " + RED + "%%AMOUNT%%";
	
	
}
