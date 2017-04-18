package nc.itemblock.basic;

import nc.itemblock.ItemBlockMeta;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemBlockOre extends ItemBlockMeta {

	public ItemBlockOre(Block block) {
		super(block);
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
	
	public String[] info(String string) {
		String[] inf = {
			string
		};
		return inf;
	}
}