package nc.multiblock.turbine.block;

import nc.multiblock.MultiblockBlockPartBase;
import nc.tab.NCTabs;
import net.minecraft.block.material.Material;

public abstract class BlockTurbinePartBase extends MultiblockBlockPartBase {
	
	public BlockTurbinePartBase() {
		super(Material.IRON, NCTabs.MULTIBLOCK);
	}
	
	public static abstract class Transparent extends MultiblockBlockPartBase.Transparent {
		
		public Transparent(boolean smartRender) {
			super(Material.IRON, NCTabs.MULTIBLOCK, smartRender);
		}
	}
}
