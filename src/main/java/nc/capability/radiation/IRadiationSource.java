package nc.capability.radiation;

import nc.Global;
import nc.capability.ICapability;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public interface IRadiationSource extends IRadiation, ICapability<IRadiationSource> {
	
	@CapabilityInject(IRadiationSource.class)
	public static Capability<IRadiationSource> CAPABILITY_RADIATION_SOURCE = null;
	
	public static final ResourceLocation CAPABILITY_RADIATION_SOURCE_NAME = new ResourceLocation(Global.MOD_ID, "capability_radiation_source");
	
	public double getRadiationBuffer();
	
	public void setRadiationBuffer(double newBuffer);
}
