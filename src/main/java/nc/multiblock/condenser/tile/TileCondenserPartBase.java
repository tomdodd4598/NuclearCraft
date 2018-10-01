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
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setBoolean("isCondenserOn", isCondenserOn);
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		isCondenserOn = nbt.getBoolean("isCondenserOn");
	}
}
