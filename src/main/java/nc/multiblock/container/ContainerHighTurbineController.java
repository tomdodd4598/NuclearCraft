package nc.multiblock.container;

import nc.multiblock.highTurbine.HighTurbine;
import nc.multiblock.highTurbine.tile.TileHighTurbineController;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerHighTurbineController extends ContainerMultiblockController<HighTurbine, TileHighTurbineController> {
	
	public ContainerHighTurbineController(EntityPlayer player, TileHighTurbineController controller) {
		super(player, controller);
	}
}
