package nc.multiblock.turbine.high.tile;

import nc.multiblock.turbine.high.HighTurbine;
import nc.multiblock.turbine.tile.TileTurbineWall;

public class TileHighTurbineWall extends TileTurbineWall<HighTurbine> {
	
	public TileHighTurbineWall() {
		super(HighTurbine.class);
	}
	
	@Override
	public HighTurbine createNewMultiblock() {
		return new HighTurbine(getWorld());
	}
}
