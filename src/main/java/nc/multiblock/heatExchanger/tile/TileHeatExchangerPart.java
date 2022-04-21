package nc.multiblock.heatExchanger.tile;

import nc.multiblock.cuboidal.*;
import nc.multiblock.heatExchanger.HeatExchanger;
import net.minecraft.nbt.NBTTagCompound;

public abstract class TileHeatExchangerPart extends TileCuboidalMultiblockPart<HeatExchanger, IHeatExchangerPart> implements IHeatExchangerPart {
	
	public boolean isHeatExchangerOn;
	
	public TileHeatExchangerPart(CuboidalPartPositionType positionType) {
		super(HeatExchanger.class, IHeatExchangerPart.class, positionType);
	}
	
	@Override
	public HeatExchanger createNewMultiblock() {
		return new HeatExchanger(world);
	}
	
	public void setIsHeatExchangerOn() {
		if (getMultiblock() != null) {
			isHeatExchangerOn = getMultiblock().isHeatExchangerOn;
		}
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
