package nc.item;

import java.util.List;

import javax.annotation.Nullable;

import nc.util.InfoHelper;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.*;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.*;

public class NCItemDoor extends ItemDoor implements IInfoItem {
	
	private final String[] tooltip;
	public String[] info;
	
	public NCItemDoor(Block block, String... tooltip) {
		super(block);
		this.tooltip = tooltip;
	}
	
	@Override
	public void setInfo() {
		info = InfoHelper.buildInfo(getTranslationKey(), tooltip);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, @Nullable World world, List<String> currentTooltip, ITooltipFlag flag) {
		super.addInformation(itemStack, world, currentTooltip, flag);
		if (info.length > 0) {
			InfoHelper.infoFull(currentTooltip, TextFormatting.RED, InfoHelper.EMPTY_ARRAY, TextFormatting.AQUA, info);
		}
	}
}
