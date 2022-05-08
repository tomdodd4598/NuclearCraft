package nc.multiblock.turbine.block;

import nc.multiblock.block.BlockMultiblockPart;
import nc.tab.NCTabs;
import net.minecraft.block.material.Material;

public abstract class BlockTurbinePart extends BlockMultiblockPart {
	
	public BlockTurbinePart() {
		super(Material.IRON, NCTabs.MULTIBLOCK);
	}
	
	public static abstract class Transparent extends BlockMultiblockPart.Transparent {
		
		public Transparent(boolean smartRender) {
			super(Material.IRON, NCTabs.MULTIBLOCK, smartRender);
		}
	}
}
