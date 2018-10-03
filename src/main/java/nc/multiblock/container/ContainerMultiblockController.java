package nc.multiblock.container;

import nc.multiblock.MultiblockBase;
import nc.multiblock.MultiblockTileBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public class ContainerMultiblockController<MULTIBLOCK extends MultiblockBase, CONTROLLER extends MultiblockTileBase<MULTIBLOCK>> extends Container {
	
	protected final CONTROLLER controller;
	
	public ContainerMultiblockController(EntityPlayer player, CONTROLLER controller) {
		this.controller = controller;
		controller.getMultiblock().beginUpdatingPlayer(player);
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
