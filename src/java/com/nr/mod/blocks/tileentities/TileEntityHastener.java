package com.nr.mod.blocks.tileentities;

import com.nr.mod.NuclearRelativistics;
import com.nr.mod.crafting.HastenerRecipes;

public class TileEntityHastener extends TileEntityMachine {
	public static final int[] input = {0, 1};
	public static final int[] output = {0, 1};
	
	public TileEntityHastener() {
		super("Decay Hastener", 25000, 1, 1, true, true, 200, 8000, NuclearRelativistics.hastenerSpeed, HastenerRecipes.instance());
	}
	
	public void updateEntity() {
		super.updateEntity();
		if (flag != flag1) {
			flag1 = flag;
			BlockHastener.updateBlockState(flag, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
		}
		markDirty();
	}
	
	public int[] getAccessibleSlotsFromSide(int i) {
		return i == 1 ? input : output;
	}
}