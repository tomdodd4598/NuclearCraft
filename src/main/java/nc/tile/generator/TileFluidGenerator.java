package nc.tile.generator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import nc.ModCheck;
import nc.recipe.AbstractRecipeHandler;
import nc.recipe.IngredientSorption;
import nc.recipe.NCRecipes;
import nc.recipe.ProcessorRecipe;
import nc.recipe.ProcessorRecipeHandler;
import nc.recipe.ingredient.IFluidIngredient;
import nc.tile.energy.ITileEnergy;
import nc.tile.energyFluid.TileEnergyFluidSidedInventory;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.energy.EnergyConnection;
import nc.tile.internal.fluid.FluidConnection;
import nc.tile.internal.fluid.Tank;
import nc.tile.internal.fluid.TankSorption;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fluids.FluidStack;

public abstract class TileFluidGenerator extends TileEnergyFluidSidedInventory implements IFluidGenerator {

	public final int defaultProcessTime, defaultProcessPower;
	public double baseProcessTime = 1, baseProcessPower = 0, baseProcessRadiation = 0;
	public double processPower = 0;
	public double speedMultiplier = 1;
	public final int fluidInputSize, fluidOutputSize, otherSlotsSize;
	
	public double time;
	public boolean isProcessing, hasConsumed, canProcessInputs;
	
	public final NCRecipes.Type recipeType;
	protected ProcessorRecipe recipe;
	
	protected Set<EntityPlayer> playersToUpdate;
	
	public TileFluidGenerator(String name, int fluidInSize, int fluidOutSize, int otherSize, @Nonnull List<Integer> fluidCapacity, @Nonnull List<TankSorption> tankSorptions, List<List<String>> allowedFluids, int capacity, @Nonnull NCRecipes.Type recipeType) {
		super(name, otherSize, capacity, ITileEnergy.energyConnectionAll(EnergyConnection.OUT), fluidCapacity, fluidCapacity, tankSorptions, allowedFluids, ITileFluid.fluidConnectionAll(FluidConnection.BOTH));
		fluidInputSize = fluidInSize;
		fluidOutputSize = fluidOutSize;
		
		otherSlotsSize = otherSize;
		setTanksShared(fluidInSize > 1);
		
		defaultProcessTime = 1;
		defaultProcessPower = 0;
		
		this.recipeType = recipeType;
		
		playersToUpdate = new HashSet<EntityPlayer>();
	}
	
	public static List<Integer> defaultTankCapacities(int capacity, int inSize, int outSize) {
		List<Integer> tankCapacities = new ArrayList<Integer>();
		for (int i = 0; i < 2*inSize + outSize; i++) tankCapacities.add(capacity);
		return tankCapacities;
	}
	
	public static List<TankSorption> defaultTankSorptions(int inSize, int outSize) {
		List<TankSorption> tankSorptions = new ArrayList<TankSorption>();
		for (int i = 0; i < inSize; i++) tankSorptions.add(TankSorption.IN);
		for (int i = 0; i < outSize; i++) tankSorptions.add(TankSorption.OUT);
		for (int i = 0; i < inSize; i++) tankSorptions.add(TankSorption.NON);
		return tankSorptions;
	}
	
	// Ticking
	
	@Override
	public void onAdded() {
		super.onAdded();
		if (!world.isRemote) {
			refreshRecipe();
			refreshActivity();
			isProcessing = isProcessing();
			hasConsumed = hasConsumed();
		}
	}
	
	@Override
	public void update() {
		super.update();
		updateGenerator();
	}
	
	public abstract void updateGenerator();
	
	public void updateBlockType() {
		if (ModCheck.ic2Loaded()) removeTileFromENet();
		setState(isProcessing);
		world.notifyNeighborsOfStateChange(pos, getBlockType(), true);
		if (ModCheck.ic2Loaded()) addTileToENet();
	}
	
	@Override
	public void refreshRecipe() {
		if (recipe == null || !recipe.matchingInputs(new ArrayList<ItemStack>(), getFluidInputs(hasConsumed))) {
			recipe = getRecipeHandler().getRecipeFromInputs(new ArrayList<ItemStack>(), getFluidInputs(hasConsumed));
		}
		consumeInputs();
	}
	
	@Override
	public void refreshActivity() {
		canProcessInputs = canProcessInputs();
	}
	
	// Processor Stats
	
	public abstract boolean setRecipeStats();
	
	// Processing
	
	public boolean isProcessing() {
		return readyToProcess() && isRedstonePowered();
	}
	
	public boolean readyToProcess() {
		return canProcessInputs && hasConsumed;
	}
	
	public boolean hasConsumed() {
		if (world.isRemote) return hasConsumed;
		for (int i = 0; i < fluidInputSize; i++) {
			if (!getTanks().get(i + fluidInputSize + fluidOutputSize).isEmpty()) return true;
		}
		return false;
	}
	
	public boolean canProcessInputs() {
		if (!setRecipeStats()) {
			if (hasConsumed) {
				for (Tank tank : getFluidInputs(true)) tank.setFluidStored(null);
				hasConsumed = false;
			}
			return false;
		}
		if (time >= baseProcessTime) return true;
		return canProduceProducts();
	}
	
	public boolean canProduceProducts() {
		for(int j = 0; j < fluidOutputSize; j++) {
			IFluidIngredient fluidProduct = getFluidProducts().get(j);
			if (fluidProduct.getMaxStackSize() <= 0) continue;
			if (fluidProduct.getStack() == null) return false;
			else if (!getTanks().get(j + fluidInputSize).isEmpty()) {
				if (!getTanks().get(j + fluidInputSize).getFluid().isFluidEqual(fluidProduct.getStack())) {
					return false;
				} else if (!getVoidExcessFluidOutputs() && getTanks().get(j + fluidInputSize).getFluidAmount() + fluidProduct.getMaxStackSize() > getTanks().get(j + fluidInputSize).getCapacity()) {
					return false;
				}
			}
		}
		return true;
	}
	
	public void consumeInputs() {
		if (hasConsumed || recipe == null) return;
		List<Integer> fluidInputOrder = getFluidInputOrder();
		if (fluidInputOrder == AbstractRecipeHandler.INVALID) return;
		
		for (int i = 0; i < fluidInputSize; i++) {
			if (!getTanks().get(i + fluidInputSize + fluidOutputSize).isEmpty()) {
				getTanks().get(i + fluidInputSize + fluidOutputSize).setFluid(null);
			}
		}
		for (int i = 0; i < fluidInputSize; i++) {
			IFluidIngredient fluidIngredient = getFluidIngredients().get(fluidInputOrder.get(i));
			if (fluidIngredient.getMaxStackSize() > 0) {
				getTanks().get(i + fluidInputSize + fluidOutputSize).setFluidStored(new FluidStack(getTanks().get(i).getFluid(), fluidIngredient.getMaxStackSize()));
				getTanks().get(i).changeFluidAmount(-fluidIngredient.getMaxStackSize());
			}
			if (getTanks().get(i).isEmpty()) getTanks().get(i).setFluid(null);
		}
		hasConsumed = true;
	}
	
	public void process() {
		time += speedMultiplier;
		getEnergyStorage().changeEnergyStored((int) processPower);
		getRadiationSource().setRadiationLevel(baseProcessRadiation*speedMultiplier);
		if (time >= baseProcessTime) finishProcess();
	}
	
	public void finishProcess() {
		double oldProcessTime = baseProcessTime;
		produceProducts();
		refreshRecipe();
		if (!setRecipeStats()) {
			time = 0;
			if (getEmptyUnusableTankInputs()) for (int i = 0; i < fluidInputSize; i++) getTanks().get(i).setFluid(null);
		}
		else time = MathHelper.clamp(time - oldProcessTime, 0D, baseProcessTime);
		refreshActivity();
	}
		
	public void produceProducts() {
		for (int i = fluidInputSize + fluidOutputSize; i < 2*fluidInputSize + fluidOutputSize; i++) getTanks().get(i).setFluid(null);
		
		if (!hasConsumed || recipe == null) return;
		
		for (int j = 0; j < fluidOutputSize; j++) {
			IFluidIngredient fluidProduct = getFluidProducts().get(j);
			if (fluidProduct.getNextStackSize() <= 0) continue;
			if (getTanks().get(j + fluidInputSize).isEmpty()) {
				getTanks().get(j + fluidInputSize).setFluidStored(fluidProduct.getNextStack());
			} else if (getTanks().get(j + fluidInputSize).getFluid().isFluidEqual(fluidProduct.getStack())) {
				getTanks().get(j + fluidInputSize).changeFluidAmount(fluidProduct.getNextStackSize());
			}
		}
		hasConsumed = false;
	}
	
	// IProcessor
	
	@Override
	public ProcessorRecipeHandler getRecipeHandler() {
		return recipeType.getRecipeHandler();
	}
		
	@Override
	public ProcessorRecipe getRecipe() {
		return recipe;
	}
	
	@Override
	public List<Tank> getFluidInputs(boolean consumed) {
		return consumed ? getTanks().subList(fluidInputSize + fluidOutputSize, 2*fluidInputSize + fluidOutputSize) : getTanks().subList(0, fluidInputSize);
	}
	
	@Override
	public List<IFluidIngredient> getFluidIngredients() {
		return recipe.fluidIngredients();
	}
	
	@Override
	public List<IFluidIngredient> getFluidProducts() {
		return recipe.fluidProducts();
	}
	
	@Override
	public List<Integer> getFluidInputOrder() {
		List<Integer> fluidInputOrder = new ArrayList<Integer>();
		List<IFluidIngredient> fluidIngredients = recipe.fluidIngredients();
		for (int i = 0; i < fluidInputSize; i++) {
			int position = -1;
			for (int j = 0; j < fluidIngredients.size(); j++) {
				if (fluidIngredients.get(j).matches(getFluidInputs(false).get(i), IngredientSorption.INPUT)) {
					position = j;
					break;
				}
			}
			if (position == -1) return AbstractRecipeHandler.INVALID;
			fluidInputOrder.add(position);
		}
		return fluidInputOrder;
	}
	
	// IInventory

	@Override
	public void markDirty() {
		refreshRecipe();
		refreshActivity();
		super.markDirty();
	}
	
	@Override
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		return false;
	}
	
	// ISidedInventory
	
	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		return new int[] {};
	}

	@Override
	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing direction) {
		return false;
	}

	@Override
	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing direction) {
		return false;
	}
	
	// Fluids
	
	@Override
	public boolean isNextToFill(FluidStack resource, int tankNumber) {
		if (tankNumber >= fluidInputSize) return false;
		if (!getTanksShared()) return true;
		
		for (int i = 0; i < fluidInputSize; i++) {
			if (tankNumber != i && getTanks().get(i).canFill() && getTanks().get(i).getFluid() != null) {
				if (getTanks().get(i).getFluid().isFluidEqual(resource)) return false;
			}
		}
		return true;
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setDouble("time", time);
		nbt.setBoolean("isProcessing", isProcessing);
		nbt.setBoolean("hasConsumed", hasConsumed);
		nbt.setBoolean("canProcessInputs", canProcessInputs);
		return nbt;
	}
		
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		time = nbt.getDouble("time");
		isProcessing = nbt.getBoolean("isProcessing");
		hasConsumed = nbt.getBoolean("hasConsumed");
		canProcessInputs = nbt.getBoolean("canProcessInputs");
	}
}
