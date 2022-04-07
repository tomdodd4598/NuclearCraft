package nc.item.armor;

import java.util.List;

import javax.annotation.Nullable;

import nc.item.IInfoItem;
import nc.util.*;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.*;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.*;

public class NCItemArmor extends ItemArmor implements IInfoItem {
	
	private final TextFormatting infoColor;
	private final String[] tooltip;
	public String[] info;
	
	public NCItemArmor(ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn, TextFormatting infoColor, String... tooltip) {
		super(materialIn, renderIndexIn, equipmentSlotIn);
		this.infoColor = infoColor;
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
			InfoHelper.infoFull(currentTooltip, TextFormatting.RED, InfoHelper.EMPTY_ARRAY, infoColor, info);
		}
	}
	
	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
		ItemStack mat = getArmorMaterial().getRepairItemStack();
		return mat != null && !mat.isEmpty() && OreDictHelper.isOreMatching(mat, repair);
	}
}
