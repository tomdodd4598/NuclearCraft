package nc.multiblock.turbine.low.tile;

import nc.multiblock.turbine.low.LowTurbine;
import nc.multiblock.turbine.low.block.BlockLowTurbineController;
import nc.multiblock.turbine.tile.TileTurbineController;

public class TileLowTurbineController extends TileTurbineController<LowTurbine, BlockLowTurbineController> {
	
	public TileLowTurbineController() {
		super(LowTurbine.class, BlockLowTurbineController.class);
	}
	
	@Override
	public LowTurbine createNewMultiblock() {
		return new LowTurbine(getWorld());
	}
}
