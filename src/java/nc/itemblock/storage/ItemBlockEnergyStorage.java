package nc.itemblock.storage;

import java.util.List;

import nc.itemblock.ItemBlockNC;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import cofh.api.energy.IEnergyContainerItem;

public abstract class ItemBlockEnergyStorage extends ItemBlockNC implements IEnergyContainerItem {
	
	public int storedRF;

	public ItemBlockEnergyStorage(Block block, int storage) {
		super(block, "Stores " + (storage >= 10000000 ? storage/1000000 + " M" : (storage >= 10000 ? storage/1000 + " k" : storage + " ")) + "RF. Right click on a side without sneaking to", "change the side mode to input, output or disabled.");
		storedRF = storage;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean whatIsThis) {
		NBTTagCompound tagCompound = itemStack.getTagCompound();
		if (tagCompound != null && tagCompound.getInteger("Energy") > 0) {
			list.add(EnumChatFormatting.LIGHT_PURPLE + "Energy: " + getEnergyStored(itemStack) + " / " + storedRF + " RF");
		}
        super.addInformation(itemStack, player, list, whatIsThis);
    }
	
	public boolean showDurabilityBar(ItemStack stack) {
		return getEnergyStored(stack) > 0;
    }

	public double getDurabilityForDisplay(ItemStack stack) {
		return 1 - (double) getEnergyStored(stack) / storedRF;
	}

	public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate) {
		if (container.stackSize > 1) {
			return 0;
		}
		int energy = getEnergyStored(container);
		int energyReceived = Math.min(storedRF - energy, Math.min(maxReceive, (int) Math.ceil(storedRF/20)));

		if (!simulate) {
			energy += energyReceived;
			setStoredEnergyForItem(container, energy);
		}
		return energyReceived;

	}

	public int extractEnergy(ItemStack container, int maxExtract, boolean simulate) {
		if (container.stackSize > 1) {
			return 0;
		}
		int energy = getEnergyStored(container);

		int energyExtracted = Math.min(energy, Math.min(maxExtract, (int) Math.ceil(storedRF/20)));

		if (!simulate) {
			energy -= energyExtracted;
			setStoredEnergyForItem(container, energy);
		}
		return energyExtracted;

	}
	
	public static void setStoredEnergyForItem(ItemStack item, int storedEnergy) {
		NBTTagCompound tag = item.getTagCompound();
		if(tag == null) {
			tag = new NBTTagCompound();
		}
		tag.setInteger("Energy", storedEnergy);
		item.setTagCompound(tag);
	}
	
	public static int getStoredEnergyForItem(ItemStack item) {
		NBTTagCompound tag = item.getTagCompound();
		if(tag == null) {
			return 0;
		}
		return tag.getInteger("Energy");
	}

	public int getEnergyStored(ItemStack container) {
		return getStoredEnergyForItem(container);
	}


	public int getMaxEnergyStored(ItemStack container) {
		return storedRF;
	}
}
