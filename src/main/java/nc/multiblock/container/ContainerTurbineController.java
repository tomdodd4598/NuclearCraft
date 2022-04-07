package nc.multiblock.container;

import nc.multiblock.turbine.Turbine;
import nc.multiblock.turbine.tile.*;
import nc.network.multiblock.TurbineUpdatePacket;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerTurbineController extends ContainerMultiblockController<Turbine, ITurbinePart, TurbineUpdatePacket, TileTurbineController> {
	
	public ContainerTurbineController(EntityPlayer player, TileTurbineController controller) {
		super(player, controller);
	}
}
