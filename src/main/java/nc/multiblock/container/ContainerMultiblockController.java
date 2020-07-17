package nc.multiblock.container;

import nc.multiblock.Multiblock;
import nc.multiblock.tile.IMultiblockGuiPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public class ContainerMultiblockController<MULTIBLOCK extends Multiblock, TILE extends IMultiblockGuiPart<MULTIBLOCK>> extends Container {
	
	protected final TILE tile;
	
	public ContainerMultiblockController(EntityPlayer player, TILE tile) {
		this.tile = tile;
		tile.getMultiblock().beginUpdatingPlayer(player);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return true;
	}
	
	@Override
	public void putStackInSlot(int slot, ItemStack stack) {
		
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		if (tile.getMultiblock() != null) {
			tile.getMultiblock().stopUpdatingPlayer(player);
		}
	}
}
