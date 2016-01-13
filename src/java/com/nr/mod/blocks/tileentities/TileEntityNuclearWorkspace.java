package com.nr.mod.blocks.tileentities;

import java.util.Arrays;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;

public class TileEntityNuclearWorkspace extends TileEntityInventory implements IInventory {

		  private ItemStack[] inventory;
		  protected String inventoryTitle;
		  protected boolean hasCustomName;
		  protected int stackSizeLimit;
		  protected int displaySlot = 0;
	  
		  public TileEntityNuclearWorkspace() {
			  super.slots = new ItemStack[26];
		      super.localizedName = "Heavy Duty Workspace";
		  }
		  
		  public String getInvName()
			{
				return this.isInvNameLocalized() ? this.localizedName : "Reaction Generator";
			}
			
			public boolean isInvNameLocalized()
			{
				return this.localizedName != null && this.localizedName.length() > 0;
			}

		  public void resize(int size) {
		    inventory = Arrays.copyOf(inventory, size);
		  }

		  public int getSizeInventory() {
		    return 26;
		  }

		  public int getInventoryStackLimit() {
		    return stackSizeLimit;
		  }

		  public ItemStack decrStackSize(int slot, int quantity) {
		    ItemStack itemStack = getStackInSlot(slot);

		    if(itemStack == null) {
		      return null;
		    }

		    // whole itemstack taken out
		    if(itemStack.stackSize <= quantity) {
		      setInventorySlotContents(slot, null);
		      this.markDirty();
		      return itemStack;
		    }

		    // split itemstack
		    itemStack = itemStack.splitStack(quantity);
		    // slot is empty, set to null
		    if(getStackInSlot(slot).stackSize == 0) {
		      setInventorySlotContents(slot, null);
		    }

		    this.markDirty();
		    // return remainder
		    return itemStack;
		  }

		  public ItemStack removeStackFromSlot(int slot) {
		    ItemStack itemStack = getStackInSlot(slot);
		    setInventorySlotContents(slot, null);
		    return itemStack;
		  }

		  @Override
		  public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
		    if(slot < getSizeInventory()) {
		      if(inventory[slot] == null || itemstack.stackSize + inventory[slot].stackSize <= getInventoryStackLimit()) {
		        return true;
		      }
		    }
		    return false;
		  }

		  public void clear() {
		    for(int i = 0; i < inventory.length; i++) {
		      inventory[i] = null;
		    }
		  }

		  public String getName() {
		    return this.inventoryTitle;
		  }

		  public boolean hasCustomName() {
		    return this.hasCustomName;
		  }

		  public void setCustomName(String customName) {
		    this.hasCustomName = true;
		    this.inventoryTitle = customName;
		  }

		  public void readFromNBT(NBTTagCompound nbt)
		    {
		        super.readFromNBT(nbt);
		        NBTTagList list = nbt.getTagList("Items", 10);
		        this.slots = new ItemStack[this.getSizeInventory()];

		        for (int i = 0; i < list.tagCount(); ++i)
		        {
		            NBTTagCompound compound = list.getCompoundTagAt(i);
		            byte b = compound.getByte("Slot");

		            if (b >= 0 && b < this.slots.length)
		            {
		                this.slots[b] = ItemStack.loadItemStackFromNBT(compound);
		            }
		        }
		    }

		    public void writeToNBT(NBTTagCompound nbt)
		    {
		        super.writeToNBT(nbt);
		        NBTTagList list = new NBTTagList();

		        for (int i = 0; i < this.slots.length; ++i)
		        {
		            if (this.slots[i] != null)
		            {
		                NBTTagCompound compound = new NBTTagCompound();
		                compound.setByte("Slot", (byte)i);
		                this.slots[i].writeToNBT(compound);
		                list.appendTag(compound);
		            }
		        }

		        nbt.setTag("Items", list);
		        
		        if(this.isInvNameLocalized())
				{
					nbt.setString("CustomName", this.localizedName);
				}
		    }

		    /**
		     * Overriden in a sign to provide the text.
		     */
		    public Packet getDescriptionPacket()
		    {
		        NBTTagCompound nbtTag = new NBTTagCompound();
		        this.writeToNBT(nbtTag);
		        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, nbtTag);
		    }

		    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet)
		    {
		    	super.onDataPacket(net, packet);
		        this.readFromNBT(packet.func_148857_g());
		    }
}