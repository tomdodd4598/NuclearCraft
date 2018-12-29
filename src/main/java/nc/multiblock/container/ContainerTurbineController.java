package nc.multiblock.container;

import nc.multiblock.turbine.Turbine;
import nc.multiblock.turbine.tile.TileTurbineController;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerTurbineController extends ContainerMultiblockController<Turbine, TileTurbineController> {
	
	public ContainerTurbineController(EntityPlayer player, TileTurbineController controller) {
		super(player, controller);
	}
}
