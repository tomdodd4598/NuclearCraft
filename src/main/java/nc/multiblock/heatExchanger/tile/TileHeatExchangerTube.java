package nc.multiblock.heatExchanger.tile;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import nc.ModCheck;
import nc.config.NCConfig;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.heatExchanger.HeatExchanger;
import nc.multiblock.heatExchanger.HeatExchangerTubeType;
import nc.recipe.AbstractRecipeHandler;
import nc.recipe.IngredientSorption;
import nc.recipe.NCRecipes;
import nc.recipe.ProcessorRecipe;
import nc.recipe.ProcessorRecipeHandler;
import nc.recipe.ingredient.IFluidIngredient;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.fluid.FluidConnection;
import nc.tile.internal.fluid.FluidTileWrapper;
import nc.tile.internal.fluid.GasTileWrapper;
import nc.tile.internal.fluid.Tank;
import nc.tile.internal.fluid.TankSorption;
import nc.tile.passive.ITilePassive;
import nc.tile.processor.IFluidProcessor;
import nc.util.GasHelper;
import nc.util.RecipeHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TileHeatExchangerTube extends TileHeatExchangerPartBase implements IFluidProcessor, ITileFluid {
	
	private static final FluidConnection PRODUCT_OUT = FluidConnection.IN;
	private static final FluidConnection FLUID_OUT = FluidConnection.OUT;
	private static final FluidConnection DEFAULT = FluidConnection.BOTH;
	private static final FluidConnection DISABLED = FluidConnection.NON;
	
	private final @Nonnull List<Tank> tanks = Lists.newArrayList(new Tank(8000, TankSorption.IN, RecipeHelper.validFluids(NCRecipes.Type.HEAT_EXCHANGER).get(0)), new Tank(32000, TankSorption.OUT, new ArrayList<String>()));

	private @Nonnull FluidConnection[] fluidConnections = ITileFluid.fluidConnectionAll(FluidConnection.NON);
	
	private @Nonnull FluidTileWrapper[] fluidSides;
	
	private @Nonnull GasTileWrapper gasWrapper;
	
	public final int fluidInputSize = 1, fluidOutputSize = 1;
	
	public final int defaultProcessTime = 16000;
	public double baseProcessTime = defaultProcessTime;
	
	private int fluidToHold;
	
	public double time;
	public boolean isProcessing, canProcessInputs;
	public double speedMultiplier = 0;
	
	private int inputTemperature = 0, outputTemperature = 0;
	private EnumFacing flowDir = null;
	
	public final NCRecipes.Type recipeType = NCRecipes.Type.HEAT_EXCHANGER;
	protected ProcessorRecipe recipe;
	
	private final double conductivity;
	
	public static class Copper extends TileHeatExchangerTube {
		
		public Copper() {
			super(HeatExchangerTubeType.COPPER);
		}
	}
	
	public static class HardCarbon extends TileHeatExchangerTube {
		
		public HardCarbon() {
			super(HeatExchangerTubeType.HARD_CARBON);
		}
	}
	
	public static class Thermoconducting extends TileHeatExchangerTube {
		
		public Thermoconducting() {
			super(HeatExchangerTubeType.THERMOCONDUCTING);
		}
	}
	
	public TileHeatExchangerTube(HeatExchangerTubeType tubeType) {
		super(CuboidalPartPositionType.INTERIOR);
		fluidSides = ITileFluid.getDefaultFluidSides(this);
		gasWrapper = new GasTileWrapper(this);
		
		this.conductivity = tubeType.getConductivity();
	}
	
	@Override
	public void onMachineAssembled(HeatExchanger controller) {
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
	
	public int checkPosition() {
		if (!isMultiblockAssembled() || !canProcessInputs) {
			speedMultiplier = 0;
			return 0;
		}
		
		int adjCount = 0;
		double speedCount = 0;
		
		for (EnumFacing dir : EnumFacing.VALUES) {
			double mult = getTubeSpeedMultiplier(dir);
			speedCount += mult;
			if (mult > 0) adjCount++;
		}
		
		speedMultiplier = speedCount;
		return adjCount;
	}
	
	private double getTubeSpeedMultiplier(EnumFacing dir) {
		TileEntity tile = world.getTileEntity(pos.offset(dir));
		if (!(tile instanceof TileHeatExchangerTube)) return 0;
		TileHeatExchangerTube tube = (TileHeatExchangerTube) tile;
		
		if (!tube.canProcessInputs || (requiresContraflow(tube) && !isContraflow(tube))) return 0;
		
		if (!canConnectFluid(dir) || !tube.canConnectFluid(dir.getOpposite())) {
			return conductivityMult()*(isHeating() != tube.isHeating() ? tube.getAbsRecipeTempDiff() : -getAbsInputTempDiff(tube));
		}
		return 0;
	}
	
	private boolean isContraflow(TileHeatExchangerTube tube) {
		if (flowDir == null || tube.flowDir == null) return !(flowDir == null ^ tube.flowDir == null);
		return flowDir.getIndex() != tube.flowDir.getIndex();
	}
	
	private boolean requiresContraflow(TileHeatExchangerTube tube) {
		return inputTemperature > tube.inputTemperature ^ outputTemperature > tube.outputTemperature;
	}
	
	private int getAbsRecipeTempDiff() {
		return Math.abs(inputTemperature - outputTemperature);
	}
	
	private int getAbsInputTempDiff(TileHeatExchangerTube tube) {
		return Math.abs(inputTemperature - tube.inputTemperature);
	}
	
	private double conductivityMult() {
		return isHeating() ? conductivity : 1D/conductivity;
	}
	
	// Processing
	
	public double getSpeedMultiplier() {
		return speedMultiplier;
	}
	
	@Override
	public void onAdded() {
		super.onAdded();
		if (!world.isRemote) isProcessing = isProcessing();
	}
	
	@Override
	public void update() {
		super.update();
		updateTube();
	}
	
	public void updateTube() {
		setIsHeatExchangerOn();
		recipe = getRecipeHandler().getRecipeFromInputs(new ArrayList<ItemStack>(), getFluidInputs());
		canProcessInputs = canProcessInputs();
		boolean wasProcessing = isProcessing;
		isProcessing = isProcessing();
		boolean shouldUpdate = false;
		if (!world.isRemote) {
			tickTile();
			if (isProcessing) process();
			if (wasProcessing != isProcessing) {
				shouldUpdate = true;
			}
			if (shouldTileCheck()) pushFluid();
		}
		if (shouldUpdate) markDirty();
	}
	
	@Override
	public void tickTile() {
		tickCount++; tickCount %= NCConfig.machine_update_rate / 2;
	}
	
	public boolean isProcessing() {
		return readyToProcess() && isHeatExchangerOn;
	}
	
	public boolean readyToProcess() {
		return canProcessInputs && isMultiblockAssembled();
	}
	
	public void process() {
		time = Math.max(0, time + getSpeedMultiplier());
		if (time >= baseProcessTime) {
			double oldProcessTime = baseProcessTime;
			produceProducts();
			recipe = getRecipeHandler().getRecipeFromInputs(new ArrayList<ItemStack>(), getFluidInputs());
			setRecipeStats();
			if (recipe == null) time = 0;
			else time = MathHelper.clamp(time - oldProcessTime, 0D, baseProcessTime);
		}
	}
	
	public boolean canProcessInputs() {
		setDefaultRecipeStats();
		if (recipe == null) return false;
		setRecipeStats();
		if (time >= baseProcessTime) return true;
		
		for (int j = 0; j < fluidOutputSize; j++) {
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
	
	public void setRecipeStats() {
		if (recipe == null) {
			setDefaultRecipeStats();
			return;
		}
		
		baseProcessTime = recipe.getHeatExchangerProcessTime(defaultProcessTime);
		
		fluidToHold = getFluidIngredients().get(0).getMaxStackSize();
		
		inputTemperature = recipe.getHeatExchangerInputTemperature();
		outputTemperature = recipe.getHeatExchangerOutputTemperature();
	}
	
	public void setDefaultRecipeStats() {
		baseProcessTime = defaultProcessTime;
		
		inputTemperature = 0;
		outputTemperature = 0;
	}
	
	private boolean isHeating() {
		return inputTemperature < outputTemperature;
	}
	
	public void produceProducts() {
		if (recipe == null) return;
		List<Integer> fluidInputOrder = getFluidInputOrder();
		if (fluidInputOrder == AbstractRecipeHandler.INVALID) return;
		
		for (int i = 0; i < fluidInputSize; i++) {
			int fluidIngredientStackSize = getFluidIngredients().get(fluidInputOrder.get(i)).getMaxStackSize();
			if (fluidIngredientStackSize > 0) tanks.get(i).changeFluidAmount(-fluidIngredientStackSize);
			if (tanks.get(i).getFluidAmount() <= 0) tanks.get(i).setFluidStored(null);
		}
		for (int j = 0; j < fluidOutputSize; j++) {
			IFluidIngredient fluidProduct = getFluidProducts().get(j);
			if (fluidProduct.getMaxStackSize() <= 0) continue;
			if (tanks.get(j + fluidInputSize).isEmpty()) {
				tanks.get(j + fluidInputSize).setFluidStored(fluidProduct.getNextStack());
			} else if (tanks.get(j + fluidInputSize).getFluid().isFluidEqual(fluidProduct.getStack())) {
				tanks.get(j + fluidInputSize).changeFluidAmount(fluidProduct.getNextStackSize());
			}
		}
	}
	
	@Override
	public ProcessorRecipeHandler getRecipeHandler() {
		return recipeType.getRecipeHandler();
	}
	
	@Override
	public ProcessorRecipe getRecipe() {
		return recipe;
	}
	
	@Override
	public List<Tank> getFluidInputs() {
		return tanks.subList(0, fluidInputSize);
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
				if (fluidIngredients.get(j).matches(getFluidInputs().get(i), IngredientSorption.INPUT)) {
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
	public boolean alternativeFluidToggle() {
		return true;
	}
	
	@Override
	public void toggleFluidConnection(@Nonnull EnumFacing side) {
		ITileFluid.super.toggleFluidConnection(side);
		updateFlowDir();
	}
	
	public void updateFlowDir() {
		for (EnumFacing side : EnumFacing.VALUES) {
			FluidConnection thisConnection = getFluidConnection(side);
			if (thisConnection == DISABLED) continue;
			
			TileEntity tile = getTileWorld().getTileEntity(getTilePos().offset(side));
			
			if (tile instanceof TileHeatExchangerVent) {
				if (thisConnection == DEFAULT) {
					flowDir = side.getOpposite();
					return;
				}
				else if (thisConnection == PRODUCT_OUT) {
					flowDir = side;
					return;
				}
			}
			else if (tile instanceof TileHeatExchangerTube) {
				TileHeatExchangerTube tube = (TileHeatExchangerTube)tile;
				FluidConnection tubeConnection = tube.getFluidConnection(side.getOpposite());
				
				if ((thisConnection == FLUID_OUT && tubeConnection == DEFAULT) || (thisConnection == PRODUCT_OUT && (tubeConnection == DEFAULT || tubeConnection == FLUID_OUT))) {
					flowDir = side;
					return;
				}
			}
		}
		
		flowDir = null;
	}
	
	@Override
	public void pushFluidToSide(@Nonnull EnumFacing side) {
		FluidConnection thisConnection = getFluidConnection(side);
		if (thisConnection == DISABLED) return;
		
		TileEntity tile = getTileWorld().getTileEntity(getTilePos().offset(side));
		
		if (tile instanceof TileHeatExchangerTube) {
			TileHeatExchangerTube tube = (TileHeatExchangerTube)tile;
			FluidConnection tubeConnection = tube.getFluidConnection(side.getOpposite());
			
			if (thisConnection == FLUID_OUT) {
				if (tubeConnection == DEFAULT) {
					pushInputFluid(tube);
					pushProduct(tube);
				} else if (tubeConnection == PRODUCT_OUT) {
					pushInputFluid(tube);
				}
			} else if (thisConnection == PRODUCT_OUT && (tubeConnection == DEFAULT || tubeConnection == FLUID_OUT)) {
				pushProduct(tube);
			}
		}
		
		else if (thisConnection == PRODUCT_OUT) {
			if (tile instanceof ITilePassive) if (!((ITilePassive) tile).canPushFluidsTo()) return;
			IFluidHandler adjStorage = tile == null ? null : tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite());
			
			if (adjStorage == null) return;
			
			for (int i = 0; i < getTanks().size(); i++) {
				if (getTanks().get(i).getFluid() == null || !getTanks().get(i).canDrain()) continue;
				
				getTanks().get(i).drainInternal(adjStorage.fill(getTanks().get(i).drainInternal(getTanks().get(i).getCapacity(), false), true), true);
			}
		}
	}
	
	public void pushInputFluid(TileHeatExchangerTube other) {
		if (getTanks().get(0).getFluidAmount() > other.getTanks().get(0).getFluidAmount()) {
			getTanks().get(0).drainInternal(other.getTanks().get(0).fillInternal(getTanks().get(0).drainInternal(getTanks().get(0).getFluidAmount() - fluidToHold, false), true), true);
		}
	}
	
	public void pushProduct(TileHeatExchangerTube other) {
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
		
		nbt.setDouble("time", time);
		nbt.setBoolean("isProcessing", isProcessing);
		nbt.setBoolean("canProcessInputs", canProcessInputs);
		nbt.setDouble("speedMultiplier", speedMultiplier);
		
		nbt.setInteger("fluidToHold", fluidToHold);
		
		nbt.setInteger("inputTemperature", inputTemperature);
		nbt.setInteger("outputTemperature", outputTemperature);
		
		nbt.setInteger("flowDir", flowDir == null ? -1 : flowDir.getIndex());
		return nbt;
	}
		
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		readTanks(nbt);
		readFluidConnections(nbt);
		
		baseProcessTime = nbt.getDouble("baseProcessTime");
		
		time = nbt.getDouble("time");
		isProcessing = nbt.getBoolean("isProcessing");
		canProcessInputs = nbt.getBoolean("canProcessInputs");
		speedMultiplier = nbt.getDouble("speedMultiplier");
		
		fluidToHold = nbt.getInteger("fluidToHold");
		
		inputTemperature = nbt.getInteger("inputTemperature");
		outputTemperature = nbt.getInteger("outputTemperature");
		
		flowDir = nbt.getInteger("flowDir") == -1 ? null : EnumFacing.VALUES[nbt.getInteger("flowDir")];
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
