package nc.tile.machine;

import nc.NuclearCraft;
import nc.block.machine.BlockCooler;
import nc.crafting.CoolerRecipes;

public class TileCooler extends TileMachine {
	public static final int[] input = {0, 1};
	public static final int[] output = {0, 1};
	
	public TileCooler() {
		super("Cooler", 50000, 1, 1, true, true, 20000, 2000000, NuclearCraft.coolerSpeed, CoolerRecipes.instance());
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