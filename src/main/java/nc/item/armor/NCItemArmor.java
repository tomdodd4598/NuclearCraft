package nc.item.armor;

import java.util.List;

import nc.Global;
import nc.util.NCInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class NCItemArmor extends ItemArmor {
	
	String[] info;

	public NCItemArmor(String unlocalizedName, ArmorMaterial materialIn, int renderIndexIn, EntityEquipmentSlot equipmentSlotIn, String... tooltip) {
		super(materialIn, renderIndexIn, equipmentSlotIn);
		setUnlocalizedName(unlocalizedName);
		setRegistryName(new ResourceLocation(Global.MOD_ID, unlocalizedName));
		
		String[] strings = new String[tooltip.length];
		for (int i = 0; i < tooltip.length; i++) {
			strings[i] = tooltip[i];
		}
		info = strings;
	}
	
	public void addInformation(ItemStack itemStack, EntityPlayer player, List<String> tooltip, boolean advanced) {
        super.addInformation(itemStack, player, tooltip, advanced);
        if (info.length > 0) NCInfo.infoFull(tooltip, info);
    }
}
