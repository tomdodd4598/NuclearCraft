package nc.tile.fluid;

import java.util.ArrayList;
import java.util.List;

import nc.config.NCConfig;
import nc.enumm.MetaEnums.CoolerType;
import nc.tile.dummy.IInterfaceable;
import nc.tile.energyFluid.IBufferable;
import nc.tile.internal.fluid.FluidConnection;

public class TileActiveCooler extends TileFluid implements IInterfaceable, IBufferable, IFluidSpread {
	
	public TileActiveCooler() {
		super(4*4*NCConfig.machine_update_rate*NCConfig.active_cooler_max_rate, FluidConnection.IN, validFluids());
	}
	
	@Override
	public void update() {
		super.update();
		if(!world.isRemote) {
			if(shouldTileCheck()) spreadFluid();
			tickTile();
		}
	}
	
	@Override
	public void tickTile() {
		tickCount++; tickCount %= NCConfig.machine_update_rate / 5;
	}
	
	private static List<String> validFluids() {
		List<String> validFluids = new ArrayList<String>();
		for (int i = 1; i < CoolerType.values().length; i++) validFluids.add(CoolerType.values()[i].getFluidName());
		return validFluids;
	}
}
