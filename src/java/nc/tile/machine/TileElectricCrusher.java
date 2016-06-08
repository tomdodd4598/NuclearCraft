package nc.tile.machine;
 
import nc.NuclearCraft;
import nc.block.machine.BlockElectricCrusher;
import nc.crafting.machine.CrusherRecipes;

public class TileElectricCrusher extends TileMachine {
	public static final int[] input = {0, 1};
	public static final int[] output = {0, 1};
	
	public TileElectricCrusher() {
		super("Electric Crusher", 250000, 1, 1, true, true, 200, 8000, NuclearCraft.electricCrusherCrushSpeed, NuclearCraft.electricCrusherCrushEfficiency, CrusherRecipes.instance());
	}
	
	public void updateEntity() {
		super.updateEntity();
		if (flag != flag1) {
			flag1 = flag;
			BlockElectricCrusher.updateBlockState(flag, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
		}
		markDirty();
	}
	
	public int[] getAccessibleSlotsFromSide(int i) {
		return i == 1 ? input : output;
	}
}