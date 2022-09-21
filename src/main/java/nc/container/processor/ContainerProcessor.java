package nc.container.processor;

import nc.container.ContainerTile;
import nc.container.slot.*;
import nc.network.tile.ProcessorUpdatePacket;
import nc.recipe.BasicRecipeHandler;
import nc.tile.processor.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public abstract class ContainerProcessor<TILE extends TileEntity & IProcessor<TILE, INFO>, INFO extends ProcessorContainerInfo<TILE, INFO>> extends ContainerTile<TILE, ProcessorUpdatePacket, INFO> {
	
	protected final TILE tile;
	protected final BasicRecipeHandler recipeHandler;
	
	public ContainerProcessor(EntityPlayer player, TILE tile, BasicRecipeHandler recipeHandler) {
		super(tile);
		this.tile = tile;
		this.recipeHandler = recipeHandler;
		
		addMachineSlots(player);
		addPlayerSlots(player);
		tile.addTileUpdatePacketListener(player);
	}
	
	protected void addMachineSlots(EntityPlayer player) {
		for (int i = 0; i < info.itemInputSize; ++i) {
			int[] stackXY = info.getItemInputStackXY(i);
			addSlotToContainer(new SlotProcessorInput(tile, recipeHandler, i, stackXY[0], stackXY[1]));
		}
		
		for (int i = 0; i < info.itemOutputSize; ++i) {
			int[] stackXY = info.getItemOutputStackXY(i);
			addSlotToContainer(new SlotFurnace(player, tile, i + info.itemInputSize, stackXY[0], stackXY[1]));
		}
	}
	
	protected void addPlayerSlots(EntityPlayer player) {
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				addSlotToContainer(new Slot(player.inventory, j + 9 * i + 9, 8 + info.playerGuiX + 18 * j, 84 + info.playerGuiY + 18 * i));
			}
		}
		
		for (int i = 0; i < 9; ++i) {
			addSlotToContainer(new Slot(player.inventory, i, 8 + info.playerGuiX + 18 * i, 142 + info.playerGuiY));
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return tile.isUsableByPlayer(player);
	}
	
	@Override
	public void onContainerClosed(EntityPlayer player) {
		super.onContainerClosed(player);
		tile.removeTileUpdatePacketListener(player);
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack stackCopy = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);
		int invStart = info.getInventorySize();
		int invEnd = info.getCombinedInventorySize();
		
		if (slot != null && slot.getHasStack()) {
			ItemStack stack = slot.getStack();
			stackCopy = stack.copy();
			
			if (index >= info.itemInputSize && index < invStart) {
				if (!mergeItemStack(stack, invStart, invEnd, false)) {
					return ItemStack.EMPTY;
				}
				slot.onSlotChange(stack, stackCopy);
			}
			else if (index >= invStart) {
				ItemStack transfer = transferPlayerStack(player, index, invStart, invEnd, stack);
				if (transfer != null) {
					return transfer;
				}
			}
			else if (!mergeItemStack(stack, invStart, invEnd, false)) {
				return ItemStack.EMPTY;
			}
			
			if (stack.isEmpty()) {
				slot.putStack(ItemStack.EMPTY);
			}
			else {
				slot.onSlotChanged();
			}
			
			if (stack.getCount() == stackCopy.getCount()) {
				return ItemStack.EMPTY;
			}
			slot.onTake(player, stack);
		}
		return stackCopy;
	}
	
	public ItemStack transferPlayerStack(EntityPlayer player, int index, int invStart, int invEnd, ItemStack stack) {
		if (recipeHandler.isValidItemInput(stack)) {
			if (!mergeItemStack(stack, 0, info.itemInputSize, false)) {
				return ItemStack.EMPTY;
			}
		}
		else if (index >= invStart && index < invEnd - 9) {
			if (!mergeItemStack(stack, invEnd - 9, invEnd, false)) {
				return ItemStack.EMPTY;
			}
		}
		else if (index >= invEnd - 9 && index < invEnd && !mergeItemStack(stack, invStart, invEnd - 9, false)) {
			return ItemStack.EMPTY;
		}
		return null;
	}
}
