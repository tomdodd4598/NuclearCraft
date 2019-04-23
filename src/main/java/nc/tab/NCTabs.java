package nc.tab;

import nc.config.NCConfig;
import net.minecraft.creativetab.CreativeTabs;

public class NCTabs {
	
	public static final CreativeTabs NUCLEARCRAFT =  NCConfig.single_creative_tab ? new TabNuclearCraft() : CreativeTabs.MISC;
	
	public static final CreativeTabs BASE_BLOCK_MATERIALS = NCConfig.single_creative_tab ? NUCLEARCRAFT : new TabBaseBlockMaterials();
	public static final CreativeTabs BASE_ITEM_MATERIALS = NCConfig.single_creative_tab ? NUCLEARCRAFT : new TabBaseItemMaterials();
	public static final CreativeTabs RADIATION = (NCConfig.single_creative_tab || !NCConfig.radiation_enabled_public) ? NUCLEARCRAFT : new TabRadiation();
	public static final CreativeTabs MACHINES = NCConfig.single_creative_tab ? NUCLEARCRAFT : new TabMachines();
	public static final CreativeTabs FISSION_BLOCKS = NCConfig.single_creative_tab ? NUCLEARCRAFT : new TabFissionBlocks();
	public static final CreativeTabs FISSION_MATERIALS = NCConfig.single_creative_tab ? NUCLEARCRAFT : new TabFissionMaterials();
	public static final CreativeTabs FISSION_FUELS = NCConfig.single_creative_tab ? NUCLEARCRAFT : new TabFissionFuels();
	public static final CreativeTabs FUSION = NCConfig.single_creative_tab ? NUCLEARCRAFT : new TabFusion();
	public static final CreativeTabs SALT_FISSION_BLOCKS = NCConfig.single_creative_tab ? NUCLEARCRAFT : new TabSaltFissionBlocks();
	public static final CreativeTabs HEAT_EXCHANGER_BLOCKS = NCConfig.single_creative_tab ? NUCLEARCRAFT : new TabHeatExchangerBlocks();
	public static final CreativeTabs TURBINE_BLOCKS = NCConfig.single_creative_tab ? NUCLEARCRAFT : new TabTurbineBlocks();
	public static final CreativeTabs ACCELERATOR = NCConfig.single_creative_tab ? NUCLEARCRAFT : new TabAccelerator();
	public static final CreativeTabs MISC = NCConfig.single_creative_tab ? NUCLEARCRAFT : new TabMisc();
}
