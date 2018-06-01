package nc.multiblock.fission.moltensalt.tile;

import java.util.ArrayList;

import nc.config.NCConfig;
import nc.multiblock.MultiblockControllerBase;
import nc.recipe.BaseRecipeHandler;
import nc.recipe.IIngredient;
import nc.recipe.IRecipe;
import nc.recipe.NCRecipes;
import nc.recipe.RecipeMethods;
import nc.recipe.SorptionType;
import nc.tile.internal.fluid.FluidConnection;
import nc.tile.internal.fluid.Tank;
import nc.util.BlockPosHelper;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fluids.capability.templates.EmptyFluidHandler;

public class TileSaltFissionHeater extends TileSaltFissionPartBase implements IFluidHandler {
	
	public FluidConnection[] fluidConnections = new FluidConnection[] {FluidConnection.IN, FluidConnection.OUT};
	public final Tank[] tanks = new Tank[2];
	
	public final int fluidInputSize = 1, fluidOutputSize = 1;
	
	public double baseProcessCooling;
	public double reactorCoolingRate; // Based on the reactor efficiency, but with heat/cooling taken into account
	public boolean checked;
	
	public double time;
	public boolean isProcessing, canProcessStacks, isInValidPosition;
	
	public final NCRecipes.Type recipeType = NCRecipes.Type.COOLANT_HEATER;
	
	public TileSaltFissionHeater() {
		super(PartPositionType.INTERIOR);
		
		tanks[0] = new Tank(NCConfig.salt_fission_cooling_max_rate*9, RecipeMethods.validFluids(NCRecipes.Type.COOLANT_HEATER)[0]);
		tanks[1] = new Tank(NCConfig.salt_fission_cooling_max_rate*9);
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
		return tanks[0].getFluidName();
	}
	
	private boolean isWall(EnumFacing dir) {
		if (!(world.getTileEntity(pos.offset(dir)) instanceof TileSaltFissionPartBase)) return false;
		TileSaltFissionPartBase part = (TileSaltFissionPartBase) world.getTileEntity(pos.offset(dir));
		return part.getPartPositionType() == PartPositionType.WALL;
	}
	
	private boolean isHeaterWithCoolant(EnumFacing dir, String name) {
		if (!(world.getTileEntity(pos.offset(dir)) instanceof TileSaltFissionHeater)) return false;
		TileSaltFissionHeater heater = (TileSaltFissionHeater) world.getTileEntity(pos.offset(dir));
		return heater.tanks[0].getFluidName().equals(name) && heater.isInValidPosition;
	}
	
	private boolean isModerator(EnumFacing dir) {
		if (!(world.getTileEntity(pos.offset(dir)) instanceof TileSaltFissionModerator)) return false;
		TileSaltFissionModerator moderator = (TileSaltFissionModerator) world.getTileEntity(pos.offset(dir));
		return moderator.isInValidPosition;
	}
	
	private boolean isVessel(EnumFacing dir) {
		if (!(world.getTileEntity(pos.offset(dir)) instanceof TileSaltFissionVessel)) return false;
		TileSaltFissionVessel vessel = (TileSaltFissionVessel) world.getTileEntity(pos.offset(dir));
		return vessel.canProcessStacks && vessel.isMultiblockAssembled();
	}
	
	// Processing
	
	public double getSpeedMultiplier() {
		return reactorCoolingRate;
	}
	
	public double getProcessTime() {
		return 20D;
	}
	
	public double getProcessCooling() {
		return 0.05D*NCConfig.salt_fission_cooling_max_rate*baseProcessCooling;
	}
	
	public BaseRecipeHandler getRecipeHandler() {
		return recipeType.getRecipeHandler();
	}
	
	@Override
	public void onAdded() {
		super.onAdded();
		if (!world.isRemote) isProcessing = canProcess();
	}
	
	@Override
	public void update() {
		super.update();
		updateHeater();
	}
	
	public void updateHeater() {
		canProcessStacks = canProcessStacks();
		boolean wasProcessing = isProcessing;
		isProcessing = canProcess();
		boolean shouldUpdate = false;
		if (!world.isRemote) {
			if (isProcessing) process();
			if (wasProcessing != isProcessing) {
				shouldUpdate = true;
			}
			tick();
			if (shouldCheck()) pushFluid();
		}
		if (shouldUpdate) markDirty();
	}
	
	@Override
	public void tick() {
		tickCount++; tickCount %= NCConfig.machine_update_rate / 4;
	}
	
	public boolean canProcess() {
		return canProcessStacks && isInValidPosition && isMultiblockAssembled();
	}
	
	public void process() {
		time += getSpeedMultiplier();
		if (time >= getProcessTime()) completeProcess();
	}
	
	public void completeProcess() {
		time = 0;
		produceProducts();
	}
	
	public boolean canProcessStacks() {
		baseProcessCooling = 0D;
		
		for (int i = 0; i < fluidInputSize; i++) {
			if (tanks[i].getFluidAmount() <= 0) return false;
		}
		
		Object[] outputs = outputs();
		if (outputs == null || outputs.length != fluidOutputSize) return false;
		for (int j = 0; j < fluidOutputSize; j++) {
			if (outputs[j] == null) {
				return false;
			} else {
				if (tanks[j + fluidInputSize].getFluid() != null) {
					if (!tanks[j + fluidInputSize].getFluid().isFluidEqual((FluidStack) outputs[j])) {
						return false;
					} else if (tanks[j + fluidInputSize].getFluidAmount() + ((FluidStack) outputs[j]).amount > tanks[j + fluidInputSize].getCapacity()) {
						return false;
					}
				}
			}
		}
		
		IRecipe recipe = getRecipeHandler().getRecipeFromInputs(inputs());
		if (recipe != null) {
			Object coolingVar = recipe.extras().get(0);
			if (coolingVar instanceof Double) baseProcessCooling = (double) coolingVar;
		}
		
		return true;
	}
	
	public void produceProducts() {
		IRecipe recipe = getRecipe();
		Object[] outputs = outputs();
		int[] inputOrder = inputOrder();
		if (outputs == null || inputOrder == RecipeMethods.INVALID) return;
		for (int j = 0; j < fluidOutputSize; j++) {
			FluidStack outputStack = (FluidStack) outputs[j];
			if (tanks[j + fluidInputSize].getFluid() == null) {
				tanks[j + fluidInputSize].setFluidStored(outputStack);
			} else if (tanks[j + fluidInputSize].getFluid().isFluidEqual(outputStack)) {
				tanks[j + fluidInputSize].changeFluidStored(outputStack.amount);
			}
		}
		for (int i = 0; i < fluidInputSize; i++) {
			if (getRecipeHandler() != null) {
				tanks[i].changeFluidStored(-recipe.inputs().get(inputOrder[i]).getStackSize());
			} else {
				tanks[i].changeFluidStored(-1000);
			}
			if (tanks[i].getFluidAmount() <= 0) {
				tanks[i].setFluidStored(null);
			}
		}
	}
	
	public IRecipe getRecipe() {
		return getRecipeHandler().getRecipeFromInputs(inputs());
	}
	
	public Object[] inputs() {
		Object[] input = new Object[fluidInputSize];
		for (int i = 0; i < fluidInputSize; i++) {
			input[i] = tanks[i].getFluid();
		}
		return input;
	}
	
	public int[] inputOrder() {
		int[] inputOrder = new int[fluidInputSize];
		IRecipe recipe = getRecipe();
		if (recipe == null) return new int[] {};
		ArrayList<IIngredient> recipeIngredients = recipe.inputs();
		for (int i = 0; i < fluidInputSize; i++) {
			inputOrder[i] = -1;
			for (int j = 0; j < recipeIngredients.size(); j++) {
				if (recipeIngredients.get(j).matches(inputs()[i], SorptionType.INPUT)) {
					inputOrder[i] = j;
					break;
				}
			}
			if (inputOrder[i] == -1) return RecipeMethods.INVALID;
		}
		return inputOrder;
	}
	
	public Object[] outputs() {
		Object[] output = new Object[fluidOutputSize];
		IRecipe recipe = getRecipe();
		if (recipe == null) return null;
		ArrayList<IIngredient> outputs = recipe.outputs();
		for (int i = 0; i < fluidOutputSize; i++) {
			Object out = RecipeMethods.getIngredientFromList(outputs, i);
			if (out == null) return null;
			else output[i] = out;
		}
		return output;
	}
	
	// Fluid
	
	@Override
	public IFluidTankProperties[] getTankProperties() {
		if (tanks.length == 0 || tanks == null) return EmptyFluidHandler.EMPTY_TANK_PROPERTIES_ARRAY;
		IFluidTankProperties[] properties = new IFluidTankProperties[tanks.length];
		for (int i = 0; i < tanks.length; i++) {
			properties[i] = new FluidTankProperties(tanks[i].getFluid(), tanks[i].getCapacity(), fluidConnections[i].canFill(), fluidConnections[i].canDrain());
		}
		return properties;
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		if (tanks.length == 0 || tanks == null) return 0;
		for (int i = 0; i < tanks.length; i++) {
			if (fluidConnections[i].canFill() && tanks[i].isFluidValid(resource) && tanks[i].getFluidAmount() < tanks[i].getCapacity() && (tanks[i].getFluid() == null || tanks[i].getFluid().isFluidEqual(resource))) {
				return tanks[i].fill(resource, doFill);
			}
		}
		return 0;
	}

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) {
		if (tanks.length == 0 || tanks == null) return null;
		for (int i = 0; i < tanks.length; i++) {
			if (fluidConnections[i].canDrain() && tanks[i].getFluid() != null && tanks[i].getFluidAmount() > 0) {
				if (resource.isFluidEqual(tanks[i].getFluid()) && tanks[i].drain(resource, false) != null) return tanks[i].drain(resource, doDrain);
			}
		}
		return null;
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		if (tanks.length == 0 || tanks == null) return null;
		for (int i = 0; i < tanks.length; i++) {
			if (fluidConnections[i].canDrain() && tanks[i].getFluid() != null && tanks[i].getFluidAmount() > 0) {
				if (tanks[i].drain(maxDrain, false) != null) return tanks[i].drain(maxDrain, doDrain);
			}
		}
		return null;
	}
	
	public void pushFluid() {
		Tank tank = tanks[1];
		if (tank != null) {
			FluidConnection fluidConnection = fluidConnections[1];
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
		if (tanks.length > 0 && tanks != null) for (int i = 0; i < tanks.length; i++) {
			nbt.setInteger("fluidAmount" + i, tanks[i].getFluidAmount());
			nbt.setString("fluidName" + i, tanks[i].getFluidName());
		}
		
		nbt.setDouble("baseProcessCooling", baseProcessCooling);
		nbt.setDouble("reactorCoolingRate", reactorCoolingRate);
		
		nbt.setDouble("time", time);
		nbt.setBoolean("isProcessing", isProcessing);
		nbt.setBoolean("canProcessStacks", canProcessStacks);
		nbt.setBoolean("isInValidPosition", isInValidPosition);
		return nbt;
	}
		
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if (tanks.length > 0 && tanks != null) for (int i = 0; i < tanks.length; i++) {
			if (nbt.getString("fluidName" + i) == "nullFluid" || nbt.getInteger("fluidAmount" + i) == 0) tanks[i].setFluidStored(null);
			else tanks[i].setFluidStored(FluidRegistry.getFluid(nbt.getString("fluidName" + i)), nbt.getInteger("fluidAmount" + i));
		}
		
		baseProcessCooling = nbt.getDouble("baseProcessCooling");
		reactorCoolingRate = nbt.getDouble("reactorCoolingRate");
		
		time = nbt.getDouble("time");
		isProcessing = nbt.getBoolean("isProcessing");
		canProcessStacks = nbt.getBoolean("canProcessStacks");
		isInValidPosition = nbt.getBoolean("isInValidPosition");
	}
	
	// Capability
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (tanks.length > 0 && tanks != null) {
			if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) return true;
			//else if (capability == Capabilities.GAS_HANDLER_CAPABILITY) return true;
			//else if (capability == Capabilities.TUBE_CONNECTION_CAPABILITY) return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (tanks.length > 0 && tanks != null) {
			if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this);
			//if (capability == Capabilities.GAS_HANDLER_CAPABILITY) return Capabilities.GAS_HANDLER_CAPABILITY.cast(this);
			//if (capability == Capabilities.TUBE_CONNECTION_CAPABILITY) return Capabilities.TUBE_CONNECTION_CAPABILITY.cast(this);
		}
		return super.getCapability(capability, facing);
	}
}
