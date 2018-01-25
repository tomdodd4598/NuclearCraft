package nc.item;

import java.util.List;

import nc.Global;
import nc.enumm.IItemMeta;
import nc.util.InfoHelper;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class NCItemMeta<T extends Enum<T> & IStringSerializable & IItemMeta> extends Item {
	
	public final T[] values;
	public final String[][] info;
	
	public NCItemMeta(String nameIn, Class<T> enumm, String[]... tooltips) {
		setUnlocalizedName(Global.MOD_ID + "." + nameIn);
		setRegistryName(new ResourceLocation(Global.MOD_ID, nameIn));
		setHasSubtypes(true);
		values = enumm.getEnumConstants();
		info = InfoHelper.buildInfo(getUnlocalizedName(), enumm, tooltips);
	}
	
	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if (this.isInCreativeTab(tab)) for (int i = 0; i < values.length; i++) {
			items.add(new ItemStack(this, 1, i));
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		for (int i = 0; i < values.length; i++) {
			if (stack.getItemDamage() == i) return getUnlocalizedName() + "." + values[i].getName();
			else continue;
		}
		return getUnlocalizedName() + "." + values[0].getName();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack itemStack, World world, List<String> tooltip, ITooltipFlag flag) {
		super.addInformation(itemStack, world, tooltip, flag);
		if (info.length != 0) if (info[itemStack.getMetadata()].length > 0) InfoHelper.infoFull(tooltip, info[itemStack.getMetadata()]);
	}
}
