package nc.multiblock.highTurbine.tile;

import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.cuboidal.TileCuboidalMultiblockPartBase;
import nc.multiblock.highTurbine.HighTurbine;
import net.minecraft.nbt.NBTTagCompound;

public abstract class TileHighTurbinePartBase extends TileCuboidalMultiblockPartBase<HighTurbine> {
	
	public boolean isHighTurbineOn;
	
	public TileHighTurbinePartBase(CuboidalPartPositionType positionType) {
		super(HighTurbine.class, positionType);
	}
	
	@Override
	public HighTurbine createNewMultiblock() {
		return new HighTurbine(getWorld());
	}
	
	public void setIsHighTurbineOn() {
		if (getMultiblock() != null) isHighTurbineOn = getMultiblock().isHighTurbineOn;
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setBoolean("isHighTurbineOn", isHighTurbineOn);
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		isHighTurbineOn = nbt.getBoolean("isHighTurbineOn");
	}
}
