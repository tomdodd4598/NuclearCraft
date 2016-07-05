package nc.itemblock.basic;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlockWithMetadata;
import net.minecraft.item.ItemStack;

public class ItemBlockOre extends ItemBlockWithMetadata {

	public ItemBlockOre(Block block) {
		super(block, block);
	}

	public String getUnlocalizedName(ItemStack stack) {
		switch (stack.getItemDamage()) {
			case 0: return "copperOre";
			case 1: return "tinOre";
			case 2: return "leadOre";
			case 3: return "silverOre";
			case 4: return "uraniumOre";
			case 5: return "thoriumOre";
			case 6: return "plutoniumOre";
			case 7: return "lithiumOre";
			case 8: return "boronOre";
			case 9: return "magnesiumOre";
			default: return this.getUnlocalizedName();
		}
	}
}