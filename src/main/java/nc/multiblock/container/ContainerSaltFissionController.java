package nc.multiblock.container;

import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.salt.tile.TileSaltFissionController;
import nc.multiblock.fission.tile.IFissionPart;
import nc.network.multiblock.FissionUpdatePacket;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSaltFissionController extends ContainerMultiblockController<FissionReactor, IFissionPart, FissionUpdatePacket, TileSaltFissionController> {
	
	public ContainerSaltFissionController(EntityPlayer player, TileSaltFissionController controller) {
		super(player, controller);
	}
}
