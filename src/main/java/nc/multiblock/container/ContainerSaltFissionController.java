package nc.multiblock.container;

import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.tile.IFissionController;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSaltFissionController extends ContainerMultiblockController<FissionReactor, IFissionController> {
	
	public ContainerSaltFissionController(EntityPlayer player, IFissionController controller) {
		super(player, controller);
	}
}
