package nc.multiblock.container;

import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.tile.IFissionController;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSolidFissionController extends ContainerMultiblockController<FissionReactor, IFissionController> {
	
	public ContainerSolidFissionController(EntityPlayer player, IFissionController controller) {
		super(player, controller);
	}
}
