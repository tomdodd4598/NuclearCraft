package nc.tile.processor;

import java.util.List;

import nc.recipe.ProcessorRecipe;
import nc.recipe.ProcessorRecipeHandler;
import nc.tile.dummy.IInterfaceable;
import nc.tile.energyFluid.IBufferable;
import nc.tile.internal.fluid.*;
import nc.tile.internal.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.*;
import net.minecraftforge.items.*;

public interface IProcessor extends IInterfaceable, IBufferable {
	
	public void refreshRecipe();
	
	public void refreshActivity();
	
	public void refreshActivityOnProduction();
	
	public static double maxStat(ProcessorRecipeHandler recipeHandler, int i) {
		double max = 1D;
		List<ProcessorRecipe> recipes = recipeHandler.getRecipeList();
		for (ProcessorRecipe recipe : recipes) {
			if (recipe == null || recipe.extras().size() <= i) continue;
			else if (recipe.extras().get(i) instanceof Double) max = Math.max(max, (double) recipe.extras().get(i));
		}
		return max;
	}
	
	public static double maxBaseProcessTime(ProcessorRecipeHandler recipeHandler, int defaultProcessTime) {
		return maxStat(recipeHandler, 0)*defaultProcessTime;
	}
	
	public static double maxBaseProcessPower(ProcessorRecipeHandler recipeHandler, int defaultProcessPower) {
		return maxStat(recipeHandler, 1)*defaultProcessPower;
	}
	
	public static int getCapacity(ProcessorRecipeHandler recipeHandler, int defaultProcessTime, double speedMultiplier, int defaultProcessPower, double powerMultiplier) {
		return Math.max(1, (int) Math.round(Math.ceil(maxBaseProcessTime(recipeHandler, defaultProcessTime)/speedMultiplier)))*Math.min(Integer.MAX_VALUE, (int) (maxBaseProcessPower(recipeHandler, defaultProcessPower)*powerMultiplier));
	}
	
	// Auto-pushing
	
	public static boolean pushItems(World world, BlockPos pos, List<ItemStack> inventoryStacks, InventoryConnection[] connections, int firstOutputSlot, int outputSize) {
		IItemHandler[] adjacentInventories = new IItemHandler[EnumFacing.VALUES.length];
		boolean[] checkedSides = new boolean[EnumFacing.VALUES.length];
		boolean hasDoneWork = false;
		for (int i = 0; i < outputSize; i++) {
			hasDoneWork |= pushItem(world, pos, inventoryStacks, connections, firstOutputSlot + i, adjacentInventories, checkedSides);
		}
		return hasDoneWork;
	}
	
	public static boolean pushItem(World world, BlockPos pos, List<ItemStack> inventoryStacks, InventoryConnection[] connections, int slot, IItemHandler[] adjacentInventories, boolean[] checkedSides) {
		ItemStack stackInSlot = inventoryStacks.get(slot);
		if (stackInSlot.isEmpty()) return false;
		
		boolean hasDoneWork = false;
		for (EnumFacing side : EnumFacing.VALUES) {
			if (connections[side.ordinal()].getItemSorption(slot) != ItemSorption.PUSH) {
				continue;
			}
			
			IItemHandler inventory = getAdjacentInventory(world, pos, side, adjacentInventories, checkedSides);
			if (inventory == null) continue;
			
			ItemStack remainder = ItemHandlerHelper.insertItemStacked(inventory, stackInSlot, false);
			hasDoneWork |= remainder.getCount() != stackInSlot.getCount();
			stackInSlot = remainder;
			if (stackInSlot.isEmpty()) break;
		}
		
		inventoryStacks.set(slot, stackInSlot);
		return hasDoneWork;
	}
	
	public static IItemHandler getAdjacentInventory(World world, BlockPos pos, EnumFacing side, IItemHandler[] adjacentInventories, boolean[] checkedSides) {
		int sideIndex = side.ordinal();
		if (!checkedSides[sideIndex]) {
			checkedSides[sideIndex] = true;
			TileEntity tile = world.getTileEntity(pos.offset(side));
			if (tile != null) {
				adjacentInventories[sideIndex] = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, side.getOpposite());
			}
		}
		return adjacentInventories[sideIndex];
	}
	
	public static boolean pushFluids(World world, BlockPos pos, List<Tank> tanks, FluidConnection[] connections, int firstOutputTank, int outputSize) {
		IFluidHandler[] adjacentTanks = new IFluidHandler[EnumFacing.VALUES.length];
		boolean[] checkedSides = new boolean[EnumFacing.VALUES.length];
		boolean hasDoneWork = false;
		for (int i = 0; i < outputSize; i++) {
			hasDoneWork |= pushFluid(world, pos, tanks, connections, firstOutputTank + i, adjacentTanks, checkedSides);
		}
		return hasDoneWork;
	}
	
	public static boolean pushFluid(World world, BlockPos pos, List<Tank> tanks, FluidConnection[] connections, int tankNumber, IFluidHandler[] adjacentTanks, boolean[] checkedSides) {
		Tank tank = tanks.get(tankNumber);
		FluidStack fluidInTank = tank.getFluid();
		if (fluidInTank == null) return false;
		
		boolean hasDoneWork = false;
		for (EnumFacing side : EnumFacing.VALUES) {
			if (connections[side.ordinal()].getTankSorption(tankNumber) != TankSorption.PUSH) {
				continue;
			}
			
			IFluidHandler externalTanks = getAdjacentTanks(world, pos, side, adjacentTanks, checkedSides);
			if (externalTanks == null) continue;
			
			int filledAmount = externalTanks.fill(fluidInTank.copy(), true);
			if (filledAmount <= 0) continue;
			
			hasDoneWork = true;
			tank.drain(filledAmount, true);
			fluidInTank = tank.getFluid();
			if (fluidInTank == null) break;
		}
		
		return hasDoneWork;
	}
	
	public static IFluidHandler getAdjacentTanks(World world, BlockPos pos, EnumFacing side, IFluidHandler[] adjacentTanks, boolean[] checkedSides) {
		int sideIndex = side.ordinal();
		if (!checkedSides[sideIndex]) {
			checkedSides[sideIndex] = true;
			TileEntity tile = world.getTileEntity(pos.offset(side));
			if (tile != null) {
				adjacentTanks[sideIndex] = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite());
			}
		}
		return adjacentTanks[sideIndex];
	}
}
