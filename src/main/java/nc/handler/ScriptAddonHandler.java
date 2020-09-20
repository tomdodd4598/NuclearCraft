package nc.handler;

import java.io.*;

import org.apache.commons.io.FileUtils;

import it.unimi.dsi.fastutil.objects.*;
import nc.util.*;

public class ScriptAddonHandler {
	
	public static void init() throws IOException {
		File nc = new File("resources/nuclearcraft");
		if (!nc.exists()) {
			nc.mkdirs();
		}
		
		for (String s : new String[] {"addons", "advancements", "blockstates", "lang", "loot_tables", "models/block", "models/item", "patchouli_books/guide", "textures/blocks", "textures/items"}) {
			File f = new File("resources/nuclearcraft/" + s);
			if (!f.exists()) {
				f.mkdirs();
			}
		}
		
		File scripts = new File("scripts/nc_script_addons");
		FileUtils.deleteDirectory(scripts);
		scripts.mkdirs();
		new File("scripts/nc_script_addons/DONT_PUT_YOUR_SCRIPTS_IN_HERE").createNewFile();
		
		File legacy = new File("scripts/nuclearcraft/DONT_PUT_YOUR_SCRIPTS_IN_HERE");
		if (legacy.exists()) {
			legacy.delete();
		}
		
		File addons = new File("resources/nuclearcraft/addons");
		for (File f : addons.listFiles()) {
			if (f.isDirectory()) {
				copyAddons(f);
			}
		}
	}
	
	public static final String[] NC_ASSETS = {"advancements", "blockstates", "loot_tables", "models", "patchouli_books", "textures"};
	public static final String[] ADDON_ASSETS = {"advancements", "blockstates", "contenttweaker", "lang", "loot_tables", "models", "patchouli_books", "scripts", "textures"};
	
	public static void copyAddons(File dir) throws IOException {
		if (dir.getName().toLowerCase().endsWith(".disabled")) {
			return;
		}
		
		for (File f : dir.listFiles()) {
			if (f.isDirectory()) {
				for (String s : NC_ASSETS) {
					if (f.getName().equals(s)) {
						FileUtils.copyDirectory(f, new File("resources/nuclearcraft/" + s));
						break;
					}
				}
				
				if (f.getName().equals("lang")) {
					copyLangs(dir, f);
				}
				
				else if (f.getName().equals("scripts")) {
					File legacy = new File("scripts/nuclearcraft/" + dir.getName());
					if (legacy.exists()) {
						FileUtils.deleteDirectory(legacy);
					}
					
					FileUtils.copyDirectory(f, new File("scripts/nc_script_addons/" + dir.getName()));
				}
				
				else if (f.getName().equals("contenttweaker")) {
					FileUtils.copyDirectory(f, new File("resources/contenttweaker"));
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
					String s = System.lineSeparator();
					IOHelper.appendFile(lang, f, (lang.length() == 0 ? "" : (s + s)) + "# " + addonDir.getName() + s + s);
				}
			}
		}
	}
}
