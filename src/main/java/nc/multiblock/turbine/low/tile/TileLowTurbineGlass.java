package nc.multiblock.turbine.low.tile;

import nc.multiblock.turbine.low.LowTurbine;
import nc.multiblock.turbine.tile.TileTurbineGlass;

public class TileLowTurbineGlass extends TileTurbineGlass<LowTurbine> {
	
	public TileLowTurbineGlass() {
		super(LowTurbine.class);
	}
	
	@Override
	public LowTurbine createNewMultiblock() {
		return new LowTurbine(getWorld());
	}
}
