package ca.kanoa.rodsthegame;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Copier {

	public static void copyFolder(File src, File dest) throws IOException {

		if(src.isDirectory()) {

			if(!dest.exists()) {
				dest.mkdir();
				System.out.println("Directory copied from [" + src + "]  to [" + dest + "]");
			}

			String files[] = src.list();

			for (String file : files) {
				File srcFile = new File(src, file);
				File destFile = new File(dest, file);

				copyFolder(srcFile,destFile);
			}

		}
		else {
			InputStream in = new FileInputStream(src);
			OutputStream out = new FileOutputStream(dest); 

			byte[] buffer = new byte[1024];

			int length;
			while ((length = in.read(buffer)) > 0) {
				out.write(buffer, 0, length);
			}

			in.close();
			out.close();
			System.out.println("File copied from [" + src + "] to [" + dest + "]");
		}
	}
	
	public static boolean deleteDirectory(File directory) {
		
	    if(directory.exists()) {
	        File[] files = directory.listFiles();
	        if(null!=files)
	            for(int i=0; i<files.length; i++) 
	                if(files[i].isDirectory()) 
	                    deleteDirectory(files[i]);
	                else 
	                    files[i].delete();
	    }
	    return (directory.delete());
	    
	}

}
