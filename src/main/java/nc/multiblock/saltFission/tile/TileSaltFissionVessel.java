package nc.multiblock.saltFission.tile;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import nc.Global;
import nc.ModCheck;
import nc.capability.radiation.source.IRadiationSource;
import nc.config.NCConfig;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.saltFission.SaltFissionReactor;
import nc.multiblock.saltFission.SaltFissionVesselSetting;
import nc.radiation.RadiationHelper;
import nc.recipe.AbstractRecipeHandler;
import nc.recipe.NCRecipes;
import nc.recipe.ProcessorRecipe;
import nc.recipe.ProcessorRecipeHandler;
import nc.recipe.RecipeInfo;
import nc.recipe.ingredient.IFluidIngredient;
import nc.tile.fluid.ITileFluid;
import nc.tile.generator.IFluidGenerator;
import nc.tile.internal.fluid.FluidConnection;
import nc.tile.internal.fluid.FluidTileWrapper;
import nc.tile.internal.fluid.GasTileWrapper;
import nc.tile.internal.fluid.Tank;
import nc.tile.internal.fluid.TankOutputSetting;
import nc.tile.internal.fluid.TankSorption;
import nc.tile.passive.ITilePassive;
import nc.util.FluidStackHelper;
import nc.util.GasHelper;
import nc.util.RegistryHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TileSaltFissionVessel extends TileSaltFissionPartBase implements IFluidGenerator, ITileFluid {
	
	private final @Nonnull List<Tank> tanks = Lists.newArrayList(new Tank(FluidStackHelper.INGOT_BLOCK_VOLUME*2, NCRecipes.salt_fission_valid_fluids.get(0)), new Tank(FluidStackHelper.INGOT_BLOCK_VOLUME*4, new ArrayList<String>()), new Tank(FluidStackHelper.INGOT_BLOCK_VOLUME*2, new ArrayList<String>()));
	
	private @Nonnull FluidConnection[] fluidConnections = ITileFluid.fluidConnectionAll(Lists.newArrayList(TankSorption.NON, TankSorption.NON, TankSorption.NON));
	
	private @Nonnull FluidTileWrapper[] fluidSides;
	
	private @Nonnull GasTileWrapper gasWrapper;
	
	private @Nonnull SaltFissionVesselSetting[] vesselSettings = new SaltFissionVesselSetting[] {SaltFissionVesselSetting.DISABLED, SaltFissionVesselSetting.DISABLED, SaltFissionVesselSetting.DISABLED, SaltFissionVesselSetting.DISABLED, SaltFissionVesselSetting.DISABLED, SaltFissionVesselSetting.DISABLED};
	
	public final int fluidInputSize = 1, fluidOutputSize = 1;
	
	public double baseProcessTime = 1, baseProcessHeat = 0, baseProcessRadiation = 0;
	private int baseVesselEfficiency, baseVesselMaxEfficiency;
	private double moderatorExtraEfficiency, moderatorMaxExtraEfficiency, moderatorHeatFactor, moderatorMaxHeatFactor;
	
	public boolean distributedTo = false, retrievedFrom = false;
	
	public double time;
	public boolean isProcessing, hasConsumed, canProcessInputs;
	
	public static final ProcessorRecipeHandler RECIPE_HANDLER = NCRecipes.salt_fission;
	protected RecipeInfo<ProcessorRecipe> recipeInfo;
	
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
		int newEfficiency = 1, newMaxEfficiency = 1;
		dirLoop: for (EnumFacing dir : EnumFacing.VALUES) {
			PartCheckInfo info = isVessel(dir, 1);
			if (info.partValid) {
				if (info.partActive) newEfficiency++;
				newMaxEfficiency++;
			}
			else for (int i = 1; i <= NCConfig.fission_neutron_reach; i++) {
				if (isModerator(dir, i, false)) {
					info = isVessel(dir, i + 1);
					if (info.partValid) {
						if (info.partActive) newEfficiency++;
						newMaxEfficiency++;
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
		baseVesselMaxEfficiency = newMaxEfficiency;
		moderatorExtraEfficiency = newEfficiency*moderatorCount*NCConfig.fission_moderator_extra_power/6D;
		moderatorMaxExtraEfficiency = newMaxEfficiency*moderatorCount*NCConfig.fission_moderator_extra_power/6D;
		moderatorHeatFactor = newEfficiency*moderatorCount*NCConfig.fission_moderator_extra_heat/6D;
		moderatorMaxHeatFactor = newMaxEfficiency*moderatorCount*NCConfig.fission_moderator_extra_heat/6D;
	}
	
	private PartCheckInfo isVessel(EnumFacing dir, int offset) {
		TileEntity tile = world.getTileEntity(pos.offset(dir, offset));
		if (!(tile instanceof TileSaltFissionVessel)) return new PartCheckInfo(false, false);
		TileSaltFissionVessel vessel = (TileSaltFissionVessel) tile;
		return new PartCheckInfo(vessel.isMultiblockAssembled(), vessel.canProcessInputs && vessel.isMultiblockAssembled());
	}
	
	private boolean isModerator(EnumFacing dir, int offset, boolean isInValidPosition) {
		TileEntity tile = world.getTileEntity(pos.offset(dir, offset));
		if (!(tile instanceof TileSaltFissionModerator)) return false;
		TileSaltFissionModerator moderator = (TileSaltFissionModerator) tile;
		moderator.isInModerationLine = true;
		if (isInValidPosition && canProcessInputs) moderator.isInValidPosition = true;
		return true;
		
	}
	
	private static class PartCheckInfo {
		
		final boolean partValid, partActive;
		
		PartCheckInfo(boolean partValid, boolean partActive) {
			this.partValid = partValid;
			this.partActive = partActive;
		}
	}
	
	public void doMeltdown() {
		IRadiationSource chunkSource = RadiationHelper.getRadiationSource(world.getChunk(pos));
		if (chunkSource != null) {
			RadiationHelper.addToSourceRadiation(chunkSource, 8D*baseProcessRadiation*getSpeedMultiplier()*NCConfig.salt_fission_meltdown_radiation_multiplier);
		}
		
		IBlockState corium = RegistryHelper.getBlock(Global.MOD_ID + ":fluid_corium").getDefaultState();
		world.removeTileEntity(pos);
		world.setBlockState(pos, corium);
		
		if (getMultiblock() != null) {
			for (EnumFacing dir : EnumFacing.VALUES) {
				BlockPos offPos = pos.offset(dir);
				if (getMultiblock().rand.nextDouble() < 0.75D) {
					world.removeTileEntity(offPos);
					world.setBlockState(offPos, corium);
				}
			}
		}
	}
	
	// Processing
	
	public double getEfficiency() {
		return baseVesselEfficiency + moderatorExtraEfficiency;
	}
	
	public double getMaxEfficiency() {
		return baseVesselMaxEfficiency + moderatorMaxExtraEfficiency;
	}
	
	public double getHeatMultiplier() {
		return baseVesselEfficiency*(baseVesselEfficiency + 1D)*0.5D + moderatorHeatFactor;
	}
	
	public double getMaxHeatMultiplier() {
		return baseVesselMaxEfficiency*(baseVesselMaxEfficiency + 1D)*0.5D + moderatorMaxHeatFactor;
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
		recipeInfo = RECIPE_HANDLER.getRecipeInfoFromInputs(new ArrayList<ItemStack>(), getFluidInputs(hasConsumed));
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
	
	public double getSpeedMultiplier() {
		return NCConfig.salt_fission_fuel_use;
	}
	
	public boolean setRecipeStats() {
		if (recipeInfo == null) {
			baseProcessTime = 1D;
			baseProcessHeat = 0D;
			baseProcessRadiation = 0D;
			return false;
		}
		baseProcessTime = recipeInfo.getRecipe().getSaltFissionFuelTime();
		baseProcessHeat = recipeInfo.getRecipe().getSaltFissionFuelHeat();
		baseProcessRadiation = recipeInfo.getRecipe().getSaltFissionFuelRadiation();
		return true;
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
		
	public void consumeInputs() {
		if (hasConsumed || recipeInfo == null) return;
		List<Integer> fluidInputOrder = recipeInfo.getFluidInputOrder();
		if (fluidInputOrder == AbstractRecipeHandler.INVALID) return;
		
		for (int i = 0; i < fluidInputSize; i++) {
			if (!tanks.get(i + fluidInputSize + fluidOutputSize).isEmpty()) {
				tanks.get(i + fluidInputSize + fluidOutputSize).setFluid(null);
			}
		}
		for (int i = 0; i < fluidInputSize; i++) {
			int maxStackSize = getFluidIngredients().get(fluidInputOrder.get(i)).getMaxStackSize(recipeInfo.getFluidIngredientNumbers().get(i));
			if (maxStackSize > 0) {
				tanks.get(i + fluidInputSize + fluidOutputSize).setFluidStored(new FluidStack(tanks.get(i).getFluid(), maxStackSize));
				tanks.get(i).changeFluidAmount(-maxStackSize);
			}
			if (tanks.get(i).isEmpty()) tanks.get(i).setFluid(null);
		}
		hasConsumed = true;
	}
	
	public void process() {
		time += getSpeedMultiplier();
		getRadiationSource().setRadiationLevel(baseProcessRadiation*getSpeedMultiplier());
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
		for (int i = fluidInputSize + fluidOutputSize; i < 2*fluidInputSize + fluidOutputSize; i++) tanks.get(i).setFluid(null);
		
		if (!hasConsumed || recipeInfo == null) return;
		
		for (int j = 0; j < fluidOutputSize; j++) {
			IFluidIngredient fluidProduct = getFluidProducts().get(j);
			if (fluidProduct.getNextStackSize(0) <= 0) continue;
			if (tanks.get(j + fluidInputSize).isEmpty()) {
				tanks.get(j + fluidInputSize).setFluidStored(fluidProduct.getNextStack(0));
			} else if (tanks.get(j + fluidInputSize).getFluid().isFluidEqual(fluidProduct.getStack())) {
				tanks.get(j + fluidInputSize).changeFluidAmount(fluidProduct.getNextStackSize(0));
			}
		}
		hasConsumed = false;
	}
	
	// IProcessor
	
	@Override
	public List<Tank> getFluidInputs(boolean consumed) {
		return consumed ? tanks.subList(fluidInputSize + fluidOutputSize, 2*fluidInputSize + fluidOutputSize) : tanks.subList(0, fluidInputSize);
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
	
	public @Nonnull SaltFissionVesselSetting[] getVesselSettings() {
		return vesselSettings;
	}
	
	public void setVesselSettings(@Nonnull SaltFissionVesselSetting[] settings) {
		vesselSettings = settings;
	}
	
	public SaltFissionVesselSetting getVesselSetting(@Nonnull EnumFacing side) {
		return vesselSettings[side.getIndex()];
	}
	
	public void setVesselSetting(@Nonnull EnumFacing side, @Nonnull SaltFissionVesselSetting setting) {
		vesselSettings[side.getIndex()] = setting;
	}
	
	public void toggleVesselSetting(@Nonnull EnumFacing side) {
		setVesselSetting(side, getVesselSetting(side).next());
		refreshFluidConnections(side);
		markDirtyAndNotify();
	}
	
	public void refreshFluidConnections(@Nonnull EnumFacing side) {
		switch (getVesselSetting(side)) {
		case DISABLED:
			setTankSorption(side, 0, TankSorption.NON);
			setTankSorption(side, 1, TankSorption.NON);
			break;
		case DEFAULT:
			setTankSorption(side, 0, TankSorption.IN);
			setTankSorption(side, 1, TankSorption.NON);
			break;
		case DEPLETED_OUT:
			setTankSorption(side, 0, TankSorption.NON);
			setTankSorption(side, 1, TankSorption.OUT);
			break;
		case FUEL_SPREAD:
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
		SaltFissionVesselSetting thisSetting = getVesselSetting(side);
		if (thisSetting == SaltFissionVesselSetting.DISABLED) return;
		
		TileEntity tile = getTileWorld().getTileEntity(getTilePos().offset(side));
		
		if (tile instanceof TileSaltFissionVessel) {
			TileSaltFissionVessel vessel = (TileSaltFissionVessel)tile;
			SaltFissionVesselSetting vesselSetting = vessel.getVesselSetting(side.getOpposite());
			
			if (thisSetting == SaltFissionVesselSetting.FUEL_SPREAD) {
				if (vesselSetting == SaltFissionVesselSetting.DEFAULT) {
					pushFuel(vessel);
					pushDepleted(vessel);
				} else if (vesselSetting == SaltFissionVesselSetting.DEPLETED_OUT) {
					pushFuel(vessel);
				}
			} else if (thisSetting == SaltFissionVesselSetting.DEPLETED_OUT && (vesselSetting == SaltFissionVesselSetting.DEFAULT || vesselSetting == SaltFissionVesselSetting.FUEL_SPREAD)) {
				pushDepleted(vessel);
			}
		}
		
		else if (thisSetting == SaltFissionVesselSetting.DEPLETED_OUT) {
			if (tile instanceof ITilePassive) if (!((ITilePassive) tile).canPushFluidsTo()) return;
			IFluidHandler adjStorage = tile == null ? null : tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite());
			
			if (adjStorage == null) return;
			
			for (int i = 0; i < getTanks().size(); i++) {
				if (getTanks().get(i).getFluid() == null || !getTankSorption(side, i).canDrain()) continue;
				
				getTanks().get(i).drain(adjStorage.fill(getTanks().get(i).drain(getTanks().get(i).getCapacity(), false), true), true);
			}
		}
	}
	
	public void pushFuel(TileSaltFissionVessel other) {
		int diff = getTanks().get(0).getFluidAmount() - other.getTanks().get(0).getFluidAmount();
		if (diff > 1) {
			getTanks().get(0).drain(other.getTanks().get(0).fillInternal(getTanks().get(0).drain(diff/2, false), true), true);
		}
	}
	
	public void pushDepleted(TileSaltFissionVessel other) {
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
	
	public NBTTagCompound writeVesselSettings(NBTTagCompound nbt) {
		NBTTagCompound settingsTag = new NBTTagCompound();
		for (EnumFacing side : EnumFacing.VALUES) {
			settingsTag.setInteger("setting" + side.getIndex(), getVesselSetting(side).ordinal());
		}
		nbt.setTag("vesselSettings", settingsTag);
		return nbt;
	}
	
	public void readVesselSettings(NBTTagCompound nbt) {
		if (nbt.hasKey("fluidConnections0")) {
			for (EnumFacing side : EnumFacing.VALUES) {
				TankSorption sorption = TankSorption.values()[nbt.getInteger("fluidConnections" + side.getIndex())];
				switch (sorption) {
				case NON:
					setTankSorption(side, 0, TankSorption.NON);
					setTankSorption(side, 1, TankSorption.NON);
					setVesselSetting(side, SaltFissionVesselSetting.DISABLED);
					break;
				case BOTH:
					setTankSorption(side, 0, TankSorption.IN);
					setTankSorption(side, 1, TankSorption.NON);
					setVesselSetting(side, SaltFissionVesselSetting.DEFAULT);
					break;
				case IN:
					setTankSorption(side, 0, TankSorption.NON);
					setTankSorption(side, 1, TankSorption.OUT);
					setVesselSetting(side, SaltFissionVesselSetting.DEPLETED_OUT);
					break;
				case OUT:
					setTankSorption(side, 0, TankSorption.OUT);
					setTankSorption(side, 1, TankSorption.NON);
					setVesselSetting(side, SaltFissionVesselSetting.FUEL_SPREAD);
					break;
				default:
					setTankSorption(side, 0, TankSorption.NON);
					setTankSorption(side, 1, TankSorption.NON);
					setVesselSetting(side, SaltFissionVesselSetting.DISABLED);
					break;
				}
			}
		}
		else {
			NBTTagCompound settingsTag = nbt.getCompoundTag("vesselSettings");
			for (EnumFacing side : EnumFacing.VALUES) {
				setVesselSetting(side, SaltFissionVesselSetting.values()[settingsTag.getInteger("setting" + side.getIndex())]);
				refreshFluidConnections(side);
			}
		}
	}
	
	@Override
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		writeTanks(nbt);
		writeVesselSettings(nbt);
		
		nbt.setDouble("baseProcessTime", baseProcessTime);
		nbt.setDouble("baseProcessHeat", baseProcessHeat);
		nbt.setInteger("baseVesselEfficiency", baseVesselEfficiency);
		nbt.setInteger("baseVesselMaxEfficiency", baseVesselMaxEfficiency);
		nbt.setDouble("moderatorExtraEfficiency", moderatorExtraEfficiency);
		nbt.setDouble("moderatorMaxExtraEfficiency", moderatorMaxExtraEfficiency);
		nbt.setDouble("moderatorHeatFactor", moderatorHeatFactor);
		nbt.setDouble("moderatorMaxHeatFactor", moderatorMaxHeatFactor);
		
		nbt.setDouble("time", time);
		nbt.setBoolean("isProcessing", isProcessing);
		nbt.setBoolean("hasConsumed", hasConsumed);
		nbt.setBoolean("canProcessInputs", canProcessInputs);
		return nbt;
	}
	
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		readTanks(nbt);
		readVesselSettings(nbt);
		
		baseProcessTime = nbt.getDouble("baseProcessTime");
		baseProcessHeat = nbt.getDouble("baseProcessHeat");
		baseVesselEfficiency = nbt.getInteger("baseVesselEfficiency");
		baseVesselMaxEfficiency = nbt.getInteger("baseVesselMaxEfficiency");
		moderatorExtraEfficiency = nbt.getDouble("moderatorExtraEfficiency");
		moderatorMaxExtraEfficiency = nbt.getDouble("moderatorMaxExtraEfficiency");
		moderatorHeatFactor = nbt.getDouble("moderatorHeatFactor");
		moderatorMaxHeatFactor = nbt.getDouble("moderatorMaxHeatFactor");
		
		time = nbt.getDouble("time");
		isProcessing = nbt.getBoolean("isProcessing");
		hasConsumed = nbt.getBoolean("hasConsumed");
		canProcessInputs = nbt.getBoolean("canProcessInputs");
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
