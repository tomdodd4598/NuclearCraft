package nc.tile.machine;

import nc.NuclearCraft;
import nc.block.machine.BlockHastener;
import nc.crafting.machine.HastenerRecipes;

public class TileHastener extends TileMachineBase {
	
	public TileHastener() {
		super("decayHastener", 250000, 1, 1, true, true, 200, 8000, NuclearCraft.hastenerSpeed, NuclearCraft.hastenerEfficiency, HastenerRecipes.instance());
	}
	
	public void updateEntity() {
		super.updateEntity();
		if (flag != flag1) {
			flag1 = flag;
			BlockHastener.updateBlockState(flag, worldObj, xCoord, yCoord, zCoord);
		}
		markDirty();
	}
}