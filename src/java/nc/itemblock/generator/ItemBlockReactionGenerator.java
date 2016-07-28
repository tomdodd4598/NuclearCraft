package nc.itemblock.generator;

import nc.itemblock.ItemBlockNC;
import nc.tile.generator.TileReactionGenerator;
import net.minecraft.block.Block;

public class ItemBlockReactionGenerator extends ItemBlockNC {

	public ItemBlockReactionGenerator(Block block) {
		super(block, "Uses basic nuclear fuel and Universal Reactant", "to generate " + (TileReactionGenerator.power) + " RF/t");
	}
}