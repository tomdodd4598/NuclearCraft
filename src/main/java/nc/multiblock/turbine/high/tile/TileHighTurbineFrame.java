package nc.multiblock.turbine.high.tile;

import nc.multiblock.turbine.high.HighTurbine;
import nc.multiblock.turbine.tile.TileTurbineFrame;

public class TileHighTurbineFrame extends TileTurbineFrame<HighTurbine> {
	
	public TileHighTurbineFrame() {
		super(HighTurbine.class);
	}
	
	@Override
	public HighTurbine createNewMultiblock() {
		return new HighTurbine(getWorld());
	}
}
