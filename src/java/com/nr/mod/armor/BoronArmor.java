package com.nr.mod.armor;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import com.nr.mod.NuclearRelativistics;
import com.nr.mod.items.NRItems;

public class BoronArmor extends ItemArmor {

	public BoronArmor(ArmorMaterial material, int id, int slot) {
		super(material, id, slot);
		this.setCreativeTab(NuclearRelativistics.tabNR);
	}
	
	public String getArmorTexture(ItemStack itemstack, Entity entity, int slot, String type) {
		if (itemstack.getItem() == NRItems.boronHelm || itemstack.getItem() == NRItems.boronChest || itemstack.getItem() == NRItems.boronBoots) {
			return "nr:textures/model/boron1.png";
		} else if (itemstack.getItem() == NRItems.boronLegs) {
			return "nr:textures/model/boron2.png";
		} else {
			return null;
		}
	}
}
