package com.nr.mod.armor;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import com.nr.mod.NuclearRelativistics;
import com.nr.mod.items.NRItems;

public class ToughArmor extends ItemArmor {

	public ToughArmor(ArmorMaterial material, int id, int slot) {
		super(material, id, slot);
		this.setCreativeTab(NuclearRelativistics.tabNR);
	}
	
	public String getArmorTexture(ItemStack itemstack, Entity entity, int slot, String type) {
		if (itemstack.getItem() == NRItems.toughHelm || itemstack.getItem() == NRItems.toughChest || itemstack.getItem() == NRItems.toughBoots) {
			return "nr:textures/model/tough1.png";
		} else if (itemstack.getItem() == NRItems.toughLegs) {
			return "nr:textures/model/tough2.png";
		} else {
			return null;
		}
	}
}
