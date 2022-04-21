package nc.multiblock.heatExchanger.tile;

import static nc.config.NCConfig.enable_mek_gas;

import java.util.*;

import javax.annotation.*;

import com.google.common.collect.Lists;

import it.unimi.dsi.fastutil.ints.IntList;
import nc.ModCheck;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.heatExchanger.*;
import nc.recipe.*;
import nc.recipe.ingredient.IFluidIngredient;
import nc.tile.fluid.ITileFluid;
import nc.tile.internal.fluid.*;
import nc.tile.passive.ITilePassive;
import nc.tile.processor.IFluidProcessor;
import nc.util.CapabilityHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.capability.*;

public class TileHeatExchangerTube extends TileHeatExchangerPart implements IFluidProcessor {
	
	protected final @Nonnull List<Tank> tanks = Lists.newArrayList(new Tank(32000, NCRecipes.heat_exchanger_valid_fluids.get(0)), new Tank(64000, new ArrayList<>()));
	
	protected @Nonnull FluidConnection[] fluidConnections = ITileFluid.fluidConnectionAll(Lists.newArrayList(TankSorption.NON, TankSorption.NON));
	
	protected @Nonnull FluidTileWrapper[] fluidSides;
	
	protected @Nonnull GasTileWrapper gasWrapper;
	
	protected @Nonnull HeatExchangerTubeSetting[] tubeSettings = new HeatExchangerTubeSetting[] {HeatExchangerTubeSetting.DISABLED, HeatExchangerTubeSetting.DISABLED, HeatExchangerTubeSetting.DISABLED, HeatExchangerTubeSetting.DISABLED, HeatExchangerTubeSetting.DISABLED, HeatExchangerTubeSetting.DISABLED};
	
	protected final int fluidInputSize = 1, fluidOutputSize = 1;
	
	public double baseProcessTime = 16000D;
	
	public double time;
	public boolean isProcessing, canProcessInputs;
	public double speedMultiplier = 0;
	
	public int inputTemperature = 300, outputTemperature = 300;
	public EnumFacing flowDir = null;
	
	protected RecipeInfo<BasicRecipe> recipeInfo;
	
	public final double conductivity;
	
	// protected int tubeCount;
	
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
	
	protected TileHeatExchangerTube(HeatExchangerTubeType tubeType) {
		super(CuboidalPartPositionType.INTERIOR);
		fluidSides = ITileFluid.getDefaultFluidSides(this);
		gasWrapper = new GasTileWrapper(this);
		
		conductivity = tubeType.getConductivity();
	}
	
	@Override
	public void onMachineAssembled(HeatExchanger controller) {
		doStandardNullControllerResponse(controller);
		super.onMachineAssembled(controller);
	}
	
	@Override
	public void onMachineBroken() {
		super.onMachineBroken();
	}
	
	public int[] checkPosition() {
		boolean canProcess = isMultiblockAssembled() && canProcessInputs;
		int adjRealCount = 0, adjMaxCount = 0;
		double speedCount = 0D;
		
		for (EnumFacing dir : EnumFacing.VALUES) {
			SpeedMultiplierInfo info = getTubeSpeedMultiplier(dir);
			speedCount += info.multiplier;
			if (info.multiplier > 0D) {
				++adjRealCount;
			}
			if (info.contributeMax) {
				++adjMaxCount;
			}
		}
		
		speedMultiplier = canProcess ? speedCount : 0D;
		return new int[] {canProcess ? adjRealCount : 0, adjMaxCount};
	}
	
	private SpeedMultiplierInfo getTubeSpeedMultiplier(EnumFacing dir) {
		TileEntity tile = world.getTileEntity(pos.offset(dir));
		if (!(tile instanceof TileHeatExchangerTube)) {
			return new SpeedMultiplierInfo(0D, false);
		}
		TileHeatExchangerTube tube = (TileHeatExchangerTube) tile;
		
		boolean tubeActive = tube.canProcessInputs && (!requiresContraflow(tube) || isContraflow(tube));
		
		if (!canConnectFluid(dir) || !tube.canConnectFluid(dir.getOpposite())) {
			return new SpeedMultiplierInfo(tubeActive ? conductivityMult() * getRawTubeSpeedMultiplier(tube) : 0D, true);
		}
		return new SpeedMultiplierInfo(0D, false);
	}
	
	private static class SpeedMultiplierInfo {
		
		final double multiplier;
		final boolean contributeMax;
		
		SpeedMultiplierInfo(double multiplier, boolean contributeMax) {
			this.multiplier = multiplier;
			this.contributeMax = contributeMax;
		}
	}
	
	private double getRawTubeSpeedMultiplier(TileHeatExchangerTube tube) {
		return isHeating() != tube.isHeating() ? tube.getAbsRecipeTempDiff() : isHeating() ? -getAbsInputTempDiff(tube) : 0D;
	}
	
	private boolean isContraflow(TileHeatExchangerTube tube) {
		if (flowDir == null || tube.flowDir == null) {
			return !(flowDir == null ^ tube.flowDir == null);
		}
		return flowDir.getIndex() != tube.flowDir.getIndex();
	}
	
	private boolean requiresContraflow(TileHeatExchangerTube tube) {
		return inputTemperature > tube.inputTemperature ^ outputTemperature > tube.outputTemperature;
	}
	
	// Ticking
	
	@Override
	public void onLoad() {
		super.onLoad();
		if (!world.isRemote) {
			refreshRecipe();
			refreshActivity();
			isProcessing = isProcessing();
		}
	}
	
	@Override
	public void update() {
		if (!world.isRemote) {
			setIsHeatExchangerOn();
			boolean wasProcessing = isProcessing;
			isProcessing = isProcessing();
			boolean shouldUpdate = false;
			if (isProcessing) {
				process();
			}
			if (wasProcessing != isProcessing) {
				shouldUpdate = true;
			}
			if (shouldUpdate) {
				markDirty();
			}
		}
	}
	
	@Override
	public void refreshRecipe() {
		recipeInfo = NCRecipes.heat_exchanger.getRecipeInfoFromInputs(new ArrayList<>(), getFluidInputs());
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
	
	private int getAbsRecipeTempDiff() {
		return Math.abs(inputTemperature - outputTemperature);
	}
	
	private int getAbsInputTempDiff(TileHeatExchangerTube tube) {
		return Math.abs(inputTemperature - tube.inputTemperature);
	}
	
	private double conductivityMult() {
		return isHeating() ? conductivity : 1D / conductivity;
	}
	
	private boolean isHeating() {
		return inputTemperature < outputTemperature;
	}
	
	public boolean setRecipeStats() {
		if (recipeInfo == null) {
			baseProcessTime = 16000D;
			inputTemperature = 300;
			outputTemperature = 300;
			return false;
		}
		BasicRecipe recipe = recipeInfo.getRecipe();
		baseProcessTime = recipe.getHeatExchangerProcessTime();
		inputTemperature = recipe.getHeatExchangerInputTemperature();
		outputTemperature = recipe.getHeatExchangerOutputTemperature();
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
		for (int j = 0; j < fluidOutputSize; ++j) {
			IFluidIngredient fluidProduct = getFluidProducts().get(j);
			if (fluidProduct.getMaxStackSize(0) <= 0) {
				continue;
			}
			if (fluidProduct.getStack() == null) {
				return false;
			}
			else if (!tanks.get(j + fluidInputSize).isEmpty()) {
				if (!tanks.get(j + fluidInputSize).getFluid().isFluidEqual(fluidProduct.getStack())) {
					return false;
				}
				else if (tanks.get(j + fluidInputSize).getFluidAmount() + fluidProduct.getMaxStackSize(0) > tanks.get(j + fluidInputSize).getCapacity()) {
					return false;
				}
			}
		}
		return true;
	}
	
	public void process() {
		time = Math.max(0, time + getSpeedMultiplier());
		while (time >= baseProcessTime) {
			finishProcess();
		}
	}
	
	public void finishProcess() {
		double oldProcessTime = baseProcessTime;
		produceProducts();
		refreshRecipe();
		time = Math.max(0D, time - oldProcessTime);
		refreshActivityOnProduction();
		if (!canProcessInputs) {
			time = 0;
		}
	}
	
	public void produceProducts() {
		if (recipeInfo == null) {
			return;
		}
		IntList fluidInputOrder = recipeInfo.getFluidInputOrder();
		if (fluidInputOrder == AbstractRecipeHandler.INVALID) {
			return;
		}
		
		for (int i = 0; i < fluidInputSize; ++i) {
			int fluidIngredientStackSize = getFluidIngredients().get(fluidInputOrder.get(i)).getMaxStackSize(recipeInfo.getFluidIngredientNumbers().get(i));
			if (fluidIngredientStackSize > 0) {
				tanks.get(i).changeFluidAmount(-fluidIngredientStackSize);
			}
			if (tanks.get(i).getFluidAmount() <= 0) {
				tanks.get(i).setFluidStored(null);
			}
		}
		for (int j = 0; j < fluidOutputSize; ++j) {
			IFluidIngredient fluidProduct = getFluidProducts().get(j);
			if (fluidProduct.getMaxStackSize(0) <= 0) {
				continue;
			}
			if (tanks.get(j + fluidInputSize).isEmpty()) {
				tanks.get(j + fluidInputSize).setFluidStored(fluidProduct.getNextStack(0));
			}
			else if (tanks.get(j + fluidInputSize).getFluid().isFluidEqual(fluidProduct.getStack())) {
				tanks.get(j + fluidInputSize).changeFluidAmount(fluidProduct.getNextStackSize(0));
			}
		}
	}
	
	// IProcessor
	
	@Override
	public int getFluidInputSize() {
		return fluidInputSize;
	}
	
	@Override
	public int getFluidOutputputSize() {
		return fluidOutputSize;
	}
	
	@Override
	public List<Tank> getFluidInputs() {
		return tanks.subList(0, fluidInputSize);
	}
	
	@Override
	public List<IFluidIngredient> getFluidIngredients() {
		return recipeInfo.getRecipe().getFluidIngredients();
	}
	
	@Override
	public List<IFluidIngredient> getFluidProducts() {
		return recipeInfo.getRecipe().getFluidProducts();
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
		updateFlowDir();
		markDirtyAndNotify(true);
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
	
	public void updateFlowDir() {
		for (EnumFacing side : EnumFacing.VALUES) {
			HeatExchangerTubeSetting thisSetting = getTubeSetting(side);
			if (thisSetting == HeatExchangerTubeSetting.DISABLED) {
				continue;
			}
			
			TileEntity tile = getTileWorld().getTileEntity(getTilePos().offset(side));
			
			if (tile instanceof TileHeatExchangerVent && thisSetting == HeatExchangerTubeSetting.PRODUCT_OUT) {
				flowDir = side;
				return;
			}
			else if (tile instanceof TileHeatExchangerTube) {
				TileHeatExchangerTube tube = (TileHeatExchangerTube) tile;
				HeatExchangerTubeSetting tubeSetting = tube.getTubeSetting(side.getOpposite());
				
				if (thisSetting == HeatExchangerTubeSetting.INPUT_SPREAD && tubeSetting == HeatExchangerTubeSetting.DEFAULT || thisSetting == HeatExchangerTubeSetting.PRODUCT_OUT && (tubeSetting == HeatExchangerTubeSetting.DEFAULT || tubeSetting == HeatExchangerTubeSetting.INPUT_SPREAD)) {
					flowDir = side;
					return;
				}
			}
		}
		
		flowDir = null;
	}
	
	@Override
	public void pushFluidToSide(@Nonnull EnumFacing side) {
		HeatExchangerTubeSetting thisSetting = getTubeSetting(side);
		if (thisSetting == HeatExchangerTubeSetting.DISABLED) {
			return;
		}
		
		TileEntity tile = getTileWorld().getTileEntity(getTilePos().offset(side));
		
		if (tile instanceof TileHeatExchangerTube) {
			TileHeatExchangerTube tube = (TileHeatExchangerTube) tile;
			HeatExchangerTubeSetting tubeSetting = tube.getTubeSetting(side.getOpposite());
			
			if (thisSetting == HeatExchangerTubeSetting.INPUT_SPREAD) {
				if (tubeSetting == HeatExchangerTubeSetting.DEFAULT) {
					pushInputFluid(tube);
					pushProduct(tube);
				}
				else if (tubeSetting == HeatExchangerTubeSetting.PRODUCT_OUT) {
					pushInputFluid(tube);
				}
			}
			else if (thisSetting == HeatExchangerTubeSetting.PRODUCT_OUT && (tubeSetting == HeatExchangerTubeSetting.DEFAULT || tubeSetting == HeatExchangerTubeSetting.INPUT_SPREAD)) {
				pushProduct(tube);
			}
		}
		
		else if (thisSetting == HeatExchangerTubeSetting.PRODUCT_OUT) {
			if (tile instanceof ITilePassive) {
				if (!((ITilePassive) tile).canPushFluidsTo()) {
					return;
				}
			}
			IFluidHandler adjStorage = tile == null ? null : tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite());
			
			if (adjStorage == null) {
				return;
			}
			
			for (int i = 0; i < getTanks().size(); ++i) {
				Tank tank = getTanks().get(i);
				if (tank.getFluid() == null || !getTankSorption(side, i).canDrain()) {
					continue;
				}
				
				tank.drain(adjStorage.fill(tank.drain(tank.getCapacity(), false), true), true);
			}
		}
	}
	
	public void pushInputFluid(TileHeatExchangerTube other) {
		int diff = getTanks().get(0).getFluidAmount() - other.getTanks().get(0).getFluidAmount();
		if (diff > 1) {
			getTanks().get(0).drain(other.getTanks().get(0).fillInternal(getTanks().get(0).drain(diff / 2, false), true), true);
		}
	}
	
	public void pushProduct(TileHeatExchangerTube other) {
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
		
		nbt.setInteger("inputTemperature", inputTemperature);
		nbt.setInteger("outputTemperature", outputTemperature);
		
		nbt.setInteger("flowDir", flowDir == null ? -1 : flowDir.getIndex());
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
		
		inputTemperature = nbt.getInteger("inputTemperature");
		outputTemperature = nbt.getInteger("outputTemperature");
		
		flowDir = nbt.getInteger("flowDir") == -1 ? null : EnumFacing.VALUES[nbt.getInteger("flowDir")];
	}
	
	// Capability
	
	@Override
	public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing side) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || ModCheck.mekanismLoaded() && enable_mek_gas && capability == CapabilityHelper.GAS_HANDLER_CAPABILITY) {
			return !getTanks().isEmpty() && hasFluidSideCapability(side);
		}
		return super.hasCapability(capability, side);
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing side) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			if (!getTanks().isEmpty() && hasFluidSideCapability(side)) {
				return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(getFluidSide(nonNullSide(side)));
			}
			return null;
		}
		else if (ModCheck.mekanismLoaded() && capability == CapabilityHelper.GAS_HANDLER_CAPABILITY) {
			if (enable_mek_gas && !getTanks().isEmpty() && hasFluidSideCapability(side)) {
				return CapabilityHelper.GAS_HANDLER_CAPABILITY.cast(getGasWrapper());
			}
			return null;
		}
		return super.getCapability(capability, side);
	}
}
