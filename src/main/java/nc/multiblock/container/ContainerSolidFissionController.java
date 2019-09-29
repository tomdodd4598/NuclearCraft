package nc.multiblock.container;

import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.solid.tile.TileSolidFissionController;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSolidFissionController extends ContainerMultiblockController<FissionReactor, TileSolidFissionController> {
	
	public ContainerSolidFissionController(EntityPlayer player, TileSolidFissionController controller) {
		super(player, controller);
	}
}
