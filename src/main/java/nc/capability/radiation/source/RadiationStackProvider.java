package nc.capability.radiation.source;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import nc.radiation.RadSources;
import nc.util.ItemInfo;
import nc.util.OreDictHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class RadiationStackProvider implements ICapabilityProvider {
	
	private IRadiationSource radiation = null;
	private ItemStack stack = null;
	
	public RadiationStackProvider(ItemStack stack) {
		this.stack = stack;
	}
	
	private IRadiationSource getRadiation() {
		if (radiation == null) {
			ItemInfo itemInfo = new ItemInfo(stack);
			if (RadSources.STACK_MAP.containsKey(itemInfo)) {
				radiation = new RadiationSource(RadSources.STACK_MAP.get(itemInfo));
			}
			else for (String oreName : OreDictHelper.getOreNames(stack)) {
				if (RadSources.ORE_MAP.containsKey(oreName)) {
					radiation = new RadiationSource(RadSources.ORE_MAP.get(oreName));
					break;
				}
			}
			if (radiation == null) {
				radiation = new RadiationSource(0D);
			}
		}
		return radiation;
	}
	
	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == IRadiationSource.CAPABILITY_RADIATION_SOURCE;
	}
	
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == IRadiationSource.CAPABILITY_RADIATION_SOURCE) return IRadiationSource.CAPABILITY_RADIATION_SOURCE.cast(getRadiation());
		return null;
	}
	
	/*@Override
	public NBTBase serializeNBT() {
		return IRadiationSource.CAPABILITY_RADIATION_SOURCE.writeNBT(getRadiation(), null);
	}
	
	@Override
	public void deserializeNBT(NBTBase nbt) {
		IRadiationSource.CAPABILITY_RADIATION_SOURCE.readNBT(getRadiation(), null, nbt);
	}*/
}
