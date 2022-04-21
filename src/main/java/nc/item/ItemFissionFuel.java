package nc.item;

import java.util.List;

import javax.annotation.Nullable;

import nc.enumm.IFissionFuelEnum;
import nc.util.*;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.*;

public class ItemFissionFuel<T extends Enum<T> & IStringSerializable & IFissionFuelEnum> extends Item implements IInfoItem {
	
	private final Class<T> enumm;
	public final T[] values;
	public String[] fixedInfo;
	public String[][] info;
	
	public ItemFissionFuel(Class<T> enumm) {
		setHasSubtypes(true);
		this.enumm = enumm;
		values = enumm.getEnumConstants();
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (isInCreativeTab(tab)) {
			for (int i = 0; i < values.length; ++i) {
				items.add(new ItemStack(this, 1, i));
			}
		}
	}
	
	@Override
	public String getTranslationKey(ItemStack stack) {
		for (int i = 0; i < values.length; ++i) {
			if (StackHelper.getMetadata(stack) == i) {
				return getTranslationKey() + "." + values[i].getName();
			}
		}
		return getTranslationKey() + "." + values[0].getName();
	}
	
	@Override
	public void setInfo() {
		fixedInfo = InfoHelper.buildFixedInfo(getTranslationKey(), InfoHelper.EMPTY_ARRAY);
		info = InfoHelper.buildInfo(getTranslationKey(), enumm, InfoHelper.EMPTY_ARRAYS);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		super.addInformation(stack, world, tooltip, flag);
		int meta = StackHelper.getMetadata(stack);
		if (info.length > meta) {
			InfoHelper.infoFull(tooltip, TextFormatting.GREEN, fixedInfo, new TextFormatting[] {TextFormatting.GREEN, TextFormatting.YELLOW, TextFormatting.LIGHT_PURPLE, TextFormatting.RED, TextFormatting.DARK_AQUA}, info[meta]);
		}
	}
}
