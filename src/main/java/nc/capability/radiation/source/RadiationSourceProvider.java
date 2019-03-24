package nc.capability.radiation.source;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class RadiationSourceProvider implements ICapabilitySerializable {
	
	private IRadiationSource radiation = null;
	
	public RadiationSourceProvider(double startRadiation) {
		radiation = new RadiationSource(startRadiation);
		
	}
	
	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == IRadiationSource.CAPABILITY_RADIATION_SOURCE;
	}
	
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == IRadiationSource.CAPABILITY_RADIATION_SOURCE) return IRadiationSource.CAPABILITY_RADIATION_SOURCE.cast(radiation);
		return null;
	}
	
	@Override
	public NBTBase serializeNBT() {
		return IRadiationSource.CAPABILITY_RADIATION_SOURCE.writeNBT(radiation, null);
	}
	
	@Override
	public void deserializeNBT(NBTBase nbt) {
		IRadiationSource.CAPABILITY_RADIATION_SOURCE.readNBT(radiation, null, nbt);
	}
}
