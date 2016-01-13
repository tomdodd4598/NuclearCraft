package com.nr.mod.armor;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import com.nr.mod.NuclearRelativistics;
import com.nr.mod.items.NRItems;

public class DUArmor extends ItemArmor {

	public DUArmor(ArmorMaterial material, int id, int slot) {
		super(material, id, slot);
		this.setCreativeTab(NuclearRelativistics.tabNR);
	}
	
	public String getArmorTexture(ItemStack itemstack, Entity entity, int slot, String type) {
		if (itemstack.getItem() == NRItems.dUHelm || itemstack.getItem() == NRItems.dUChest || itemstack.getItem() == NRItems.dUBoots) {
			return "nr:textures/model/dU1.png";
		} else if (itemstack.getItem() == NRItems.dULegs) {
			return "nr:textures/model/dU2.png";
		} else {
			return null;
		}
	}
}
