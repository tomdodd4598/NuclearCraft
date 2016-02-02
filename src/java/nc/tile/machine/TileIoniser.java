package nc.tile.machine;
 
import nc.NuclearCraft;
import nc.block.machine.BlockIoniser;
import nc.crafting.IoniserRecipes;

public class TileIoniser extends TileMachine {
	public static final int[] input = {0, 1, 2, 3};
	public static final int[] output = {0, 1, 2, 3};
	
	public TileIoniser() {
		super("Ioniser", 40000, 2, 2, true, true, 600, 60000, NuclearCraft.ioniserSpeed, IoniserRecipes.instance());
	}
	
	public void updateEntity() {
		super.updateEntity();
		if (flag != flag1) {
			flag1 = flag;
			BlockIoniser.updateBlockState(flag, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
		}
		markDirty();
	}
	
	public int[] getAccessibleSlotsFromSide(int i) {
		return i == 1 ? input : output;
	}
}