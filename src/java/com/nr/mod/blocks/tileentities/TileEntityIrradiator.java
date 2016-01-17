package com.nr.mod.blocks.tileentities;
 
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.nr.mod.NuclearRelativistics;
import com.nr.mod.crafting.IrradiatorRecipes;
import com.nr.mod.items.NRItems;

public class TileEntityIrradiator extends TileEntityMachine {
	public static final int[] input = {0, 1, 2, 3, 4};
	public static final int[] output = {0, 1, 2, 3, 4};
	
	public TileEntityIrradiator() {
		super("Irradiator", 50000, 2, 3, true, true, 1000, 100000, NuclearRelativistics.irradiatorSpeed, IrradiatorRecipes.instance());
	}
	
	public void updateEntity() {
		super.updateEntity();
		if (flag != flag1) {
			flag1 = flag;
			BlockIrradiator.updateBlockState(flag, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
		}
		markDirty();
	}
	
	public boolean isNeutronCapsule(ItemStack stack) {
		if (stack == null) {return false;} else { Item i = stack.getItem();
		if(i == new ItemStack (NRItems.fuel, 1, 47).getItem() && i.getDamage(stack) == 47) {return true;}}
		return false;
	}
	
	public boolean isIrradiator() {
		return true;
	}
	
	public int[] getAccessibleSlotsFromSide(int i) {
		return i == 1 ? input : output;
	}
}