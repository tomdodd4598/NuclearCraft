package nc.multiblock.turbine.high;

import nc.multiblock.turbine.Turbine;
import nc.multiblock.turbine.high.tile.TileHighTurbineController;
import net.minecraft.world.World;

public class HighTurbine extends Turbine<TileHighTurbineController> {
	
	public HighTurbine(World world) {
		super(world, TileHighTurbineController.class);
	}
}
