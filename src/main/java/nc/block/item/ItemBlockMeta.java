package nc.block.item;

import java.util.List;

import javax.annotation.Nullable;

import nc.util.InfoHelper;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockMeta extends ItemBlock {
	
	public final TextFormatting fixedColor;
	public final String[][] fixedInfo;
	public final String[][] info;
	
	public <T extends Enum<T>> ItemBlockMeta(Block block, Class<T> enumm, TextFormatting fixedColor, String[][] fixedTooltips, String[]... tooltips) {
		super(block);
		if (!(block instanceof IMetaBlockName)) {
			throw new IllegalArgumentException(String.format("The given block %s is not an instance of IMetaBlockName!", block.getUnlocalizedName()));
		}
		setMaxDamage(0);
		setHasSubtypes(true);
		this.fixedColor = fixedColor;
		fixedInfo = InfoHelper.buildFixedInfo(block.getUnlocalizedName(), enumm, fixedTooltips);
		info = InfoHelper.buildInfo(block.getUnlocalizedName(), enumm, tooltips);
	}
	
	public <T extends Enum<T>> ItemBlockMeta(Block block, Class<T> enumm, TextFormatting fixedColor, String[]... tooltips) {
		this(block, enumm, fixedColor, InfoHelper.EMPTY_ARRAYS, tooltips);
	}
	
	public <T extends Enum<T>> ItemBlockMeta(Block block, Class<T> enumm, String[][] fixedTooltips, String[]... tooltips) {
		this(block, enumm, TextFormatting.AQUA, fixedTooltips, tooltips);
	}
	
	public <T extends Enum<T>> ItemBlockMeta(Block block, Class<T> enumm, String[]... tooltips) {
		this(block, enumm, InfoHelper.EMPTY_ARRAYS, tooltips);
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
	public void addInformation(ItemStack itemStack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		super.addInformation(itemStack, world, tooltip, flag);
		int meta = itemStack.getMetadata();
		if (info.length != 0 && info.length > meta) if (info[meta].length > 0) {
			InfoHelper.infoFull(tooltip, fixedColor, fixedInfo[meta], info[meta]);
		}
	}
}
