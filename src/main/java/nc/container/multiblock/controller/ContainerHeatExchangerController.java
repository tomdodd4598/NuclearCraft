package nc.container.multiblock.controller;

import nc.multiblock.hx.HeatExchanger;
import nc.network.multiblock.HeatExchangerUpdatePacket;
import nc.tile.hx.*;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerHeatExchangerController extends ContainerMultiblockController<HeatExchanger, IHeatExchangerPart, HeatExchangerUpdatePacket, TileHeatExchangerController> {
	
	public ContainerHeatExchangerController(EntityPlayer player, TileHeatExchangerController controller) {
		super(player, controller);
	}
}
