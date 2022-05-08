package nc.multiblock.qComputer.block;

import nc.multiblock.block.BlockMultiblockPart;
import nc.tab.NCTabs;
import net.minecraft.block.material.Material;

public abstract class BlockQuantumComputerPart extends BlockMultiblockPart {
	
	public BlockQuantumComputerPart() {
		super(Material.IRON, NCTabs.MISC);
	}
	
	public static abstract class Transparent extends BlockMultiblockPart.Transparent {
		
		public Transparent(boolean smartRender) {
			super(Material.IRON, NCTabs.MULTIBLOCK, smartRender);
		}
	}
}
