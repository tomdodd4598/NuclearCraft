package nc.multiblock.lowTurbine.block;

import nc.multiblock.MultiblockBlockPartBase;
import nc.tab.NCTabs;
import net.minecraft.block.material.Material;

public abstract class BlockLowTurbinePartBase extends MultiblockBlockPartBase {
	
	public BlockLowTurbinePartBase(String name) {
		super(name, Material.IRON, NCTabs.TURBINE_BLOCKS);
	}
	
	public static abstract class Transparent extends MultiblockBlockPartBase.Transparent {
		
		public Transparent(String name, boolean smartRender) {
			super(name, Material.IRON, NCTabs.TURBINE_BLOCKS, smartRender);
		}
	}
}
