package nc.multiblock.turbine.low.tile;

import nc.multiblock.turbine.low.LowTurbine;
import nc.multiblock.turbine.tile.TileTurbineFrame;

public class TileLowTurbineFrame extends TileTurbineFrame<LowTurbine> {
	
	public TileLowTurbineFrame() {
		super(LowTurbine.class);
	}
	
	@Override
	public LowTurbine createNewMultiblock() {
		return new LowTurbine(getWorld());
	}
}
