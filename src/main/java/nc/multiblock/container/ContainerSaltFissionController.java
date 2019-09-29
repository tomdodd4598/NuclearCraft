package nc.multiblock.container;

import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.salt.tile.TileSaltFissionController;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSaltFissionController extends ContainerMultiblockController<FissionReactor, TileSaltFissionController> {
	
	public ContainerSaltFissionController(EntityPlayer player, TileSaltFissionController controller) {
		super(player, controller);
	}
}
