package nc.multiblock.saltFission.tile;

import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.cuboidal.TileCuboidalMultiblockPartBase;
import nc.multiblock.saltFission.SaltFissionReactor;
import net.minecraft.nbt.NBTTagCompound;

public abstract class TileSaltFissionPartBase extends TileCuboidalMultiblockPartBase<SaltFissionReactor> {
	
	public boolean isReactorOn;
	
	public TileSaltFissionPartBase(CuboidalPartPositionType positionType) {
		super(SaltFissionReactor.class, positionType);
	}
	
	@Override
	public SaltFissionReactor createNewMultiblock() {
		return new SaltFissionReactor(getWorld());
	}
	
	public void setIsReactorOn() {
		if (getMultiblock() != null) isReactorOn = getMultiblock().isReactorOn;
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setBoolean("isReactorOn", isReactorOn);
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		isReactorOn = nbt.getBoolean("isReactorOn");
	}
}
