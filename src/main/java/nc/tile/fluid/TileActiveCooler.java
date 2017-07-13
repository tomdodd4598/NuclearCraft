package nc.tile.fluid;

import nc.config.NCConfig;
import nc.fluid.EnumTank.FluidConnection;
import nc.tile.dummy.IInterfaceable;

public class TileActiveCooler extends TileFluid implements IInterfaceable {
	
	public TileActiveCooler() {
		super(new int[] {NCConfig.fission_update_rate*2*NCConfig.fission_active_cooler_max_rate}, new FluidConnection[] {FluidConnection.IN}, new String[] {"water", "redstone", "glowstone", "liquidhelium", "ender", "cryotheum", "ice"});
	}
}
