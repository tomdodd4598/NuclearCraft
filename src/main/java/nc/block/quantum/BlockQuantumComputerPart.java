package nc.block.quantum;

import nc.block.multiblock.BlockMultiblockPart;
import nc.tab.NCTabs;
import net.minecraft.block.material.Material;

public abstract class BlockQuantumComputerPart extends BlockMultiblockPart {
	
	public BlockQuantumComputerPart() {
		super(Material.IRON, NCTabs.multiblock);
	}
	
	public static abstract class Transparent extends BlockMultiblockPart.Transparent {
		
		public Transparent(boolean smartRender) {
			super(Material.IRON, NCTabs.multiblock, smartRender);
		}
	}
}
