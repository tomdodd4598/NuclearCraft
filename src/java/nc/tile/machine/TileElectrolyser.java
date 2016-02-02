package nc.tile.machine;
 
import nc.NuclearCraft;
import nc.block.machine.BlockElectrolyser;
import nc.crafting.ElectrolyserRecipes;

public class TileElectrolyser extends TileMachine {
	public static final int[] input = {0, 1, 2, 3, 4};
	public static final int[] output = {0, 1, 2, 3, 4};
	
	public TileElectrolyser() {
		super("Electrolyser", 50000, 1, 4, true, true, 25600, 2560000, NuclearCraft.electrolyserSpeed, ElectrolyserRecipes.instance());
	}
	
	public void updateEntity() {
		super.updateEntity();
		if (flag != flag1) {
			flag1 = flag;
			BlockElectrolyser.updateBlockState(flag, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
		}
		markDirty();
	}
	
	public int[] getAccessibleSlotsFromSide(int i) {
		return i == 1 ? input : output;
	}
}