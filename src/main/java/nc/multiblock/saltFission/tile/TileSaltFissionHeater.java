package nc.multiblock.saltFission.tile;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import nc.config.NCConfig;
import nc.multiblock.MultiblockControllerBase;
import nc.recipe.IFluidIngredient;
import nc.recipe.IRecipeHandler;
import nc.recipe.NCRecipes;
import nc.recipe.ProcessorRecipe;
import nc.recipe.ProcessorRecipeHandler;
import nc.recipe.SorptionType;
import nc.tile.internal.fluid.FluidConnection;
import nc.tile.internal.fluid.Tank;
import nc.tile.processor.IFluidProcessor;
import nc.util.BlockPosHelper;
import nc.util.RecipeHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fluids.capability.templates.EmptyFluidHandler;

public class TileSaltFissionHeater extends TileSaltFissionPartBase implements IFluidProcessor, IFluidHandler {
	
	public List<FluidConnection> fluidConnections = Lists.newArrayList(FluidConnection.IN, FluidConnection.OUT);
	public final List<Tank> tanks = Lists.newArrayList(new Tank(NCConfig.salt_fission_cooling_max_rate*9, RecipeHelper.validFluids(NCRecipes.Type.COOLANT_HEATER).get(0)), new Tank(NCConfig.salt_fission_cooling_max_rate*9, new ArrayList<String>()));
	
	public final int fluidInputSize = 1, fluidOutputSize = 1;
	
	public double baseProcessCooling;
	public double baseProcessTime = 20;
	public double reactorCoolingRate; // Based on the reactor efficiency, but with heat/cooling taken into account
	public boolean checked;
	
	public double time;
	public boolean isProcessing, canProcessInputs, isInValidPosition;
	
	public final NCRecipes.Type recipeType = NCRecipes.Type.COOLANT_HEATER;
	protected ProcessorRecipe recipe;
	
	public TileSaltFissionHeater() {
		super(PartPositionType.INTERIOR);
	}
	
	@Override
	public void onMachineAssembled(MultiblockControllerBase controller) {
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
			short nak = 0;
			boolean quartz = false;
			for (EnumFacing dir : EnumFacing.VALUES) {
				if (nak < 2) if (isHeaterWithCoolant(dir, "nak")) nak++;
				if (!quartz) if (isHeaterWithCoolant(dir, "quartz_nak")) quartz = true;
				if (nak >= 2 && quartz) {
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
		
		isInValidPosition = false;
	}
	
	public String getCoolantName() {
		return tanks.get(0).getFluidName();
	}
	
	private boolean isWall(EnumFacing dir) {
		if (!(world.getTileEntity(pos.offset(dir)) instanceof TileSaltFissionPartBase)) return false;
		TileSaltFissionPartBase part = (TileSaltFissionPartBase) world.getTileEntity(pos.offset(dir));
		return part.getPartPositionType() == PartPositionType.WALL;
	}
	
	private boolean isHeaterWithCoolant(EnumFacing dir, String name) {
		if (!(world.getTileEntity(pos.offset(dir)) instanceof TileSaltFissionHeater)) return false;
		TileSaltFissionHeater heater = (TileSaltFissionHeater) world.getTileEntity(pos.offset(dir));
		return heater.tanks.get(0).getFluidName().equals(name) && heater.isInValidPosition;
	}
	
	private boolean isModerator(EnumFacing dir) {
		if (!(world.getTileEntity(pos.offset(dir)) instanceof TileSaltFissionModerator)) return false;
		TileSaltFissionModerator moderator = (TileSaltFissionModerator) world.getTileEntity(pos.offset(dir));
		return moderator.isInValidPosition;
	}
	
	private boolean isVessel(EnumFacing dir) {
		if (!(world.getTileEntity(pos.offset(dir)) instanceof TileSaltFissionVessel)) return false;
		TileSaltFissionVessel vessel = (TileSaltFissionVessel) world.getTileEntity(pos.offset(dir));
		return vessel.canProcessInputs() && vessel.isMultiblockAssembled();
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
		tickCount++; tickCount %= NCConfig.machine_update_rate / 4;
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
			produceProducts();
			recipe = getRecipeHandler().getRecipeFromInputs(new ArrayList<ItemStack>(), getFluidInputs());
			setRecipeStats();
			if (recipe == null) time = 0; else time = MathHelper.clamp(time - baseProcessTime, 0D, baseProcessTime);
		}
	}
	
	public boolean canProcessInputs() {
		baseProcessCooling = 0;
		if (recipe == null) return false;
		else if (time >= baseProcessTime) return true;
		
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
		setRecipeStats();
		return true;
	}
	
	public void setRecipeStats() {
		if (recipe == null) {
			setDefaultRecipeStats();
			return;
		}
		
		baseProcessCooling = recipe.getCoolantHeaterCoolingRate();
	}
	
	public void setDefaultRecipeStats() {
		baseProcessCooling = 0;
	}
	
	public void produceProducts() {
		if (recipe == null) return;
		List<Integer> fluidInputOrder = getFluidInputOrder();
		if (fluidInputOrder == IRecipeHandler.INVALID) return;
		
		for (int i = 0; i < fluidInputSize; i++) {
			int fluidIngredientStackSize = getFluidIngredients().get(fluidInputOrder.get(i)).getMaxStackSize();
			if (fluidIngredientStackSize > 0) tanks.get(i).changeFluidStored(-fluidIngredientStackSize);
			if (tanks.get(i).getFluidAmount() <= 0) tanks.get(i).setFluidStored(null);
		}
		for (int j = 0; j < fluidOutputSize; j++) {
			IFluidIngredient fluidProduct = getFluidProducts().get(j);
			if (fluidProduct.getMaxStackSize() <= 0) continue;
			if (tanks.get(j + fluidInputSize).isEmpty()) {
				tanks.get(j + fluidInputSize).setFluidStored(fluidProduct.getNextStack());
			} else if (tanks.get(j + fluidInputSize).getFluid().isFluidEqual(fluidProduct.getStack())) {
				tanks.get(j + fluidInputSize).changeFluidStored(fluidProduct.getNextStackSize());
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
				if (fluidIngredients.get(j).matches(getFluidInputs().get(i), SorptionType.INPUT)) {
					position = j;
					break;
				}
			}
			if (position == -1) return IRecipeHandler.INVALID;
			fluidInputOrder.add(position);
		}
		return fluidInputOrder;
	}
	
	// Fluid
	
	@Override
	public IFluidTankProperties[] getTankProperties() {
		if (tanks == null || tanks.isEmpty()) return EmptyFluidHandler.EMPTY_TANK_PROPERTIES_ARRAY;
		IFluidTankProperties[] properties = new IFluidTankProperties[tanks.size()];
		for (int i = 0; i < tanks.size(); i++) {
			properties[i] = new FluidTankProperties(tanks.get(i).getFluid(), tanks.get(i).getCapacity(), fluidConnections.get(i).canFill(), fluidConnections.get(i).canDrain());
		}
		return properties;
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		if (tanks == null || tanks.isEmpty()) return 0;
		for (int i = 0; i < tanks.size(); i++) {
			if (fluidConnections.get(i).canFill() && tanks.get(i).isFluidValid(resource) && tanks.get(i).getFluidAmount() < tanks.get(i).getCapacity() && (tanks.get(i).getFluid() == null || tanks.get(i).getFluid().isFluidEqual(resource))) {
				return tanks.get(i).fill(resource, doFill);
			}
		}
		return 0;
	}

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) {
		if (tanks == null || tanks.isEmpty()) return null;
		for (int i = 0; i < tanks.size(); i++) {
			if (fluidConnections.get(i).canDrain() && tanks.get(i).getFluid() != null && tanks.get(i).getFluidAmount() > 0) {
				if (resource.isFluidEqual(tanks.get(i).getFluid()) && tanks.get(i).drain(resource, false) != null) return tanks.get(i).drain(resource, doDrain);
			}
		}
		return null;
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		if (tanks == null || tanks.isEmpty()) return null;
		for (int i = 0; i < tanks.size(); i++) {
			if (fluidConnections.get(i).canDrain() && tanks.get(i).getFluid() != null && tanks.get(i).getFluidAmount() > 0) {
				if (tanks.get(i).drain(maxDrain, false) != null) return tanks.get(i).drain(maxDrain, doDrain);
			}
		}
		return null;
	}
	
	public void pushFluid() {
		Tank tank = tanks.get(1);
		if (tank != null) {
			FluidConnection fluidConnection = fluidConnections.get(1);
			if (tank.getFluidAmount() <= 0 || !fluidConnection.canDrain()) return;
			for (EnumFacing side : EnumFacing.VALUES) {
				TileEntity tile = world.getTileEntity(getPos().offset(side));
				if (!(tile instanceof TileSaltFissionVent)) continue;
				TileSaltFissionVent vent = (TileSaltFissionVent) tile;
				tank.drain(vent.fill(tank.drain(tank.getCapacity(), false), true), true);
				if (tank.getFluidAmount() <= 0) return;
			}
		}
	}
	
	// Mekanism Gas
	
	/*
	 * 
	 */
	
	// NBT
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		if (tanks != null && !tanks.isEmpty()) for (int i = 0; i < tanks.size(); i++) {
			nbt.setInteger("fluidAmount" + i, tanks.get(i).getFluidAmount());
			nbt.setString("fluidName" + i, tanks.get(i).getFluidName());
		}
		
		nbt.setDouble("baseProcessCooling", baseProcessCooling);
		nbt.setDouble("reactorCoolingRate", reactorCoolingRate);
		
		nbt.setDouble("time", time);
		nbt.setBoolean("isProcessing", isProcessing);
		nbt.setBoolean("canProcessInputs", canProcessInputs);
		nbt.setBoolean("isInValidPosition", isInValidPosition);
		return nbt;
	}
		
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if (tanks != null && !tanks.isEmpty()) for (int i = 0; i < tanks.size(); i++) {
			if (nbt.getString("fluidName" + i) == "nullFluid" || nbt.getInteger("fluidAmount" + i) == 0) tanks.get(i).setFluidStored(null);
			else tanks.get(i).setFluidStored(FluidRegistry.getFluid(nbt.getString("fluidName" + i)), nbt.getInteger("fluidAmount" + i));
		}
		
		baseProcessCooling = nbt.getDouble("baseProcessCooling");
		reactorCoolingRate = nbt.getDouble("reactorCoolingRate");
		
		time = nbt.getDouble("time");
		isProcessing = nbt.getBoolean("isProcessing");
		canProcessInputs = nbt.getBoolean("canProcessInputs");
		isInValidPosition = nbt.getBoolean("isInValidPosition");
	}
	
	// Capability
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (tanks != null && !tanks.isEmpty()) {
			if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) return true;
			//else if (capability == Capabilities.GAS_HANDLER_CAPABILITY) return true;
			//else if (capability == Capabilities.TUBE_CONNECTION_CAPABILITY) return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (tanks != null && !tanks.isEmpty()) {
			if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this);
			//if (capability == Capabilities.GAS_HANDLER_CAPABILITY) return Capabilities.GAS_HANDLER_CAPABILITY.cast(this);
			//if (capability == Capabilities.TUBE_CONNECTION_CAPABILITY) return Capabilities.TUBE_CONNECTION_CAPABILITY.cast(this);
		}
		return super.getCapability(capability, facing);
	}
}
