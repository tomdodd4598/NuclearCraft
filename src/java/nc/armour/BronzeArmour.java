package nc.armour;

import nc.NuclearCraft;
import nc.item.NCItems;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class BronzeArmour extends ItemArmor {

	public BronzeArmour(ArmorMaterial material, int id, int slot) {
		super(material, id, slot);
		this.setCreativeTab(NuclearCraft.tabNC);
	}
	
	public String getArmorTexture(ItemStack itemstack, Entity entity, int slot, String type) {
		if (itemstack.getItem() == NCItems.bronzeHelm || itemstack.getItem() == NCItems.bronzeChest || itemstack.getItem() == NCItems.bronzeBoots) {
			return "nc:textures/model/bronze1.png";
		} else if (itemstack.getItem() == NCItems.bronzeLegs) {
			return "nc:textures/model/bronze2.png";
		} else {
			return null;
		}
	}
}
