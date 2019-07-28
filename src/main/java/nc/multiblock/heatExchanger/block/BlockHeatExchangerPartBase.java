package nc.multiblock.heatExchanger.block;

import nc.multiblock.MultiblockBlockPartBase;
import nc.tab.NCTabs;
import net.minecraft.block.material.Material;

public abstract class BlockHeatExchangerPartBase extends MultiblockBlockPartBase {
	
	public BlockHeatExchangerPartBase() {
		super(Material.IRON, NCTabs.HEAT_EXCHANGER_BLOCKS);
	}
	
	public static abstract class Transparent extends MultiblockBlockPartBase.Transparent {
		
		public Transparent(boolean smartRender) {
			super(Material.IRON, NCTabs.HEAT_EXCHANGER_BLOCKS, smartRender);
		}
	}
}
