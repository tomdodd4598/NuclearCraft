package nc.container;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.*;

public abstract class NCContainer extends Container {
	
	protected final @Nullable IInventory inv;
	
	protected NCContainer(TileEntity tile) {
		inv = tile instanceof IInventory ? (IInventory) tile : null;
	}
	
	@Override
	public void addListener(IContainerListener listener) {
		super.addListener(listener);
		if (inv != null) {
			listener.sendAllWindowProperties(this, inv);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data) {
		if (inv != null) {
			inv.setField(id, data);
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return inv != null && inv.isUsableByPlayer(player);
	}
	
	public ItemStack transferPlayerStackDefault(EntityPlayer player, int index, int invStart, int invEnd, ItemStack stack) {
		if (index >= invStart && index < invEnd - 9) {
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
