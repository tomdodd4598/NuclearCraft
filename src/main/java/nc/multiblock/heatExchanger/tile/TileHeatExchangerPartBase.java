package nc.multiblock.heatExchanger.tile;

import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.cuboidal.TileCuboidalMultiblockPartBase;
import nc.multiblock.heatExchanger.HeatExchanger;
import net.minecraft.nbt.NBTTagCompound;

public abstract class TileHeatExchangerPartBase extends TileCuboidalMultiblockPartBase<HeatExchanger> {
	
	public boolean isHeatExchangerOn;
	
	public TileHeatExchangerPartBase(CuboidalPartPositionType positionType) {
		super(HeatExchanger.class, positionType);
	}
	
	@Override
	public HeatExchanger createNewMultiblock() {
		return new HeatExchanger(getWorld());
	}
	
	public void setIsHeatExchangerOn() {
		if (getMultiblock() != null) isHeatExchangerOn = getMultiblock().isHeatExchangerOn;
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setBoolean("isHeatExchangerOn", isHeatExchangerOn);
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		isHeatExchangerOn = nbt.getBoolean("isHeatExchangerOn");
	}
}
