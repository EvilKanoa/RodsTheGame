package kieronwiltshire.rods.gamemode;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import ca.kanoa.RodsTwo.RodsTwo;

public class ClassLoader {

	public static HashMap<String, ItemStack[]> classes;
	private static String sz = File.separator;
	
	public static void loadFromFiles() {
		classes = new HashMap<String, ItemStack[]>();
		File folder = new File("." + sz + "plugins" + sz + "RodsTheGame" + sz + "Classes");
		if (!folder.exists())
			folder.mkdirs();
		for (File f : folder.listFiles())
			if (f.isFile() && f.getName().endsWith(".txt"))
				classes.put(f.getName().replace(".txt", ""), getRods(readFile(f)));
	}
	
	
	private static String readFile(File file) {
		String str = "";
		try {
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			StringBuffer stringBuffer = new StringBuffer();
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				stringBuffer.append(line);
				stringBuffer.append("\n");
			}
			fileReader.close();
			str = stringBuffer.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}


	private static ItemStack[] getRods(String str) {
		String[] raw = str.split("\n");
		raw[raw.length - 1] = raw[raw.length - 1].replace("\n", "");
		List<ItemStack> rods = new ArrayList<ItemStack>();
		for (String s : raw) {
			try {
				String type = s.substring(0, s.indexOf("."));
				String s2 = s.substring(s.indexOf(".") + 1, s.length());
				String thing = s2.split(":")[0];
				String amount = s2.split(":")[1];
				
				if (type.equalsIgnoreCase("rod"))
					rods.add(RodsTwo.getRod(thing).getItem(Integer.parseInt(amount)));
				else if (type.equalsIgnoreCase("item"))
					rods.add(new ItemStack(Material.getMaterial(thing.toUpperCase()), Integer.parseInt(amount)));
				else if (type.equalsIgnoreCase("itemid"))
					rods.add(new ItemStack(Material.getMaterial(Integer.parseInt(thing)), Integer.parseInt(amount)));
				else
					Bukkit.getLogger().severe(ChatColor.RED + "Unkown type of item [" + type + "] at: " + s);
				
			} catch (Exception e) {
				e.printStackTrace();
				Bukkit.getLogger().severe(ChatColor.RED + "Error (" + e.getCause().toString() + ") while loading item from string: " + s);
			}
		}
		return rods.toArray(new ItemStack[0]);
	}
	
}
