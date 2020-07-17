package nc.multiblock.container;

import nc.multiblock.heatExchanger.HeatExchanger;
import nc.multiblock.heatExchanger.tile.TileCondenserController;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerCondenserController extends ContainerMultiblockController<HeatExchanger, TileCondenserController> {
	
	public ContainerCondenserController(EntityPlayer player, TileCondenserController controller) {
		super(player, controller);
	}
}
