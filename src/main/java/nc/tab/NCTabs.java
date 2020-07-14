package nc.tab;

import static nc.config.NCConfig.*;

import net.minecraft.creativetab.CreativeTabs;

public class NCTabs {
	
	private static final CreativeTabs NUCLEARCRAFT = single_creative_tab ? new TabNuclearCraft() : CreativeTabs.MISC;
	
	public static final CreativeTabs MATERIAL = single_creative_tab ? NUCLEARCRAFT : new TabMaterial();
	public static final CreativeTabs MACHINE = single_creative_tab ? NUCLEARCRAFT : new TabMachine();
	public static final CreativeTabs MULTIBLOCK = single_creative_tab ? NUCLEARCRAFT : new TabMultiblock();
	public static final CreativeTabs RADIATION = single_creative_tab || !radiation_enabled_public ? MATERIAL : new TabRadiation();
	public static final CreativeTabs MISC = single_creative_tab ? NUCLEARCRAFT : new TabMisc();
}
