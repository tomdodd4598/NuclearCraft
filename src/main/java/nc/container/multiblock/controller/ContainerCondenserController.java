package nc.container.multiblock.controller;

import nc.multiblock.hx.HeatExchanger;
import nc.network.multiblock.HeatExchangerUpdatePacket;
import nc.tile.TileContainerInfo;
import nc.tile.hx.*;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerCondenserController extends ContainerMultiblockController<HeatExchanger, IHeatExchangerPart, HeatExchangerUpdatePacket, TileCondenserController, TileContainerInfo<TileCondenserController>> {
	
	public ContainerCondenserController(EntityPlayer player, TileCondenserController controller) {
		super(player, controller);
	}
}
