package nc.tile.fluid;

import nc.config.NCConfig;
import nc.enumm.MetaEnums.CoolerType;
import nc.tile.dummy.IInterfaceable;
import nc.tile.energyFluid.IBufferable;
import nc.tile.internal.fluid.FluidConnection;

public class TileActiveCooler extends TileFluid implements IInterfaceable, IBufferable, IFluidSpread {
	
	public TileActiveCooler() {
		super(4*NCConfig.machine_update_rate*NCConfig.active_cooler_max_rate, FluidConnection.IN, validFluids());
	}
	
	@Override
	public void update() {
		super.update();
		if (!world.isRemote && shouldCheck()) {
			spreadFluid();
		}
		tick();
	}
	
	private static String[] validFluids() {
		String[] validFluids = new String[CoolerType.values().length - 1];
		for (int i = 1; i < CoolerType.values().length; i++) validFluids[i - 1] = CoolerType.values()[i].getFluidName();
		return validFluids;
	}
}
