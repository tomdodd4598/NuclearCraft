package nc.container.machine;

import nc.crafting.NCRecipeHelper;
import nc.crafting.machine.HeliumExtractorRecipes;
import nc.tile.machine.TileHeliumExtractor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ContainerHeliumExtractor extends Container {
	public TileHeliumExtractor entity;
	public NCRecipeHelper recipes;
	public int lastCookTime;
	public int lastEnergy;
	public int lastHelium;
  
	public ContainerHeliumExtractor(InventoryPlayer inventory, TileHeliumExtractor tileentity) {
		entity = tileentity;
		recipes = HeliumExtractorRecipes.instance();
		
		addSlotToContainer(new Slot(tileentity, 0, 53, 35));
		addSlotToContainer(new SlotFurnace(inventory.player, tileentity, 1, 108, 35));
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(inventory, j + i*9 + 9, 8 + j*18, 84 + i*18));
			}
		}
		for(int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(inventory, i, 8 + i*18, 142));
		}
	}
  
	public void addCraftingToCrafters(ICrafting icrafting) {
		super.addCraftingToCrafters(icrafting);
		icrafting.sendProgressBarUpdate(this, 0, entity.cookTime);
	}
  
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		for (int i = 0; i < crafters.size(); i++) {
			ICrafting icrafting = (ICrafting)crafters.get(i);
			if (lastCookTime != entity.cookTime) {
				icrafting.sendProgressBarUpdate(this, 0, entity.cookTime);
			}
			if (entity.hasEnergy) {
				icrafting.sendProgressBarUpdate(this, 200, entity.energyStorage.getEnergyStored());
				icrafting.sendProgressBarUpdate(this, 201, entity.energyStorage.getEnergyStored() >> 16);
			}
			icrafting.sendProgressBarUpdate(this, 300, entity.tank.getFluidAmount());
			icrafting.sendProgressBarUpdate(this, 301, entity.tank.getFluidAmount() >> 16);
		}
		lastCookTime = entity.cookTime;
	}
  
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int slot, int value) {
		super.updateProgressBar(slot, value);
		if (slot == 0) {
			entity.cookTime = value;
		}
		if (entity.hasEnergy) {
			if (slot == 200) {
				lastEnergy = upcastShort(value);
			}
			if (slot == 201) {
				entity.energy = lastEnergy | value << 16;
			}
		}
		if (slot == 300) {
			lastHelium = upcastShort(value);
		}
		if (slot == 301) {
			entity.fluid = lastHelium | value << 16;
		}
	}
  
	public int upcastShort(int input) {
		if (input < 0) input += 65536;
		return input;
	}

	public ItemStack transferStackInSlot(EntityPlayer player, int clickedSlotNumber) {
		ItemStack itemstack = null;
		Slot slot = (Slot)inventorySlots.get(clickedSlotNumber);
		int invStart = entity.inputSize + entity.outputSize;
		int invEnd = entity.inputSize + entity.outputSize + 36;
		if ((slot != null) && (slot.getHasStack())) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if (clickedSlotNumber >= entity.inputSize && clickedSlotNumber < entity.inputSize + entity.outputSize) {
				if (!mergeItemStack(itemstack1, invStart, invEnd, true)) {
					return null;
				}
				slot.onSlotChange(itemstack1, itemstack);
			}
			else if(clickedSlotNumber > invStart - 1) {
				if (recipes.validInput(itemstack1)) {
					if (!mergeItemStack(itemstack1, 0, entity.inputSize, false)) {
						return null;
					}
				}
				else if ((clickedSlotNumber >= invStart) && (clickedSlotNumber < invEnd - 9)) {
					if (!mergeItemStack(itemstack1, invEnd - 9, invEnd, false)) {
						return null;
					}
				}
				else if ((clickedSlotNumber >= invEnd - 9) && (clickedSlotNumber < invEnd) && (!mergeItemStack(itemstack1, invStart, invEnd - 9, false))) {
					return null;
				}
			}
			else if (!mergeItemStack(itemstack1, invStart, invEnd, false)) {
				return null;
			}
			if (itemstack1.stackSize == 0) {
				slot.putStack((ItemStack)null);
			}
			else {
				slot.onSlotChanged();
			}
			if (itemstack1.stackSize == itemstack.stackSize) {
				return null;
			}
			slot.onPickupFromSlot(player, itemstack1);
		}
		return itemstack;
	}

	public boolean canInteractWith(EntityPlayer var1) {
		return entity.isUseableByPlayer(var1);
	}
}