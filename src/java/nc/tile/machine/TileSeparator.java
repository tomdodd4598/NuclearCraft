package nc.tile.machine;
 
import nc.NuclearCraft;
import nc.block.machine.BlockSeparator;
import nc.crafting.SeparatorRecipes;

public class TileSeparator extends TileMachine {
	public static final int[] input = {0, 1, 2};
	public static final int[] output = {0, 1, 2};
	
	public TileSeparator() {
		super("Isotope Separator", 25000, 1, 2, true, true, 200, 8000, NuclearCraft.separatorSpeed, SeparatorRecipes.instance());
	}
	
	public void updateEntity() {
		super.updateEntity();
		if (flag != flag1) {
			flag1 = flag;
			BlockSeparator.updateBlockState(flag, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
		}
		markDirty();
	}
	
	public int[] getAccessibleSlotsFromSide(int i) {
		return i == 1 ? input : output;
	}
}