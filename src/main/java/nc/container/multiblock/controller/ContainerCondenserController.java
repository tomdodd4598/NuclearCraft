package nc.container.multiblock.controller;

import nc.multiblock.hx.HeatExchanger;
import nc.network.multiblock.HeatExchangerUpdatePacket;
import nc.tile.hx.*;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerCondenserController extends ContainerMultiblockController<HeatExchanger, IHeatExchangerPart, HeatExchangerUpdatePacket, TileCondenserController> {
	
	public ContainerCondenserController(EntityPlayer player, TileCondenserController controller) {
		super(player, controller);
	}
}
