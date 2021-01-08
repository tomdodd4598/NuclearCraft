package nc.util;

import java.io.*;
import java.util.zip.*;

public class IOHelper {
	
	public static final String NEW_LINE = System.lineSeparator();
	
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
	
	/** Modified from Nam Ha Minh's posts at https://www.codejava.net/file-io-tutorials */
	public static void unzip(File zipFile, String dest) throws IOException {
		File destDir = new File(dest);
		if (!destDir.exists()) {
			destDir.mkdir();
		}
		
		ZipInputStream zipStream = new ZipInputStream(new FileInputStream(zipFile));
		ZipEntry entry = zipStream.getNextEntry();
		while (entry != null) {
			String fileDest = dest + "/" + entry.getName();
			if (!entry.isDirectory()) {
				extract(zipStream, fileDest);
			}
			else {
				new File(fileDest).mkdirs();
			}
			zipStream.closeEntry();
			entry = zipStream.getNextEntry();
		}
		zipStream.close();
	}
	
	private static void extract(ZipInputStream zipStream, String fileDest) throws IOException {
		BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(fileDest));
		byte[] bytesIn = new byte[1024];
		int read = 0;
		while ((read = zipStream.read(bytesIn)) != -1) {
			outStream.write(bytesIn, 0, read);
		}
		outStream.close();
	}
}
