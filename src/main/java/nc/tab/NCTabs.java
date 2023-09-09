package nc.tab;

import static nc.config.NCConfig.*;

import it.unimi.dsi.fastutil.objects.*;
import net.minecraft.creativetab.CreativeTabs;

public class NCTabs {
	
	public static final Object2ObjectMap<String, CreativeTabs> CREATIVE_TAB_MAP = new Object2ObjectOpenHashMap<>();
	
	private static CreativeTabs nuclearcraft;
	
	public static CreativeTabs material;
	public static CreativeTabs machine;
	public static CreativeTabs multiblock;
	public static CreativeTabs radiation;
	public static CreativeTabs misc;
	
	public static void init() {
		nuclearcraft = single_creative_tab ? new TabNuclearCraft() : CreativeTabs.MISC;
		material = single_creative_tab ? nuclearcraft : new TabMaterial();
		machine = single_creative_tab ? nuclearcraft : new TabMachine();
		multiblock = single_creative_tab ? nuclearcraft : new TabMultiblock();
		radiation = single_creative_tab || !radiation_enabled_public ? material : new TabRadiation();
		misc = single_creative_tab ? nuclearcraft : new TabMisc();
	}
	
	public static CreativeTabs getCreativeTab(String name) {
		return CREATIVE_TAB_MAP.get(name);
	}
}
