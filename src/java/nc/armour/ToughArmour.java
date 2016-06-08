package nc.armour;

import nc.NuclearCraft;
import nc.item.NCItems;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

public class ToughArmour extends ItemArmor {

	public ToughArmour(ArmorMaterial material, int id, int slot) {
		super(material, id, slot);
		this.setCreativeTab(NuclearCraft.tabNC);
	}
	
	public String getArmorTexture(ItemStack itemstack, Entity entity, int slot, String type) {
		if (itemstack.getItem() == NCItems.toughHelm || itemstack.getItem() == NCItems.toughChest || itemstack.getItem() == NCItems.toughBoots) {
			return "nc:textures/model/tough1.png";
		} else if (itemstack.getItem() == NCItems.toughLegs) {
			return "nc:textures/model/tough2.png";
		} else {
			return null;
		}
	}
}
