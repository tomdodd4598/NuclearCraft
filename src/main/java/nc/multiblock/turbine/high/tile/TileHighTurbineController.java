package nc.multiblock.turbine.high.tile;

import nc.multiblock.turbine.high.HighTurbine;
import nc.multiblock.turbine.high.block.BlockHighTurbineController;
import nc.multiblock.turbine.tile.TileTurbineController;

public class TileHighTurbineController extends TileTurbineController<HighTurbine, BlockHighTurbineController> {
	
	public TileHighTurbineController() {
		super(HighTurbine.class, BlockHighTurbineController.class);
	}
	
	@Override
	public HighTurbine createNewMultiblock() {
		return new HighTurbine(getWorld());
	}
}
