package kieronwiltshire.rods.gamemode;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardHandler {

	private static Scoreboard board;
	private static Objective score;
	
	public static void setupBoard() {
		board = Bukkit.getScoreboardManager().getNewScoreboard();
		score = board.registerNewObjective("Score", "dummy");
		score.setDisplaySlot(DisplaySlot.SIDEBAR);
		score.setDisplayName(ChatMessages.scoreboardTitle);
	}
	
	public static void addKill(String playerStr) {
		OfflinePlayer player = Bukkit.getOfflinePlayer(playerStr);
		score.getScore(player).setScore(score.getScore(player).getScore() + 1);
	}
	
	public static void setKills(String playerStr, int kills) {
		OfflinePlayer player = Bukkit.getOfflinePlayer(playerStr);
		score.getScore(player).setScore(kills);
	}
	
	public static int getKills(String playerStr) {
		return score.getScore(Bukkit.getOfflinePlayer(playerStr)).getScore();
	}
	
	public static void initPlayer(Player player) {
		player.setScoreboard(board);
		score.getScore(player).setScore(1);
		score.getScore(player).setScore(0);
	}
	
	public static void sendBoard(Player player) {
		int kills = score.getScore(player).getScore();
		score.getScore(player).setScore(1);
		score.getScore(player).setScore(kills);
		player.setScoreboard(board);
	}
	
	public static boolean isOnBoard(Player player) {
		return board.getPlayers().contains(player);
	}
	
	public static HashMap<String, Integer> getOnlineScores() {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		for (OfflinePlayer p : board.getPlayers())
			if (p.isOnline())
				map.put(p.getName(), score.getScore(p).getScore());
		return map;
	}
	
	public static Scoreboard getScoreboard() {
		return board;
	}
	
	public static Objective getScore() {
		return score;
	}
	
	public static void reset() {
		for (OfflinePlayer player : board.getPlayers())
			board.resetScores(player);
	}
	
}
