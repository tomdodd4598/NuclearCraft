package com.nr.mod.blocks.tileentities;
 
import com.nr.mod.NuclearRelativistics;
import com.nr.mod.crafting.CollectorRecipes;

public class TileEntityCollector extends TileEntityMachine {
	public static final int[] input = {0, 1};
	public static final int[] output = {0, 1};
	
	public TileEntityCollector() {
		super("Helium Collector", 1, 1, 1, false, false, 8000, 0, NuclearRelativistics.collectorSpeed, CollectorRecipes.instance());
	}
	
	public void updateEntity() {
		super.updateEntity();
		if (flag != flag1) {
			flag1 = flag;
			BlockCollector.updateBlockState(flag, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
		}
		markDirty();
	}
	
	public int[] getAccessibleSlotsFromSide(int i) {
		return i == 1 ? input : output;
	}
}