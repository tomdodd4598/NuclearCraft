package nc.capability.radiation.source;

import nc.Global;
import nc.capability.ICapability;
import nc.capability.radiation.IRadiation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.*;

public interface IRadiationSource extends IRadiation, ICapability<IRadiationSource> {
	
	@CapabilityInject(IRadiationSource.class)
	public static Capability<IRadiationSource> CAPABILITY_RADIATION_SOURCE = null;
	
	public static final ResourceLocation CAPABILITY_RADIATION_SOURCE_NAME = new ResourceLocation(Global.MOD_ID, "capability_radiation_source");
	
	public double getRadiationBuffer();
	
	public void setRadiationBuffer(double newBuffer);
	
	public double getScrubbingFraction();
	
	public void setScrubbingFraction(double newFraction);
	
	public double getEffectiveScrubberCount();
	
	public void setEffectiveScrubberCount(double newScrubberCount);
}
