package nc.multiblock.saltFission.tile;

import nc.multiblock.MultiblockControllerBase;
import net.minecraft.nbt.NBTTagCompound;

public class TileSaltFissionModerator extends TileSaltFissionPartBase {
	
	public boolean isInValidPosition;
	
	public TileSaltFissionModerator() {
		super(PartPositionType.INTERIOR);
	}
	
	@Override
	public void onMachineAssembled(MultiblockControllerBase controller) {
		doStandardNullControllerResponse(controller);
		super.onMachineAssembled(controller);
		if (getWorld().isRemote) return;
	}
	
	@Override
	public void onMachineBroken() {
		super.onMachineBroken();
		if (getWorld().isRemote) return;
		//getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos()), 2);
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setBoolean("isInValidPosition", isInValidPosition);
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		isInValidPosition = nbt.getBoolean("isInValidPosition");
	}
}
