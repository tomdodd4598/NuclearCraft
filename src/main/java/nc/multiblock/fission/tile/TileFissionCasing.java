package nc.multiblock.fission.tile;

import nc.block.property.BlockProperties;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.fission.FissionReactor;

public class TileFissionCasing extends TileFissionPart {
	
	public TileFissionCasing() {
		super(CuboidalPartPositionType.EXTERIOR);
	}
	
	@Override
	public void onMachineAssembled(FissionReactor controller) {
		doStandardNullControllerResponse(controller);
		super.onMachineAssembled(controller);
		if (!getWorld().isRemote && getPartPosition().isFrame()) {
			getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos()).withProperty(BlockProperties.FRAME, true), 2);
		}
	}
	
	@Override
	public void onMachineBroken() {
		if (!getWorld().isRemote && getPartPosition().isFrame()) {
			getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos()).withProperty(BlockProperties.FRAME, false), 2);
		}
		super.onMachineBroken();
	}
}
