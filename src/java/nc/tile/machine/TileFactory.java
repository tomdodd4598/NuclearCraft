package nc.tile.machine;

import nc.NuclearCraft;
import nc.block.machine.BlockFactory;
import nc.crafting.machine.FactoryRecipes;

public class TileFactory extends TileMachineBase {
	
	public TileFactory() {
		super("manufactory", 250000, 1, 1, true, true, 200, 8000, NuclearCraft.factorySpeed, NuclearCraft.factoryEfficiency, FactoryRecipes.instance());
	}
	
	public void updateEntity() {
		super.updateEntity();
		if (flag != flag1) {
			flag1 = flag;
			BlockFactory.updateBlockState(flag, worldObj, xCoord, yCoord, zCoord);
		}
		markDirty();
	}
}