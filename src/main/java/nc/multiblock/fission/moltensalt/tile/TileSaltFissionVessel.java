package nc.multiblock.fission.moltensalt.tile;

import java.util.ArrayList;
import java.util.Random;

import nc.Global;
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
import nc.util.FluidHelper;
import nc.util.RegistryHelper;
import net.minecraft.block.Block;
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

public class TileSaltFissionVessel extends TileSaltFissionPartBase implements IFluidHandler {
	
	public FluidConnection[] fluidConnections = new FluidConnection[] {FluidConnection.IN, FluidConnection.OUT,  FluidConnection.NON};
	public final Tank[] tanks = new Tank[3];
	
	private Random rand = new Random();
	
	public final int fluidInputSize = 1, fluidOutputSize = 1;
	
	public double baseProcessTime = 1D, baseProcessHeat;
	public double vesselEfficiency;
	
	public double time;
	public boolean isGenerating, hasConsumed, canProcessStacks;
	
	public final NCRecipes.Type recipeType = NCRecipes.Type.SALT_FISSION;
	
	public TileSaltFissionVessel() {
		super(PartPositionType.INTERIOR);
		
		tanks[0] = new Tank(FluidHelper.INGOT_VOLUME, RecipeMethods.validFluids(NCRecipes.Type.SALT_FISSION)[0]);
		tanks[1] = new Tank(FluidHelper.INGOT_VOLUME);
		tanks[2] = new Tank(FluidHelper.INGOT_VOLUME);
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
	
	public void calculateEfficiency() {
		double newEfficiency = 1D;
		for (EnumFacing dir : EnumFacing.VALUES) {
			if (isVessel(dir)) newEfficiency += 1D;
		}
		
		double extraEfficiency = 0D;
		for (EnumFacing dir : EnumFacing.VALUES) {
			if (isModerator(dir)) {
				extraEfficiency += newEfficiency*NCConfig.fission_moderator_extra_heat/6D;
			}
		}
		vesselEfficiency = newEfficiency + extraEfficiency;
	}
	
	private boolean isVessel(EnumFacing dir) {
		if (!(world.getTileEntity(pos.offset(dir)) instanceof TileSaltFissionVessel)) return false;
		TileSaltFissionVessel vessel = (TileSaltFissionVessel) world.getTileEntity(pos.offset(dir));
		return vessel.canProcessStacks && vessel.isMultiblockAssembled();
	}
	
	private boolean isModerator(EnumFacing dir) {
		if (world.getTileEntity(pos.offset(dir)) instanceof TileSaltFissionModerator) {
			TileSaltFissionModerator moderator = (TileSaltFissionModerator) world.getTileEntity(pos.offset(dir));
			moderator.isInValidPosition = true;
			return true;
		}
		return false;
	}
	
	public void doMeltdown() {
		Block corium = RegistryHelper.getBlock(Global.MOD_ID, "fluid_corium");
		world.removeTileEntity(pos);
		world.setBlockState(pos, corium.getDefaultState());
	}
	
	// Processing
	
	public double getSpeedMultiplier() {
		return NCConfig.salt_fission_fuel_use;
	}
	
	public double getProcessTime() {
		return baseProcessTime;
	}
	
	public double getProcessHeat() {
		return vesselEfficiency*(vesselEfficiency + 1D)*0.5D*baseProcessHeat*NCConfig.salt_fission_heat_generation;
	}
	
	public BaseRecipeHandler getRecipeHandler() {
		return recipeType.getRecipeHandler();
	}
	
	@Override
	public void onAdded() {
		super.onAdded();
		if (!world.isRemote) {
			isGenerating = isGenerating();
			hasConsumed = hasConsumed();
		}
	}
	
	@Override
	public void update() {
		super.update();
		updateVessel();
	}
	
	public void updateVessel() {
		setIsReactorOn();
		canProcessStacks = canProcessStacks();
		boolean wasGenerating = isGenerating;
		isGenerating = canProcess();
		boolean shouldUpdate = false;
		if(!world.isRemote) {
			if (time == 0) consume();
			if (isGenerating) process();
			if (wasGenerating != isGenerating) {
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
	
	public boolean isGenerating() {
		return canProcess();
	}
	
	public boolean canProcess() {
		return canProcessStacks && isReactorOn && isMultiblockAssembled();
	}
	
	public void process() {
		time += getSpeedMultiplier();
		if (time >= getProcessTime()) completeProcess();
	}
	
	public void completeProcess() {
		time = 0;
		produceProducts();
	}
	
	public boolean hasConsumed() {
		if (world.isRemote) return hasConsumed;
		for (int i = 0; i < fluidInputSize; i++) {
			if (tanks[i + fluidInputSize + fluidOutputSize].getFluid() != null) {
				return true;
			}
		}
		return false;
	}
		
	public boolean canProcessStacks() {
		baseProcessTime = 1D;
		baseProcessHeat = 0D;
		
		for (int i = 0; i < fluidInputSize; i++) {
			if (tanks[i].getFluidAmount() <= 0 && !hasConsumed) return false;
		}
		
		Object[] output = hasConsumed ? outputs() : outputs();
		if (output == null || output.length != fluidOutputSize) return false;
		for (int j = 0; j < fluidOutputSize; j++) {
			if (output[j] == null) {
				return false;
			} else {
				if (tanks[j + fluidInputSize].getFluid() != null) {
					if (!tanks[j + fluidInputSize].getFluid().isFluidEqual((FluidStack)output[j])) {
						return false;
					} else if (tanks[j + fluidInputSize].getFluidAmount() + ((FluidStack)output[j]).amount > tanks[j + fluidInputSize].getCapacity()) {
						return false;
					}
				}
			}
		}
		
		IRecipe recipe = getRecipeHandler().getRecipeFromInputs(hasConsumed ? consumedInputs() : inputs());
		if (recipe != null) {
			Object timeVar = recipe.extras().get(0);
			if (timeVar instanceof Double) baseProcessTime = (double) timeVar;
			Object heatVar = recipe.extras().get(1);
			if (heatVar instanceof Double) baseProcessHeat = (double) heatVar;
		}
		
		return true;
	}
		
	public void consume() {
		IRecipe recipe = getRecipe(false);
		Object[] outputs = outputs();
		int[] inputOrder = inputOrder();
		if (outputs == null || inputOrder == RecipeMethods.INVALID) return;
		if (!hasConsumed) {
			for (int i = 0; i < fluidInputSize; i++) {
				if (tanks[i + fluidInputSize + fluidOutputSize].getFluid() != null) {
					tanks[i + fluidInputSize + fluidOutputSize].setFluid(null);
				}
			}
			for (int i = 0; i < fluidInputSize; i++) {
				if (getRecipeHandler() != null) {
					tanks[i + fluidInputSize + fluidOutputSize].changeFluidStored(tanks[i].getFluid().getFluid(), recipe.inputs().get(inputOrder[i]).getStackSize());
					tanks[i].changeFluidStored(-recipe.inputs().get(inputOrder[i]).getStackSize());
				} else {
					tanks[i + fluidInputSize + fluidOutputSize].changeFluidStored(tanks[i].getFluid().getFluid(), 1000);
					tanks[i].changeFluidStored(-1000);
				}
				if (tanks[i].getFluidAmount() <= 0) {
					tanks[i].setFluidStored(null);
				}
			}
			hasConsumed = true;
		}
	}
		
	public void produceProducts() {
		if (hasConsumed) {
			Object[] outputs = outputs();
			for (int j = 0; j < fluidOutputSize; j++) {
				FluidStack outputStack = (FluidStack) outputs[j];
				if (tanks[j + fluidInputSize].getFluid() == null) {
					tanks[j + fluidInputSize].setFluidStored(outputStack);
				} else if (tanks[j + fluidInputSize].getFluid().isFluidEqual(outputStack)) {
					tanks[j + fluidInputSize].changeFluidStored(outputStack.amount);
				}
			}
			for (int i = fluidInputSize + fluidOutputSize; i < 2*fluidInputSize + fluidOutputSize; i++) {
				tanks[i].setFluid(null);
			}
			hasConsumed = false;
		}
	}
		
	public IRecipe getRecipe(boolean consumed) {
		return getRecipeHandler().getRecipeFromInputs(consumed ? consumedInputs() : inputs());
	}
	
	public Object[] inputs() {
		Object[] input = new Object[fluidInputSize];
		for (int i = 0; i < fluidInputSize; i++) {
			input[i] = tanks[i].getFluid();
		}
		return input;
	}
	
	public Object[] consumedInputs() {
		Object[] input = new Object[fluidInputSize];
		for (int i = 0; i < fluidInputSize; i++) {
			input[i] = tanks[i + fluidInputSize + fluidOutputSize].getFluid();
		}
		return input;
	}
	
	public int[] inputOrder() {
		int[] inputOrder = new int[fluidInputSize];
		IRecipe recipe = getRecipe(false);
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
		IRecipe recipe = getRecipe(hasConsumed);
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
		
		nbt.setDouble("baseProcessTime", baseProcessTime);
		nbt.setDouble("baseProcessHeat", baseProcessHeat);
		nbt.setDouble("vesselEfficiency", vesselEfficiency);
		
		nbt.setDouble("time", time);
		nbt.setBoolean("isGenerating", isGenerating);
		nbt.setBoolean("hasConsumed", hasConsumed);
		nbt.setBoolean("canProcessStacks", canProcessStacks);
		return nbt;
	}
		
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if (tanks.length > 0 && tanks != null) for (int i = 0; i < tanks.length; i++) {
			if (nbt.getString("fluidName" + i) == "nullFluid" || nbt.getInteger("fluidAmount" + i) == 0) tanks[i].setFluidStored(null);
			else tanks[i].setFluidStored(FluidRegistry.getFluid(nbt.getString("fluidName" + i)), nbt.getInteger("fluidAmount" + i));
		}
		
		baseProcessTime = nbt.getDouble("baseProcessTime");
		baseProcessHeat = nbt.getDouble("baseProcessHeat");
		vesselEfficiency = nbt.getDouble("vesselEfficiency");
		
		time = nbt.getDouble("time");
		isGenerating = nbt.getBoolean("isGenerating");
		hasConsumed = nbt.getBoolean("hasConsumed");
		canProcessStacks = nbt.getBoolean("canProcessStacks");
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
