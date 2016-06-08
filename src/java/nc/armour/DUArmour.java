package nc.armour;

import nc.NuclearCraft;
import nc.item.NCItems;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class DUArmour extends ItemArmor {

	public DUArmour(ArmorMaterial material, int id, int slot) {
		super(material, id, slot);
		this.setCreativeTab(NuclearCraft.tabNC);
	}
	
	public String getArmorTexture(ItemStack itemstack, Entity entity, int slot, String type) {
		if (itemstack.getItem() == NCItems.dUHelm || itemstack.getItem() == NCItems.dUChest || itemstack.getItem() == NCItems.dUBoots) {
			return "nc:textures/model/dU1.png";
		} else if (itemstack.getItem() == NCItems.dULegs) {
			return "nc:textures/model/dU2.png";
		} else {
			return null;
		}
	}
}
