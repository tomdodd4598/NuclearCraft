package nc.item;

import java.util.List;

import javax.annotation.Nullable;

import nc.util.InfoHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.*;

public class NCItem extends Item implements IInfoItem {
	
	private final TextFormatting fixedColor, infoColor;
	private final String[] fixedTooltip, tooltip;
	public String[] fixedInfo, info;
	
	public NCItem(TextFormatting fixedColor, String[] fixedTooltip, TextFormatting infoColor, String... tooltip) {
		this.fixedColor = fixedColor;
		this.fixedTooltip = fixedTooltip;
		this.infoColor = infoColor;
		this.tooltip = tooltip;
	}
	
	public NCItem(TextFormatting infoColor, String... tooltip) {
		this(TextFormatting.RED, InfoHelper.EMPTY_ARRAY, infoColor, tooltip);
	}
	
	public NCItem(String... tooltip) {
		this(TextFormatting.RED, InfoHelper.EMPTY_ARRAY, TextFormatting.AQUA, tooltip);
	}
	
	@Override
	public void setInfo() {
		fixedInfo = InfoHelper.buildFixedInfo(getTranslationKey(), fixedTooltip);
		info = InfoHelper.buildInfo(getTranslationKey(), tooltip);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, @Nullable World world, List<String> currentTooltip, ITooltipFlag flag) {
		super.addInformation(itemStack, world, currentTooltip, flag);
		if (fixedInfo.length > 0 || info.length > 0) {
			InfoHelper.infoFull(currentTooltip, fixedColor, fixedInfo, infoColor, info);
		}
	}
	
	protected ActionResult<ItemStack> actionResult(boolean success, ItemStack stack) {
		return new ActionResult<>(success ? EnumActionResult.SUCCESS : EnumActionResult.FAIL, stack);
	}
	
	protected boolean isStackOnHotbar(ItemStack itemStack, EntityPlayer player) {
		for (ItemStack hotbarStack : player.inventory.mainInventory.subList(0, 9)) {
			if (itemStack.isItemEqual(hotbarStack)) {
				return true;
			}
		}
		return false;
	}
	
	protected boolean isStackInInventory(ItemStack itemStack, EntityPlayer player) {
		for (ItemStack hotbarStack : player.inventory.mainInventory) {
			if (itemStack.isItemEqual(hotbarStack)) {
				return true;
			}
		}
		return false;
	}
}
