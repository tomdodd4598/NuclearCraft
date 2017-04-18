package nc.itemblock.generator;

import nc.itemblock.ItemBlockNC;
import net.minecraft.block.Block;

public class ItemBlockFissionReactorSteam extends ItemBlockNC {

	public ItemBlockFissionReactorSteam(Block block) {
		super(block, "Uses fissile fuels to generate steam. Check the", "individual fuel cells' info for more detail.", "Requires a fission multiblock structure to function.");
	}
}