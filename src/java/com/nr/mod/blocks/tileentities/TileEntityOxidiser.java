package com.nr.mod.blocks.tileentities;
 
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.nr.mod.NuclearRelativistics;
import com.nr.mod.crafting.OxidiserRecipes;
import com.nr.mod.items.NRItems;

public class TileEntityOxidiser extends TileEntityMachine {
	public static final int[] input = {0, 1, 2, 3};
	public static final int[] output = {0, 1, 2, 3};
	
	public TileEntityOxidiser() {
		super("Oxidiser", 40000, 2, 2, true, true, 600, 60000, NuclearRelativistics.oxidiserSpeed, OxidiserRecipes.instance());
	}
	
	public void updateEntity() {
		super.updateEntity();
		if (flag != flag1) {
			flag1 = flag;
			BlockOxidiser.updateBlockState(flag, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
		}
		markDirty();
	}
	
	public boolean isOxygen(ItemStack stack) {
		if (stack == null) {return false;} else { Item i = stack.getItem();
		if(i == new ItemStack (NRItems.fuel, 1, 35).getItem() && i.getDamage(stack) == 35) {return true;}}
		return false;
	}
	
	public boolean isOxidiser() {
		return true;
	}
	
	public int[] getAccessibleSlotsFromSide(int i) {
		return i == 1 ? input : output;
	}
}