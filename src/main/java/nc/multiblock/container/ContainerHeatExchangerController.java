package nc.multiblock.container;

import nc.multiblock.heatExchanger.HeatExchanger;
import nc.multiblock.heatExchanger.tile.TileHeatExchangerController;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerHeatExchangerController extends ContainerMultiblockController<HeatExchanger, TileHeatExchangerController> {
	
	public ContainerHeatExchangerController(EntityPlayer player, TileHeatExchangerController controller) {
		super(player, controller);
	}
}
