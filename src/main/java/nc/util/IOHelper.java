package nc.util;

import java.io.*;

public class IOHelper {
	
	/** Modified from Srikanth A's answer at https://stackoverflow.com/a/45951007 */
	public static void appendFile(File target, File source, String separator) throws IOException {
		FileWriter writer = new FileWriter(target, true);
		FileReader reader = new FileReader(source);
		
		writer.write(separator);
		
		int c = reader.read();
		while (c != -1) {
			writer.write(c);
			c = reader.read();
		}
		
		writer.close();
		reader.close();
	}
}
