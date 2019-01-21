package nc.multiblock.saltFission.tile;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import nc.Global;
import nc.ModCheck;
import nc.config.NCConfig;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.saltFission.SaltFissionReactor;
import nc.recipe.AbstractRecipeHandler;
import nc.recipe.IngredientSorption;
import nc.recipe.NCRecipes;
import nc.recipe.ProcessorRecipe;
import nc.recipe.ProcessorRecipeHandler;
import nc.recipe.ingredient.IFluidIngredient;
import nc.tile.fluid.ITileFluid;
import nc.tile.generator.IFluidGenerator;
import nc.tile.internal.fluid.FluidConnection;
import nc.tile.internal.fluid.FluidTileWrapper;
import nc.tile.internal.fluid.GasTileWrapper;
import nc.tile.internal.fluid.Tank;
import nc.tile.internal.fluid.TankSorption;
import nc.tile.passive.ITilePassive;
import nc.util.FluidStackHelper;
import nc.util.GasHelper;
import nc.util.RadiationHelper;
import nc.util.RecipeHelper;
import nc.util.RegistryHelper;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TileSaltFissionVessel extends TileSaltFissionPartBase implements IFluidGenerator, ITileFluid {
	
	private static final FluidConnection DEPLETED_OUT = FluidConnection.IN;
	private static final FluidConnection FUEL_OUT = FluidConnection.OUT;
	private static final FluidConnection DEFAULT = FluidConnection.BOTH;
	private static final FluidConnection DISABLED = FluidConnection.NON;
	
	private final @Nonnull List<Tank> tanks = Lists.newArrayList(new Tank(FluidStackHelper.INGOT_BLOCK_VOLUME*2, TankSorption.IN, RecipeHelper.validFluids(NCRecipes.Type.SALT_FISSION).get(0)), new Tank(FluidStackHelper.INGOT_BLOCK_VOLUME*4, TankSorption.OUT, new ArrayList<String>()), new Tank(FluidStackHelper.INGOT_BLOCK_VOLUME*2, TankSorption.NON, new ArrayList<String>()));
	
	private @Nonnull FluidConnection[] fluidConnections = ITileFluid.fluidConnectionAll(FluidConnection.BOTH);
	
	private @Nonnull FluidTileWrapper[] fluidSides;
	
	private @Nonnull GasTileWrapper gasWrapper;
	
	public final int fluidInputSize = 1, fluidOutputSize = 1;
	
	public double baseProcessTime = 1, baseProcessHeat = 0, baseProcessRadiation = 0;
	private int baseVesselEfficiency;
	private double moderatorExtraEfficiency, moderatorHeatFactor;
	
	private int fluidToHold;
	
	public boolean distributedTo = false;
	public boolean retrievedFrom = false;
	
	public double time;
	public boolean isProcessing, hasConsumed, canProcessInputs;
	
	public final NCRecipes.Type recipeType = NCRecipes.Type.SALT_FISSION;
	protected ProcessorRecipe recipe;
	
	protected int vesselCount;
	
	public TileSaltFissionVessel() {
		super(CuboidalPartPositionType.INTERIOR);
		fluidSides = ITileFluid.getDefaultFluidSides(this);
		gasWrapper = new GasTileWrapper(this);
	}
	
	@Override
	public void onMachineAssembled(SaltFissionReactor controller) {
		doStandardNullControllerResponse(controller);
		super.onMachineAssembled(controller);
		if (getWorld().isRemote) return;
	}
	
	@Override
	public void onMachineBroken() {
		super.onMachineBroken();
		if (getWorld().isRemote) return;
		//getWorld().setBlockState(getPos(), getWorld().getBlockState(getPos()), 2);
	}
	
	public void calculateEfficiency() {
		int newEfficiency = 1;
		dirLoop: for (EnumFacing dir : EnumFacing.VALUES) {
			if (isVessel(dir, 1)) newEfficiency++;
			else for (int i = 1; i <= NCConfig.fission_neutron_reach; i++) {
				if (isModerator(dir, i, false)) {
					if (isVessel(dir, i + 1)) {
						newEfficiency++;
						continue dirLoop;
					} else continue;
				} else continue dirLoop;
			}
		}
		
		int moderatorCount = 0;
		for (EnumFacing dir : EnumFacing.VALUES) {
			if (isModerator(dir, 1, true)) {
				moderatorCount++;
			}
		}
		
		baseVesselEfficiency = newEfficiency;
		moderatorExtraEfficiency = newEfficiency*moderatorCount*NCConfig.fission_moderator_extra_power/6D;
		moderatorHeatFactor = newEfficiency*moderatorCount*NCConfig.fission_moderator_extra_heat/6D;
	}
	
	private boolean isVessel(EnumFacing dir, int offset) {
		TileEntity tile = world.getTileEntity(pos.offset(dir, offset));
		if (!(tile instanceof TileSaltFissionVessel)) return false;
		TileSaltFissionVessel vessel = (TileSaltFissionVessel) tile;
		return vessel.canProcessInputs && vessel.isMultiblockAssembled();
	}
	
	private boolean isModerator(EnumFacing dir, int offset, boolean isInValidPosition) {
		TileEntity tile = world.getTileEntity(pos.offset(dir, offset));
		if (!(tile instanceof TileSaltFissionModerator)) return false;
		TileSaltFissionModerator moderator = (TileSaltFissionModerator) tile;
		moderator.isInModerationLine = true;
		if (isInValidPosition && canProcessInputs) moderator.isInValidPosition = true;
		return true;
		
	}
	
	public void doMeltdown() {
		RadiationHelper.addToChunkRadiation(world.getChunkFromBlockCoords(pos), baseProcessRadiation*NCConfig.salt_fission_fuel_use);
		
		Block corium = RegistryHelper.getBlock(Global.MOD_ID + ":fluid_corium");
		world.removeTileEntity(pos);
		world.setBlockState(pos, corium.getDefaultState());
	}
	
	// Processing
	
	public double getEfficiency() {
		return baseVesselEfficiency + moderatorExtraEfficiency;
	}
	
	public double getHeatMultiplier() {
		return baseVesselEfficiency*(baseVesselEfficiency + 1D)*0.5D + moderatorHeatFactor;
	}
	
	public double getProcessHeat() {
		return getHeatMultiplier()*baseProcessHeat*NCConfig.salt_fission_heat_generation;
	}
	
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
	
	// Ticking
	
	@Override
	public void update() {
		super.update();
		updateVessel();
	}
	
	public void updateVessel() {
		if(!world.isRemote) {
			setIsReactorOn();
			boolean wasProcessing = isProcessing;
			isProcessing = isProcessing();
			boolean shouldUpdate = false;
			tickVessel();
			if (isProcessing) process();
			else getRadiationSource().setRadiationLevel(0D);
			if (wasProcessing != isProcessing) {
				shouldUpdate = true;
			}
			if (vesselCount == 0) {
				pushFluid();
				refreshRecipe();
				refreshActivity();
			}
			if (shouldUpdate) markDirty();
		}
	}
	
	public void tickVessel() {
		vesselCount++; vesselCount %= NCConfig.machine_update_rate / 2;
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
		canProcessInputs = canProcessInputs(false);
	}
	
	@Override
	public void refreshActivityOnProduction() {
		canProcessInputs = canProcessInputs(true);
	}
	
	// Processor Stats
	
	public boolean setRecipeStats() {
		if (recipe == null) {
			baseProcessTime = 1D;
			baseProcessHeat = 0D;
			baseProcessRadiation = 0D;
			return false;
		}
		baseProcessTime = recipe.getSaltFissionFuelTime();
		baseProcessHeat = recipe.getSaltFissionFuelHeat();
		baseProcessRadiation = recipe.getSaltFissionFuelRadiation();
		fluidToHold = getFluidToHold();
		return true;
	}
	
	private int getFluidToHold() {
		return Math.min(FluidStackHelper.INGOT_BLOCK_VOLUME/2, getFluidIngredients().get(0).getMaxStackSize()*NCConfig.machine_update_rate / 2);
	}
	
	// Processing
	
	public boolean isProcessing() {
		return readyToProcess() && isReactorOn;
	}
	
	public boolean readyToProcess() {
		return canProcessInputs && hasConsumed && isMultiblockAssembled();
	}
	
	public boolean hasConsumed() {
		if (world.isRemote) return hasConsumed;
		for (int i = 0; i < fluidInputSize; i++) {
			if (!tanks.get(i + fluidInputSize + fluidOutputSize).isEmpty()) return true;
		}
		return false;
	}
		
	public boolean canProcessInputs(boolean justProduced) {
		if (!setRecipeStats()) {
			if (hasConsumed) {
				for (Tank tank : getFluidInputs(true)) tank.setFluidStored(null);
				hasConsumed = false;
			}
			return false;
		}
		if (!justProduced && time >= baseProcessTime) return true;
		return canProduceProducts();
	}
	
	public boolean canProduceProducts() {
		for(int j = 0; j < fluidOutputSize; j++) {
			IFluidIngredient fluidProduct = getFluidProducts().get(j);
			if (fluidProduct.getMaxStackSize() <= 0) continue;
			if (fluidProduct.getStack() == null) return false;
			else if (!tanks.get(j + fluidInputSize).isEmpty()) {
				if (!tanks.get(j + fluidInputSize).getFluid().isFluidEqual(fluidProduct.getStack())) {
					return false;
				} else if (tanks.get(j + fluidInputSize).getFluidAmount() + fluidProduct.getMaxStackSize() > tanks.get(j + fluidInputSize).getCapacity()) {
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
			if (!tanks.get(i + fluidInputSize + fluidOutputSize).isEmpty()) {
				tanks.get(i + fluidInputSize + fluidOutputSize).setFluid(null);
			}
		}
		for (int i = 0; i < fluidInputSize; i++) {
			IFluidIngredient fluidIngredient = getFluidIngredients().get(fluidInputOrder.get(i));
			if (fluidIngredient.getMaxStackSize() > 0) {
				tanks.get(i + fluidInputSize + fluidOutputSize).setFluidStored(new FluidStack(tanks.get(i).getFluid(), fluidIngredient.getMaxStackSize()));
				tanks.get(i).changeFluidAmount(-fluidIngredient.getMaxStackSize());
			}
			if (tanks.get(i).isEmpty()) tanks.get(i).setFluid(null);
		}
		hasConsumed = true;
	}
	
	public void process() {
		time += NCConfig.salt_fission_fuel_use;
		getRadiationSource().setRadiationLevel(baseProcessRadiation*NCConfig.salt_fission_fuel_use);
		if (time >= baseProcessTime) finishProcess();
	}
	
	public void finishProcess() {
		double oldProcessTime = baseProcessTime;
		produceProducts();
		refreshRecipe();
		if (!setRecipeStats()) time = 0;
		else time = MathHelper.clamp(time - oldProcessTime, 0D, baseProcessTime);
		refreshActivityOnProduction();
	}
		
	public void produceProducts() {
		for (int i = fluidInputSize + fluidOutputSize; i < 2*fluidInputSize + fluidOutputSize; i++) tanks.get(i).setFluid(null);
		
		if (!hasConsumed || recipe == null) return;
		
		for (int j = 0; j < fluidOutputSize; j++) {
			IFluidIngredient fluidProduct = getFluidProducts().get(j);
			if (fluidProduct.getNextStackSize() <= 0) continue;
			if (tanks.get(j + fluidInputSize).isEmpty()) {
				tanks.get(j + fluidInputSize).setFluidStored(fluidProduct.getNextStack());
			} else if (tanks.get(j + fluidInputSize).getFluid().isFluidEqual(fluidProduct.getStack())) {
				tanks.get(j + fluidInputSize).changeFluidAmount(fluidProduct.getNextStackSize());
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
		return consumed ? tanks.subList(fluidInputSize + fluidOutputSize, 2*fluidInputSize + fluidOutputSize) : tanks.subList(0, fluidInputSize);
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
	
	// Fluids
	
	@Override
	@Nonnull
	public List<Tank> getTanks() {
		return tanks;
	}

	@Override
	@Nonnull
	public FluidConnection[] getFluidConnections() {
		return fluidConnections;
	}
	
	@Override
	public void setFluidConnections(@Nonnull FluidConnection[] connections) {
		fluidConnections = connections;
	}

	@Override
	@Nonnull
	public FluidTileWrapper[] getFluidSides() {
		return fluidSides;
	}
	
	@Override
	public @Nonnull GasTileWrapper getGasWrapper() {
		return gasWrapper;
	}
	
	@Override
	public void pushFluidToSide(@Nonnull EnumFacing side) {
		FluidConnection thisConnection = getFluidConnection(side);
		if (thisConnection == DISABLED) return;
		
		TileEntity tile = getTileWorld().getTileEntity(getTilePos().offset(side));
		
		if (tile instanceof TileSaltFissionVessel) {
			TileSaltFissionVessel vessel = (TileSaltFissionVessel)tile;
			FluidConnection vesselConnection = vessel.getFluidConnection(side.getOpposite());
			
			if (thisConnection == FUEL_OUT) {
				if (vesselConnection == DEFAULT) {
					pushFuel(vessel);
					pushDepleted(vessel);
				} else if (vesselConnection == DEPLETED_OUT) {
					pushFuel(vessel);
				}
			} else if (thisConnection == DEPLETED_OUT && (vesselConnection == DEFAULT || vesselConnection == FUEL_OUT)) {
				pushDepleted(vessel);
			}
		}
		
		else if (thisConnection == DEPLETED_OUT) {
			if (tile instanceof ITilePassive) if (!((ITilePassive) tile).canPushFluidsTo()) return;
			IFluidHandler adjStorage = tile == null ? null : tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite());
			
			if (adjStorage == null) return;
			
			for (int i = 0; i < getTanks().size(); i++) {
				if (getTanks().get(i).getFluid() == null || !getTanks().get(i).canDrain()) continue;
				
				getTanks().get(i).drainInternal(adjStorage.fill(getTanks().get(i).drainInternal(getTanks().get(i).getCapacity(), false), true), true);
			}
		}
	}
	
	public void pushFuel(TileSaltFissionVessel other) {
		if (getTanks().get(0).getFluidAmount() > other.getTanks().get(0).getFluidAmount()) {
			getTanks().get(0).drainInternal(other.getTanks().get(0).fillInternal(getTanks().get(0).drainInternal(getTanks().get(0).getFluidAmount() - fluidToHold, false), true), true);
		}
	}
	
	public void pushDepleted(TileSaltFissionVessel other) {
		getTanks().get(1).drainInternal(other.getTanks().get(1).fillInternal(getTanks().get(1).drainInternal(getTanks().get(1).getCapacity(), false), true), true);
	}

	@Override
	public boolean getTanksShared() {
		return false;
	}

	@Override
	public void setTanksShared(boolean shared) {}

	@Override
	public boolean getEmptyUnusableTankInputs() {
		return false;
	}

	@Override
	public void setEmptyUnusableTankInputs(boolean emptyUnusableTankInputs) {}

	@Override
	public boolean getVoidExcessFluidOutputs() {
		return false;
	}

	@Override
	public void setVoidExcessFluidOutputs(boolean voidExcessFluidOutputs) {}
	
	@Override
	public boolean hasConfigurableFluidConnections() {
		return true;
	}
	
	// NBT
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		writeTanks(nbt);
		writeFluidConnections(nbt);
		
		nbt.setDouble("baseProcessTime", baseProcessTime);
		nbt.setDouble("baseProcessHeat", baseProcessHeat);
		nbt.setInteger("baseVesselEfficiency", baseVesselEfficiency);
		nbt.setDouble("moderatorExtraEfficiency", moderatorExtraEfficiency);
		nbt.setDouble("moderatorHeatFactor", moderatorHeatFactor);
		
		nbt.setDouble("time", time);
		nbt.setBoolean("isProcessing", isProcessing);
		nbt.setBoolean("hasConsumed", hasConsumed);
		nbt.setBoolean("canProcessInputs", canProcessInputs);
		nbt.setInteger("fluidToHold", fluidToHold);
		return nbt;
	}
		
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		readTanks(nbt);
		readFluidConnections(nbt);
		
		baseProcessTime = nbt.getDouble("baseProcessTime");
		baseProcessHeat = nbt.getDouble("baseProcessHeat");
		baseVesselEfficiency = nbt.getInteger("baseVesselEfficiency");
		moderatorExtraEfficiency = nbt.getDouble("moderatorExtraEfficiency");
		moderatorHeatFactor = nbt.getDouble("moderatorHeatFactor");
		
		time = nbt.getDouble("time");
		isProcessing = nbt.getBoolean("isProcessing");
		hasConsumed = nbt.getBoolean("hasConsumed");
		canProcessInputs = nbt.getBoolean("canProcessInputs");
		fluidToHold = nbt.getInteger("fluidToHold");
	}
	
	// Capability
	
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
		if (!getTanks().isEmpty() && hasFluidSideCapability(side)) {
			side = nonNullSide(side);
			if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) return getFluidSide(side) != null;
			if (ModCheck.mekanismLoaded()) if (GasHelper.isGasCapability(capability)) return getGasWrapper() != null;
		}
		return super.hasCapability(capability, side);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
		if (!getTanks().isEmpty() && hasFluidSideCapability(side)) {
			side = nonNullSide(side);
			if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) return (T) getFluidSide(side);
			if (ModCheck.mekanismLoaded()) if (GasHelper.isGasCapability(capability)) return (T) getGasWrapper();
		}
		return super.getCapability(capability, side);
	}
}
