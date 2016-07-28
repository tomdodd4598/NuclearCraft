package nc.itemblock.nuke;

import nc.itemblock.ItemBlockNC;
import net.minecraft.block.Block;

public class ItemBlockEMP extends ItemBlockNC {

	public ItemBlockEMP(Block block) {
		super(block, "A devastating weapon which removes all RF from", "every block in a 50 block radius.");
	}
}