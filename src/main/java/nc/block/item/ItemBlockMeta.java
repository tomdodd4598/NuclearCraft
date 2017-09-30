package nc.block.item;

import java.util.List;

import nc.util.NCInfo;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public abstract class ItemBlockMeta extends ItemBlock {
	
	public final String[][] info;

	public ItemBlockMeta(Block block, String[][] tooltips) {
		super(block);
		if (!(block instanceof IMetaBlockName)) {
			throw new IllegalArgumentException(String.format("The given block %s is not an instance of IMetaBlockName!", block.getUnlocalizedName()));
		}
		setHasSubtypes(true);
		setMaxDamage(0);
		info = tooltips;
	}
	
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName() + "." + ((IMetaBlockName) block).getSpecialName(stack);
	}
	
	public int getMetadata(int damage) {
		return damage;
	}
	
	public void addInformation(ItemStack itemStack, World world, List<String> tooltip, ITooltipFlag flag) {
        super.addInformation(itemStack, world, tooltip, flag);
        if (info.length != 0) if (info[itemStack.getMetadata()].length > 0) NCInfo.infoFull(tooltip, info[itemStack.getMetadata()]);
    }
}
