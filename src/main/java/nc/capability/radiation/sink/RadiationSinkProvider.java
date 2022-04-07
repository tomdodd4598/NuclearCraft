package nc.capability.radiation.sink;

import javax.annotation.*;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.*;

public class RadiationSinkProvider implements ICapabilitySerializable<NBTBase> {
	
	private final IRadiationSink radiation;
	
	public RadiationSinkProvider(double startRadiation) {
		radiation = new RadiationSink(startRadiation);
	}
	
	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == IRadiationSink.CAPABILITY_RADIATION_SINK;
	}
	
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == IRadiationSink.CAPABILITY_RADIATION_SINK) {
			return IRadiationSink.CAPABILITY_RADIATION_SINK.cast(radiation);
		}
		return null;
	}
	
	@Override
	public NBTBase serializeNBT() {
		return IRadiationSink.CAPABILITY_RADIATION_SINK.writeNBT(radiation, null);
	}
	
	@Override
	public void deserializeNBT(NBTBase nbt) {
		IRadiationSink.CAPABILITY_RADIATION_SINK.readNBT(radiation, null, nbt);
	}
}
