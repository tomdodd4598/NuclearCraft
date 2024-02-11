package nc.tile.quantum;

import nc.multiblock.quantum.QuantumComputer;
import nc.tile.multiblock.TileMultiblockPart;
import net.minecraft.nbt.NBTTagCompound;

public abstract class TileQuantumComputerPart extends TileMultiblockPart<QuantumComputer, IQuantumComputerPart> implements IQuantumComputerPart {
	
	public boolean isHeatExchangerOn;
	
	public TileQuantumComputerPart() {
		super(QuantumComputer.class, IQuantumComputerPart.class);
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
