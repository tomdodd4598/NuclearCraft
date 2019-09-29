package nc.multiblock.fission.block;

import nc.multiblock.MultiblockBlockPartBase;
import nc.tab.NCTabs;
import net.minecraft.block.material.Material;

public abstract class BlockFissionPartBase extends MultiblockBlockPartBase {
	
	public BlockFissionPartBase() {
		super(Material.IRON, NCTabs.FISSION_BLOCKS);
	}
	
	public static abstract class Transparent extends MultiblockBlockPartBase.Transparent {
		
		public Transparent(boolean smartRender) {
			super(Material.IRON, NCTabs.FISSION_BLOCKS, smartRender);
		}
	}
}
