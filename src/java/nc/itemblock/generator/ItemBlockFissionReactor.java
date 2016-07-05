package nc.itemblock.generator;

import nc.itemblock.ItemBlockNC;
import net.minecraft.block.Block;

public class ItemBlockFissionReactor extends ItemBlockNC {

	public ItemBlockFissionReactor(Block block) {
		super(block, "Uses fissile fuels to generate RF. Check the", "individual fuel cells' info for more detail.", "Requires a fission multiblock structure to function.");
	}
}