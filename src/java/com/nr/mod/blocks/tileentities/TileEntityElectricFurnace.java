package com.nr.mod.blocks.tileentities;
 
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import cofh.api.energy.IEnergyHandler;

import com.nr.mod.NuclearRelativistics;

public class TileEntityElectricFurnace extends TileEntityMachine1to1 implements IEnergyHandler, ISidedInventory {
	public TileEntityElectricFurnace() {
		this.localizedName = "Electric Furnace";
	}

	public void updateEntity() {
		super.updateEntity();
		if (flag != flag1) { flag1 = flag; BlockElectricFurnace.updateBlockState(flag, this.worldObj, this.xCoord, this.yCoord, this.zCoord); }
		markDirty();
	}

	public ItemStack getOutput(ItemStack stack) {
		return FurnaceRecipes.smelting().getSmeltingResult(stack);
	}

	public double FurnaceSpeed() {
		return 100*(100/NuclearRelativistics.electricFurnaceSmeltSpeed);
	}

	public double RequiredEnergy() {
		return 2000;
	}
}