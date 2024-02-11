package nc.container.multiblock;

import nc.multiblock.fission.FissionReactor;
import nc.network.multiblock.FissionUpdatePacket;
import nc.tile.fission.*;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSolidFissionController extends ContainerMultiblockController<FissionReactor, IFissionPart, FissionUpdatePacket, TileSolidFissionController> {
	
	public ContainerSolidFissionController(EntityPlayer player, TileSolidFissionController controller) {
		super(player, controller);
	}
}
