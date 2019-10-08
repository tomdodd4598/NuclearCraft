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

public class NCItemBlock extends ItemBlock {
	
	private final TextFormatting[] fixedColors;
	private final TextFormatting fixedColor, infoColor;
	public final String[] fixedInfo, info;
	
	private NCItemBlock(Block block, TextFormatting[] fixedColors, TextFormatting fixedColor, String[] fixedTooltip, TextFormatting infoColor, String... tooltip) {
		super(block);
		this.fixedColors = fixedColors;
		this.fixedColor = fixedColor;
		fixedInfo = InfoHelper.buildFixedInfo(block.getTranslationKey(), fixedTooltip);
		this.infoColor = infoColor;
		info = InfoHelper.buildInfo(block.getTranslationKey(), tooltip);
	}
	
	public NCItemBlock(Block block, TextFormatting[] fixedColors, String[] fixedTooltip, TextFormatting infoColor, String... tooltip) {
		this(block, fixedColors, null, fixedTooltip, infoColor, tooltip);
	}
	
	public NCItemBlock(Block block, TextFormatting fixedColor, String[] fixedTooltip, TextFormatting infoColor, String... tooltip) {
		this(block, null, fixedColor, fixedTooltip, infoColor, tooltip);
	}
	
	public NCItemBlock(Block block, TextFormatting infoColor, String... tooltip) {
		this(block, TextFormatting.RED, InfoHelper.EMPTY_ARRAY, infoColor, tooltip);
	}
	
	public NCItemBlock(Block block, String... tooltip) {
		this(block, TextFormatting.RED, InfoHelper.EMPTY_ARRAY, TextFormatting.AQUA, tooltip);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		super.addInformation(itemStack, world, tooltip, flag);
		if (info.length + fixedInfo.length > 0) {
			if (fixedColors != null) {
				InfoHelper.infoFull(tooltip, fixedColors, fixedInfo, infoColor, info);
			}
			else {
				InfoHelper.infoFull(tooltip, fixedColor, fixedInfo, infoColor, info);
			}
		}
	}
}
