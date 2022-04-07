package nc.multiblock.container;

import nc.multiblock.heatExchanger.HeatExchanger;
import nc.multiblock.heatExchanger.tile.*;
import nc.network.multiblock.HeatExchangerUpdatePacket;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerCondenserController extends ContainerMultiblockController<HeatExchanger, IHeatExchangerPart, HeatExchangerUpdatePacket, TileCondenserController> {
	
	public ContainerCondenserController(EntityPlayer player, TileCondenserController controller) {
		super(player, controller);
	}
}
