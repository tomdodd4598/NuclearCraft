package nc.multiblock.qComputer.tile;

import nc.multiblock.qComputer.QuantumComputer;
import nc.multiblock.tile.TileMultiblockPart;
import net.minecraft.nbt.NBTTagCompound;

public abstract class TileQuantumComputerPart extends TileMultiblockPart<QuantumComputer> implements IQuantumComputerPart {
	
	public boolean isHeatExchangerOn;
	
	public TileQuantumComputerPart() {
		super(QuantumComputer.class);
	}
	
	@Override
	public QuantumComputer createNewMultiblock() {
		return new QuantumComputer(world);
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
	}
}
