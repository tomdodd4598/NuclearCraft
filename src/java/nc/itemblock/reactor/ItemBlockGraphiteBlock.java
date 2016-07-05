package nc.itemblock.reactor;

import nc.itemblock.ItemBlockNC;
import net.minecraft.block.Block;

public class ItemBlockGraphiteBlock extends ItemBlockNC {

	public ItemBlockGraphiteBlock(Block block) {
		super(block, "Generates additional power and heat in", "Fission Reactors. In a Fission Reactor of", "c cells and heat level h, using a fuel of", "base power P, each Graphite block produces", "c*P*(1+h/1000000)/10 RF/t and c*P/5 H/t");
	}
}
