package nc.tile.machine;
 
import nc.block.machine.BlockAutoWorkspace;
import nc.crafting.machine.AutoWorkspaceRecipes;

public class TileAutoWorkspace extends TileMachine {
	public static final int[] input = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
	public static final int[] output = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
	
	public TileAutoWorkspace() {
		super("Auto Workspace", 1, 9, 1, false, false, 0, 0, 100, 100, AutoWorkspaceRecipes.instance());
	}
	
	public void updateEntity() {
		super.updateEntity();
		if (flag != flag1) {
			flag1 = flag;
			BlockAutoWorkspace.updateBlockState(flag, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
		}
		markDirty();
	}
	
	public int[] getAccessibleSlotsFromSide(int i) {
		return i == 1 ? input : output;
	}
}