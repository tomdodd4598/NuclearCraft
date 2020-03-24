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
import nc.multiblock.heatExchanger.HeatExchangerTubeSetting;
import nc.multiblock.heatExchanger.HeatExchangerTubeType;
import nc.recipe.AbstractRecipeHandler;
import nc.recipe.NCRecipes;
import nc.recipe.ProcessorRecipe;
import nc.recipe.ProcessorRecipeHandler;
import nc.recipe.RecipeInfo;
import nc.recipe.ingredient.IFluidIngredient;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.fluid.FluidConnection;
import nc.tile.internal.fluid.FluidTileWrapper;
import nc.tile.internal.fluid.GasTileWrapper;
import nc.tile.internal.fluid.Tank;
import nc.tile.internal.fluid.TankOutputSetting;
import nc.tile.internal.fluid.TankSorption;
import nc.tile.passive.ITilePassive;
import nc.tile.processor.IFluidProcessor;
import nc.util.GasHelper;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TileHeatExchangerCondenserTube extends TileHeatExchangerPartBase implements IFluidProcessor, ITileFluid {
	
	private final @Nonnull List<Tank> tanks = Lists.newArrayList(new Tank(128000, NCRecipes.condenser_valid_fluids.get(0)), new Tank(32000, new ArrayList<String>()));
	
	private @Nonnull FluidConnection[] fluidConnections = ITileFluid.fluidConnectionAll(Lists.newArrayList(TankSorption.NON, TankSorption.NON));
	
	private @Nonnull FluidTileWrapper[] fluidSides;
	
	private @Nonnull GasTileWrapper gasWrapper;
	
	private @Nonnull HeatExchangerTubeSetting[] tubeSettings = new HeatExchangerTubeSetting[] {HeatExchangerTubeSetting.DISABLED, HeatExchangerTubeSetting.DISABLED, HeatExchangerTubeSetting.DISABLED, HeatExchangerTubeSetting.DISABLED, HeatExchangerTubeSetting.DISABLED, HeatExchangerTubeSetting.DISABLED};
	
	public final int fluidInputSize = 1, fluidOutputSize = 1;
	
	public final int defaultProcessTime = 16000;
	public double baseProcessTime = defaultProcessTime;
	
	public double time;
	public boolean isProcessing, canProcessInputs;
	public double speedMultiplier = 0;
	
	public int condensingTemperature = 300;
	
	public int[] adjacentTemperatures = new int[] {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE};
	
	public static final ProcessorRecipeHandler RECIPE_HANDLER = NCRecipes.condenser;
	protected RecipeInfo<ProcessorRecipe> recipeInfo;
	
	public final double conductivity;
	
	protected int tubeCount;
	
	public static class Copper extends TileHeatExchangerCondenserTube {
		
		public Copper() {
			super(HeatExchangerTubeType.COPPER);
		}
	}
	
	public static class HardCarbon extends TileHeatExchangerCondenserTube {
		
		public HardCarbon() {
			super(HeatExchangerTubeType.HARD_CARBON);
		}
	}
	
	public static class Thermoconducting extends TileHeatExchangerCondenserTube {
		
		public Thermoconducting() {
			super(HeatExchangerTubeType.THERMOCONDUCTING);
		}
	}
	
	private TileHeatExchangerCondenserTube(HeatExchangerTubeType tubeType) {
		super(CuboidalPartPositionType.INTERIOR);
		fluidSides = ITileFluid.getDefaultFluidSides(this);
		gasWrapper = new GasTileWrapper(this);
		
		conductivity = tubeType.getConductivity();
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
			speedMultiplier = 0D;
			return 0;
		}
		
		int adjCount = 0;
		double speedCount = 0D;
		
		for (EnumFacing dir : EnumFacing.VALUES) {
			double mult = getTubeSpeedMultiplier(dir);
			speedCount += mult;
			if (mult > 0D) adjCount++;
		}
		
		speedMultiplier = speedCount;
		return adjCount;
	}
	
	private double getTubeSpeedMultiplier(EnumFacing dir) {
		if (adjacentTemperatures[dir.getIndex()] > condensingTemperature) {
			return 0D;
		}
		return conductivity*(1D + Math.log((double)condensingTemperature/(double)adjacentTemperatures[dir.getIndex()]));
	}
	
	public void updateAdjacentTemperatures() {
		for (EnumFacing dir : EnumFacing.VALUES) {
			Block block = world.getBlockState(pos.offset(dir)).getBlock();
			if (block == Blocks.WATER || block == Blocks.FLOWING_WATER) {
				adjacentTemperatures[dir.getIndex()] = 300;
			}
			else if (block == Blocks.LAVA || block == Blocks.FLOWING_LAVA) {
				adjacentTemperatures[dir.getIndex()] = 1300;
			}
			else if (block instanceof IFluidBlock && ((IFluidBlock)block).getFluid() != null) {
				adjacentTemperatures[dir.getIndex()] = Math.max(1, ((IFluidBlock)block).getFluid().getTemperature());
			}
			else {
				adjacentTemperatures[dir.getIndex()] = Integer.MAX_VALUE;
			}
		}
	}
	
	// Ticking
	
	@Override
	public void onAdded() {
		super.onAdded();
		if (!world.isRemote) {
			refreshRecipe();
			refreshActivity();
			isProcessing = isProcessing();
		}
	}
	
	@Override
	public void update() {
		super.update();
		updateTube();
	}
	
	public void updateTube() {
		if (!world.isRemote) {
			setIsHeatExchangerOn();
			boolean wasProcessing = isProcessing;
			isProcessing = isProcessing();
			boolean shouldUpdate = false;
			tickTube();
			if (isProcessing) process();
			if (wasProcessing != isProcessing) {
				shouldUpdate = true;
			}
			if (tubeCount == 0) {
				pushFluid();
				refreshRecipe();
				refreshActivity();
			}
			if (shouldUpdate) markDirty();
		}
	}
	
	public void tickTube() {
		tubeCount++; tubeCount %= NCConfig.machine_update_rate / 4;
	}
	
	@Override
	public void refreshRecipe() {
		recipeInfo = RECIPE_HANDLER.getRecipeInfoFromInputs(new ArrayList<ItemStack>(), getFluidInputs());
	}
	
	@Override
	public void refreshActivity() {
		canProcessInputs = canProcessInputs();
	}
	
	@Override
	public void refreshActivityOnProduction() {
		canProcessInputs = canProcessInputs();
	}
	
	// Processor Stats
	
	public double getSpeedMultiplier() {
		return speedMultiplier;
	}
	
	public boolean setRecipeStats() {
		if (recipeInfo == null) {
			baseProcessTime = defaultProcessTime;
			condensingTemperature = 300;
			return false;
		}
		baseProcessTime = recipeInfo.getRecipe().getCondenserProcessTime(defaultProcessTime);
		condensingTemperature = recipeInfo.getRecipe().getCondenserCondensingTemperature();
		return true;
	}
	
	// Processing
	
	public boolean isProcessing() {
		return readyToProcess() && isHeatExchangerOn;
	}
	
	public boolean readyToProcess() {
		return canProcessInputs && isMultiblockAssembled();
	}
	
	public boolean canProcessInputs() {
		boolean validRecipe = setRecipeStats(), canProcess = validRecipe && canProduceProducts();
		if (!canProcess) {
			time = MathHelper.clamp(time, 0D, baseProcessTime - 1D);
		}
		return canProcess;
	}
	
	public boolean canProduceProducts() {
		for (int j = 0; j < fluidOutputSize; j++) {
			IFluidIngredient fluidProduct = getFluidProducts().get(j);
			if (fluidProduct.getMaxStackSize(0) <= 0) continue;
			if (fluidProduct.getStack() == null) return false;
			else if (!tanks.get(j + fluidInputSize).isEmpty()) {
				if (!tanks.get(j + fluidInputSize).getFluid().isFluidEqual(fluidProduct.getStack())) {
					return false;
				} else if (tanks.get(j + fluidInputSize).getFluidAmount() + fluidProduct.getMaxStackSize(0) > tanks.get(j + fluidInputSize).getCapacity()) {
					return false;
				}
			}
		}
		return true;
	}
	
	public void process() {
		time = Math.max(0, time + getSpeedMultiplier());
		if (time >= baseProcessTime) finishProcess();
	}
	
	public void finishProcess() {
		double oldProcessTime = baseProcessTime;
		produceProducts();
		refreshRecipe();
		if (!setRecipeStats()) time = 0;
		else time = MathHelper.clamp(time - oldProcessTime, 0D, baseProcessTime);
		refreshActivityOnProduction();
		if (!canProcessInputs) time = 0;
	}
	
	public void produceProducts() {
		if (recipeInfo == null) return;
		List<Integer> fluidInputOrder = recipeInfo.getFluidInputOrder();
		if (fluidInputOrder == AbstractRecipeHandler.INVALID) return;
		
		for (int i = 0; i < fluidInputSize; i++) {
			int fluidIngredientStackSize = getFluidIngredients().get(fluidInputOrder.get(i)).getMaxStackSize(recipeInfo.getFluidIngredientNumbers().get(i));
			if (fluidIngredientStackSize > 0) tanks.get(i).changeFluidAmount(-fluidIngredientStackSize);
			if (tanks.get(i).getFluidAmount() <= 0) tanks.get(i).setFluidStored(null);
		}
		for (int j = 0; j < fluidOutputSize; j++) {
			IFluidIngredient fluidProduct = getFluidProducts().get(j);
			if (fluidProduct.getMaxStackSize(0) <= 0) continue;
			if (tanks.get(j + fluidInputSize).isEmpty()) {
				tanks.get(j + fluidInputSize).setFluidStored(fluidProduct.getNextStack(0));
			} else if (tanks.get(j + fluidInputSize).getFluid().isFluidEqual(fluidProduct.getStack())) {
				tanks.get(j + fluidInputSize).changeFluidAmount(fluidProduct.getNextStackSize(0));
			}
		}
	}
	
	// IProcessor
	
	@Override
	public List<Tank> getFluidInputs() {
		return tanks.subList(0, fluidInputSize);
	}
	
	@Override
	public List<IFluidIngredient> getFluidIngredients() {
		return recipeInfo.getRecipe().fluidIngredients();
	}
	
	@Override
	public List<IFluidIngredient> getFluidProducts() {
		return recipeInfo.getRecipe().fluidProducts();
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
	
	public @Nonnull HeatExchangerTubeSetting[] getTubeSettings() {
		return tubeSettings;
	}
	
	public void setTubeSettings(@Nonnull HeatExchangerTubeSetting[] settings) {
		tubeSettings = settings;
	}
	
	public HeatExchangerTubeSetting getTubeSetting(@Nonnull EnumFacing side) {
		return tubeSettings[side.getIndex()];
	}
	
	public void setTubeSetting(@Nonnull EnumFacing side, @Nonnull HeatExchangerTubeSetting setting) {
		tubeSettings[side.getIndex()] = setting;
	}
	
	public void toggleTubeSetting(@Nonnull EnumFacing side) {
		setTubeSetting(side, getTubeSetting(side).next());
		refreshFluidConnections(side);
		markDirtyAndNotify();
	}
	
	public void refreshFluidConnections(@Nonnull EnumFacing side) {
		switch (getTubeSetting(side)) {
		case DISABLED:
			setTankSorption(side, 0, TankSorption.NON);
			setTankSorption(side, 1, TankSorption.NON);
			break;
		case DEFAULT:
			setTankSorption(side, 0, TankSorption.IN);
			setTankSorption(side, 1, TankSorption.NON);
			break;
		case PRODUCT_OUT:
			setTankSorption(side, 0, TankSorption.NON);
			setTankSorption(side, 1, TankSorption.OUT);
			break;
		case INPUT_SPREAD:
			setTankSorption(side, 0, TankSorption.OUT);
			setTankSorption(side, 1, TankSorption.NON);
			break;
		default:
			setTankSorption(side, 0, TankSorption.NON);
			setTankSorption(side, 1, TankSorption.NON);
			break;
		}
	}
	
	@Override
	public void pushFluidToSide(@Nonnull EnumFacing side) {
		HeatExchangerTubeSetting thisSetting = getTubeSetting(side);
		if (thisSetting == HeatExchangerTubeSetting.DISABLED) return;
		
		TileEntity tile = getTileWorld().getTileEntity(getTilePos().offset(side));
		
		if (tile instanceof TileHeatExchangerCondenserTube) {
			TileHeatExchangerCondenserTube tube = (TileHeatExchangerCondenserTube)tile;
			HeatExchangerTubeSetting tubeSetting = tube.getTubeSetting(side.getOpposite());
			
			if (thisSetting == HeatExchangerTubeSetting.INPUT_SPREAD) {
				if (tubeSetting == HeatExchangerTubeSetting.DEFAULT) {
					pushInputFluid(tube);
					pushProduct(tube);
				} else if (tubeSetting == HeatExchangerTubeSetting.PRODUCT_OUT) {
					pushInputFluid(tube);
				}
			} else if (thisSetting == HeatExchangerTubeSetting.PRODUCT_OUT && (tubeSetting == HeatExchangerTubeSetting.DEFAULT || tubeSetting == HeatExchangerTubeSetting.INPUT_SPREAD)) {
				pushProduct(tube);
			}
		}
		
		else if (thisSetting == HeatExchangerTubeSetting.PRODUCT_OUT) {
			if (tile instanceof ITilePassive) if (!((ITilePassive) tile).canPushFluidsTo()) return;
			IFluidHandler adjStorage = tile == null ? null : tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite());
			
			if (adjStorage == null) return;
			
			for (int i = 0; i < getTanks().size(); i++) {
				if (getTanks().get(i).getFluid() == null || !getTankSorption(side, i).canDrain()) continue;
				
				getTanks().get(i).drain(adjStorage.fill(getTanks().get(i).drain(getTanks().get(i).getCapacity(), false), true), true);
			}
		}
	}
	
	public void pushInputFluid(TileHeatExchangerCondenserTube other) {
		int diff = getTanks().get(0).getFluidAmount() - other.getTanks().get(0).getFluidAmount();
		if (diff > 1) {
			getTanks().get(0).drain(other.getTanks().get(0).fillInternal(getTanks().get(0).drain((int)(diff*NCConfig.heat_exchanger_condenser_tube_spread_ratio), false), true), true);
		}
	}
	
	public void pushProduct(TileHeatExchangerCondenserTube other) {
		getTanks().get(1).drain(other.getTanks().get(1).fillInternal(getTanks().get(1).drain(getTanks().get(1).getCapacity(), false), true), true);
	}

	@Override
	public boolean getInputTanksSeparated() {
		return false;
	}

	@Override
	public void setInputTanksSeparated(boolean separated) {}

	@Override
	public boolean getVoidUnusableFluidInput(int tankNumber) {
		return false;
	}

	@Override
	public void setVoidUnusableFluidInput(int tankNumber, boolean voidUnusableFluidInput) {}

	@Override
	public TankOutputSetting getTankOutputSetting(int tankNumber) {
		return TankOutputSetting.DEFAULT;
	}
	
	@Override
	public void setTankOutputSetting(int tankNumber, TankOutputSetting setting) {}
	
	@Override
	public boolean hasConfigurableFluidConnections() {
		return true;
	}
	
	// NBT
	
	public NBTTagCompound writeTubeSettings(NBTTagCompound nbt) {
		NBTTagCompound settingsTag = new NBTTagCompound();
		for (EnumFacing side : EnumFacing.VALUES) {
			settingsTag.setInteger("setting" + side.getIndex(), getTubeSetting(side).ordinal());
		}
		nbt.setTag("tubeSettings", settingsTag);
		return nbt;
	}
	
	public void readTubeSettings(NBTTagCompound nbt) {
		if (nbt.hasKey("fluidConnections0")) {
			for (EnumFacing side : EnumFacing.VALUES) {
				TankSorption sorption = TankSorption.values()[nbt.getInteger("fluidConnections" + side.getIndex())];
				switch (sorption) {
				case NON:
					setTankSorption(side, 0, TankSorption.NON);
					setTankSorption(side, 1, TankSorption.NON);
					setTubeSetting(side, HeatExchangerTubeSetting.DISABLED);
					break;
				case BOTH:
					setTankSorption(side, 0, TankSorption.IN);
					setTankSorption(side, 1, TankSorption.NON);
					setTubeSetting(side, HeatExchangerTubeSetting.DEFAULT);
					break;
				case IN:
					setTankSorption(side, 0, TankSorption.NON);
					setTankSorption(side, 1, TankSorption.OUT);
					setTubeSetting(side, HeatExchangerTubeSetting.PRODUCT_OUT);
					break;
				case OUT:
					setTankSorption(side, 0, TankSorption.OUT);
					setTankSorption(side, 1, TankSorption.NON);
					setTubeSetting(side, HeatExchangerTubeSetting.INPUT_SPREAD);
					break;
				default:
					setTankSorption(side, 0, TankSorption.NON);
					setTankSorption(side, 1, TankSorption.NON);
					setTubeSetting(side, HeatExchangerTubeSetting.DISABLED);
					break;
				}
			}
		}
		else {
			NBTTagCompound settingsTag = nbt.getCompoundTag("tubeSettings");
			for (EnumFacing side : EnumFacing.VALUES) {
				setTubeSetting(side, HeatExchangerTubeSetting.values()[settingsTag.getInteger("setting" + side.getIndex())]);
				refreshFluidConnections(side);
			}
		}
	}
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		writeTanks(nbt);
		writeTubeSettings(nbt);
		
		nbt.setDouble("baseProcessTime", baseProcessTime);
		
		nbt.setDouble("time", time);
		nbt.setBoolean("isProcessing", isProcessing);
		nbt.setBoolean("canProcessInputs", canProcessInputs);
		nbt.setDouble("speedMultiplier", speedMultiplier);
		
		nbt.setInteger("condensingTemperature", condensingTemperature);
		
		for (EnumFacing side : EnumFacing.VALUES) {
			nbt.setInteger("adjacentTemperature" + side.getIndex(), adjacentTemperatures[side.getIndex()]);
		}
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		readTanks(nbt);
		readTubeSettings(nbt);
		
		baseProcessTime = nbt.getDouble("baseProcessTime");
		
		time = nbt.getDouble("time");
		isProcessing = nbt.getBoolean("isProcessing");
		canProcessInputs = nbt.getBoolean("canProcessInputs");
		speedMultiplier = nbt.getDouble("speedMultiplier");
		
		condensingTemperature = nbt.getInteger("condensingTemperature");
		
		for (EnumFacing side : EnumFacing.VALUES) {
			adjacentTemperatures[side.getIndex()] = nbt.getInteger("adjacentTemperature" + side.getIndex());
		}
	}
	
	// Capability
	
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || (ModCheck.mekanismLoaded() && NCConfig.enable_mek_gas && capability == GasHelper.GAS_HANDLER_CAPABILITY)) {
			return !getTanks().isEmpty() && hasFluidSideCapability(side);
		}
		return super.hasCapability(capability, side);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			if (!getTanks().isEmpty() && hasFluidSideCapability(side)) {
				return (T) getFluidSide(nonNullSide(side));
			}
			return null;
		}
		else if (ModCheck.mekanismLoaded() && capability == GasHelper.GAS_HANDLER_CAPABILITY) {
			if (NCConfig.enable_mek_gas && !getTanks().isEmpty() && hasFluidSideCapability(side)) {
				return (T) getGasWrapper();
			}
			return null;
		}
		return super.getCapability(capability, side);
	}
}
