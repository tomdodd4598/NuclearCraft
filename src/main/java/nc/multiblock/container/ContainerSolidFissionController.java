package nc.multiblock.container;

import nc.multiblock.fission.FissionReactor;
import nc.multiblock.fission.solid.tile.TileSolidFissionController;
import nc.multiblock.fission.tile.IFissionPart;
import nc.network.multiblock.FissionUpdatePacket;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSolidFissionController extends ContainerMultiblockController<FissionReactor, IFissionPart, FissionUpdatePacket, TileSolidFissionController> {
	
	public ContainerSolidFissionController(EntityPlayer player, TileSolidFissionController controller) {
		super(player, controller);
	}
}
