package nc.multiblock.turbine.tile;

import nc.multiblock.turbine.Turbine;
import nc.util.BlockPosHelper;
import net.minecraft.util.EnumFacing;

public class TileTurbineCoilConnector extends TileTurbineDynamoPart {
	
	public TileTurbineCoilConnector() {
		super("connector", null);
	}
	
	@Override
	public void onMachineAssembled(Turbine controller) {
		doStandardNullControllerResponse(controller);
		super.onMachineAssembled(controller);
		//if (getWorld().isRemote) return;
	}
	
	@Override
	public void onMachineBroken() {
		super.onMachineBroken();
		//if (getWorld().isRemote) return;
		//getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos()), 2);
	}
	
	@Override
	protected boolean checkDynamoCoilValid() {
		for (EnumFacing dir : BlockPosHelper.getHorizontals(getMultiblock().flowDir)) {
			if (isDynamoCoil(pos.offset(dir), null)) return true;
		}
		return false;
	}
}
