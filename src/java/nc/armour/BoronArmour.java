package nc.armour;

import nc.NuclearCraft;
import nc.item.NCItems;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class BoronArmour extends ItemArmor {

	public BoronArmour(ArmorMaterial material, int id, int slot) {
		super(material, id, slot);
		this.setCreativeTab(NuclearCraft.tabNC);
	}
	
	public String getArmorTexture(ItemStack itemstack, Entity entity, int slot, String type) {
		if (itemstack.getItem() == NCItems.boronHelm || itemstack.getItem() == NCItems.boronChest || itemstack.getItem() == NCItems.boronBoots) {
			return "nc:textures/model/boron1.png";
		} else if (itemstack.getItem() == NCItems.boronLegs) {
			return "nc:textures/model/boron2.png";
		} else {
			return null;
		}
	}
}
