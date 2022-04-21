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
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.capability.*;

public class TileCondenserTube extends TileHeatExchangerPart implements IFluidProcessor {
	
	protected final @Nonnull List<Tank> tanks = Lists.newArrayList(new Tank(32000, NCRecipes.condenser_valid_fluids.get(0)), new Tank(64000, new ArrayList<>()));
	
	protected @Nonnull FluidConnection[] fluidConnections = ITileFluid.fluidConnectionAll(Lists.newArrayList(TankSorption.NON, TankSorption.NON));
	
	protected @Nonnull FluidTileWrapper[] fluidSides;
	
	protected @Nonnull GasTileWrapper gasWrapper;
	
	protected @Nonnull HeatExchangerTubeSetting[] tubeSettings = new HeatExchangerTubeSetting[] {HeatExchangerTubeSetting.DISABLED, HeatExchangerTubeSetting.DISABLED, HeatExchangerTubeSetting.DISABLED, HeatExchangerTubeSetting.DISABLED, HeatExchangerTubeSetting.DISABLED, HeatExchangerTubeSetting.DISABLED};
	
	protected final int fluidInputSize = 1, fluidOutputSize = 1;
	
	public double baseProcessTime = 40D;
	
	public double time;
	public boolean isProcessing, canProcessInputs;
	public double speedMultiplier = 0;
	
	public int condensingTemperature = 300;
	
	public int[] adjacentTemperatures = new int[] {Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE};
	
	protected RecipeInfo<BasicRecipe> recipeInfo;
	
	public final double conductivity;
	
	// protected int tubeCount;
	
	public static class Copper extends TileCondenserTube {
		
		public Copper() {
			super(HeatExchangerTubeType.COPPER);
		}
	}
	
	public static class HardCarbon extends TileCondenserTube {
		
		public HardCarbon() {
			super(HeatExchangerTubeType.HARD_CARBON);
		}
	}
	
	public static class Thermoconducting extends TileCondenserTube {
		
		public Thermoconducting() {
			super(HeatExchangerTubeType.THERMOCONDUCTING);
		}
	}
	
	protected TileCondenserTube(HeatExchangerTubeType tubeType) {
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
			if (mult > 0D) {
				++adjCount;
			}
		}
		
		speedMultiplier = speedCount;
		return adjCount;
	}
	
	private double getTubeSpeedMultiplier(EnumFacing dir) {
		if (adjacentTemperatures[dir.getIndex()] > condensingTemperature) {
			return 0D;
		}
		return conductivity * (1D + Math.log((double) condensingTemperature / (double) adjacentTemperatures[dir.getIndex()]));
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
			else if (block instanceof IFluidBlock && ((IFluidBlock) block).getFluid() != null) {
				adjacentTemperatures[dir.getIndex()] = Math.max(1, ((IFluidBlock) block).getFluid().getTemperature());
			}
			else {
				adjacentTemperatures[dir.getIndex()] = Integer.MAX_VALUE;
			}
		}
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
		recipeInfo = NCRecipes.condenser.getRecipeInfoFromInputs(new ArrayList<>(), getFluidInputs());
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
			baseProcessTime = 40D;
			condensingTemperature = 300;
			return false;
		}
		BasicRecipe recipe = recipeInfo.getRecipe();
		baseProcessTime = recipe.getCondenserProcessTime();
		condensingTemperature = recipe.getCondenserCondensingTemperature();
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
	
	@Override
	public void pushFluidToSide(@Nonnull EnumFacing side) {
		HeatExchangerTubeSetting thisSetting = getTubeSetting(side);
		if (thisSetting == HeatExchangerTubeSetting.DISABLED) {
			return;
		}
		
		TileEntity tile = getTileWorld().getTileEntity(getTilePos().offset(side));
		
		if (tile instanceof TileCondenserTube) {
			TileCondenserTube tube = (TileCondenserTube) tile;
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
	
	public void pushInputFluid(TileCondenserTube other) {
		int diff = getTanks().get(0).getFluidAmount() - other.getTanks().get(0).getFluidAmount();
		if (diff > 1) {
			getTanks().get(0).drain(other.getTanks().get(0).fillInternal(getTanks().get(0).drain(diff / 2, false), true), true);
		}
	}
	
	public void pushProduct(TileCondenserTube other) {
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
