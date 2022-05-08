package nc.multiblock.heatExchanger.block;

import nc.multiblock.block.BlockMultiblockPart;
import nc.tab.NCTabs;
import net.minecraft.block.material.Material;

public abstract class BlockHeatExchangerPart extends BlockMultiblockPart {
	
	public BlockHeatExchangerPart() {
		super(Material.IRON, NCTabs.MULTIBLOCK);
	}
	
	public static abstract class Transparent extends BlockMultiblockPart.Transparent {
		
		public Transparent(boolean smartRender) {
			super(Material.IRON, NCTabs.MULTIBLOCK, smartRender);
		}
	}
}
