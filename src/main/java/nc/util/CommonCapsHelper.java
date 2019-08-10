package nc.util;

import org.cyclops.commoncapabilities.api.capability.temperature.ITemperature;
import org.cyclops.commoncapabilities.api.capability.work.IWorker;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class CommonCapsHelper {
	
	@CapabilityInject(ITemperature.class)
	public static Capability<ITemperature> CAPABILITY_TEMPERATURE = null;
	
	@CapabilityInject(IWorker.class)
	public static Capability<IWorker> CAPABILITY_WORKER = null;
}
