package nc.tab;

import nc.config.NCConfig;
import net.minecraft.creativetab.CreativeTabs;

public class NCTabs {
	
	public static final CreativeTabs NUCLEARCRAFT =  NCConfig.single_creative_tab ? new TabNuclearCraft() : CreativeTabs.MISC;
	
	public static final CreativeTabs MATERIAL = NCConfig.single_creative_tab ? NUCLEARCRAFT : new TabMaterial();
	public static final CreativeTabs MACHINE = NCConfig.single_creative_tab ? NUCLEARCRAFT : new TabMachine();
	public static final CreativeTabs MULTIBLOCK = NCConfig.single_creative_tab ? NUCLEARCRAFT : new TabMultiblock();
	public static final CreativeTabs RADIATION = (NCConfig.single_creative_tab || !NCConfig.radiation_enabled_public) ? MATERIAL : new TabRadiation();
	public static final CreativeTabs MISC = NCConfig.single_creative_tab ? NUCLEARCRAFT : new TabMisc();
}
