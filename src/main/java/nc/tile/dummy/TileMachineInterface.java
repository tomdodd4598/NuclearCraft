package nc.tile.dummy;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class TileMachineInterface extends TileDummy {
	
	public int tickCount;

	public TileMachineInterface() {
		super("machine_interface", 20);
	}
	
	public void update() {
		super.update();
		if(!world.isRemote) {
			pushEnergy();
		}
	}
	
	// Find Master
	
	protected void findMaster() {
		for (EnumFacing side : EnumFacing.VALUES) {
			TileEntity tile = world.getTileEntity(getPos().offset(side));
			if (tile != null) {
				if (isMaster(getPos().offset(side))) masterPosition = getPos().offset(side);
				return;
			}
		}
		masterPosition = null;
	}
	
	/*public boolean isMaster(BlockPos pos) {
		return world.getTileEntity(pos) instanceof TileEnergyItemProcessor
				|| world.getTileEntity(pos) instanceof TileEnergyFluidProcessor
				|| world.getTileEntity(pos) instanceof TileEnergyItemFluidProcessor
				|| world.getTileEntity(pos) instanceof TileNuclearFurnace
				|| world.getTileEntity(pos) instanceof TileItemGenerator
				|| world.getTileEntity(pos) instanceof TileItemFluidGenerator;
	}*/
	
	public boolean isMaster(BlockPos pos) {
		return world.getTileEntity(pos) instanceof IInterfaceable;
	}
}
