package nc.capability.radiation.source;

import nc.Global;
import nc.capability.ICapability;
import nc.capability.radiation.IRadiation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.*;

public interface IRadiationSource extends IRadiation, ICapability<IRadiationSource> {
	
	@CapabilityInject(IRadiationSource.class)
    Capability<IRadiationSource> CAPABILITY_RADIATION_SOURCE = null;
	
	ResourceLocation CAPABILITY_RADIATION_SOURCE_NAME = new ResourceLocation(Global.MOD_ID, "capability_radiation_source");
	
	double getRadiationBuffer();
	
	void setRadiationBuffer(double newBuffer);
	
	double getScrubbingFraction();
	
	void setScrubbingFraction(double newFraction);
	
	double getEffectiveScrubberCount();
	
	void setEffectiveScrubberCount(double newScrubberCount);
}
