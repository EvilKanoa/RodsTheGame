package ca.kanoa.rodsthegame;

import java.util.HashMap;

import kieronwiltshire.rods.gamemode.ChatMessages;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardHandler {

	//The scoreboard
	private static Scoreboard board;
	//The 'Score' objective
	private static Objective score;
	
	/**
	 * Setup a new scoreboard for use with the scoreboardhandler
	 */
	public static void setupBoard() {
		board = Bukkit.getScoreboardManager().getNewScoreboard();
		score = board.registerNewObjective("Score", "dummy");
		score.setDisplaySlot(DisplaySlot.SIDEBAR);
		score.setDisplayName(ChatMessages.scoreboardTitle);
	}
	
	/**
	 * Adds a kill to the players score
	 * @param playerStr The player's name to add a kill for
	 */
	public static void addKill(String playerStr) {
		OfflinePlayer player = Bukkit.getOfflinePlayer(playerStr);
		score.getScore(player).setScore(score.getScore(player).getScore() + 1);
	}
	
	/**
	 * Sets the number of kills a player has
	 * @param playerStr The name of to player to set kills
	 * @param kills The number of kills to set this players score to
	 */
	public static void setKills(String playerStr, int kills) {
		OfflinePlayer player = Bukkit.getOfflinePlayer(playerStr);
		score.getScore(player).setScore(kills);
	}
	
	/**
	 * Removes a player from the scoreboard, in-turn resetting his/her score to zero
	 * @param name
	 */
	public static void removePlayer(String name) {
		OfflinePlayer player = Bukkit.getOfflinePlayer(name);
		board.resetScores(player);
	}
	
	/**
	 * Gets the number of kills a player has
	 * @param playerStr The name of the player to check how many kills (s)he has
	 * @return The number of kills the player has
	 */
	public static int getKills(String playerStr) {
		return score.getScore(Bukkit.getOfflinePlayer(playerStr)).getScore();
	}
	
	/**
	 * Sets up the scoreboard for the specified player
	 * @param player The player to setup the scoreboard for
	 */
	public static void initPlayer(Player player) {
		score.getScore(player).setScore(1);
		score.getScore(player).setScore(0);
	}
	
	/**
	 * Sends the current scoreboard to the player to allows them to view it
	 * @param player The player to send the scoreboard to
	 */
	public static void sendBoard(Player player) {
		int kills = score.getScore(player).getScore();
		score.getScore(player).setScore(1);
		score.getScore(player).setScore(kills);
		player.setScoreboard(board);
	}
	
	/**
	 * Checks if the scoreboard contains a score for the specified player
	 * @param player
	 * @return If the scoreboard contains a score for the player
	 */
	public static boolean isOnBoard(Player player) {
		return board.getPlayers().contains(player);
	}
	
	/**
	 * Gets all the scores for online players
	 * @return A Map containing the scores for all online players that are in the scoreboard
	 */
	public static HashMap<String, Integer> getOnlineScores() {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		for (OfflinePlayer p : board.getPlayers())
			if (p.isOnline())
				map.put(p.getName(), score.getScore(p).getScore());
		return map;
	}
	
	/**
	 * Retrieve the current scoreboard
	 * @return The current scoreboard
	 */
	public static Scoreboard getScoreboard() {
		return board;
	}
	
	/**
	 * Gets the Objective object associated with the scores
	 * @return The score object
	 */
	public static Objective getScore() {
		return score;
	}
	
	/**
	 * Resets all scores on the scoreboard to zero, for all players
	 */
	public static void reset() {
		for (OfflinePlayer player : board.getPlayers())
			board.resetScores(player);
	}
	
	/**
	 * Removes a players scoreboard
	 * @param player The player to remove the scoreboard from
	 */
	public static void hide(Player player) {
		player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
	}
	
}
