package nc.multiblock.saltFission.tile;

import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.saltFission.SaltFissionReactor;
import net.minecraft.nbt.NBTTagCompound;

public class TileSaltFissionModerator extends TileSaltFissionPartBase {
	
	public boolean isInValidPosition, isInModerationLine;
	
	public TileSaltFissionModerator() {
		super(CuboidalPartPositionType.INTERIOR);
	}
	
	@Override
	public void onMachineAssembled(SaltFissionReactor controller) {
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
	
	public boolean contributeExtraHeat() {
		return isInModerationLine && !isInValidPosition;
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setBoolean("isInValidPosition", isInValidPosition);
		nbt.setBoolean("isInModerationLine", isInModerationLine);
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		isInValidPosition = nbt.getBoolean("isInValidPosition");
		isInModerationLine = nbt.getBoolean("isInModerationLine");
	}
}
