package nc.multiblock.container;

import nc.multiblock.MultiblockBase;
import nc.multiblock.MultiblockTileBase;
import nc.multiblock.saltFission.SaltFissionReactor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public class ContainerMultiblockController<T extends MultiblockBase, V extends MultiblockTileBase<T>> extends Container {
	
	protected final V controller;
	
	public ContainerMultiblockController(EntityPlayer player, V controller) {
		this.controller = controller;
		if (controller.getMultiblock() instanceof SaltFissionReactor) {
			T reactor = controller.getMultiblock();
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
		if (controller.getMultiblock() != null) controller.getMultiblock().stopUpdatingPlayer(player);
	}
}
