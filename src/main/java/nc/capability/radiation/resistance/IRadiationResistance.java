package nc.capability.radiation.resistance;

import nc.Global;
import nc.capability.ICapability;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.*;

public interface IRadiationResistance extends ICapability<IRadiationResistance> {
	
	@CapabilityInject(IRadiationResistance.class)
	public static Capability<IRadiationResistance> CAPABILITY_RADIATION_RESISTANCE = null;
	
	public static final ResourceLocation CAPABILITY_RADIATION_RESISTANCE_NAME = new ResourceLocation(Global.MOD_ID, "capability_default_radiation_resistance");
	
	public double getRadiationResistance();
	
	public void setRadiationResistance(double newResistance);
}
