package nc.itemblock.generator;

import nc.NuclearCraft;
import nc.itemblock.ItemBlockNC;
import net.minecraft.block.Block;

public class ItemBlockAmRTG extends ItemBlockNC {

	public ItemBlockAmRTG(Block block) {
		super(block, "Generates a constant stream of " + NuclearCraft.AmRTGRF + " RF/t");
	}
}