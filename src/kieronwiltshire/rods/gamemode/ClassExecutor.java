package kieronwiltshire.rods.gamemode;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClassExecutor implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// TODO Auto-generated method stub
		return true;
	}


	public static void choseClass(Player player, String classStr) {
		if (player.hasPermission("rtg.class." + classStr)) {
			for (String str : ClassLoader.classes.keySet())
				if (str.equalsIgnoreCase(classStr)) {
					Main.playerClasses.put(player.getName(), str);
					player.sendMessage(ChatMessages.chosenClass + str);
				}
		}
		else
			player.sendMessage(ChatMessages.noClassPermission);
					
	}

}
