package com.nr.mod.blocks.tileentities;
 
import com.nr.mod.NuclearRelativistics;
import com.nr.mod.crafting.ElectrolyserRecipes;

public class TileEntityElectrolyser extends TileEntityMachine {
	public static final int[] input = {0, 1, 2, 3, 4};
	public static final int[] output = {0, 1, 2, 3, 4};
	
	public TileEntityElectrolyser() {
		super("Electrolyser", 50000, 1, 4, true, true, 25600, 2560000, NuclearRelativistics.electrolyserSpeed, ElectrolyserRecipes.instance());
	}
	
	public void updateEntity() {
		super.updateEntity();
		if (flag != flag1) {
			flag1 = flag;
			BlockElectrolyser.updateBlockState(flag, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
		}
		markDirty();
	}
	
	public int[] getAccessibleSlotsFromSide(int i) {
		return i == 1 ? input : output;
	}
}