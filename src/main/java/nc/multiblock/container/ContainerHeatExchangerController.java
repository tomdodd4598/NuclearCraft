package nc.multiblock.container;

import nc.multiblock.heatExchanger.HeatExchanger;
import nc.multiblock.heatExchanger.tile.*;
import nc.network.multiblock.HeatExchangerUpdatePacket;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerHeatExchangerController extends ContainerMultiblockController<HeatExchanger, IHeatExchangerPart, HeatExchangerUpdatePacket, TileHeatExchangerController> {
	
	public ContainerHeatExchangerController(EntityPlayer player, TileHeatExchangerController controller) {
		super(player, controller);
	}
}
