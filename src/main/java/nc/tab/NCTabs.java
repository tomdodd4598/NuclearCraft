package nc.tab;

import static nc.config.NCConfig.*;

import net.minecraft.creativetab.CreativeTabs;

public class NCTabs {
	
	private static final NCTabs INSTANCE = new NCTabs();
	
	private CreativeTabs nuclearcraft;
	private CreativeTabs material;
	private CreativeTabs machine;
	private CreativeTabs multiblock;
	private CreativeTabs radiation;
	private CreativeTabs misc;
	
	public static void init() {
		INSTANCE.nuclearcraft = single_creative_tab ? new TabNuclearCraft() : CreativeTabs.MISC;
		INSTANCE.material = single_creative_tab ? INSTANCE.nuclearcraft : new TabMaterial();
		INSTANCE.machine = single_creative_tab ? INSTANCE.nuclearcraft : new TabMachine();
		INSTANCE.multiblock = single_creative_tab ? INSTANCE.nuclearcraft : new TabMultiblock();
		INSTANCE.radiation = single_creative_tab || !radiation_enabled_public ? INSTANCE.material : new TabRadiation();
		INSTANCE.misc = single_creative_tab ? INSTANCE.nuclearcraft : new TabMisc();
	}
	
	public static CreativeTabs material() {
		return INSTANCE.material;
	}
	
	public static CreativeTabs machine() {
		return INSTANCE.machine;
	}
	
	public static CreativeTabs multiblock() {
		return INSTANCE.multiblock;
	}
	
	public static CreativeTabs radiation() {
		return INSTANCE.radiation;
	}
	
	public static CreativeTabs misc() {
		return INSTANCE.misc;
	}
}
