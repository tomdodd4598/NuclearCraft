package nc.itemblock.generator;


import nc.itemblock.ItemBlockNC;
import net.minecraft.block.Block;

public class ItemBlockFusionReactor extends ItemBlockNC {

	public ItemBlockFusionReactor(Block block) {
		super(block, "An advanced RF generator that fuses Hydrogen, Deuterium,", "Tritium, Helium-3, Lithium-6, Lithium-7 and Boron-11 to", "generate a very large amount of RF.", "Requires a ring of powered electromagnets to function.", "Mekanism gas can be used as a fuel if pumped in.");
	}
}
