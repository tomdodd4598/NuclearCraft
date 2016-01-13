package com.nr.mod.blocks.tileentities;
 
import com.nr.mod.NuclearRelativistics;
import com.nr.mod.crafting.IoniserRecipes;

public class TileEntityIoniser extends TileEntityMachine {
	public static final int[] input = {0, 1, 2, 3};
	public static final int[] output = {0, 1, 2, 3};
	
	public TileEntityIoniser() {
		super("Ioniser", 40000, 2, 2, true, true, 600, 60000, NuclearRelativistics.ioniserSpeed, IoniserRecipes.instance());
	}
	
	public void updateEntity() {
		super.updateEntity();
		if (flag != flag1) {
			flag1 = flag;
			BlockIoniser.updateBlockState(flag, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
		}
		markDirty();
	}
	
	public int[] getAccessibleSlotsFromSide(int i) {
		return i == 1 ? input : output;
	}
}