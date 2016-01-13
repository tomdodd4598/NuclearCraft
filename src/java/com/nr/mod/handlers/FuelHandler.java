package com.nr.mod.handlers;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.nr.mod.blocks.NRBlocks;

import cpw.mods.fml.common.IFuelHandler;

public class FuelHandler implements IFuelHandler {

   public int getBurnTime(ItemStack fuel) {
	   Item item = fuel.getItem();
	   
	   if(item == Item.getItemFromBlock(NRBlocks.graphiteBlock)) {return 16000;}
	   
	   return 0;
   }
}
