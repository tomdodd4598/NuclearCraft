package nc.capability.radiation.sink;

import nc.Global;
import nc.capability.ICapability;
import nc.capability.radiation.IRadiation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.*;

public interface IRadiationSink extends IRadiation, ICapability<IRadiationSink> {
	
	@CapabilityInject(IRadiationSink.class)
	public static Capability<IRadiationSink> CAPABILITY_RADIATION_SINK = null;
	
	public static final ResourceLocation CAPABILITY_RADIATION_SINK_NAME = new ResourceLocation(Global.MOD_ID, "capability_radiation_sink");
}
