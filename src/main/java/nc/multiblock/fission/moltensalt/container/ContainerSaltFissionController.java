package nc.multiblock.fission.moltensalt.container;

import nc.multiblock.fission.moltensalt.tile.SaltFissionReactor;
import nc.multiblock.fission.moltensalt.tile.TileSaltFissionController;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public class ContainerSaltFissionController extends Container {
	
	TileSaltFissionController tile;

	public ContainerSaltFissionController(EntityPlayer player, TileSaltFissionController controller) {
		tile = controller;
		if (tile.getMultiblockController() !=null) ((SaltFissionReactor)tile.getMultiblockController()).beginUpdatingPlayer(player);
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
		if(tile != null && tile.getMultiblockController() != null) ((SaltFissionReactor)tile.getMultiblockController()).stopUpdatingPlayer(player);
	}
}
