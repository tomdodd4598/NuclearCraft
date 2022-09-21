package nc.tile.processor;

import static nc.config.NCConfig.*;

import java.util.List;

import nc.handler.TileInfoHandler;
import nc.init.NCItems;
import nc.recipe.NCRecipes;
import nc.tile.energy.ITileEnergy;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.inventory.ItemSorption;
import nc.tile.inventory.ITileInventory;
import nc.util.StackHelper;
import net.minecraft.item.ItemStack;

public class TileBasicUpgradeProcessor<TILE extends TileBasicUpgradeProcessor<TILE>> extends TileProcessor<TILE, BasicUpgradeProcessorContainerInfo<TILE>> implements IBasicUpgradeProcessor {
	
	protected TileBasicUpgradeProcessor(String name) {
		this(name, TileInfoHandler.getContainerInfoProcessorInfo(name));
	}
	
	protected TileBasicUpgradeProcessor(String name, BasicUpgradeProcessorContainerInfo<TILE> processorContainerInfo) {
		super(name, processorContainerInfo, ITileInventory.inventoryConnectionAll(defaultItemSorptions(processorContainerInfo)), IProcessor.energyCapacity(processorContainerInfo, 1D, 1D), ITileEnergy.energyConnectionAll(IProcessor.defaultEnergyConnection(processorContainerInfo)), IProcessor.defaultTankCapacities(processorContainerInfo), NCRecipes.getValidFluids(name), ITileFluid.fluidConnectionAll(IProcessor.defaultTankSorptions(processorContainerInfo)));
	}
	
	public static <TILE extends TileBasicUpgradeProcessor<TILE>> List<ItemSorption> defaultItemSorptions(BasicUpgradeProcessorContainerInfo<TILE> containerInfo) {
		List<ItemSorption> itemSorptions = IProcessor.defaultItemSorptions(containerInfo);
		itemSorptions.add(ItemSorption.IN);
		itemSorptions.add(ItemSorption.IN);
		return itemSorptions;
	}
	
	@Override
	public void refreshEnergyCapacity() {
		int capacity = IProcessor.energyCapacity(containerInfo, getSpeedMultiplier(), getPowerMultiplier());
		getEnergyStorage().setStorageCapacity(capacity);
		getEnergyStorage().setMaxTransfer(capacity);
	}
	
	@Override
	public double getSpeedMultiplier() {
		return 1D + speed_upgrade_multipliers_fp[0] * powerLawFactor(getSpeedCount(), speed_upgrade_power_laws_fp[0]);
	}
	
	@Override
	public double getPowerMultiplier() {
		return (1D + speed_upgrade_multipliers_fp[1] * powerLawFactor(getSpeedCount(), speed_upgrade_power_laws_fp[1])) / (1D + energy_upgrade_multipliers_fp[0] * powerLawFactor(getEnergyCount(), energy_upgrade_power_laws_fp[0]));
	}
	
	public static double powerLawFactor(int upgradeCount, double power) {
		return Math.pow(upgradeCount, power) - 1D;
	}
	
	public int getSpeedCount() {
		return 1 + getInventoryStacks().get(getSpeedUpgradeSlot()).getCount();
	}
	
	public int getEnergyCount() {
		return Math.min(getSpeedCount(), 1 + getInventoryStacks().get(getEnergyUpgradeSlot()).getCount());
	}
	
	// ITileInventory
	
	@Override
	public ItemStack decrStackSize(int slot, int amount) {
		ItemStack stack = super.decrStackSize(slot, amount);
		if (!world.isRemote) {
			if (slot < containerInfo.itemInputSize) {
				refreshRecipe();
				refreshActivity();
			}
			else if (slot < containerInfo.itemInputSize + containerInfo.itemOutputSize) {
				refreshActivity();
			}
			else if (slot == getSpeedUpgradeSlot() || slot == getEnergyUpgradeSlot()) {
				refreshEnergyCapacity();
			}
		}
		return stack;
	}
	
	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		super.setInventorySlotContents(slot, stack);
		if (!world.isRemote) {
			if (slot == getSpeedUpgradeSlot() || slot == getEnergyUpgradeSlot()) {
				refreshEnergyCapacity();
			}
		}
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if (stack.getItem() == NCItems.upgrade) {
			if (slot == getSpeedUpgradeSlot()) {
				return StackHelper.getMetadata(stack) == 0;
			}
			else if (slot == getEnergyUpgradeSlot()) {
				return StackHelper.getMetadata(stack) == 1;
			}
		}
		return super.isItemValidForSlot(slot, stack);
	}
	
	// IBasicUpgradable
	
	@Override
	public int getSpeedUpgradeSlot() {
		return containerInfo.getSpeedUpgradeSlot();
	}
	
	@Override
	public int getEnergyUpgradeSlot() {
		return containerInfo.getEnergyUpgradeSlot();
	}
}
