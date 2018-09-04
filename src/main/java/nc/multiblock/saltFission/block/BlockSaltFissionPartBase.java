package nc.multiblock.saltFission.block;

import nc.multiblock.MultiblockBlockPartBase;
import nc.tab.NCTabs;
import net.minecraft.block.material.Material;

public abstract class BlockSaltFissionPartBase extends MultiblockBlockPartBase {
	
	public BlockSaltFissionPartBase(String name) {
		super(name, Material.IRON, NCTabs.SALT_FISSION_BLOCKS);
	}
	
	public static abstract class Transparent extends MultiblockBlockPartBase.Transparent {
		
		public Transparent(String name, boolean smartRender) {
			super(name, Material.IRON, NCTabs.SALT_FISSION_BLOCKS, smartRender);
		}
	}
}
