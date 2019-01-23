package nc.multiblock.turbine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;

import com.google.common.collect.Sets;
import nc.Global;
import nc.config.NCConfig;
import nc.multiblock.IMultiblockPart;
import nc.multiblock.MultiblockBase;
import nc.multiblock.TileBeefBase.SyncReason;
import nc.multiblock.container.ContainerTurbineController;
import nc.multiblock.cuboidal.CuboidalMultiblockBase;
import nc.multiblock.network.TurbineUpdatePacket;
import nc.multiblock.turbine.tile.TileTurbineController;
import nc.multiblock.turbine.tile.TileTurbineDynamoCoil;
import nc.multiblock.turbine.tile.TileTurbineInlet;
import nc.multiblock.turbine.tile.TileTurbineOutlet;
import nc.multiblock.turbine.tile.TileTurbinePartBase;
import nc.multiblock.turbine.tile.TileTurbineRotorBearing;
import nc.multiblock.turbine.tile.TileTurbineRotorBlade;
import nc.multiblock.turbine.tile.TileTurbineRotorShaft;
import nc.multiblock.turbine.tile.TileTurbineRotorStator;
import nc.multiblock.validation.IMultiblockValidator;
import nc.recipe.NCRecipes;
import nc.recipe.ProcessorRecipe;
import nc.recipe.ingredient.IFluidIngredient;
import nc.tile.internal.energy.EnergyStorage;
import nc.tile.internal.fluid.Tank;
import nc.tile.internal.fluid.TankSorption;
import nc.util.MaterialHelper;
import nc.util.NCUtil;
import nc.util.RecipeHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;

public class Turbine extends CuboidalMultiblockBase<TurbineUpdatePacket> {
	
	private Set<TileTurbineController> controllers;
	private Set<TileTurbineRotorShaft> rotorShafts;
	private Set<TileTurbineRotorBearing> rotorBearings;
	private Set<TileTurbineDynamoCoil> dynamoCoils;
	private Set<TileTurbineInlet> inlets;
	private Set<TileTurbineOutlet> outlets;
	
	private TileTurbineController controller;
	
	public final EnergyStorage energyStorage;
	public final List<Tank> tanks;
	private static final int BASE_MAX_ENERGY = 64000, BASE_MAX_INPUT = 4000, BASE_MAX_OUTPUT = 16000;
	
	public final NCRecipes.Type recipeType = NCRecipes.Type.TURBINE;
	protected ProcessorRecipe recipe;
	
	private int updateCount = 0;
	
	public boolean isTurbineOn;
	public double power = 0D, rawConductivity = 0D;
	public EnumFacing flowDir = null;
	public int shaftWidth = 0, shaftVolume = 0, bladeLength = 0, noBladeSets = 0, recipeRate = 0;
	public double totalExpansionLevel = 1D, idealTotalExpansionLevel = 1D, basePowerPerMB = 0D;
	public List<Double> expansionLevels = new ArrayList<Double>(), rawBladeEfficiencies = new ArrayList<Double>();
	
	private short dynamoCoilCheckCount = 0;
	
	public Turbine(World world) {
		super(world);
		
		controllers = new HashSet<TileTurbineController>();
		rotorShafts = new HashSet<TileTurbineRotorShaft>();
		rotorBearings = new HashSet<TileTurbineRotorBearing>();
		dynamoCoils = new HashSet<TileTurbineDynamoCoil>();
		inlets = new HashSet<TileTurbineInlet>();
		outlets = new HashSet<TileTurbineOutlet>();
		
		energyStorage = new EnergyStorage(BASE_MAX_ENERGY);
		tanks = Lists.newArrayList(new Tank(BASE_MAX_INPUT, TankSorption.BOTH, RecipeHelper.validFluids(NCRecipes.Type.TURBINE).get(0)), new Tank(BASE_MAX_OUTPUT, TankSorption.BOTH, null));
	}
	
	// Multiblock Part Getters
	
	public Set<TileTurbineController> getControllers() {
		return controllers;
	}
	
	public Set<TileTurbineRotorShaft> getRotorShafts() {
		return rotorShafts;
	}
	
	public Set<TileTurbineRotorBearing> getRotorBearings() {
		return rotorBearings;
	}
	
	public Set<TileTurbineDynamoCoil> getDynamoCoils() {
		return dynamoCoils;
	}
	
	public Set<TileTurbineInlet> getInlets() {
		return inlets;
	}
	
	public Set<TileTurbineOutlet> getOutlets() {
		return outlets;
	}
	
	// Multiblock Size Limits
	
	@Override
	protected int getMinimumInteriorLength() {
		return NCConfig.turbine_min_size;
	}
	
	@Override
	protected int getMaximumInteriorLength() {
		return NCConfig.turbine_max_size;
	}
	
	// Multiblock Methods
	
	@Override
	public void onAttachedPartWithMultiblockData(IMultiblockPart part, NBTTagCompound data) {
		syncDataFrom(data, SyncReason.FullSync);
	}
	
	@Override
	protected void onBlockAdded(IMultiblockPart newPart) {
		if (newPart instanceof TileTurbineController) controllers.add((TileTurbineController) newPart);
		if (newPart instanceof TileTurbineRotorShaft) rotorShafts.add((TileTurbineRotorShaft) newPart);
		if (newPart instanceof TileTurbineRotorBearing) rotorBearings.add((TileTurbineRotorBearing) newPart);
		if (newPart instanceof TileTurbineDynamoCoil) dynamoCoils.add((TileTurbineDynamoCoil) newPart);
		if (newPart instanceof TileTurbineInlet) inlets.add((TileTurbineInlet) newPart);
		if (newPart instanceof TileTurbineOutlet) outlets.add((TileTurbineOutlet) newPart);
	}
	
	@Override
	protected void onBlockRemoved(IMultiblockPart oldPart) {
		if (oldPart instanceof TileTurbineController) controllers.remove(oldPart);
		if (oldPart instanceof TileTurbineRotorShaft) rotorShafts.remove(oldPart);
		if (oldPart instanceof TileTurbineRotorBearing) rotorBearings.remove(oldPart);
		if (oldPart instanceof TileTurbineDynamoCoil) dynamoCoils.remove(oldPart);
		if (oldPart instanceof TileTurbineInlet) inlets.remove(oldPart);
		if (oldPart instanceof TileTurbineOutlet) outlets.remove(oldPart);
	}
	
	@Override
	protected void onMachineAssembled() {
		for (TileTurbineController contr : controllers) controller = contr;
		onTurbineFormed();
	}
	
	@Override
	protected void onMachineRestored() {
		onTurbineFormed();
	}
	
	protected void onTurbineFormed() {
		setIsTurbineOn();
		
		energyStorage.setStorageCapacity(BASE_MAX_ENERGY*getNumConnectedBlocks());
		tanks.get(0).setCapacity(BASE_MAX_INPUT*getNumConnectedBlocks());
		tanks.get(1).setCapacity(BASE_MAX_OUTPUT*getNumConnectedBlocks());
		
		doDynamoCoilPlacementChecks();
	}
	
	private static final ArrayList<String> STAGE_0_COILS = Lists.newArrayList("magnesium");
	private static final ArrayList<String> STAGE_1_COILS = Lists.newArrayList("beryllium");
	private static final ArrayList<String> STAGE_2_COILS = Lists.newArrayList("gold");
	private static final ArrayList<String> STAGE_3_COILS = Lists.newArrayList("copper", "silver");
	private static final ArrayList<String> STAGE_4_COILS = Lists.newArrayList("aluminum");
	
	protected void doDynamoCoilPlacementChecks() {
		if (dynamoCoils.size() < 1) {
			rawConductivity = 0D;
		}
		
		for (TileTurbineDynamoCoil dynamoCoil : dynamoCoils) {
			dynamoCoil.isInValidPosition = false;
			dynamoCoil.checked = false;
		}
		
		double newConductivity = 0D;
		
		for (short i = 0; i <= 4; i++) for (TileTurbineDynamoCoil dynamoCoil : dynamoCoils) {
			dynamoCoilCheckCount = i;
			if (!dynamoCoil.checked) newConductivity += dynamoCoil.contributeConductivity(dynamoCoilCheckCount);
		}
		
		rawConductivity = newConductivity/dynamoCoils.size();
	}
	
	@Override
	protected void onMachinePaused() {
		
	}
	
	@Override
	protected void onMachineDisassembled() {
		isTurbineOn = false;
		if (controller != null) controller.updateBlock(false);
		power = rawConductivity = 0D;
		flowDir = null;
		shaftWidth = shaftVolume = bladeLength = noBladeSets = recipeRate = 0;
		totalExpansionLevel = idealTotalExpansionLevel = 1D;
		basePowerPerMB = 0D;
		expansionLevels = new ArrayList<Double>();
		rawBladeEfficiencies = new ArrayList<Double>();
	}
	
	@Override
	protected boolean isMachineWhole(IMultiblockValidator validatorCallback) {
		
		// Only one controller
		
		if (controllers.size() == 0) {
			validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.no_controller", null);
			return false;
		}
		if (controllers.size() > 1) {
			validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.too_many_controllers", null);
			return false;
		}
		
		if (!super.isMachineWhole(validatorCallback)) return false;
		
		int minX = getMinX(), minY = getMinY(), minZ = getMinZ();
		int maxX = getMaxX(), maxY = getMaxY(), maxZ = getMaxZ();
		
		// Bearings -> flow axis
		
		boolean dirMinX = false, dirMaxX = false, dirMinY = false, dirMaxY = false, dirMinZ = false, dirMaxZ = false;
		EnumFacing.Axis axis = null;
		boolean tooManyAxes = false; //Is any of the bearings in more than a single axis?
		boolean notInAWall = false; //Is the bearing somewhere else in the structure other than the wall?
		
		for (TileTurbineRotorBearing rotorBearing : rotorBearings) {
			BlockPos pos = rotorBearing.getPos();
			
			if (pos.getX() == minX) dirMinX = true;
			else if (pos.getX() == maxX) dirMaxX = true;
			else if (pos.getY() == minY) dirMinY = true;
			else if (pos.getY() == maxY) dirMaxY = true;
			else if (pos.getZ() == minZ) dirMinZ = true;
			else if (pos.getZ() == maxZ) dirMaxZ = true;
			else notInAWall = true; // If the bearing is not at any of those positions, that means our bearing isn't part of the wall at all
		}
		
		if (dirMinX && dirMaxX) {
			axis = EnumFacing.Axis.X;
		}
		if (dirMinY && dirMaxY) {
			if (axis != null) tooManyAxes = true;
			else axis = EnumFacing.Axis.Y;
		}
		if (dirMinZ && dirMaxZ) {
			if (axis != null) tooManyAxes = true;
			else axis = EnumFacing.Axis.Z;
		}
		
		if (axis == null) {
			validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.need_bearings", null);
			return false;
		}
		
		if ((axis == EnumFacing.Axis.X && getInteriorLengthY() != getInteriorLengthZ()) ||
			(axis == EnumFacing.Axis.Y && getInteriorLengthZ() != getInteriorLengthX()) ||
			(axis == EnumFacing.Axis.Z && getInteriorLengthX() != getInteriorLengthY()) ||
			tooManyAxes || notInAWall) {
			validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.bearings_side_square", null);
			return false;
		}
		
		// At this point, all bearings are guaranteed to be part of walls in the same axis
		// Also, it can only ever succeed up to this point if we already have at least two bearings, so no need to check for that
		
		int internalDiameter;
		if (axis == EnumFacing.Axis.X) internalDiameter = getInteriorLengthY();
		else if (axis == EnumFacing.Axis.Y) internalDiameter = getInteriorLengthZ();
		else internalDiameter = getInteriorLengthX();
		boolean isEvenDiameter = internalDiameter % 2 == 0;
		boolean validAmountOfBearings = false;
		
		for (shaftWidth = isEvenDiameter? 2 : 1; shaftWidth <= internalDiameter - 2; shaftWidth += 2) {
			if (rotorBearings.size() == 2*shaftWidth*shaftWidth) {
				validAmountOfBearings = true;
				break;
			}
		}
		
		if (!validAmountOfBearings) {
			validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.bearings_centre_and_square", null);
			return false;
		}
		
		// Last thing that needs to be checked concerning bearings is whether they are grouped correctly at the centre of their respective walls
		
		bladeLength = (internalDiameter - shaftWidth)/2;
		
		for (BlockPos pos : getInteriorPlane(EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.NEGATIVE, axis), -1, bladeLength, bladeLength, bladeLength, bladeLength)) {
			if (WORLD.getTileEntity(pos) instanceof TileTurbineRotorBearing) continue;
			validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.bearings_centre_and_square", pos);
			return false;
		}
		
		for (BlockPos pos : getInteriorPlane(EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.POSITIVE, axis), -1, bladeLength, bladeLength, bladeLength, bladeLength)) {
			if (WORLD.getTileEntity(pos) instanceof TileTurbineRotorBearing) continue;
			validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.bearings_centre_and_square", pos);
			return false;
		}
		
		// All bearings should be valid by now!
		
		// Inlets/outlets -> flowDir
		
		if (inlets.size() == 0 || outlets.size() == 0) {
			validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.valve_wrong_wall", null);
			return false;
		}
		
		for (TileTurbineInlet inlet : inlets) {
			BlockPos pos = inlet.getPos();
			
			if (isInMinWall(axis, pos)) {
				EnumFacing thisFlowDir = EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.POSITIVE, axis);
				if (flowDir != null && flowDir != thisFlowDir) { //make sure that all inlets are in the same wall
					validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.valve_wrong_wall", pos);
					return false;
				} else flowDir = thisFlowDir;
			} else if (isInMaxWall(axis, pos)) {
				EnumFacing thisFlowDir = EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.NEGATIVE, axis);
				if (flowDir != null && flowDir != thisFlowDir) { //make sure that all inlets are in the same wall
					validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.valve_wrong_wall", pos);
					return false;
				} else flowDir = thisFlowDir;
			}
			else {
				validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.valve_wrong_wall", pos);
				return false;
			}
		}
		
		if (flowDir == null) {
			validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.valve_wrong_wall", null);
			return false;
		}
		
		for (TileTurbineOutlet outlet : outlets) {
			BlockPos pos = outlet.getPos();
			
			if (!isInWall(flowDir, pos)) {
				validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.valve_wrong_wall", pos);
				return false;
			}
		}
		
		// Shaft
		
		int flowLength = getFlowLength();
		
		for (int slice = 0; slice < flowLength; slice++) {
			for (BlockPos pos : getInteriorPlane(EnumFacing.getFacingFromAxis(EnumFacing.AxisDirection.POSITIVE, axis), slice, bladeLength, bladeLength, bladeLength, bladeLength)) {
				if (WORLD.getTileEntity(pos) instanceof TileTurbineRotorShaft) continue;
				validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.shaft_centre", pos);
				return false;
			}
		}
		
		// Interior
		
		shaftVolume = shaftWidth*shaftWidth*flowLength;
		noBladeSets = 0;
		
		totalExpansionLevel = 1D;
		expansionLevels = new ArrayList<Double>();
		rawBladeEfficiencies = new ArrayList<Double>();
		
		for (int depth = 0; depth < flowLength; depth++) {
			
			// Free space
			
			for (BlockPos pos : getInteriorPlane(flowDir, depth, 0, 0, shaftWidth + bladeLength, shaftWidth + bladeLength)) {
				if (!MaterialHelper.isReplaceable(WORLD.getBlockState(pos).getMaterial())) {
					validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.space_between_blades", pos);
					return false;
				}
			}
			
			for (BlockPos pos : getInteriorPlane(flowDir, depth, shaftWidth + bladeLength, 0, 0, shaftWidth + bladeLength)) {
				if (!MaterialHelper.isReplaceable(WORLD.getBlockState(pos).getMaterial())) {
					validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.space_between_blades", pos);
					return false;
				}
			}
			
			for (BlockPos pos : getInteriorPlane(flowDir, depth, 0, shaftWidth + bladeLength, shaftWidth + bladeLength, 0)) {
				if (!MaterialHelper.isReplaceable(WORLD.getBlockState(pos).getMaterial())) {
					validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.space_between_blades", pos);
					return false;
				}
			}
			
			for (BlockPos pos : getInteriorPlane(flowDir, depth, shaftWidth + bladeLength, shaftWidth + bladeLength, 0, 0)) {
				if (!MaterialHelper.isReplaceable(WORLD.getBlockState(pos).getMaterial())) {
					validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.space_between_blades", pos);
					return false;
				}
			}
			
			// Blades/stators
			
			Blade currentBlade = null;
			
			for (BlockPos pos : getInteriorPlane(flowDir.getOpposite(), depth, bladeLength, 0, bladeLength, shaftWidth + bladeLength)) {
				Blade thisBlade = getBladeType(pos);
				if (thisBlade == null) {
					validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.missing_blades", pos);
					return false;
				} else if (currentBlade == null) {
					currentBlade = thisBlade;
				} else if (currentBlade != thisBlade) {
					validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.different_type_blades", pos);
					return false;
				}
			}
			
			for (BlockPos pos : getInteriorPlane(flowDir.getOpposite(), depth, 0, bladeLength, shaftWidth + bladeLength, bladeLength)) {
				Blade thisBlade = getBladeType(pos);
				if (thisBlade == null) {
					validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.missing_blades", pos);
					return false;
				} else if (currentBlade != thisBlade) {
					validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.different_type_blades", pos);
					return false;
				}
			}
			
			for (BlockPos pos : getInteriorPlane(flowDir.getOpposite(), depth, shaftWidth + bladeLength, bladeLength, 0, bladeLength)) {
				Blade thisBlade = getBladeType(pos);
				if (thisBlade == null) {
					validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.missing_blades", pos);
					return false;
				} else if (currentBlade != thisBlade) {
					validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.different_type_blades", pos);
					return false;
				}
			}
			
			for (BlockPos pos : getInteriorPlane(flowDir.getOpposite(), depth, bladeLength, shaftWidth + bladeLength, bladeLength, 0)) {
				Blade thisBlade = getBladeType(pos);
				if (thisBlade == null) {
					validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.missing_blades", pos);
					return false;
				} else if (currentBlade != thisBlade) {
					validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.different_type_blades", pos);
					return false;
				}
			}
			
			if (currentBlade == null) {
				validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.missing_blades", null);
				return false;
			}
			
			double prevExpansionLevel = totalExpansionLevel;
			if (currentBlade.isStator) {
				totalExpansionLevel *= NCConfig.turbine_stator_expansion;
				expansionLevels.add((prevExpansionLevel + totalExpansionLevel)/2D);
				rawBladeEfficiencies.add(0D);
			} else {
				totalExpansionLevel *= currentBlade.bladeType.getExpansionCoefficient();
				expansionLevels.add((prevExpansionLevel + totalExpansionLevel)/2D);
				rawBladeEfficiencies.add(currentBlade.bladeType.getEfficiency());
				noBladeSets++;
			}
		}
		
		if (!NCUtil.areEqual(getFlowLength(), expansionLevels.size(), rawBladeEfficiencies.size())) {
			validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.missing_blades", null);
			return false;
		}
		
		return true;
	}
	
	// Kurtchekov stuff!
	
	protected static class Blade {
		
		Class<? extends TileTurbinePartBase> bladeClass;
		TurbineRotorBladeType bladeType;
		boolean isStator;
		
		public Blade(Class<? extends TileTurbinePartBase> clazz, TurbineRotorBladeType type, boolean stator) {
			bladeClass = clazz;
			bladeType = type;
			isStator = stator;
		}
	}
	
	protected static final HashSet<Blade> BLADES = Sets.newHashSet(
			new Blade(TileTurbineRotorBlade.Steel.class, TurbineRotorBladeType.STEEL, false),
			new Blade(TileTurbineRotorBlade.Extreme.class, TurbineRotorBladeType.EXTREME, false),
			new Blade(TileTurbineRotorBlade.SicSicCMC.class, TurbineRotorBladeType.SIC_SIC_CMC, false),
			new Blade(TileTurbineRotorStator.class, null, true)
	);
	
	protected Blade getBladeType(BlockPos pos) {
		TileEntity tile = WORLD.getTileEntity(pos);
		for (Blade blade : BLADES) {
			if (!blade.bladeClass.isInstance(tile)) continue;
			return blade;
		}
		return null;
	}
	
	// End of Kurtchekov stuff!
	
	protected int getFlowLength() {
		return getInteriorLength(flowDir);
	}
	
	protected int getBladeVolume() {
		return 4*shaftWidth*bladeLength*noBladeSets;
	}
	
	@Override
	protected void onAssimilate(MultiblockBase assimilated) {
		
	}
	
	@Override
	protected void onAssimilated(MultiblockBase assimilator) {
		
	}
	
	// Server
	
	@Override
	protected boolean updateServer() {
		//setIsTurbineOn();
		updateTurbine();
		if (shouldUpdate()) sendUpdateToListeningPlayers();
		incrementUpdateCount();
		return true;
	}
	
	public void setIsTurbineOn() {
		boolean oldIsTurbineOn = isTurbineOn;
		isTurbineOn = isRedstonePowered() && isAssembled();
		if (isTurbineOn != oldIsTurbineOn) {
			if (controller != null) controller.updateBlock(isTurbineOn);
			sendUpdateToAllPlayers();
		}
	}
	
	protected boolean isRedstonePowered() {
		if (controller != null && controller.checkIsRedstonePowered(WORLD, controller.getPos())) return true;
		return false;
	}
	
	protected void updateTurbine() {
		if (shouldUpdate()) {
			refreshRecipe();
			double previousPower = power;
			if (canProcessInputs()) {
				produceProducts();
				power = getNewProcessPower(previousPower, true);
			}
			else {
				power = getNewProcessPower(previousPower, false);
			}
		}
		energyStorage.changeEnergyStored((int)power);
	}
	
	protected void refreshRecipe() {
		if (recipe == null || !recipe.matchingInputs(new ArrayList<ItemStack>(), tanks.subList(0, 1))) {
			recipe = recipeType.getRecipeHandler().getRecipeFromInputs(new ArrayList<ItemStack>(), tanks.subList(0, 1));
		}
	}
	
	protected boolean canProcessInputs() {
		if (!isTurbineOn || !setRecipeStats()) return false;
		return canProduceProducts();
	}
	
	protected boolean setRecipeStats() {
		if (recipe == null) {
			recipeRate = 0;
			basePowerPerMB = 0D;
			idealTotalExpansionLevel = 1D;
			return false;
		}
		basePowerPerMB = recipe.getTurbinePowerPerMB();
		idealTotalExpansionLevel = Math.max(1D, (double)recipe.fluidProducts().get(0).getMaxStackSize()/(double)recipe.fluidIngredients().get(0).getMaxStackSize());
		return true;
	}
	
	protected boolean canProduceProducts() {
		IFluidIngredient fluidProduct = recipe.fluidProducts().get(0);
		if (fluidProduct.getMaxStackSize() <= 0 || fluidProduct.getStack() == null) return false;
		recipeRate = Math.min(tanks.get(0).getFluidAmount(), getMaxRecipeRateMultiplier()*updateTime());
		if (!tanks.get(1).isEmpty()) {
			if (!tanks.get(1).getFluid().isFluidEqual(fluidProduct.getStack())) {
				return false;
			} else if (tanks.get(1).getFluidAmount() + fluidProduct.getMaxStackSize()*recipeRate > tanks.get(1).getCapacity()) {
				return false;
			}
		}
		return true;
	}
	
	public void produceProducts() {
		int fluidIngredientStackSize = recipe.fluidIngredients().get(0).getMaxStackSize()*recipeRate;
		if (fluidIngredientStackSize > 0) tanks.get(0).changeFluidAmount(-fluidIngredientStackSize);
		if (tanks.get(0).getFluidAmount() <= 0) tanks.get(0).setFluidStored(null);
		
		IFluidIngredient fluidProduct = recipe.fluidProducts().get(0);
		if (fluidProduct.getMaxStackSize() <= 0) return;
		if (tanks.get(1).isEmpty()) {
			tanks.get(1).setFluidStored(fluidProduct.getNextStack());
			tanks.get(1).setFluidAmount(tanks.get(1).getFluidAmount()*recipeRate);
		} else if (tanks.get(1).getFluid().isFluidEqual(fluidProduct.getStack())) {
			tanks.get(1).changeFluidAmount(fluidProduct.getNextStackSize()*recipeRate);
		}
	}
	
	public int getMaxRecipeRateMultiplier() {
		return getBladeVolume()*NCConfig.turbine_mb_per_blade;
	}
	
	protected double getNewProcessPower(double previousPower, boolean increasing) {
		if (increasing) {
			return (shaftVolume*previousPower + getMaxProcessPower())/(shaftVolume + 1D);
		}
		else {
			return (Math.sqrt(shaftVolume)*previousPower)/(Math.sqrt(shaftVolume) + 1D);
		}
	}
	
	protected double getMaxProcessPower() {
		if (noBladeSets == 0) return 0D;
		
		double bladeMultiplier = 0D;
		
		for (int depth = 0; depth < getFlowLength(); depth++) {
			if (rawBladeEfficiencies.get(depth) <= 0D) continue;
			bladeMultiplier += rawBladeEfficiencies.get(depth)*getExpansionIdealityMultiplier(getIdealExpansionLevel(depth), expansionLevels.get(depth));
		}
		bladeMultiplier /= (double)noBladeSets;
		
		return bladeMultiplier*getExpansionIdealityMultiplier(idealTotalExpansionLevel, totalExpansionLevel)*getEffectiveConductivity()*(recipeRate/(double)updateTime())*basePowerPerMB;
	}
	
	protected double getExpansionIdealityMultiplier(double ideal, double actual) {
		if (ideal <= 0 || actual <= 0) return 0D;
		return ideal < actual ? ideal/actual : actual/ideal;
	}
	
	protected double getIdealExpansionLevel(int depth) {
		return Math.pow(idealTotalExpansionLevel, (depth + 0.5D)/(double)getFlowLength());
	}
	
	protected double getEffectiveConductivity() {
		if (rotorBearings.size() == 0 || dynamoCoils.size() == 0) return 0;
		return dynamoCoils.size() >= rotorBearings.size() ? rawConductivity : rawConductivity*(double)dynamoCoils.size()/(double)rotorBearings.size();
	}
	
	public int getActualInputRate() {
		return Math.round(recipeRate/(float)updateTime());
	}
	
	private void incrementUpdateCount() {
		updateCount++; updateCount %= updateTime();
	}
	
	private int updateTime() {
		return NCConfig.machine_update_rate / 4;
	}
	
	private boolean shouldUpdate() {
		return updateCount == 0;
	}
	
	// Client
	
	@Override
	protected void updateClient() {
		// TODO
	}
	
	// NBT
	
	@Override
	protected void syncDataTo(NBTTagCompound data, SyncReason syncReason) {
		energyStorage.writeToNBT(data);
		writeTanks(data);
		data.setBoolean("isTurbineOn", isTurbineOn);
		data.setDouble("power", power);
		data.setDouble("rawConductivity", rawConductivity);
		data.setInteger("flowDir", flowDir == null ? -1 : flowDir.getIndex());
		data.setInteger("shaftWidth", shaftWidth);
		data.setInteger("shaftVolume", shaftVolume);
		data.setInteger("bladeLength", bladeLength);
		data.setInteger("noBladeSets", noBladeSets);
		data.setInteger("recipeRate", recipeRate);
		data.setDouble("totalExpansionLevel", totalExpansionLevel);
		data.setDouble("idealTotalExpansionLevel", idealTotalExpansionLevel);
		data.setDouble("basePowerPerMB", basePowerPerMB);
		data.setInteger("expansionLevelsSize", expansionLevels.size());
		for (int i = 0; i < expansionLevels.size(); i++) data.setDouble("expansionLevels" + i, expansionLevels.get(i));
		data.setInteger("rawBladeEfficienciesSize", rawBladeEfficiencies.size());
		for (int i = 0; i < rawBladeEfficiencies.size(); i++) data.setDouble("rawBladeEfficiencies" + i, rawBladeEfficiencies.get(i));
	}
	
	@Override
	protected void syncDataFrom(NBTTagCompound data, SyncReason syncReason) {
		energyStorage.readFromNBT(data);
		readTanks(data);
		isTurbineOn = data.getBoolean("isTurbineOn");
		power = data.getDouble("power");
		rawConductivity = data.getDouble("rawConductivity");
		flowDir = data.getInteger("flowDir") < 0 ? null : EnumFacing.VALUES[data.getInteger("flowDir")];
		shaftWidth = data.getInteger("shaftWidth");
		shaftVolume = data.getInteger("shaftVolume");
		bladeLength = data.getInteger("bladeLength");
		noBladeSets = data.getInteger("noBladeSets");
		recipeRate = data.getInteger("recipeRate");
		totalExpansionLevel = data.getDouble("totalExpansionLevel");
		idealTotalExpansionLevel = data.getDouble("idealTotalExpansionLevel");
		basePowerPerMB = data.getDouble("basePowerPerMB");
		expansionLevels = new ArrayList<Double>();
		if (data.hasKey("expansionLevelsSize")) for (int i = 0; i < data.getInteger("expansionLevelsSize"); i++) {
			expansionLevels.add(data.getDouble("expansionLevels" + i));
		}
		rawBladeEfficiencies = new ArrayList<Double>();
		if (data.hasKey("rawBladeEfficienciesSize")) for (int i = 0; i < data.getInteger("rawBladeEfficienciesSize"); i++) {
			rawBladeEfficiencies.add(data.getDouble("rawBladeEfficiencies" + i));
		}
	}
	
	private NBTTagCompound writeTanks(NBTTagCompound nbt) {
		if (!tanks.isEmpty()) for (int i = 0; i < tanks.size(); i++) {
			nbt.setInteger("fluidAmount" + i, tanks.get(i).getFluidAmount());
			nbt.setString("fluidName" + i, tanks.get(i).getFluidName());
		}
		return nbt;
	}
	
	private void readTanks(NBTTagCompound nbt) {
		if (!tanks.isEmpty()) for (int i = 0; i < tanks.size(); i++) {
			if (nbt.getString("fluidName" + i) == "nullFluid" || nbt.getInteger("fluidAmount" + i) == 0) tanks.get(i).setFluidStored(null);
			else tanks.get(i).setFluidStored(FluidRegistry.getFluid(nbt.getString("fluidName" + i)), nbt.getInteger("fluidAmount" + i));
		}
	}
	
	// Packets
	
	@Override
	protected TurbineUpdatePacket getUpdatePacket() {
		return new TurbineUpdatePacket(controller.getPos(), isTurbineOn, power, rawConductivity, totalExpansionLevel, idealTotalExpansionLevel, recipeRate, shaftWidth, bladeLength, noBladeSets, energyStorage.getMaxEnergyStored(), energyStorage.getEnergyStored());
	}
	
	@Override
	public void onPacket(TurbineUpdatePacket message) {
		isTurbineOn = message.isTurbineOn;
		power = message.power;
		rawConductivity = message.rawConductivity;
		totalExpansionLevel = message.totalExpansionLevel;
		idealTotalExpansionLevel = message.idealTotalExpansionLevel;
		recipeRate = message.recipeRate;
		shaftWidth = message.shaftWidth;
		bladeLength = message.bladeLength;
		noBladeSets = message.noBladeSets;
		energyStorage.setStorageCapacity(message.capacity);
		energyStorage.setEnergyStored(message.energy);
	}
	
	public Container getContainer(EntityPlayer player) {
		return new ContainerTurbineController(player, controller);
	}
	
	// Multiblock Validators
	
	@Override
	protected boolean isBlockGoodForInterior(World world, int x, int y, int z, IMultiblockValidator validatorCallback) {
		BlockPos pos = new BlockPos(x, y, z);
		if (MaterialHelper.isReplaceable(world.getBlockState(pos).getMaterial()) || world.getTileEntity(pos) instanceof TileTurbinePartBase) return true;
		else return standardLastError(x, y, z, validatorCallback);
	}
}