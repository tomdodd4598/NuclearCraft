package nc.capability.radiation.source;

import javax.annotation.*;

import nc.radiation.RadSources;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.*;

public class RadiationSourceStackProvider implements ICapabilityProvider {
	
	private IRadiationSource radiation = null;
	private final ItemStack stack;
	
	public RadiationSourceStackProvider(ItemStack stack) {
		this.stack = stack;
	}
	
	private IRadiationSource getRadiation() {
		if (radiation == null) {
			radiation = new RadiationSource(RadSources.STACK_MAP.get(RecipeItemHelper.pack(stack)));
		}
		return radiation;
	}
	
	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == IRadiationSource.CAPABILITY_RADIATION_SOURCE;
	}
	
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == IRadiationSource.CAPABILITY_RADIATION_SOURCE) {
			return IRadiationSource.CAPABILITY_RADIATION_SOURCE.cast(getRadiation());
		}
		return null;
	}
}
