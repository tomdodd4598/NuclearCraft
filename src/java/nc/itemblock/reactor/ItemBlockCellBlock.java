package nc.itemblock.reactor;

import nc.itemblock.ItemBlockNC;
import net.minecraft.block.Block;

public class ItemBlockCellBlock extends ItemBlockNC {

	public ItemBlockCellBlock(Block block) {
		super(block, "Used in the construction of Fission Reactors.", "Adjacent blocks will generate extra power", "and heat. If adjacent to n other Cell", "Compartments, or Graphite blocks followed", "by Cell Compartments in the same direction,", "it will generate n+1 times the power and", "(n+1)(n+2)/2 times the heat.");
	}
}
