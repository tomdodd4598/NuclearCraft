package nc.tile.crafting;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileNuclearWorkspace extends TileEntity implements IInventory, ISidedInventory {
	
	private ItemStack result;
    private ItemStack[] matrix = new ItemStack[25];

    public boolean canUpdate() {
        return false;
    }

    public int getSizeInventory() {
        return 26;
    }

    public ItemStack getStackInSlot(int slot) {
        if (slot == 0) return result;
        else if (slot <= matrix.length) return matrix[slot - 1];
        else return null;
    }

    public ItemStack decrStackSize(int slot, int decrement) {
        if (slot == 0) {
            if (result != null) {
                for (int x = 1;x <= matrix.length;x++) decrStackSize(x, 1);
                if (result.stackSize <= decrement) {
                    ItemStack craft = result;
                    result = null;
                    return craft;
                }
                ItemStack split = result.splitStack(decrement);
                if (result.stackSize <= 0) result = null;
                return split;
            } else return null;
        } else if (slot <= matrix.length) {
            if (matrix[slot - 1] != null) {
                if (matrix[slot - 1].stackSize <= decrement) {
                    ItemStack ingredient = matrix[slot - 1];
                    matrix[slot - 1] = null;
                    return ingredient;
                }
                ItemStack split = matrix[slot - 1].splitStack(decrement);
                if (matrix[slot - 1].stackSize <= 0) matrix[slot - 1] = null;
                return split;
            }
        }
        return null;
    }

    public void openInventory() {}

    public void closeInventory() {}

    public boolean isUseableByPlayer(EntityPlayer player) {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && player.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64.0D;
    }

    public boolean isItemValidForSlot(int slot, ItemStack stack) {
    	return false;
    }

    public int getInventoryStackLimit() {
        return 64;
    }

    public void setInventorySlotContents(int slot, ItemStack stack) {
        if (slot == 0) result = stack;
        else if(slot <= matrix.length) matrix[slot - 1] = stack;
    }

    public ItemStack getStackInSlotOnClosing(int slot) {
        return null;
    }

    public String getInventoryName() {
        return  "Heavy Duty Workspace";
    }

    public boolean hasCustomInventoryName() {
        return false;
    }

    public int[] getAccessibleSlotsFromSide(int side) {
        return new int[]{};
    }

    public boolean canInsertItem(int slot, ItemStack item, int side) {
        return false;
    }

    public boolean canExtractItem(int slot, ItemStack item, int side) {
        return false;
    }

	public void writeToNBT(NBTTagCompound tag) {
		super.writeToNBT(tag);
		if(result != null) {
            NBTTagCompound produce = new NBTTagCompound();
            result.writeToNBT(produce);
            tag.setTag("Result", produce);
        } else tag.removeTag("Result");
        for (int x = 0;x < matrix.length;x++) {
            if (matrix[x] != null) {
                NBTTagCompound craft = new NBTTagCompound();
                matrix[x].writeToNBT(craft);
                tag.setTag("Craft" + x, craft);
            } else tag.removeTag("Craft" + x);
        }
	}

	public void readFromNBT(NBTTagCompound tag) {
		super.readFromNBT(tag);
		this.result = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Result"));
        for (int x = 0;x < matrix.length;x++) matrix[x] = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("Craft" + x));
	}

	public Packet getDescriptionPacket() {
		NBTTagCompound tag = new NBTTagCompound();
		if (result != null) {
            NBTTagCompound produce = new NBTTagCompound();
            result.writeToNBT(produce);
            tag.setTag("Result", produce);
		} else tag.removeTag("Result");
        for (int x = 0;x < matrix.length;x++) {
            if (matrix[x] != null) {
                NBTTagCompound craft = new NBTTagCompound();
                matrix[x].writeToNBT(craft);
                tag.setTag("Craft" + x, craft);
            } else tag.removeTag("Craft" + x);
        }
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 0, tag);
	}

	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
		super.onDataPacket(net, packet);
		this.result = ItemStack.loadItemStackFromNBT(packet.func_148857_g().getCompoundTag("Result"));
        for (int x = 0;x < matrix.length;x++) matrix[x] = ItemStack.loadItemStackFromNBT(packet.func_148857_g().getCompoundTag("Craft" + x));
	}

}