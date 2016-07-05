package nc.itemblock.generator;

import nc.NuclearCraft;
import nc.itemblock.ItemBlockNC;
import net.minecraft.block.Block;

public class ItemBlockElectromagnet extends ItemBlockNC {

	public ItemBlockElectromagnet(Block block) {
		super(block, "Used to control a Fusion Reactor's superhot plasma.", "Requires " + NuclearCraft.electromagnetRF + " RF/t to run continuously.");
	}
}
