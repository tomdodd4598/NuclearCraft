package nc.container.multiblock.controller;

import nc.multiblock.fission.FissionReactor;
import nc.network.multiblock.FissionUpdatePacket;
import nc.tile.TileContainerInfo;
import nc.tile.fission.*;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerPebbleFissionController extends ContainerMultiblockController<FissionReactor, IFissionPart, FissionUpdatePacket, TilePebbleFissionController, TileContainerInfo<TilePebbleFissionController>> {
	
	public ContainerPebbleFissionController(EntityPlayer player, TilePebbleFissionController controller) {
		super(player, controller);
	}
}
