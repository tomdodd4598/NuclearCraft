package nc.tile.machine;
 
import nc.NuclearCraft;
import nc.block.machine.BlockCollector;
import nc.crafting.machine.CollectorRecipes;

public class TileCollector extends TileMachine {
	public static final int[] input = {0, 1};
	public static final int[] output = {0, 1};
	
	public TileCollector() {
		super("Helium Collector", 1, 1, 1, false, false, 1800, 0, NuclearCraft.collectorSpeed, 100, CollectorRecipes.instance());
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