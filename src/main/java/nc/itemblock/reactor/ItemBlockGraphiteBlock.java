package nc.itemblock.reactor;


import nc.itemblock.ItemBlockNC;
import net.minecraft.block.Block;

public class ItemBlockGraphiteBlock extends ItemBlockNC {

	public ItemBlockGraphiteBlock(Block block) {
		super(block, "Generates additional power and heat in", "Fission Reactors. In a Fission Reactor of", "c fuel cell compartments, using a fuel of", "base power P, each Graphite block produces", "c*P/10 RF/t and c*P/5 H/t", "Will only produce additional power if adjacent", "to at least one Fuel Cell Compartment.");
	}
}
