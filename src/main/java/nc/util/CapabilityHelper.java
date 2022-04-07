package nc.util;

import mekanism.api.gas.IGasHandler;
import mekanism.common.base.ILogisticalTransporter;
import net.minecraftforge.common.capabilities.*;

public class CapabilityHelper {
	
	// Mekanism
	
	@CapabilityInject(IGasHandler.class)
	public static Capability<IGasHandler> GAS_HANDLER_CAPABILITY = null;
	
	@CapabilityInject(ILogisticalTransporter.class)
	public static Capability<ILogisticalTransporter> LOGISTICAL_TRANSPORTER_CAPABILITY = null;
}
