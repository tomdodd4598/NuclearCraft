package nc.tile.machine;
 
import nc.NuclearCraft;
import nc.block.machine.BlockElectrolyser;
import nc.crafting.machine.ElectrolyserRecipes;
import net.minecraftforge.fluids.FluidRegistry;

public class TileElectrolyser extends TileMachineFluidIn {
	
	public TileElectrolyser() {
		super("electrolyser", 250000, 48000, 1, 4, true, true, 20000, 1000000, FluidRegistry.WATER, 12000, NuclearCraft.electrolyserSpeed, NuclearCraft.electrolyserEfficiency, ElectrolyserRecipes.instance());
	}
	
	public void updateEntity() {
		super.updateEntity();
		if (flag != flag1) {
			flag1 = flag;
			BlockElectrolyser.updateBlockState(flag, worldObj, xCoord, yCoord, zCoord);
		}
		markDirty();
	}
}