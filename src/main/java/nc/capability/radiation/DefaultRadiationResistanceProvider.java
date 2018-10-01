package nc.capability.radiation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class DefaultRadiationResistanceProvider implements ICapabilityProvider {
	
	private final IDefaultRadiationResistance radiationResistance;
	
	public DefaultRadiationResistanceProvider(double resistance) {
		radiationResistance = new DefaultRadiationResistance(resistance);
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == IDefaultRadiationResistance.CAPABILITY_DEFAULT_RADIATION_RESISTANCE;
	}

	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == IDefaultRadiationResistance.CAPABILITY_DEFAULT_RADIATION_RESISTANCE) return IDefaultRadiationResistance.CAPABILITY_DEFAULT_RADIATION_RESISTANCE.cast(radiationResistance);
		return null;
	}
}
