package nc.tile.internal.energy;

import gregtech.api.capability.IEnergyContainer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.Optional;

@Optional.Interface(iface = "gregtech.api.capability.IEnergyContainer", modid = "gregtech")
public class EnergyStorageVoidGT implements IEnergyContainer {
	
	public EnergyStorageVoidGT() {}
	
	@Override
	@Optional.Method(modid = "gregtech")
	public long acceptEnergyFromNetwork(EnumFacing side, long voltage, long amperage) {
		return amperage;
	}
	
	@Override
	@Optional.Method(modid = "gregtech")
	public boolean inputsEnergy(EnumFacing side) {
		return true;
	}
	
	@Override
	@Optional.Method(modid = "gregtech")
	public long changeEnergy(long differenceAmount) {
		return 0L;
	}
	
	@Override
	@Optional.Method(modid = "gregtech")
	public long getEnergyStored() {
		return 0L;
	}
	
	@Override
	@Optional.Method(modid = "gregtech")
	public long getEnergyCapacity() {
		return Long.MAX_VALUE;
	}
	
	@Override
	@Optional.Method(modid = "gregtech")
	public long getInputAmperage() {
		return Long.MAX_VALUE;
	}
	
	@Override
	@Optional.Method(modid = "gregtech")
	public long getInputVoltage() {
		return Long.MAX_VALUE;
	}
}
