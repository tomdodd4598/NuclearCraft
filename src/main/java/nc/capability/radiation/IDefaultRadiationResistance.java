package nc.capability.radiation;

import nc.Global;
import nc.capability.ICapability;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public interface IDefaultRadiationResistance extends ICapability<IDefaultRadiationResistance> {
	
	@CapabilityInject(IDefaultRadiationResistance.class)
	public static Capability<IDefaultRadiationResistance> CAPABILITY_DEFAULT_RADIATION_RESISTANCE = null;
	
	public static final ResourceLocation CAPABILITY_DEFAULT_RADIATION_RESISTANCE_NAME = new ResourceLocation(Global.MOD_ID, "capability_default_radiation_resistance");
	
	public double getRadiationResistance();
}
