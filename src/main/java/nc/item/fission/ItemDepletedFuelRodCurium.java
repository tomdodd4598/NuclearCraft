package nc.item.fission;

import nc.Global;
import nc.handler.EnumHandler.CuriumDepletedFuelRodTypes;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class ItemDepletedFuelRodCurium extends Item {

	public ItemDepletedFuelRodCurium(String unlocalizedName, String registryName) {
		setUnlocalizedName(unlocalizedName);
		setRegistryName(new ResourceLocation(Global.MOD_ID, registryName));
		setHasSubtypes(true);
	}

	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		for (int i = 0; i < CuriumDepletedFuelRodTypes.values().length; i++) {
			items.add(new ItemStack(this, 1, i));
		}
	}
	
	public String getUnlocalizedName(ItemStack stack) {
		for (int i = 0; i < CuriumDepletedFuelRodTypes.values().length; i++) {
			if (stack.getItemDamage() == i) {
				return getUnlocalizedName() + "." + CuriumDepletedFuelRodTypes.values()[i].getName();
			} else {
				continue;
			}
		}
		return this.getUnlocalizedName() + "." + CuriumDepletedFuelRodTypes.values()[0].getName();
	}
}
