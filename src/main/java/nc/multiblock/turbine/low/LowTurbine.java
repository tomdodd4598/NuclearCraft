package nc.multiblock.turbine.low;

import nc.multiblock.turbine.Turbine;
import nc.multiblock.turbine.low.tile.TileLowTurbineController;
import net.minecraft.world.World;

public class LowTurbine extends Turbine<TileLowTurbineController> {
	
	public LowTurbine(World world) {
		super(world, TileLowTurbineController.class);
	}
}
