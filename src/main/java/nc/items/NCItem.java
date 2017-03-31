package nc.items;

import java.util.List;

import nc.Global;
import nc.util.NCInfo;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class NCItem extends Item {
	
	public final String[] info;
	
	public NCItem(String unlocalizedName, String registryName, Object... tooltip) {
		setUnlocalizedName(unlocalizedName);
		setRegistryName(new ResourceLocation(Global.MOD_ID, registryName));
		
		if (tooltip.length == 0) {
			String[] strings = {};
			info = strings;
		} else if (tooltip[0] instanceof String) {
			String[] strings = new String[tooltip.length];
			for (int i = 0; i < tooltip.length; i++) {
				strings[i] = (String) tooltip[i];
			}
			info = strings;
		} else if (tooltip[0] instanceof Integer) {
			String[] strings = new String[(int) tooltip[0]];
			for (int i = 0; i < (int) tooltip[0]; i++) {
				strings[i] = I18n.format("item." + unlocalizedName + ".des" + i);
			}
			info = strings;
		} else {
			String[] strings = {};
			info = strings;
		}
	}
	
	public void addInformation(ItemStack itemStack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        super.addInformation(itemStack, player, tooltip, advanced);
        if (info.length > 0) NCInfo.infoFull(tooltip, info);
    }
}
