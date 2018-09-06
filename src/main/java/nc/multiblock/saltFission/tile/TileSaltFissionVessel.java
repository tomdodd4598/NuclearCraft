package nc.multiblock.saltFission.tile;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import nc.Global;
import nc.config.NCConfig;
import nc.multiblock.cuboidal.CuboidalPartPositionType;
import nc.multiblock.saltFission.SaltFissionReactor;
import nc.recipe.IRecipeHandler;
import nc.recipe.NCRecipes;
import nc.recipe.ProcessorRecipe;
import nc.recipe.ProcessorRecipeHandler;
import nc.recipe.SorptionType;
import nc.recipe.ingredient.IFluidIngredient;
import nc.tile.generator.IFluidGenerator;
import nc.tile.internal.fluid.FluidConnection;
import nc.tile.internal.fluid.Tank;
import nc.util.FluidStackHelper;
import nc.util.RecipeHelper;
import nc.util.RegistryHelper;
import net.minecraft.block.Block;
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

public class TileSaltFissionVessel extends TileSaltFissionPartBase implements IFluidGenerator, IFluidHandler {
	
	public List<FluidConnection> fluidConnections = Lists.newArrayList(FluidConnection.IN, FluidConnection.OUT,  FluidConnection.NON);
	public final List<Tank> tanks = Lists.newArrayList(new Tank(FluidStackHelper.INGOT_VOLUME, RecipeHelper.validFluids(NCRecipes.Type.SALT_FISSION).get(0)), new Tank(FluidStackHelper.INGOT_VOLUME, new ArrayList<String>()), new Tank(FluidStackHelper.INGOT_VOLUME, new ArrayList<String>()));
	
	private Random rand = new Random();
	
	public final int fluidInputSize = 1, fluidOutputSize = 1;
	
	public double baseProcessTime = 1, baseProcessHeat = 0, baseProcessRadiation = 0;
	public double vesselEfficiency;
	
	public double time;
	public boolean isProcessing, hasConsumed, canProcessInputs;
	
	public final NCRecipes.Type recipeType = NCRecipes.Type.SALT_FISSION;
	protected ProcessorRecipe recipe;
	
	public TileSaltFissionVessel() {
		super(CuboidalPartPositionType.INTERIOR);
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
		return vessel.canProcessInputs && vessel.isMultiblockAssembled();
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
		Block corium = RegistryHelper.getBlock(Global.MOD_ID + ":fluid_corium");
		world.removeTileEntity(pos);
		world.setBlockState(pos, corium.getDefaultState());
	}
	
	// Processing
	
	public double getProcessHeat() {
		return vesselEfficiency*(vesselEfficiency + 1D)*0.5D*baseProcessHeat*NCConfig.salt_fission_heat_generation;
	}
	
	@Override
	public void onAdded() {
		super.onAdded();
		if (!world.isRemote) {
			isProcessing = isProcessing();
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
		recipe = getRecipeHandler().getRecipeFromInputs(new ArrayList<ItemStack>(), getFluidInputs(hasConsumed));
		canProcessInputs = canProcessInputs();
		boolean wasProcessing = isProcessing;
		isProcessing = isProcessing();
		boolean shouldUpdate = false;
		if(!world.isRemote) {
			tickTile();
			consumeInputs();
			if (isProcessing) process();
			else getRadiationSource().setRadiationLevel(0D);
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
		return readyToProcess() && isReactorOn;
	}
	
	public boolean readyToProcess() {
		return canProcessInputs && hasConsumed && isMultiblockAssembled();
	}
	
	public void process() {
		time += NCConfig.salt_fission_fuel_use;
		getRadiationSource().setRadiationLevel(baseProcessRadiation*NCConfig.salt_fission_fuel_use);
		if (time >= baseProcessTime) {
			double oldProcessTime = baseProcessTime;
			produceProducts();
			recipe = getRecipeHandler().getRecipeFromInputs(new ArrayList<ItemStack>(), getFluidInputs(hasConsumed));
			setRecipeStats();
			if (recipe == null) time = 0;
			else time = MathHelper.clamp(time - oldProcessTime, 0D, baseProcessTime);
		}
	}
	
	public boolean hasConsumed() {
		if (world.isRemote) return hasConsumed;
		for (int i = 0; i < fluidInputSize; i++) {
			if (!tanks.get(i + fluidInputSize + fluidOutputSize).isEmpty()) return true;
		}
		return false;
	}
		
	public boolean canProcessInputs() {
		baseProcessTime = 1D;
		baseProcessHeat = 0D;
		if (recipe == null) {
			if (hasConsumed) {
				for (Tank tank : getFluidInputs(true)) tank.setFluidStored(null);
				hasConsumed = false;
			}
			return false;
		}
		setRecipeStats();
		if (time >= baseProcessTime) return true;
		
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
	
	public void setRecipeStats() {
		if (recipe == null) {
			setDefaultRecipeStats();
			return;
		}
		
		baseProcessTime = recipe.getSaltFissionFuelTime();
		baseProcessHeat = recipe.getSaltFissionFuelHeat();
		baseProcessRadiation = recipe.getSaltFissionFuelRadiation();
	}
	
	public void setDefaultRecipeStats() {
		baseProcessTime = 1D;
		baseProcessHeat = 0D;
		baseProcessRadiation = 0D;
	}
		
	public void consumeInputs() {
		if (hasConsumed || recipe == null) return;
		List<Integer> fluidInputOrder = getFluidInputOrder();
		if (fluidInputOrder == IRecipeHandler.INVALID) return;
		
		for (int i = 0; i < fluidInputSize; i++) {
			if (!tanks.get(i + fluidInputSize + fluidOutputSize).isEmpty()) {
				tanks.get(i + fluidInputSize + fluidOutputSize).setFluid(null);
			}
		}
		for (int i = 0; i < fluidInputSize; i++) {
			IFluidIngredient fluidIngredient = getFluidIngredients().get(fluidInputOrder.get(i));
			if (fluidIngredient.getMaxStackSize() > 0) {
				tanks.get(i + fluidInputSize + fluidOutputSize).setFluidStored(new FluidStack(tanks.get(i).getFluid(), fluidIngredient.getMaxStackSize()));
				tanks.get(i).changeFluidStored(-fluidIngredient.getMaxStackSize());
			}
			if (tanks.get(i).isEmpty()) tanks.get(i).setFluid(null);
		}
		hasConsumed = true;
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
				tanks.get(j + fluidInputSize).changeFluidStored(fluidProduct.getNextStackSize());
			}
		}
		hasConsumed = false;
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
				if (fluidIngredients.get(j).matches(getFluidInputs(false).get(i), SorptionType.INPUT)) {
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
		if (tanks.size() > 0 && tanks != null) for (int i = 0; i < tanks.size(); i++) {
			nbt.setInteger("fluidAmount" + i, tanks.get(i).getFluidAmount());
			nbt.setString("fluidName" + i, tanks.get(i).getFluidName());
		}
		
		nbt.setDouble("baseProcessTime", baseProcessTime);
		nbt.setDouble("baseProcessHeat", baseProcessHeat);
		nbt.setDouble("vesselEfficiency", vesselEfficiency);
		
		nbt.setDouble("time", time);
		nbt.setBoolean("isProcessing", isProcessing);
		nbt.setBoolean("hasConsumed", hasConsumed);
		nbt.setBoolean("canProcessInputs", canProcessInputs);
		return nbt;
	}
		
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if (tanks.size() > 0 && tanks != null) for (int i = 0; i < tanks.size(); i++) {
			if (nbt.getString("fluidName" + i) == "nullFluid" || nbt.getInteger("fluidAmount" + i) == 0) tanks.get(i).setFluidStored(null);
			else tanks.get(i).setFluidStored(FluidRegistry.getFluid(nbt.getString("fluidName" + i)), nbt.getInteger("fluidAmount" + i));
		}
		
		baseProcessTime = nbt.getDouble("baseProcessTime");
		baseProcessHeat = nbt.getDouble("baseProcessHeat");
		vesselEfficiency = nbt.getDouble("vesselEfficiency");
		
		time = nbt.getDouble("time");
		isProcessing = nbt.getBoolean("isProcessing");
		hasConsumed = nbt.getBoolean("hasConsumed");
		canProcessInputs = nbt.getBoolean("canProcessInputs");
	}
	
	// Capability
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		if (tanks.size() > 0 && tanks != null) {
			if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) return true;
			//else if (capability == Capabilities.GAS_HANDLER_CAPABILITY) return true;
			//else if (capability == Capabilities.TUBE_CONNECTION_CAPABILITY) return true;
		}
		return super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (tanks.size() > 0 && tanks != null) {
			if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(this);
			//if (capability == Capabilities.GAS_HANDLER_CAPABILITY) return Capabilities.GAS_HANDLER_CAPABILITY.cast(this);
			//if (capability == Capabilities.TUBE_CONNECTION_CAPABILITY) return Capabilities.TUBE_CONNECTION_CAPABILITY.cast(this);
		}
		return super.getCapability(capability, facing);
	}
}
