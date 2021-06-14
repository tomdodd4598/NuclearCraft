package nc.handler;

import java.io.*;
import java.util.Locale;

import org.apache.commons.io.FileUtils;

import it.unimi.dsi.fastutil.objects.*;
import nc.util.*;

public class ScriptAddonHandler {
	
	public static final ObjectSet<File> SCRIPT_ADDON_DIRS = new ObjectOpenHashSet<>();
	
	public static final String[] NC_ASSETS = {"advancements", "blockstates", "loot_tables", "models", "patchouli_books", "textures"};
	public static final String[] ADDON_ASSETS = {"advancements", "blockstates", "contenttweaker", "lang", "loot_tables", "models", "modularmachinery", "patchouli_books", "scripts", "textures"};
	public static final String[] IGNORE_SUFFIX = {".ignore", ".ignored", ".disable", ".disabled"};
	
	public static void init() throws IOException {
		NCUtil.getLogger().info("Constructing NuclearCraft Script Addons...");
		
		for (String s : new String[] {"addons", "advancements", "blockstates", "lang", "loot_tables", "models/block", "models/item", "patchouli_books/guide", "textures/blocks", "textures/items"}) {
			new File("resources/nuclearcraft/" + s).mkdirs();
		}
		
		File scripts = new File("scripts/nc_script_addons");
		if (scripts.exists()) {
			FileUtils.deleteDirectory(scripts);
		}
		scripts.mkdirs();
		new File("scripts/nc_script_addons/DONT_PUT_YOUR_SCRIPTS_IN_HERE").createNewFile();
		
		File oldWarning = new File("scripts/nuclearcraft/DONT_PUT_YOUR_SCRIPTS_IN_HERE");
		if (oldWarning.exists()) {
			oldWarning.delete();
		}
		
		File cot = new File("resources/contenttweaker"), cotBackup = new File("resources/.contenttweaker");
		if (cot.exists()) {
			FileUtils.copyDirectory(cot, cotBackup);
		}
		
		File temp = new File("resources/nuclearcraft/addons/.temp");
		temp.mkdirs();
		
		File addons = new File("resources/nuclearcraft/addons");
		extractAddons(addons);
		copyAddons(addons);
		if (temp.exists()) {
			FileUtils.deleteDirectory(temp);
		}
		
		for (File f : SCRIPT_ADDON_DIRS) {
			NCUtil.getLogger().info("Constructed \"" + f.getName() + "\" Script Addon!");
		}
		
		if (cotBackup.exists()) {
			FileUtils.copyDirectory(cotBackup, cot);
			FileUtils.deleteDirectory(cotBackup);
		}
	}
	
	public static String removeVersionSuffix(String name) {
		int hyphen = name.lastIndexOf('-');
		if (hyphen > 0) {
			name = name.substring(0, hyphen);
		}
		return name;
	}
	
	public static void extractAddons(File dir) throws IOException {
		fileLoop: for (File f : dir.listFiles()) {
			if (f.isFile() && IOHelper.isZip(f)) {
				String fileName = f.getName();
				String fileNameLowerCase = fileName.toLowerCase(Locale.ROOT);
				for (String suffix : IGNORE_SUFFIX) {
					if (fileNameLowerCase.endsWith(suffix)) {
						continue fileLoop;
					}
				}
				if (fileName.endsWith(".zip") || fileName.endsWith(".jar")) {
					fileName = StringHelper.removeSuffix(fileName, 4);
					fileName = removeVersionSuffix(fileName);
				}
				IOHelper.unzip(f, "resources/nuclearcraft/addons/.temp/" + fileName);
			}
		}
	}
	
	public static void copyAddons(File dir) throws IOException {
		if (!dir.isDirectory()) {
			return;
		}
		
		String dirName = dir.getName();
		if (dirName.equals("__MACOSX")) {
			return;
		}
		
		String dirNameLowerCase = dirName.toLowerCase();
		for (String suffix : IGNORE_SUFFIX) {
			if (dirNameLowerCase.endsWith(suffix)) {
				return;
			}
		}
		
		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {
				for (String s : NC_ASSETS) {
					if (f.getName().equals(s)) {
						FileUtils.copyDirectory(f, new File("resources/nuclearcraft/" + s));
						SCRIPT_ADDON_DIRS.add(dir);
						break;
					}
				}
				
				if (f.getName().equals("lang")) {
					copyLangs(dir, f);
					SCRIPT_ADDON_DIRS.add(dir);
				}
				
				else if (f.getName().equals("scripts")) {
					File legacy = new File("scripts/nuclearcraft/" + dirName);
					if (legacy.exists()) {
						FileUtils.deleteDirectory(legacy);
					}
					
					FileUtils.copyDirectory(f, new File("scripts/nc_script_addons/" + removeVersionSuffix(dirName)));
					SCRIPT_ADDON_DIRS.add(dir);
				}
				
				else if (f.getName().equals("contenttweaker")) {
					FileUtils.copyDirectory(f, new File("resources/contenttweaker"));
					SCRIPT_ADDON_DIRS.add(dir);
				}
				
				else if (f.getName().equals("modularmachinery")) {
					FileUtils.copyDirectory(f, new File("config/modularmachinery/machinery"));
					SCRIPT_ADDON_DIRS.add(dir);
				}
				
				else {
					boolean a = false;
					for (File d : f.listFiles()) {
						if (d.isDirectory()) {
							boolean b = false;
							for (String s : ADDON_ASSETS) {
								if (d.getName().equals(s)) {
									if (!a) {
										copyAddons(f);
										a = true;
									}
									b = true;
								}
							}
							if (!b) {
								copyAddons(d);
							}
						}
					}
				}
			}
		}
	}
	
	public static final Object2BooleanMap<String> LANG_REFRESH_MAP = new Object2BooleanOpenHashMap<>();
	
	public static void copyLangs(File addonDir, File langDir) throws IOException {
		for (File f : langDir.listFiles()) {
			String name = f.getName().toLowerCase();
			if (f.isFile() && name.endsWith(".lang")) {
				String type = StringHelper.removeSuffix(name, 5);
				File lang = new File("resources/nuclearcraft/lang/" + type + ".lang");
				
				boolean refreshed = LANG_REFRESH_MAP.getBoolean(type);
				if (!refreshed) {
					LANG_REFRESH_MAP.put(type, true);
					
					File original = new File("resources/nuclearcraft/lang/" + type + ".original");
					
					if (original.exists()) {
						FileUtils.copyFile(original, lang);
					}
					else {
						if (!lang.exists()) {
							lang.createNewFile();
						}
						FileUtils.copyFile(lang, original);
					}
				}
				
				if (lang.exists()) {
					String s = IOHelper.NEW_LINE;
					IOHelper.appendFile(lang, f, (lang.length() == 0 ? "" : (s + s)) + "# " + addonDir.getName() + s + s);
				}
			}
		}
	}
}
