package nc.multiblock.turbine.low.tile;

import nc.multiblock.turbine.low.LowTurbine;
import nc.multiblock.turbine.tile.TileTurbineWall;

public class TileLowTurbineWall extends TileTurbineWall<LowTurbine> {
	
	public TileLowTurbineWall() {
		super(LowTurbine.class);
	}
	
	@Override
	public LowTurbine createNewMultiblock() {
		return new LowTurbine(getWorld());
	}
}
