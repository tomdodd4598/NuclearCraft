package nc.multiblock.saltFission.tile;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.Lists;

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
import nc.tile.internal.fluid.FluidConnection;
import nc.tile.internal.fluid.FluidTileWrapper;
import nc.tile.internal.fluid.GasTileWrapper;
import nc.tile.internal.fluid.Tank;
import nc.tile.internal.fluid.TankSorption;
import nc.tile.passive.ITilePassive;
import nc.tile.processor.IFluidProcessor;
import nc.util.BlockPosHelper;
import nc.util.FluidStackHelper;
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

public class TileSaltFissionHeater extends TileSaltFissionPartBase implements IFluidProcessor, ITileFluid {
	
	private static final FluidConnection HOT_COOLANT_OUT = FluidConnection.IN;
	private static final FluidConnection COOLANT_OUT = FluidConnection.OUT;
	private static final FluidConnection DEFAULT = FluidConnection.BOTH;
	private static final FluidConnection DISABLED = FluidConnection.NON;
			
	private final @Nonnull List<Tank> tanks = Lists.newArrayList(new Tank(FluidStackHelper.INGOT_BLOCK_VOLUME, TankSorption.IN, RecipeHelper.validFluids(NCRecipes.Type.COOLANT_HEATER).get(0)), new Tank(FluidStackHelper.INGOT_BLOCK_VOLUME*4, TankSorption.OUT, new ArrayList<String>()));

	private @Nonnull FluidConnection[] fluidConnections = ITileFluid.fluidConnectionAll(FluidConnection.BOTH);
	
	private @Nonnull FluidTileWrapper[] fluidSides;
	
	private @Nonnull GasTileWrapper gasWrapper;
	
	public final int fluidInputSize = 1, fluidOutputSize = 1;
	
	private double baseProcessCooling;
	public final int baseProcessTime = 20;
	public double reactorCoolingRate; // Based on the reactor efficiency, but with heat/cooling taken into account
	public boolean checked = false;
	
	private int fluidToHold;
	
	public double time;
	public boolean isProcessing, canProcessInputs, isInValidPosition;
	
	public final NCRecipes.Type recipeType = NCRecipes.Type.COOLANT_HEATER;
	protected ProcessorRecipe recipe;
	
	public TileSaltFissionHeater() {
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
	
	public void checkIsInValidPosition() {
		if (getCoolantName() == "nullFluid") {
			isInValidPosition = false;
			checked = true;
			return;
		}
		
		else if (getCoolantName() == "nak") {
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isVessel(dir) || isModerator(dir)) {
					isInValidPosition = true;
					checked = true;
					return;
				}
			}
			isInValidPosition = false;
			checked = true;
			return;
		}
		
		else if (getCoolantName() == "redstone_nak") {
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isVessel(dir)) {
					isInValidPosition = true;
					checked = true;
					return;
				}
			}
			isInValidPosition = false;
			checked = true;
			return;
		}

		else if (getCoolantName() == "quartz_nak") {
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isModerator(dir)) {
					isInValidPosition = true;
					checked = true;
					return;
				}
			}
			isInValidPosition = false;
			checked = true;
			return;
		}
		
		else if (getCoolantName() == "gold_nak") {
			boolean nak = false;
			boolean redstone = false;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (!nak) if (isHeaterWithCoolant(dir, "nak")) nak = true;
				if (!redstone) if (isHeaterWithCoolant(dir, "redstone_nak")) redstone = true;
				if (nak && redstone) {
					isInValidPosition = true;
					checked = true;
					return;
				}
			}
			isInValidPosition = false;
			checked = true;
			return;
		}
		
		else if (getCoolantName() == "glowstone_nak") {
			short moderator = 0;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isModerator(dir)) moderator++;
				if (moderator >= 2) {
					isInValidPosition = true;
					checked = true;
					return;
				}
			}
			isInValidPosition = false;
			checked = true;
			return;
		}
		
		else if (getCoolantName() == "lapis_nak") {
			boolean vessel = false;
			boolean wall = false;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (!vessel) if (isVessel(dir)) vessel = true;
				if (!wall) if (isWall(dir)) wall = true;
				if (vessel && wall) {
					isInValidPosition = true;
					checked = true;
					return;
				}
			}
			isInValidPosition = false;
			checked = true;
			return;
		}
		
		else if (getCoolantName() == "diamond_nak") {
			boolean nak = false;
			boolean quartz = false;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (!nak) if (isHeaterWithCoolant(dir, "nak")) nak = true;
				if (!quartz) if (isHeaterWithCoolant(dir, "quartz_nak")) quartz = true;
				if (nak && quartz) {
					isInValidPosition = true;
					checked = true;
					return;
				}
			}
			isInValidPosition = false;
			checked = true;
			return;
		}
		
		else if (getCoolantName() == "liquidhelium_nak") {
			boolean wall = false;
			short redstone = 0;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (!wall) if (isWall(dir)) wall = true;
				if (isHeaterWithCoolant(dir, "redstone_nak")) redstone++;
				if (redstone > 1) {
					isInValidPosition = false;
					checked = true;
					return;
				}
			}
			isInValidPosition = wall && redstone == 1;
			checked = true;
			return;
		}
		
		else if (getCoolantName() == "ender_nak") {
			short vertices = 0;
			posList: for (EnumFacing[] vertexDirList : BlockPosHelper.vertexDirList()) {
				for (EnumFacing dir : vertexDirList) if (!isWall(dir)) continue posList;
				vertices++;
				if (vertices > 1) {
					isInValidPosition = false;
					checked = true;
					return;
				}
			}
			isInValidPosition = vertices == 1;
			checked = true;
			return;
		}
		
		else if (getCoolantName() == "cryotheum_nak") {
			short vessel = 0;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isVessel(dir)) vessel++;
				if (vessel >= 2) {
					isInValidPosition = true;
					checked = true;
					return;
				}
			}
			isInValidPosition = false;
			checked = true;
			return;
		}
		
		else if (getCoolantName() == "iron_nak") {
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isHeaterWithCoolant(dir, "gold_nak")) {
					isInValidPosition = true;
					checked = true;
					return;
				}
			}
			isInValidPosition = false;
			checked = true;
			return;
		}
		
		else if (getCoolantName() == "emerald_nak") {
			boolean vessel = false;
			boolean moderator = false;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (!vessel) if (isVessel(dir)) vessel = true;
				if (!moderator) if (isModerator(dir)) moderator = true;
				if (vessel && moderator) {
					isInValidPosition = true;
					checked = true;
					return;
				}
			}
			isInValidPosition = false;
			checked = true;
			return;
		}
		
		else if (getCoolantName() == "copper_nak") {
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (isHeaterWithCoolant(dir, "glowstone_nak")) {
					isInValidPosition = true;
					checked = true;
					return;
				}
			}
			isInValidPosition = false;
			checked = true;
			return;
		}
		
		else if (getCoolantName() == "tin_nak") {
			posList: for (EnumFacing[] axialsDirList : BlockPosHelper.axialsDirList()) {
				for (EnumFacing dir : axialsDirList) if (!isHeaterWithCoolant(dir, "lapis_nak")) continue posList;
				isInValidPosition = true;
				checked = true;
				return;
			}
			isInValidPosition = false;
			checked = true;
			return;
		}
		
		else if (getCoolantName() == "magnesium_nak") {
			boolean moderator = false;
			boolean wall = false;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (!moderator) if (isModerator(dir)) moderator = true;
				if (!wall) if (isWall(dir)) wall = true;
				if (moderator && wall) {
					isInValidPosition = true;
					checked = true;
					return;
				}
			}
			isInValidPosition = false;
			checked = true;
			return;
		}
		
		isInValidPosition = true;
	}
	
	public String getCoolantName() {
		return tanks.get(0).getFluidName();
	}
	
	private boolean isWall(EnumFacing dir) {
		TileEntity tile = world.getTileEntity(pos.offset(dir));
		if (!(tile instanceof TileSaltFissionPartBase)) return false;
		TileSaltFissionPartBase part = (TileSaltFissionPartBase) tile;
		return part.getPartPositionType() == CuboidalPartPositionType.WALL;
	}
	
	private boolean isHeaterWithCoolant(EnumFacing dir, String name) {
		TileEntity tile = world.getTileEntity(pos.offset(dir));
		if (!(tile instanceof TileSaltFissionHeater)) return false;
		TileSaltFissionHeater heater = (TileSaltFissionHeater) tile;
		return heater.tanks.get(0).getFluidName().equals(name) && heater.isInValidPosition;
	}
	
	private boolean isModerator(EnumFacing dir) {
		TileEntity tile = world.getTileEntity(pos.offset(dir));
		if (!(tile instanceof TileSaltFissionModerator)) return false;
		TileSaltFissionModerator moderator = (TileSaltFissionModerator) tile;
		return moderator.isInValidPosition;
	}
	
	private boolean isVessel(EnumFacing dir) {
		if (!isMultiblockAssembled()) return false;
		TileEntity tile = world.getTileEntity(pos.offset(dir));
		if (!(tile instanceof TileSaltFissionVessel)) return false;
		TileSaltFissionVessel vessel = (TileSaltFissionVessel) tile;
		return vessel.canProcessInputs;
	}
	
	// Processing
	
	public double getSpeedMultiplier() {
		return reactorCoolingRate;
	}
	
	public double getProcessCooling() {
		return 0.05D*NCConfig.salt_fission_cooling_max_rate*baseProcessCooling;
	}
	
	@Override
	public void onAdded() {
		super.onAdded();
		if (!world.isRemote) isProcessing = isProcessing();
	}
	
	@Override
	public void update() {
		super.update();
		updateHeater();
	}
	
	public void updateHeater() {
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
		return readyToProcess();
	}
	
	public boolean readyToProcess() {
		return canProcessInputs && isInValidPosition && isMultiblockAssembled();
	}
	
	public void process() {
		time += getSpeedMultiplier();
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
		baseProcessCooling = 0;
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
		
		baseProcessCooling = recipe.getCoolantHeaterCoolingRate();
		
		fluidToHold = getFluidIngredients().get(0).getMaxStackSize();
	}
	
	public void setDefaultRecipeStats() {
		baseProcessCooling = 0D;
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
		
		if (tile instanceof TileSaltFissionHeater) {
			TileSaltFissionHeater heater = (TileSaltFissionHeater)tile;
			FluidConnection heaterConnection = heater.getFluidConnection(side.getOpposite());
			
			if (thisConnection == COOLANT_OUT) {
				if (heaterConnection == DEFAULT) {
					pushCoolant(heater);
					pushHotCoolant(heater);
				} else if (heaterConnection == HOT_COOLANT_OUT) {
					pushCoolant(heater);
				}
			} else if (thisConnection == HOT_COOLANT_OUT && (heaterConnection == DEFAULT || heaterConnection == COOLANT_OUT)) {
				pushHotCoolant(heater);
			}
		}
		
		else if (thisConnection == HOT_COOLANT_OUT) {
			if (tile instanceof ITilePassive) if (!((ITilePassive) tile).canPushFluidsTo()) return;
			IFluidHandler adjStorage = tile == null ? null : tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.getOpposite());
			
			if (adjStorage == null) return;
			
			for (int i = 0; i < getTanks().size(); i++) {
				if (getTanks().get(i).getFluid() == null || !getTanks().get(i).canDrain()) continue;
				
				getTanks().get(i).drainInternal(adjStorage.fill(getTanks().get(i).drainInternal(getTanks().get(i).getCapacity(), false), true), true);
			}
		}
	}
	
	public void pushCoolant(TileSaltFissionHeater other) {
		if (getTanks().get(0).getFluidAmount() > other.getTanks().get(0).getFluidAmount()) {
			getTanks().get(0).drainInternal(other.getTanks().get(0).fillInternal(getTanks().get(0).drainInternal(getTanks().get(0).getFluidAmount() - fluidToHold, false), true), true);
		}
	}
	
	public void pushHotCoolant(TileSaltFissionHeater other) {
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
		
		nbt.setDouble("baseProcessCooling", baseProcessCooling);
		nbt.setDouble("reactorCoolingRate", reactorCoolingRate);
		
		nbt.setDouble("time", time);
		nbt.setBoolean("isProcessing", isProcessing);
		nbt.setBoolean("canProcessInputs", canProcessInputs);
		nbt.setBoolean("isInValidPosition", isInValidPosition);
		nbt.setInteger("fluidToHold", fluidToHold);
		return nbt;
	}
		
	@Override
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		readTanks(nbt);
		readFluidConnections(nbt);
		
		baseProcessCooling = nbt.getDouble("baseProcessCooling");
		reactorCoolingRate = nbt.getDouble("reactorCoolingRate");
		
		time = nbt.getDouble("time");
		isProcessing = nbt.getBoolean("isProcessing");
		canProcessInputs = nbt.getBoolean("canProcessInputs");
		isInValidPosition = nbt.getBoolean("isInValidPosition");
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
