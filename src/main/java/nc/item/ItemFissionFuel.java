package nc.item;

import java.util.List;

import javax.annotation.Nullable;

import nc.Global;
import nc.NCInfo;
import nc.NuclearCraft;
import nc.enumm.IFissionStats;
import nc.enumm.IItemMeta;
import nc.util.InfoHelper;
import nc.util.ItemStackHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemFissionFuel<T extends Enum<T> & IStringSerializable & IItemMeta & IFissionStats> extends Item {

	public final T[] values;
	public final String[] fixedInfo;
	public final String[][] info;
	
	public ItemFissionFuel(String nameIn, Class<T> enumm) {
		setTranslationKey(Global.MOD_ID + "." + nameIn);
		if (NuclearCraft.regName) setRegistryName(new ResourceLocation(Global.MOD_ID, nameIn));
		setHasSubtypes(true);
		values = enumm.getEnumConstants();
		fixedInfo = InfoHelper.buildFixedInfo(getTranslationKey(), InfoHelper.EMPTY_ARRAY);
		info = InfoHelper.buildInfo(getTranslationKey(), enumm, NCInfo.fuelRodInfo(values));
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (this.isInCreativeTab(tab)) for (int i = 0; i < values.length; i++) {
			items.add(new ItemStack(this, 1, i));
		}
	}
	
	@Override
	public String getTranslationKey(ItemStack stack) {
		for (int i = 0; i < values.length; i++) {
			if (ItemStackHelper.getMetadata(stack) == i) return getTranslationKey() + "." + values[i].getName();
			else continue;
		}
		return getTranslationKey() + "." + values[0].getName();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
		super.addInformation(itemStack, world, tooltip, flag);
		int meta = itemStack.getMetadata();
		if (info.length != 0 && info.length > meta && info[meta].length > 0) {
			InfoHelper.infoFull(tooltip, TextFormatting.GREEN, fixedInfo, info[meta]);
		}
	}
}
