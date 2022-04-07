package nc.util;

import java.io.*;
import java.nio.file.*;
import java.util.zip.*;

public class IOHelper {
	
	public static final String NEW_LINE = System.lineSeparator();
	
	public static final int MAX_ZIP_SIZE = 50000000;
	public static final int ZIP_READ_SIZE = 0x2000;
	
	/** Modified from Srikanth A's answer at https://stackoverflow.com/a/45951007 */
	public static void appendFile(File target, File source, String separator) throws IOException {
		try (FileWriter writer = new FileWriter(target, true); FileReader reader = new FileReader(source)) {
			
			writer.write(separator);
			
			int c = reader.read();
			while (c != -1) {
				writer.write(c);
				c = reader.read();
			}
		}
	}
	
	/** Modified from Fabian Braun's answer at https://stackoverflow.com/a/47595502 */
	public static boolean isZip(File file) {
		int signature = 0;
		try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
			signature = raf.readInt();
		}
		catch (IOException e) {
			NCUtil.getLogger().catching(e);
		}
		return signature == 0x504B0304 || signature == 0x504B0506 || signature == 0x504B0708;
	}
	
	/** Thanks to sfPlayer for fixing a bug which caused some script addons not to load! Modified from Nam Ha Minh's posts at https://www.codejava.net/file-io-tutorials */
	public static void unzip(File zipFile, String dest) throws IOException {
		Path destDir = Paths.get(dest).toAbsolutePath().normalize();
		Files.createDirectories(destDir);
		
		int bytes = 0;
		
		try (ZipInputStream zipStream = new ZipInputStream(new FileInputStream(zipFile))) {
			ZipEntry entry;
			
			while ((entry = zipStream.getNextEntry()) != null) {
				Path out = destDir.resolve(entry.getName()).normalize();
				
				if (!out.startsWith(destDir)) {
					throw new IOException("Entry is outside of the target directory \"" + entry.getName() + "\"");
				}
				
				if (!entry.isDirectory()) {
					bytes = extract(zipStream, out, bytes);
				}
				else {
					Files.createDirectories(out);
				}
			}
		}
	}
	
	/** Returns the number of bytes extracted plus the original value of currentBytes */
	private static int extract(ZipInputStream zipStream, Path fileDest, int currentBytes) throws IOException {
		Files.createDirectories(fileDest.getParent());
		
		try (OutputStream outStream = Files.newOutputStream(fileDest)) {
			byte[] bytesIn = new byte[ZIP_READ_SIZE];
			int read = 0;
			while ((read = zipStream.read(bytesIn)) >= 0) {
				currentBytes += read;
				if (currentBytes > MAX_ZIP_SIZE) {
					throw new IOException("Zip file being extracted to \"" + fileDest + "\" is too big!");
				}
				outStream.write(bytesIn, 0, read);
			}
		}
		
		return currentBytes;
	}
}
