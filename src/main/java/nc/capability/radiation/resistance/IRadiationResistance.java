package nc.capability.radiation.resistance;

import nc.Global;
import nc.capability.ICapability;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.*;

public interface IRadiationResistance extends ICapability<IRadiationResistance> {
	
	@CapabilityInject(IRadiationResistance.class)
	public static Capability<IRadiationResistance> CAPABILITY_RADIATION_RESISTANCE = null;
	
	public static final ResourceLocation CAPABILITY_RADIATION_RESISTANCE_NAME = new ResourceLocation(Global.MOD_ID, "capability_default_radiation_resistance");
	
	public default double getTotalRadResistance() {
		return getBaseRadResistance() + getShieldingRadResistance();
	}
	
	public double getBaseRadResistance();
	
	public void setBaseRadResistance(double newResistance);
	
	public double getShieldingRadResistance();
	
	public void setShieldingRadResistance(double newResistance);
}
