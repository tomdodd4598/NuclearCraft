package nc.container.machine;

import nc.tile.machine.TileElectricFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerElectricFurnace extends Container {
	
	public TileElectricFurnace entity;
	
	public int lastCycledTicks;
	public int lastEnergyStored;
	public double lastEU;
	public double lastSU;
	public int lastGetSpeed;
	public int lastReqEnergy;
	
	public ContainerElectricFurnace(InventoryPlayer inventory, TileElectricFurnace tileentity)
	{
		this.entity = tileentity;
		
		this.addSlotToContainer(new Slot(tileentity, 0, 56, 35));
		this.addSlotToContainer(new SlotFurnace(inventory.player, tileentity, 1, 116, 35));
		this.addSlotToContainer(new Slot(tileentity, 2, 132, 64));
		this.addSlotToContainer(new Slot(tileentity, 3, 152, 64));
		
		for(int i = 0; i < 3; i++)
		{
			for(int j = 0; j < 9; j++)
			{
				this.addSlotToContainer(new Slot(inventory, j + i*9 + 9, 8 + j*18, 84 + i*18));
			}
		}
		
		for(int i = 0; i < 9; i++)
		{
			this.addSlotToContainer(new Slot(inventory, i, 8 + i*18, 142));
		}	
	}
	
	public void addCraftingToCrafters(ICrafting icrafting)
	{
		super.addCraftingToCrafters(icrafting);
		icrafting.sendProgressBarUpdate(this, 1, this.entity.energy);
	}
	
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		for (int i = 0; i < this.crafters.size(); i++) {
			ICrafting icrafting = (ICrafting)this.crafters.get(i);
			
			if (this.lastCycledTicks != this.entity.cookTime) {
				icrafting.sendProgressBarUpdate(this, 0, this.entity.cookTime);
				}
			
			icrafting.sendProgressBarUpdate(this, 200, this.entity.energyStorage.getEnergyStored());
			icrafting.sendProgressBarUpdate(this, 201, this.entity.energyStorage.getEnergyStored() >> 16);
			icrafting.sendProgressBarUpdate(this, 202, (int) entity.getFurnaceSpeed);
			icrafting.sendProgressBarUpdate(this, 203, (int) entity.getFurnaceSpeed >> 16);
			icrafting.sendProgressBarUpdate(this, 204, (int) entity.getRequiredEnergy);
			icrafting.sendProgressBarUpdate(this, 205, (int) entity.getRequiredEnergy >> 16);

			icrafting.sendProgressBarUpdate(this, 100, (int) this.entity.energyUpgrade);
			icrafting.sendProgressBarUpdate(this, 101, (int) this.entity.energyUpgrade >> 16);
			icrafting.sendProgressBarUpdate(this, 102, (int) this.entity.speedUpgrade);
			icrafting.sendProgressBarUpdate(this, 103, (int) this.entity.speedUpgrade >> 16);
			}
		
		this.lastCycledTicks = this.entity.cookTime;
		}
	
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int slot, int value) {
		super.updateProgressBar(slot, value);
		if (slot == 0) {this.entity.cookTime = value;}
		if (slot == 200) {this.lastEnergyStored = this.upcastShort(value);}
		if (slot == 201) {this.entity.energy = this.lastEnergyStored | value << 16;}
		if (slot == 202) {lastGetSpeed = upcastShort(value);}
		if (slot == 203) {entity.getFurnaceSpeed = lastGetSpeed | value << 16;}
		if (slot == 204) {lastReqEnergy = upcastShort(value);}
		if (slot == 205) {entity.getRequiredEnergy = lastReqEnergy | value << 16;}
	      
	      if (slot == 100) {this.lastEU = this.upcastShort(value);}
	      if (slot == 101) {this.entity.energyUpgrade = (int) this.lastEU | value << 16;}
	      if (slot == 102) {this.lastSU = this.upcastShort(value);}
	      if (slot == 103) {this.entity.speedUpgrade = (int) this.lastSU | value << 16;}
		}
	
	private int upcastShort(int input)
{
if (input < 0) input += 65536;
return input;
}
	
	public ItemStack transferStackInSlot(EntityPlayer player, int clickedSlotNumber)
	{
		ItemStack itemstack = null;
		Slot slot = (Slot) this.inventorySlots.get(clickedSlotNumber);
		
		if(slot != null && slot.getHasStack())
		{
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			
			if(clickedSlotNumber == 1)
			{
				if(!this.mergeItemStack(itemstack1, 4, 40, true))
				{
					return null;
				}
				
				slot.onSlotChange(itemstack1, itemstack);
			}
			else if(clickedSlotNumber > 3)
			{
				if (TileElectricFurnace.isSpeedUpgrade(itemstack1))
                {
                    if (!this.mergeItemStack(itemstack1, 2, 3, false))
                    {
                        return null;
                    }
                }
                else if (TileElectricFurnace.isEnergyUpgrade(itemstack1))
                {
                    if (!this.mergeItemStack(itemstack1, 3, 4, false))
                    {
                        return null;
                    }
                }
				else if(FurnaceRecipes.smelting().getSmeltingResult(itemstack1) != null)
				{
					if(!this.mergeItemStack(itemstack1, 0, 1, false))
					{
						return null;
					}
				}
				else if(clickedSlotNumber >= 4 && clickedSlotNumber < 31)
				{
					if(!this.mergeItemStack(itemstack1, 31, 40, false))
					{
						return null;
					}
				}
				else if(clickedSlotNumber >= 31 && clickedSlotNumber < 40)
				{
					if(!this.mergeItemStack(itemstack1, 4, 31, false))
					{
						return null;
					}
				}
			}
			else if(!this.mergeItemStack(itemstack1, 4, 40, false))
			{
				return null;
			}
			
			if(itemstack1.stackSize == 0)
			{
				slot.putStack((ItemStack) null);
			}
			else
			{
				slot.onSlotChanged();
			}
			
			if(itemstack1.stackSize == itemstack.stackSize)
			{
				return null;
			}
			
			slot.onPickupFromSlot(player, itemstack1);
			
		}
		
		return itemstack;
	}
	
	public boolean canInteractWith(EntityPlayer entityplayer)
	{
		return this.entity.isUseableByPlayer(entityplayer);
	}
}
