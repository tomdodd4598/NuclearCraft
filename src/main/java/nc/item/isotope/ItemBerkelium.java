package nc.item.isotope;

import java.util.List;

import nc.Global;
import nc.handler.EnumHandler.BerkeliumTypes;
import nc.util.NCInfo;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;

public class ItemBerkelium extends Item {

	public ItemBerkelium(String unlocalizedName, String registryName) {
		setUnlocalizedName(unlocalizedName);
		setRegistryName(new ResourceLocation(Global.MOD_ID, registryName));
		setHasSubtypes(true);
	}

	public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> items) {
		for (int i = 0; i < BerkeliumTypes.values().length; i++) {
			items.add(new ItemStack(item, 1, i));
		}
	}
	
	public String getUnlocalizedName(ItemStack stack) {
		for (int i = 0; i < BerkeliumTypes.values().length; i++) {
			if (stack.getItemDamage() == i) {
				return getUnlocalizedName() + "." + BerkeliumTypes.values()[i].getName();
			} else {
				continue;
			}
		}
		return this.getUnlocalizedName() + "." + BerkeliumTypes.values()[0].getName();
	}
	
	public void addInformation(ItemStack itemStack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        super.addInformation(itemStack, player, tooltip, advanced);
        NCInfo.infoFull(tooltip, I18n.translateToLocalFormatted("item.berkelium.desc"));
    }
}
