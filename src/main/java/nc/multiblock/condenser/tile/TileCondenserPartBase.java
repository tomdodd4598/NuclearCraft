package nc.multiblock.condenser.tile;

import nc.multiblock.condenser.Condenser;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.cuboidal.TileCuboidalMultiblockPartBase;
import net.minecraft.nbt.NBTTagCompound;

public abstract class TileCondenserPartBase extends TileCuboidalMultiblockPartBase<Condenser> {
	
	public boolean isCondenserOn;
	
	public TileCondenserPartBase(CuboidalPartPositionType positionType) {
		super(Condenser.class, positionType);
	}
	
	@Override
	public Condenser createNewMultiblock() {
		return new Condenser(getWorld());
	}
	
	public void setIsCondenserOn() {
		if (getMultiblock() != null) isCondenserOn = getMultiblock().isCondenserOn;
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setBoolean("isCondenserOn", isCondenserOn);
		return nbt;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		isCondenserOn = nbt.getBoolean("isCondenserOn");
	}
}
