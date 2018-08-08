package nc.tab;

import nc.config.NCConfig;
import net.minecraft.creativetab.CreativeTabs;

public class NCTabs {
	
	public static final CreativeTabs TAB_NUCLEARCRAFT =  NCConfig.single_creative_tab ? new TabNuclearCraft() : CreativeTabs.MISC;
	
	public static final CreativeTabs TAB_BASE_BLOCK_MATERIALS = NCConfig.single_creative_tab ? TAB_NUCLEARCRAFT : new TabBaseBlockMaterials();
	public static final CreativeTabs TAB_BASE_ITEM_MATERIALS = NCConfig.single_creative_tab ? TAB_NUCLEARCRAFT : new TabBaseItemMaterials();
	public static final CreativeTabs TAB_MACHINES = NCConfig.single_creative_tab ? TAB_NUCLEARCRAFT : new TabMachines();
	public static final CreativeTabs TAB_FISSION_BLOCKS = NCConfig.single_creative_tab ? TAB_NUCLEARCRAFT : new TabFissionBlocks();
	public static final CreativeTabs TAB_FISSION_MATERIALS = NCConfig.single_creative_tab ? TAB_NUCLEARCRAFT : new TabFissionMaterials();
	public static final CreativeTabs TAB_FISSION_FUELS = NCConfig.single_creative_tab ? TAB_NUCLEARCRAFT : new TabFissionFuels();
	public static final CreativeTabs TAB_FUSION = NCConfig.single_creative_tab ? TAB_NUCLEARCRAFT : new TabFusion();
	public static final CreativeTabs TAB_SALT_FISSION_BLOCKS = NCConfig.single_creative_tab ? TAB_NUCLEARCRAFT : new TabSaltFissionBlocks();
	public static final CreativeTabs TAB_ACCELERATOR = NCConfig.single_creative_tab ? TAB_NUCLEARCRAFT : new TabAccelerator();
	public static final CreativeTabs TAB_FLUIDS = NCConfig.single_creative_tab ? TAB_NUCLEARCRAFT : new TabFluids();
	public static final CreativeTabs TAB_MISC = NCConfig.single_creative_tab ? TAB_NUCLEARCRAFT : new TabMisc();
}
