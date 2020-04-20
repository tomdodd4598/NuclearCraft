package nc.multiblock.turbine.tile;

import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.cuboidal.TileCuboidalMultiblockPart;
import nc.multiblock.turbine.Turbine;
import net.minecraft.nbt.NBTTagCompound;

public abstract class TileTurbinePart extends TileCuboidalMultiblockPart<Turbine> implements ITurbinePart {
	
	public boolean isTurbineOn;
	
	public TileTurbinePart(CuboidalPartPositionType positionType) {
		super(Turbine.class, positionType);
	}
	
	@Override
	public Turbine createNewMultiblock() {
		return new Turbine(world);
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
