package nc.multiblock.turbine.high.tile;

import nc.multiblock.turbine.high.HighTurbine;
import nc.multiblock.turbine.tile.TileTurbineGlass;

public class TileHighTurbineGlass extends TileTurbineGlass<HighTurbine> {
	
	public TileHighTurbineGlass() {
		super(HighTurbine.class);
	}
	
	@Override
	public HighTurbine createNewMultiblock() {
		return new HighTurbine(getWorld());
	}
}
