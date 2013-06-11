package ca.kanoa.rodsthegame.store;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import org.bukkit.Bukkit;

public class StoreParser {

	private static String sz = File.separator;
	
	public static void loadFromFiles() {
		Buyable.items = new ArrayList<Buyable>();
		File folder = new File("." + sz + "plugins" + sz + "RodsTheGame" + sz + "Store");
		if (!folder.exists())
			folder.mkdirs();
		for (File f : folder.listFiles())
			if (f.isFile() && f.getName().endsWith(".txt"))
				try {
					Buyable.items.add(Buyable.parseString(readFile(f)));
				} catch (BuyableFormatException e) {
					Bukkit.getLogger().warning("Error (" + e.getMessage() + ") while loading buyable: " + f.getName());
				}
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


}
