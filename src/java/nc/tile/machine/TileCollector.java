package nc.tile.machine;
 
import nc.NuclearCraft;
import nc.block.machine.BlockCollector;
import nc.crafting.machine.CollectorRecipes;

public class TileCollector extends TileMachineBase {
	
	public TileCollector() {
		super("heliumCollector", 1, 1, 1, false, false, 1800, 0, NuclearCraft.collectorSpeed, 100, CollectorRecipes.instance());
	}
	
	public void updateEntity() {
		super.updateEntity();
		if (flag != flag1) {
			flag1 = flag;
			BlockCollector.updateBlockState(flag, worldObj, xCoord, yCoord, zCoord);
		}
		markDirty();
	}
}