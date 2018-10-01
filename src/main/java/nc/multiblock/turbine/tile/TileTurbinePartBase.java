package nc.multiblock.turbine.tile;

import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.cuboidal.TileCuboidalMultiblockPartBase;
import nc.multiblock.turbine.Turbine;
import net.minecraft.nbt.NBTTagCompound;

public abstract class TileTurbinePartBase<TURBINE extends Turbine> extends TileCuboidalMultiblockPartBase<TURBINE> {
	
	public boolean isTurbineOn;
	
	public TileTurbinePartBase(Class<TURBINE> tClass, CuboidalPartPositionType positionType) {
		super(tClass, positionType);
	}
	
	public void setIsTurbineOn() {
		if (getMultiblock() != null) isTurbineOn = getMultiblock().isTurbineOn;
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setBoolean("isTurbineOn", isTurbineOn);
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		isTurbineOn = nbt.getBoolean("isTurbineOn");
	}
}
