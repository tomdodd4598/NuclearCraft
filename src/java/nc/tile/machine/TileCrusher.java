package nc.tile.machine;

import nc.NuclearCraft;
import nc.block.machine.BlockCrusher;
import nc.crafting.machine.CrusherRecipes;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.registry.GameRegistry;

public class TileCrusher extends TileEntity implements ISidedInventory {
	
	private String localizedName;
	
	private static final int[] slots_top = new int[]{0};
	private static final int[] slots_bottom = new int[]{1};
	private static final int[] slots_sides = new int[]{2};
	
	private ItemStack[] slots = new ItemStack[3];
	
	public static int crusherSpeed = (int) Math.ceil((150/(double) NuclearCraft.crusherCrushSpeed)*100);
	
	public int burnTime;
	
	public int currentItemBurnTime;
	
	public int cookTime;
	
	public int getSizeInventory()
	{
		return this.slots.length;
	}
	
	public String getInvName()
	{
		return this.isInvNameLocalized() ? this.localizedName : "Crusher";
	}
	
	public boolean isInvNameLocalized()
	{
		return this.localizedName != null && this.localizedName.length() > 0;
	}
	
	public void setGuiDisplayName(String displayName)
	{
		this.localizedName = displayName;
	}

	public ItemStack getStackInSlot(int i)
	{
		return this.slots[i];
	}

	public ItemStack decrStackSize(int i, int j)
	{
		if(this.slots[i] != null)
		{
			ItemStack itemstack;
				
				if(this.slots[i].stackSize <= j)
				{
					itemstack = this.slots[i];
					
					this.slots[i] = null;
					
					return itemstack;
				}
				else
				{
					itemstack = this.slots[i].splitStack(j);
					
					if(this.slots[i].stackSize == 0)
					{
						this.slots[i] = null;
					}
					
					return itemstack;
					
				}
		}
		else
        {
            return null;
        }
	}

	public ItemStack getStackInSlotOnClosing(int i)
	{
		if(this.slots[i] != null)
		{
			ItemStack itemstack = this.slots[i];
			this.slots[i] = null;
			return itemstack;
		}
		else
        {
            return null;
        }
	}

	public void setInventorySlotContents(int i, ItemStack itemstack)
	{
		this.slots[i] = itemstack;
		
		if(itemstack != null && itemstack.stackSize > this.getInventoryStackLimit())
		{
			itemstack.stackSize = this.getInventoryStackLimit();
		}
	}

	public String getInventoryName()
	{
		return null;
	}

	public boolean hasCustomInventoryName()
	{
		return false;
	}

	public int getInventoryStackLimit()
	{
		return 64;
	}
	
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		
		NBTTagList list = nbt.getTagList("Items", 10);
		this.slots = new ItemStack[this.getSizeInventory()];
		
		for(int i = 0; i < list.tagCount(); i++)
		{
			NBTTagCompound compound = list.getCompoundTagAt(i);
			byte b = compound.getByte("Slot");
			
			if(b >= 0 && b < this.slots.length)
			{
				this.slots[b] = ItemStack.loadItemStackFromNBT(compound);
			}
		}
		
		this.burnTime = (int) nbt.getShort("BurnTime");
		this.cookTime = (int) nbt.getShort("CookTime");
		this.currentItemBurnTime = (int) nbt.getShort("CurrentBurnTime");
		
		if(nbt.hasKey("CustomName", 8))
		{
			this.localizedName = nbt.getString("CustomName");
		}
		
	}
	
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);
		
		nbt.setShort("BurnTime", (short) this.burnTime);
		nbt.setShort("CookTime", (short) this.cookTime);
		nbt.setShort("CurrentBurnTime", (short) this.currentItemBurnTime);
		
		NBTTagList list = new NBTTagList();
		
		for(int i = 0; i < this.slots.length; i++)
		{
			if(this.slots[i] != null)
			{
				NBTTagCompound compound = new NBTTagCompound();
				compound.setByte("Slot", (byte) i);
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

	public boolean isUseableByPlayer(EntityPlayer entityplayer)
	{
		return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) != this ? false : 
		entityplayer.getDistanceSq((double)this.xCoord + 0.5D, (double)this.yCoord + 0.5D, (double)this.zCoord + 0.5D) <= 64;
	}

	public void openInventory()
	{
		
	}

	public void closeInventory()
	{
		
	}
	
	public boolean isBurning()
	{
		return this.burnTime > 0;
	}
	
	public void updateEntity()
	{
		boolean flag = burnTime > 0;
		boolean flag1 = false;
		
		if(this.burnTime > 0)
		{
			this.burnTime--;
		}
		if(!this.worldObj.isRemote)
		{
			if (this.burnTime != 0 || this.slots[1] != null && this.slots[0] != null)
            {
			if(this.burnTime == 0 && this.canCrush())
			{
				this.currentItemBurnTime = this.burnTime = getItemBurnTime(this.slots[1]);
				
				if(this.burnTime > 0)
				{
					flag1 = true;
					
					if(this.slots[1] != null)
					{
						this.slots[1].stackSize--;
						
						if(this.slots[1].stackSize == 0)
						{
							this.slots[1] = this.slots[1].getItem().getContainerItem(this.slots[1]);
						}
						}
					}
				}
				
				if(this.isBurning() && this.canCrush())
				{
					this.cookTime++;
					
					if(this.cookTime == TileCrusher.crusherSpeed)
					{
						this.cookTime = 0;
						this.crushItem();
						flag1 = true;
					}
					
				}
				else
				{
					this.cookTime = 0;
				}
            }
			
			if(flag != this.burnTime > 0)
			{
				flag1 = true;
				
				BlockCrusher.updateCrusherBlockState(this.burnTime > 0, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
			}
			
		}
		
		if(flag1)
		{
			this.markDirty();
		}
	}
	
	public ItemStack getOutput(ItemStack stack) {
		   ItemStack[] outputs = CrusherRecipes.instance().getOutput(stack);
		   if(outputs!=null) {
			   return outputs[0];
			   }
		   return null;
	   }
	
	private boolean canCrush()
	{
		if(this.slots[0] == null)
		{
			return false;
		}
		else
		{
			ItemStack itemstack = getOutput(this.slots[0]);
			
			if(itemstack == null) return false;
			if(this.slots[2] == null) return true;
			if(!this.slots[2].isItemEqual(itemstack)) return false;
			
			int result = this.slots[2].stackSize + itemstack.stackSize;
			
			return result <= this.getInventoryStackLimit() && result <= this.slots[2].getMaxStackSize();
		}
	}
	
	public int getInputSize(ItemStack stack, int slot) {
		   ItemStack[] outputs = CrusherRecipes.instance().getOutput(stack);
		   if(outputs!=null) {
			   return CrusherRecipes.instance().getInputSize(slot, outputs);
		   }
		   return 1;
	}
	
	public void crushItem()
	{
		if(this.canCrush())
		{
			ItemStack itemstack = getOutput(this.slots[0]);
			
			if(this.slots[2] == null)
			{
				this.slots[2] = itemstack.copy();
			}
			else if (this.slots[2].getItem() == itemstack.getItem())
            {
                this.slots[2].stackSize += itemstack.stackSize;
            }
			
		}
			
		this.slots[0].stackSize -= getInputSize(this.slots[0], 0);
			
			if(this.slots[0].stackSize <= 0)
			{
				this.slots[0] = null;
			}
			
	}
	
	public static int getItemBurnTime(ItemStack itemstack)
    {
        if (itemstack == null)
        {
            return 0;
        }
        else
        {
            Item item = itemstack.getItem();

            if (item instanceof ItemBlock && Block.getBlockFromItem(item) != Blocks.air)
            {
                Block block = Block.getBlockFromItem(item);

                if (block == Blocks.wooden_slab)
                {
                	return (int) Math.ceil(((double) NuclearCraft.crusherCrushSpeed*(150/200)*75)/(double) NuclearCraft.crusherCrushEfficiency);
                }

                if (block.getMaterial() == Material.wood)
                {
                	return (int) Math.ceil(((double) NuclearCraft.crusherCrushSpeed*(300/200)*75)/(double) NuclearCraft.crusherCrushEfficiency);
                }

                if (block == Blocks.coal_block)
                {
                	return (int) Math.ceil(((double) NuclearCraft.crusherCrushSpeed*(16000/200)*75)/(double) NuclearCraft.crusherCrushEfficiency);
                }
            }

            if (item instanceof ItemTool && ((ItemTool)item).getToolMaterialName().equals("WOOD"))
            	return (int) Math.ceil(((double) NuclearCraft.crusherCrushSpeed*(200/200)*75)/(double) NuclearCraft.crusherCrushEfficiency);
            if (item instanceof ItemSword && ((ItemSword)item).getToolMaterialName().equals("WOOD"))
            	return (int) Math.ceil(((double) NuclearCraft.crusherCrushSpeed*(200/200)*75)/(double) NuclearCraft.crusherCrushEfficiency);
            if (item instanceof ItemHoe && ((ItemHoe)item).getToolMaterialName().equals("WOOD"))
            	return (int) Math.ceil(((double) NuclearCraft.crusherCrushSpeed*(200/200)*75)/(double) NuclearCraft.crusherCrushEfficiency);
            if (item == Items.stick) return (int) Math.ceil(((double) NuclearCraft.crusherCrushSpeed*(100/200)*75)/(double) NuclearCraft.crusherCrushEfficiency);
            if (item == Items.coal) return (int) Math.ceil(((double) NuclearCraft.crusherCrushSpeed*(1600/200)*75)/(double) NuclearCraft.crusherCrushEfficiency);
            if (item == Items.lava_bucket) return (int) Math.ceil(((double) NuclearCraft.crusherCrushSpeed*(20000/200)*75)/(double) NuclearCraft.crusherCrushEfficiency);
            if (item == Item.getItemFromBlock(Blocks.sapling))
            	return (int) Math.ceil(((double) NuclearCraft.crusherCrushSpeed*(100/200)*75)/(double) NuclearCraft.crusherCrushEfficiency);
            if (item == Items.blaze_rod) return (int) Math.ceil(((double) NuclearCraft.crusherCrushSpeed*(2400/200)*75)/(double) NuclearCraft.crusherCrushEfficiency);
            return (int) Math.ceil(((double) NuclearCraft.crusherCrushSpeed*((GameRegistry.getFuelValue(itemstack))/200)*75)/(double) NuclearCraft.crusherCrushEfficiency);
        }
    }
	
	public static boolean isItemFuel(ItemStack itemstack)
	{
		return getItemBurnTime(itemstack) > 0;
	}

	public boolean isItemValidForSlot(int slot, ItemStack itemstack)
	{
		if (slot == 2) return false;
		else if (slot == 1) {
			if (isItemFuel(itemstack)) return true;
			else return false;
		}
		else if (slot == 0) {
			return true;
		}
		else return false;
	}

	public int[] getAccessibleSlotsFromSide(int var1)
	{
		return var1 == 0 ? slots_bottom : (var1 == 1 ? slots_top : slots_sides);
	}

	public boolean canInsertItem(int i, ItemStack itemstack, int j)
	{
		return this.isItemValidForSlot(i, itemstack);
	}

	public boolean canExtractItem(int slot, ItemStack itemstack, int side)
	{
		return slot == 2;
	}

	public int getBurnTimeRemainingScaled(int i)
	{
		if(this.currentItemBurnTime == 0)
		{
			this.currentItemBurnTime = TileCrusher.crusherSpeed;
		}
		
		return this.burnTime * i / this.currentItemBurnTime;
	}
	
	public int getCookProgressScaled(int i)
	{
		return this.cookTime * i / TileCrusher.crusherSpeed;
	}
}