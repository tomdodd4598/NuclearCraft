package nc.block.distributor;

import nc.block.multiblock.BlockMultiblockPart;
import nc.tab.NCTabs;
import net.minecraft.block.material.Material;

public abstract class BlockDistributorPart extends BlockMultiblockPart {
	
	public BlockDistributorPart() {
		super(Material.IRON, NCTabs.multiblock);
	}
}
