package nc.capability.radiation.resistance;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import nc.radiation.RadiationArmor;
import nc.util.ItemInfo;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class RadiationResistanceStackProvider implements ICapabilityProvider {
	
	private IRadiationResistance radiationResistance;
	private final ItemStack stack;
	
	public RadiationResistanceStackProvider(ItemStack stack) {
		this.stack = stack;
	}
	
	private IRadiationResistance getRadiationResistance() {
		if (radiationResistance == null) {
			ItemInfo itemInfo = new ItemInfo(stack);
			if (RadiationArmor.ARMOR_RAD_RESISTANCE_MAP.containsKey(itemInfo)) {
				radiationResistance = new RadiationResistance(RadiationArmor.ARMOR_RAD_RESISTANCE_MAP.get(itemInfo));
			}
			else radiationResistance = new RadiationResistance(0D);
		}
		return radiationResistance;
	}
	
	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return capability == IRadiationResistance.CAPABILITY_RADIATION_RESISTANCE;
	}
	
	@Override
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == IRadiationResistance.CAPABILITY_RADIATION_RESISTANCE) return IRadiationResistance.CAPABILITY_RADIATION_RESISTANCE.cast(getRadiationResistance());
		return null;
	}
}
