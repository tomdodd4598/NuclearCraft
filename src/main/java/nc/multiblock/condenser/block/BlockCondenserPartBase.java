package nc.multiblock.condenser.block;

import nc.multiblock.MultiblockBlockPartBase;
import nc.tab.NCTabs;
import net.minecraft.block.material.Material;

public abstract class BlockCondenserPartBase extends MultiblockBlockPartBase {
	
	public BlockCondenserPartBase(String name) {
		super(name, Material.IRON, NCTabs.CONDENSER_BLOCKS);
	}
	
	public static abstract class Transparent extends MultiblockBlockPartBase.Transparent {
		
		public Transparent(String name, boolean smartRender) {
			super(name, Material.IRON, NCTabs.CONDENSER_BLOCKS, smartRender);
		}
	}
}
