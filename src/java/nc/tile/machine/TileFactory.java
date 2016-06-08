package nc.tile.machine;

import nc.NuclearCraft;
import nc.block.machine.BlockFactory;
import nc.crafting.machine.FactoryRecipes;

public class TileFactory extends TileMachine {
	public static final int[] input = {0, 1};
	public static final int[] output = {0, 1};
	
	public TileFactory() {
		super("Manufactory", 250000, 1, 1, true, true, 200, 8000, NuclearCraft.factorySpeed, NuclearCraft.factoryEfficiency, FactoryRecipes.instance());
	}
	
	public void updateEntity() {
		super.updateEntity();
		if (flag != flag1) {
			flag1 = flag;
			BlockFactory.updateBlockState(flag, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
		}
		markDirty();
	}
	
	public int[] getAccessibleSlotsFromSide(int i) {
		return i == 1 ? input : output;
	}
}