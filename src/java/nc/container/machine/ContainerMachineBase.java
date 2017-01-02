package nc.container.machine;

import nc.crafting.NCRecipeHelper;
import nc.tile.machine.TileMachineBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public abstract class ContainerMachineBase extends Container {
	public TileMachineBase entity;
	public NCRecipeHelper recipes;
	public int lastCookTime;
	public int lastEnergy;
	public double lastEU;
	public double lastSU;
	public int lastGetSpeed;
	public int lastReqEnergy;
	
	public ContainerMachineBase(InventoryPlayer inventory, TileMachineBase tileentity, NCRecipeHelper recipe) {
		entity = tileentity;
		recipes = recipe;
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
				icrafting.sendProgressBarUpdate(this, 202, (int) entity.getProcessTime);
				icrafting.sendProgressBarUpdate(this, 203, (int) entity.getProcessTime >> 16);
				icrafting.sendProgressBarUpdate(this, 204, (int) entity.getEnergyRequired);
				icrafting.sendProgressBarUpdate(this, 205, (int) entity.getEnergyRequired >> 16);
			}
			if (entity.hasUpgrades) {
				icrafting.sendProgressBarUpdate(this, 100, (int) entity.energyUpgrade);
				icrafting.sendProgressBarUpdate(this, 101, (int) entity.energyUpgrade >> 16);
				icrafting.sendProgressBarUpdate(this, 102, (int) entity.speedUpgrade);
				icrafting.sendProgressBarUpdate(this, 103, (int) entity.speedUpgrade >> 16);
			}
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
			
			if (slot == 202) {
				lastGetSpeed = upcastShort(value);
			}
			if (slot == 203) {
				entity.getProcessTime = lastGetSpeed | value << 16;
			}
			
			if (slot == 204) {
				lastReqEnergy = upcastShort(value);
			}
			if (slot == 205) {
				entity.getEnergyRequired = lastReqEnergy | value << 16;
			}
		}
		if (entity.hasUpgrades) {
			if (slot == 100) {
				lastEU = upcastShort(value);
			}
			if (slot == 101) {
				entity.energyUpgrade = (int) lastEU | value << 16;
			}
			if (slot == 102) {
				lastSU = upcastShort(value);
			}
			if (slot == 103) {
				entity.speedUpgrade = (int) lastSU | value << 16;
			}
		}
	}
  
	public int upcastShort(int input) {
		if (input < 0) input += 65536;
		return input;
	}

	public ItemStack transferStackInSlot(EntityPlayer player, int clickedSlotNumber) {
		ItemStack itemstack = null;
		Slot slot = (Slot)inventorySlots.get(clickedSlotNumber);
		int upgrades = 0;
		if (entity.hasUpgrades) upgrades = 2;
		int invStart = entity.inputSize + entity.outputSize + upgrades;
		int sSlot = entity.inputSize + entity.outputSize;
		int eSlot = entity.inputSize + entity.outputSize + 1;
		int invEnd = entity.inputSize + entity.outputSize + 36 + upgrades;
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
				if (TileMachineBase.isSpeedUpgrade(itemstack1) && entity.hasUpgrades) {
					if (!mergeItemStack(itemstack1, sSlot, sSlot + 1, false)) {
						return null;
					}
				}
				else if (TileMachineBase.isEnergyUpgrade(itemstack1) && entity.hasUpgrades) {
					if (!mergeItemStack(itemstack1, eSlot, eSlot + 1, false)) {
						return null;
					}
				}
				
				// Oxidiser Oxygen
				else if (entity.isOxygen(itemstack1) && entity.isOxidiser()) {
					if (!mergeItemStack(itemstack1, 1, 2, false)) {
						return null;
					}
				}
				
				// Irradiator Neutrons
				else if (entity.isNeutronCapsule(itemstack1) && entity.isIrradiator()) {
					if (!mergeItemStack(itemstack1, 1, 2, false)) {
						return null;
					}
				}
				
				// Ioniser Hydrogen
				else if (entity.isHydrogen(itemstack1) && entity.isIoniser()) {
					if (!mergeItemStack(itemstack1, 1, 2, false)) {
						return null;
					}
				}
				
				else if (recipes.validInput(itemstack1)) {
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