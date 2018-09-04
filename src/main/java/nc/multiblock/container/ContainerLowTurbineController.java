package nc.multiblock.container;

import nc.multiblock.lowTurbine.LowTurbine;
import nc.multiblock.lowTurbine.tile.TileLowTurbineController;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerLowTurbineController extends ContainerMultiblockController<LowTurbine, TileLowTurbineController> {
	
	public ContainerLowTurbineController(EntityPlayer player, TileLowTurbineController controller) {
		super(player, controller);
	}
}
