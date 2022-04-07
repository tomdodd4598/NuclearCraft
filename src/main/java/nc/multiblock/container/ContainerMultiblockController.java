package nc.multiblock.container;

import nc.multiblock.*;
import nc.multiblock.tile.*;
import nc.network.multiblock.MultiblockUpdatePacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;

public class ContainerMultiblockController<MULTIBLOCK extends Multiblock<MULTIBLOCK, T> & IPacketMultiblock<MULTIBLOCK, T, PACKET>, T extends ITileMultiblockPart<MULTIBLOCK, T>, PACKET extends MultiblockUpdatePacket, GUITILE extends IMultiblockGuiPart<MULTIBLOCK, T, PACKET, GUITILE>> extends Container {
	
	protected final GUITILE tile;
	
	public ContainerMultiblockController(EntityPlayer player, GUITILE tile) {
		this.tile = tile;
		MULTIBLOCK multiblock = tile.getMultiblock();
		if (multiblock != null) {
			multiblock.addMultiblockUpdatePacketListener(player);
		}
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
		MULTIBLOCK multiblock = tile.getMultiblock();
		if (multiblock != null) {
			multiblock.removeMultiblockUpdatePacketListener(player);
		}
	}
}
