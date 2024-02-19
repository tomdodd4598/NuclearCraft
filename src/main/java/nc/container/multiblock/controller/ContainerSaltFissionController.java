package nc.container.multiblock.controller;

import nc.multiblock.fission.FissionReactor;
import nc.network.multiblock.FissionUpdatePacket;
import nc.tile.fission.*;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerSaltFissionController extends ContainerMultiblockController<FissionReactor, IFissionPart, FissionUpdatePacket, TileSaltFissionController> {
	
	public ContainerSaltFissionController(EntityPlayer player, TileSaltFissionController controller) {
		super(player, controller);
	}
}
