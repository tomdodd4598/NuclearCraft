package nc.block.item;

import java.util.List;

import nc.util.InfoHelper;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockMeta extends ItemBlock {
	
	public final String[][] info;

	public <T extends Enum<T>> ItemBlockMeta(Block block, Class<T> enumm, String[]... tooltips) {
		super(block);
		if (!(block instanceof IMetaBlockName)) {
			throw new IllegalArgumentException(String.format("The given block %s is not an instance of IMetaBlockName!", block.getUnlocalizedName()));
		}
		setHasSubtypes(true);
		setMaxDamage(0);
		info = InfoHelper.buildInfo(block.getUnlocalizedName(), enumm, tooltips);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return getUnlocalizedName() + "." + ((IMetaBlockName) block).getSpecialName(stack);
	}
	
	@Override
	public int getMetadata(int damage) {
		return damage;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, World world, List<String> tooltip, ITooltipFlag flag) {
        super.addInformation(itemStack, world, tooltip, flag);
        if (info.length != 0) if (info[itemStack.getMetadata()].length > 0) InfoHelper.infoFull(tooltip, info[itemStack.getMetadata()]);
    }
}
