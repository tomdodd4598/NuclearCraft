package nc.capability.radiation.resistance;

import nc.Global;
import nc.capability.ICapability;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.*;

public interface IRadiationResistance extends ICapability<IRadiationResistance> {
	
	@CapabilityInject(IRadiationResistance.class)
    Capability<IRadiationResistance> CAPABILITY_RADIATION_RESISTANCE = null;
	
	ResourceLocation CAPABILITY_RADIATION_RESISTANCE_NAME = new ResourceLocation(Global.MOD_ID, "capability_default_radiation_resistance");
	
	default double getTotalRadResistance() {
		return getBaseRadResistance() + getShieldingRadResistance();
	}
	
	double getBaseRadResistance();
	
	void setBaseRadResistance(double newResistance);
	
	double getShieldingRadResistance();
	
	void setShieldingRadResistance(double newResistance);
}
