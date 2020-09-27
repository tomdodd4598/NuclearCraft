package nc.handler;

import java.io.*;

import org.apache.commons.io.FileUtils;

import it.unimi.dsi.fastutil.objects.*;
import nc.util.*;

public class ScriptAddonHandler {
	
	public static final ObjectSet<File> SCRIPT_ADDON_DIRS = new ObjectOpenHashSet<>();
	
	public static void init() throws IOException {
		NCUtil.getLogger().info("Constructing NuclearCraft Script Addons...");
		
		for (String s : new String[] {"addons", "advancements", "blockstates", "lang", "loot_tables", "models/block", "models/item", "patchouli_books/guide", "textures/blocks", "textures/items"}) {
			new File("resources/nuclearcraft/" + s).mkdirs();
		}
		
		File scripts = new File("scripts/nc_script_addons");
		FileUtils.deleteDirectory(scripts);
		scripts.mkdirs();
		new File("scripts/nc_script_addons/DONT_PUT_YOUR_SCRIPTS_IN_HERE").createNewFile();
		
		File legacy = new File("scripts/nuclearcraft/DONT_PUT_YOUR_SCRIPTS_IN_HERE");
		if (legacy.exists()) {
			legacy.delete();
		}
		
		File temp = new File("resources/nuclearcraft/addons/.temp");
		temp.mkdirs();
		
		File addons = new File("resources/nuclearcraft/addons");
		for (File f : addons.listFiles()) {
			if (IOHelper.isZip(f)) {
				String fileName = f.getName();
				if (fileName.endsWith(".zip") || fileName.endsWith(".jar")) {
					fileName = StringHelper.removeSuffix(fileName, 4);
				}
				IOHelper.unzip(f, "resources/nuclearcraft/addons/.temp/" + fileName);
			}
		}
		
		for (File f : addons.listFiles()) {
			if (f.isDirectory()) {
				copyAddons(f);
			}
		}
		
		FileUtils.deleteDirectory(temp);
		
		for (File f : SCRIPT_ADDON_DIRS) {
			NCUtil.getLogger().info("Constructed \"" + f.getName() + "\" Script Addon!");
		}
	}
	
	public static final String[] NC_ASSETS = {"advancements", "blockstates", "loot_tables", "models", "patchouli_books", "textures"};
	public static final String[] ADDON_ASSETS = {"advancements", "blockstates", "contenttweaker", "lang", "loot_tables", "models", "patchouli_books", "scripts", "textures"};
	public static final String[] IGNORE_SUFFIX = {".ignore", ".disabled"};
	
	public static void copyAddons(File dir) throws IOException {
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
					
					FileUtils.copyDirectory(f, new File("scripts/nc_script_addons/" + dirName));
					SCRIPT_ADDON_DIRS.add(dir);
				}
				
				else if (f.getName().equals("contenttweaker")) {
					FileUtils.copyDirectory(f, new File("resources/contenttweaker"));
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
			if (!f.isDirectory() && name.endsWith(".lang")) {
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
