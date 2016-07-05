package nc.itemblock.generator;

import nc.NuclearCraft;
import nc.itemblock.ItemBlockNC;
import net.minecraft.block.Block;

public class ItemBlockWRTG extends ItemBlockNC {

	public ItemBlockWRTG(Block block) {
		super(block, "Generates a constant stream of " + NuclearCraft.WRTGRF + " RF/t");
	}
}