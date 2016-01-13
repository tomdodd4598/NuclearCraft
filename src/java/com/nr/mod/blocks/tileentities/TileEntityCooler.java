package com.nr.mod.blocks.tileentities;

import com.nr.mod.NuclearRelativistics;
import com.nr.mod.crafting.CoolerRecipes;

public class TileEntityCooler extends TileEntityMachine {
	public static final int[] input = {0, 1};
	public static final int[] output = {0, 1};
	
	public TileEntityCooler() {
		super("Cooler", 50000, 1, 1, true, true, 10000, 1000000, NuclearRelativistics.coolerSpeed, CoolerRecipes.instance());
	}
	
	public void updateEntity() {
		super.updateEntity();
		if (flag != flag1) {
			flag1 = flag;
			BlockCooler.updateBlockState(flag, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
		}
		markDirty();
	}
	
	public int[] getAccessibleSlotsFromSide(int i) {
		return i == 1 ? input : output;
	}
}