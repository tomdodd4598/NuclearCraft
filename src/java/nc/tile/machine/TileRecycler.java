package nc.tile.machine;
 
import nc.NuclearCraft;
import nc.block.machine.BlockRecycler;
import nc.crafting.machine.RecyclerRecipes;

public class TileRecycler extends TileMachineBase {
	
	public TileRecycler() {
		super("fuelRecycler", 250000, 1, 4, true, true, 200, 8000, NuclearCraft.recyclerSpeed, NuclearCraft.recyclerEfficiency, RecyclerRecipes.instance());
	}
	
	public void updateEntity() {
		super.updateEntity();
		if (flag != flag1) {
			flag1 = flag;
			BlockRecycler.updateBlockState(flag, worldObj, xCoord, yCoord, zCoord);
		}
		markDirty();
	}
}