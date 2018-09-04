package nc.multiblock.container;

import nc.multiblock.condenser.Condenser;
import nc.multiblock.condenser.tile.TileCondenserController;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerCondenserController extends ContainerMultiblockController<Condenser, TileCondenserController> {
	
	public ContainerCondenserController(EntityPlayer player, TileCondenserController controller) {
		super(player, controller);
	}
}
