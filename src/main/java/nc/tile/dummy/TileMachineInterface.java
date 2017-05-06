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
		if(!worldObj.isRemote) {
			pushEnergy();
		}
	}
	
	// Find Master
	
	protected void findMaster() {
		for (EnumFacing side : EnumFacing.VALUES) {
			TileEntity tile = worldObj.getTileEntity(getPos().offset(side));
			if (tile != null) {
				if (isMaster(getPos().offset(side))) masterPosition = getPos().offset(side);
				return;
			}
		}
		masterPosition = null;
	}
	
	/*public boolean isMaster(BlockPos pos) {
	return worldObj.getTileEntity(pos) instanceof TileEnergyItemProcessor
			|| worldObj.getTileEntity(pos) instanceof TileEnergyFluidProcessor
			|| worldObj.getTileEntity(pos) instanceof TileEnergyItemFluidProcessor
			|| worldObj.getTileEntity(pos) instanceof TileNuclearFurnace
			|| worldObj.getTileEntity(pos) instanceof TileItemGenerator
			|| worldObj.getTileEntity(pos) instanceof TileItemFluidGenerator;
	}*/

	public boolean isMaster(BlockPos pos) {
		return worldObj.getTileEntity(pos) instanceof IInterfaceable;
	}
}
