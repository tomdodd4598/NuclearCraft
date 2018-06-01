package nc.multiblock.fission.moltensalt.container;

import nc.multiblock.fission.moltensalt.SaltFissionReactor;
import nc.multiblock.fission.moltensalt.tile.TileSaltFissionController;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public class ContainerSaltFissionController extends Container {
	
	TileSaltFissionController controller;
	
	public ContainerSaltFissionController(EntityPlayer player, TileSaltFissionController controller) {
		this.controller = controller;
		if (controller.getMultiblockController() instanceof SaltFissionReactor) {
			SaltFissionReactor reactor = (SaltFissionReactor) controller.getMultiblockController();
			reactor.beginUpdatingPlayer(player);
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer entityplayer) {
		return true;
	}
	
	@Override
	public void putStackInSlot(int slot, ItemStack stack) {
		
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		if (controller.getMultiblockController() instanceof SaltFissionReactor) {
			SaltFissionReactor reactor = (SaltFissionReactor) controller.getMultiblockController();
			reactor.stopUpdatingPlayer(player);
		}
	}
}
