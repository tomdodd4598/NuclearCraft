package nc.tile.machine;

import nc.NuclearCraft;
import nc.block.machine.BlockHastener;
import nc.crafting.machine.HastenerRecipes;

public class TileHastener extends TileMachine {
	public static final int[] input = {0, 1};
	public static final int[] output = {0, 1};
	
	public TileHastener() {
		super("Decay Hastener", 250000, 1, 1, true, true, 200, 8000, NuclearCraft.hastenerSpeed, NuclearCraft.hastenerEfficiency, HastenerRecipes.instance());
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