package com.nr.mod.armor;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;

import com.nr.mod.NuclearRelativistics;
import com.nr.mod.items.NRItems;

public class BronzeArmor extends ItemArmor {

	public BronzeArmor(ArmorMaterial material, int id, int slot) {
		super(material, id, slot);
		this.setCreativeTab(NuclearRelativistics.tabNR);
	}
	
	public String getArmorTexture(ItemStack itemstack, Entity entity, int slot, String type) {
		if (itemstack.getItem() == NRItems.bronzeHelm || itemstack.getItem() == NRItems.bronzeChest || itemstack.getItem() == NRItems.bronzeBoots) {
			return "nr:textures/model/bronze1.png";
		} else if (itemstack.getItem() == NRItems.bronzeLegs) {
			return "nr:textures/model/bronze2.png";
		} else {
			return null;
		}
	}
}
