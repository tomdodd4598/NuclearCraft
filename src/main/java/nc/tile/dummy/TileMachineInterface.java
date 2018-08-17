package nc.tile.dummy;

import nc.tile.energyFluid.IBufferable;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class TileMachineInterface extends TileDummy<TileEntity> implements IBufferable {
	
	public int tickCount;

	public TileMachineInterface() {
		super(TileEntity.class, "machine_interface", 20, null);
	}
	
	@Override
	public void update() {
		super.update();
		if(!world.isRemote) {
			pushEnergy();
			pushFluid();
		}
	}
	
	// Find Master
	
	@Override
	public void findMaster() {
		for (EnumFacing side : EnumFacing.VALUES) {
			TileEntity tile = world.getTileEntity(getPos().offset(side));
			if (tile != null) {
				if (isMaster(getPos().offset(side))) {
					masterPosition = getPos().offset(side);
					return;
				}
			}
		}
		masterPosition = null;
	}
	
	// Special case for interface type
	@Override
	public boolean isMaster(BlockPos pos) {
		return world.getTileEntity(pos) instanceof IInterfaceable;
	}
}
