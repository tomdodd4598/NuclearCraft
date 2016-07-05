package nc.itemblock.accelerator;

import nc.NuclearCraft;
import nc.itemblock.ItemBlockNC;
import net.minecraft.block.Block;

public class ItemBlockSuperElectromagnet extends ItemBlockNC {

	public ItemBlockSuperElectromagnet(Block block) {
		super(block, "Used to control the beams in particle accelerators.", "Requires " + NuclearCraft.superElectromagnetRF + " RF/t to run continuously.");
	}
}
