package nc.multiblock.lowTurbine.tile;

import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.cuboidal.TileCuboidalMultiblockPartBase;
import nc.multiblock.lowTurbine.LowTurbine;
import net.minecraft.nbt.NBTTagCompound;

public abstract class TileLowTurbinePartBase extends TileCuboidalMultiblockPartBase<LowTurbine> {
	
	public boolean isLowTurbineOn;
	
	public TileLowTurbinePartBase(CuboidalPartPositionType positionType) {
		super(LowTurbine.class, positionType);
	}
	
	@Override
	public LowTurbine createNewMultiblock() {
		return new LowTurbine(getWorld());
	}
	
	public void setIsLowTurbineOn() {
		if (getMultiblock() != null) isLowTurbineOn = getMultiblock().isLowTurbineOn;
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setBoolean("isLowTurbineOn", isLowTurbineOn);
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		isLowTurbineOn = nbt.getBoolean("isLowTurbineOn");
	}
}
