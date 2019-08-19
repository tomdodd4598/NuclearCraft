package nc.multiblock.turbine;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import nc.Global;
import nc.config.NCConfig;
import nc.multiblock.IMultiblockFluid;
import nc.multiblock.IMultiblockPart;
import nc.multiblock.MultiblockBase;
import nc.multiblock.TileBeefBase.SyncReason;
import nc.multiblock.container.ContainerTurbineController;
import nc.multiblock.cuboidal.CuboidalMultiblockBase;
import nc.multiblock.network.TurbineRenderPacket;
import nc.multiblock.network.TurbineUpdatePacket;
import nc.multiblock.turbine.TurbineRotorBladeUtil.IBlockRotorBlade;
import nc.multiblock.turbine.TurbineRotorBladeUtil.IRotorBladeType;
import nc.multiblock.turbine.TurbineRotorBladeUtil.ITurbineRotorBlade;
import nc.multiblock.turbine.TurbineRotorBladeUtil.TurbinePartDir;
import nc.multiblock.turbine.TurbineRotorBladeUtil.TurbineRotorBladeType;
import nc.multiblock.turbine.TurbineRotorBladeUtil.TurbineRotorStatorType;
import nc.multiblock.turbine.block.BlockTurbineRotorShaft;
import nc.multiblock.turbine.tile.TileTurbineController;
import nc.multiblock.turbine.tile.TileTurbineDynamoCoil;
import nc.multiblock.turbine.tile.TileTurbineInlet;
import nc.multiblock.turbine.tile.TileTurbineOutlet;
import nc.multiblock.turbine.tile.TileTurbinePartBase;
import nc.multiblock.turbine.tile.TileTurbineRotorBearing;
import nc.multiblock.turbine.tile.TileTurbineRotorShaft;
import nc.multiblock.validation.IMultiblockValidator;
import nc.network.PacketHandler;
import nc.recipe.NCRecipes;
import nc.recipe.ProcessorRecipe;
import nc.recipe.ProcessorRecipeHandler;
import nc.recipe.RecipeInfo;
import nc.recipe.ingredient.IFluidIngredient;
import nc.tile.internal.energy.EnergyStorage;
import nc.tile.internal.fluid.Tank;
import nc.util.MaterialHelper;
import nc.util.NCUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.MutableBlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidRegistry;

public class Turbine extends CuboidalMultiblockBase<TurbineUpdatePacket> implements IMultiblockFluid {
	
	protected Set<TileTurbineController> controllers;
	protected Set<TileTurbineRotorShaft> rotorShafts;
	protected Set<ITurbineRotorBlade> rotorBlades;
	protected Set<TileTurbineRotorBearing> rotorBearings;
	protected Set<TileTurbineDynamoCoil> dynamoCoils;
	protected Set<TileTurbineInlet> inlets;
	protected Set<TileTurbineOutlet> outlets;
	
	protected TileTurbineController controller;
	
	public final EnergyStorage energyStorage;
	public final List<Tank> tanks;
	protected static final int BASE_MAX_ENERGY = 64000, BASE_MAX_INPUT = 4000, BASE_MAX_OUTPUT = 16000;
	
	public static final ProcessorRecipeHandler RECIPE_HANDLER = NCRecipes.turbine;
	protected RecipeInfo<ProcessorRecipe> recipeInfo;
	
	protected int updateCount = 0;
	
	public boolean isTurbineOn, computerActivated, isProcessing;
	public double power = 0D, rawConductivity = 0D;
	protected double rawPower = 0D;
	public EnumFacing flowDir = null;
	public int shaftWidth = 0, inertia = 0, bladeLength = 0, noBladeSets = 0, recipeRate = 0;
	public double totalExpansionLevel = 1D, idealTotalExpansionLevel = 1D, basePowerPerMB = 0D;
	public List<Double> expansionLevels = new ArrayList<Double>(), rawBladeEfficiencies = new ArrayList<Double>();
	
	public float angVel = 0F, rotorAngle = 0F;
	public long prevRenderTime = 0L;
	
	protected Iterable<MutableBlockPos>[] inputPlane = new Iterable[4];
	
	protected short dynamoCoilCheckCount = 0;
	
	public Turbine(World world) {
		super(world);
		
		controllers = new ObjectOpenHashSet<TileTurbineController>();
		rotorShafts = new ObjectOpenHashSet<TileTurbineRotorShaft>();
		rotorBlades = new ObjectOpenHashSet<ITurbineRotorBlade>();
		rotorBearings = new ObjectOpenHashSet<TileTurbineRotorBearing>();
		dynamoCoils = new ObjectOpenHashSet<TileTurbineDynamoCoil>();
		inlets = new ObjectOpenHashSet<TileTurbineInlet>();
		outlets = new ObjectOpenHashSet<TileTurbineOutlet>();
		
		energyStorage = new EnergyStorage(BASE_MAX_ENERGY);
		tanks = Lists.newArrayList(new Tank(BASE_MAX_INPUT, NCRecipes.turbine_valid_fluids.get(0)), new Tank(BASE_MAX_OUTPUT, null));
	}
	
	// Multiblock Part Getters
	
	public Set<TileTurbineController> getControllers() {
		return controllers;
	}
	
	public Set<TileTurbineRotorShaft> getRotorShafts() {
		return rotorShafts;
	}
	
	public Set<ITurbineRotorBlade> getRotorBlades() {
		return rotorBlades;
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
		if (newPart instanceof ITurbineRotorBlade) rotorBlades.add((ITurbineRotorBlade) newPart);
		if (newPart instanceof TileTurbineRotorBearing) rotorBearings.add((TileTurbineRotorBearing) newPart);
		if (newPart instanceof TileTurbineDynamoCoil) dynamoCoils.add((TileTurbineDynamoCoil) newPart);
		if (newPart instanceof TileTurbineInlet) inlets.add((TileTurbineInlet) newPart);
		if (newPart instanceof TileTurbineOutlet) outlets.add((TileTurbineOutlet) newPart);
	}
	
	@Override
	protected void onBlockRemoved(IMultiblockPart oldPart) {
		if (oldPart instanceof TileTurbineController) controllers.remove(oldPart);
		if (oldPart instanceof TileTurbineRotorShaft) rotorShafts.remove(oldPart);
		if (oldPart instanceof ITurbineRotorBlade) rotorBlades.remove(oldPart);
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
		energyStorage.setMaxTransfer(BASE_MAX_ENERGY*getNumConnectedBlocks());
		tanks.get(0).setCapacity(BASE_MAX_INPUT*getNumConnectedBlocks());
		tanks.get(1).setCapacity(BASE_MAX_OUTPUT*getNumConnectedBlocks());
		
		inputPlane[0] = getInteriorPlane(flowDir.getOpposite(), 0, 0, 0, bladeLength, shaftWidth + bladeLength);
		inputPlane[1] = getInteriorPlane(flowDir.getOpposite(), 0, shaftWidth + bladeLength, 0, 0, bladeLength);
		inputPlane[2] = getInteriorPlane(flowDir.getOpposite(), 0, bladeLength, shaftWidth + bladeLength, 0, 0);
		inputPlane[3] = getInteriorPlane(flowDir.getOpposite(), 0, 0, bladeLength, shaftWidth + bladeLength, 0);
		
		doDynamoCoilPlacementChecks();
		
		for (TileTurbineRotorShaft rotorShaft : rotorShafts) {
			BlockPos pos = rotorShaft.getPos();
			IBlockState state = WORLD.getBlockState(pos);
			if (state.getBlock() instanceof BlockTurbineRotorShaft) {
				WORLD.setBlockState(pos, state.withProperty(TurbineRotorBladeUtil.DIR, TurbinePartDir.INVISIBLE));
			}
		}
		
		for (ITurbineRotorBlade rotorBlade : rotorBlades) {
			BlockPos pos = rotorBlade.bladePos();
			IBlockState state = WORLD.getBlockState(pos);
			if (state.getBlock() instanceof IBlockRotorBlade) {
				WORLD.setBlockState(pos, state.withProperty(TurbineRotorBladeUtil.DIR, TurbinePartDir.INVISIBLE));
			}
		}
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
		if (flowDir != null) {
			TurbinePartDir shaftDir = getShaftDir();
			for (TileTurbineRotorShaft rotorShaft : rotorShafts) {
				BlockPos pos = rotorShaft.getPos();
				IBlockState state = WORLD.getBlockState(pos);
				if (state.getBlock() instanceof BlockTurbineRotorShaft) {
					WORLD.setBlockState(pos, state.withProperty(TurbineRotorBladeUtil.DIR, shaftDir));
				}
			}
			
			for (ITurbineRotorBlade rotorBlade : rotorBlades) {
				BlockPos pos = rotorBlade.bladePos();
				IBlockState state = WORLD.getBlockState(pos);
				if (state.getBlock() instanceof IBlockRotorBlade) {
					WORLD.setBlockState(pos, state.withProperty(TurbineRotorBladeUtil.DIR, rotorBlade.getDir()));
				}
			}
		}
		
		isTurbineOn = false;
		if (controller != null) controller.updateBlockState(false);
		power = rawPower = rawConductivity = 0D;
		angVel = rotorAngle = 0F;
		flowDir = null;
		shaftWidth = inertia = bladeLength = noBladeSets = recipeRate = 0;
		totalExpansionLevel = idealTotalExpansionLevel = 1D;
		basePowerPerMB = 0D;
		expansionLevels = new ArrayList<Double>();
		rawBladeEfficiencies = new ArrayList<Double>();
		inputPlane[0] = inputPlane[1] = inputPlane[2] = inputPlane[3] = null;
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
		Axis axis = null;
		boolean tooManyAxes = false; // Is any of the bearings in more than a single axis?
		boolean notInAWall = false; // Is the bearing somewhere else in the structure other than the wall?
		
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
			axis = Axis.X;
		}
		if (dirMinY && dirMaxY) {
			if (axis != null) tooManyAxes = true;
			else axis = Axis.Y;
		}
		if (dirMinZ && dirMaxZ) {
			if (axis != null) tooManyAxes = true;
			else axis = Axis.Z;
		}
		
		if (axis == null) {
			validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.need_bearings", null);
			return false;
		}
		
		if ((axis == Axis.X && getInteriorLengthY() != getInteriorLengthZ()) ||
			(axis == Axis.Y && getInteriorLengthZ() != getInteriorLengthX()) ||
			(axis == Axis.Z && getInteriorLengthX() != getInteriorLengthY()) ||
			tooManyAxes || notInAWall) {
			validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.bearings_side_square", null);
			return false;
		}
		
		// At this point, all bearings are guaranteed to be part of walls in the same axis
		// Also, it can only ever succeed up to this point if we already have at least two bearings, so no need to check for that
		
		int internalDiameter;
		if (axis == Axis.X) internalDiameter = getInteriorLengthY();
		else if (axis == Axis.Y) internalDiameter = getInteriorLengthZ();
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
		
		for (BlockPos pos : getInteriorPlane(EnumFacing.getFacingFromAxis(AxisDirection.NEGATIVE, axis), -1, bladeLength, bladeLength, bladeLength, bladeLength)) {
			if (WORLD.getTileEntity(pos) instanceof TileTurbineRotorBearing) continue;
			validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.bearings_centre_and_square", pos);
			return false;
		}
		
		for (BlockPos pos : getInteriorPlane(EnumFacing.getFacingFromAxis(AxisDirection.POSITIVE, axis), -1, bladeLength, bladeLength, bladeLength, bladeLength)) {
			if (WORLD.getTileEntity(pos) instanceof TileTurbineRotorBearing) continue;
			validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.bearings_centre_and_square", pos);
			return false;
		}
		
		// All bearings should be valid by now!
		
		// Inlets/outlets -> flowDir
		
		flowDir = null;
		
		if (inlets.size() == 0 || outlets.size() == 0) {
			validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.valve_wrong_wall", null);
			return false;
		}
		
		for (TileTurbineInlet inlet : inlets) {
			BlockPos pos = inlet.getPos();
			
			if (isInMinWall(axis, pos)) {
				EnumFacing thisFlowDir = EnumFacing.getFacingFromAxis(AxisDirection.POSITIVE, axis);
				if (flowDir != null && flowDir != thisFlowDir) { //make sure that all inlets are in the same wall
					validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.valve_wrong_wall", pos);
					return false;
				} else flowDir = thisFlowDir;
			} else if (isInMaxWall(axis, pos)) {
				EnumFacing thisFlowDir = EnumFacing.getFacingFromAxis(AxisDirection.NEGATIVE, axis);
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
			for (BlockPos pos : getInteriorPlane(EnumFacing.getFacingFromAxis(AxisDirection.POSITIVE, axis), slice, bladeLength, bladeLength, bladeLength, bladeLength)) {
				TileEntity tile = WORLD.getTileEntity(pos);
				if (tile instanceof TileTurbineRotorShaft) {
					((TileTurbineRotorShaft)tile).render = true;
					continue;
				}
				validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.shaft_centre", pos);
				return false;
			}
		}
		
		// Interior
		
		inertia = shaftWidth*(shaftWidth + 4*bladeLength)*flowLength;
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
				WORLD.setBlockState(pos, Blocks.AIR.getDefaultState());
			}
			
			for (BlockPos pos : getInteriorPlane(flowDir, depth, shaftWidth + bladeLength, 0, 0, shaftWidth + bladeLength)) {
				if (!MaterialHelper.isReplaceable(WORLD.getBlockState(pos).getMaterial())) {
					validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.space_between_blades", pos);
					return false;
				}
				WORLD.setBlockState(pos, Blocks.AIR.getDefaultState());
			}
			
			for (BlockPos pos : getInteriorPlane(flowDir, depth, 0, shaftWidth + bladeLength, shaftWidth + bladeLength, 0)) {
				if (!MaterialHelper.isReplaceable(WORLD.getBlockState(pos).getMaterial())) {
					validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.space_between_blades", pos);
					return false;
				}
				WORLD.setBlockState(pos, Blocks.AIR.getDefaultState());
			}
			
			for (BlockPos pos : getInteriorPlane(flowDir, depth, shaftWidth + bladeLength, shaftWidth + bladeLength, 0, 0)) {
				if (!MaterialHelper.isReplaceable(WORLD.getBlockState(pos).getMaterial())) {
					validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.space_between_blades", pos);
					return false;
				}
				WORLD.setBlockState(pos, Blocks.AIR.getDefaultState());
			}
			
			// Blades/stators
			
			IRotorBladeType currentBladeType = null;
			
			for (BlockPos pos : getInteriorPlane(flowDir.getOpposite(), depth, bladeLength, 0, bladeLength, shaftWidth + bladeLength)) {
				ITurbineRotorBlade thisBlade = getBlade(pos);
				IRotorBladeType thisBladeType = thisBlade == null ? null : thisBlade.getBladeType();
				if (thisBladeType == null) {
					validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.missing_blades", pos);
					return false;
				} else if (currentBladeType == null) {
					currentBladeType = thisBladeType;
				} else if (currentBladeType != thisBladeType) {
					validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.different_type_blades", pos);
					return false;
				}
				thisBlade.setDir(getBladeDir(PlaneDir.V));
				thisBlade.setDepth(depth);
				thisBlade.setRenderRotation(45F);
			}
			
			for (BlockPos pos : getInteriorPlane(flowDir.getOpposite(), depth, 0, bladeLength, shaftWidth + bladeLength, bladeLength)) {
				ITurbineRotorBlade thisBlade = getBlade(pos);
				IRotorBladeType thisBladeType = thisBlade == null ? null : thisBlade.getBladeType();
				if (thisBladeType == null) {
					validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.missing_blades", pos);
					return false;
				} else if (currentBladeType != thisBladeType) {
					validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.different_type_blades", pos);
					return false;
				}
				thisBlade.setDir(getBladeDir(PlaneDir.U));
				thisBlade.setDepth(depth);
				thisBlade.setRenderRotation(flowDir.getAxis() == Axis.Z ? -45F : 45F);
			}
			
			for (BlockPos pos : getInteriorPlane(flowDir.getOpposite(), depth, shaftWidth + bladeLength, bladeLength, 0, bladeLength)) {
				ITurbineRotorBlade thisBlade = getBlade(pos);
				IRotorBladeType thisBladeType = thisBlade == null ? null : thisBlade.getBladeType();
				if (thisBladeType == null) {
					validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.missing_blades", pos);
					return false;
				} else if (currentBladeType != thisBladeType) {
					validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.different_type_blades", pos);
					return false;
				}
				thisBlade.setDir(getBladeDir(PlaneDir.U));
				thisBlade.setDepth(depth);
				thisBlade.setRenderRotation(flowDir.getAxis() == Axis.Z ? 45F : -45F);
			}
			
			for (BlockPos pos : getInteriorPlane(flowDir.getOpposite(), depth, bladeLength, shaftWidth + bladeLength, bladeLength, 0)) {
				ITurbineRotorBlade thisBlade = getBlade(pos);
				IRotorBladeType thisBladeType = thisBlade == null ? null : thisBlade.getBladeType();
				if (thisBladeType == null) {
					validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.missing_blades", pos);
					return false;
				} else if (currentBladeType != thisBladeType) {
					validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.different_type_blades", pos);
					return false;
				}
				thisBlade.setDir(getBladeDir(PlaneDir.V));
				thisBlade.setDepth(depth);
				thisBlade.setRenderRotation(-45F);
			}
			
			if (currentBladeType == null) {
				validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.missing_blades", null);
				return false;
			}
			
			double prevExpansionLevel = totalExpansionLevel;
			if (currentBladeType == TurbineRotorStatorType.STATOR) {
				totalExpansionLevel *= NCConfig.turbine_stator_expansion;
				expansionLevels.add((prevExpansionLevel + totalExpansionLevel)/2D);
				rawBladeEfficiencies.add(0D);
			} else if (currentBladeType instanceof TurbineRotorBladeType) {
				totalExpansionLevel *= ((TurbineRotorBladeType)currentBladeType).getExpansionCoefficient();
				expansionLevels.add((prevExpansionLevel + totalExpansionLevel)/2D);
				rawBladeEfficiencies.add(((TurbineRotorBladeType)currentBladeType).getEfficiency());
				noBladeSets++;
			}
		}
		
		if (!NCUtil.areEqual(getFlowLength(), expansionLevels.size(), rawBladeEfficiencies.size())) {
			validatorCallback.setLastError(Global.MOD_ID + ".multiblock_validation.turbine.missing_blades", null);
			return false;
		}
		
		for (TileTurbineController controller : controllers) {
			controller.isRenderer = false;
		}
		for (TileTurbineController controller : controllers) {
			controller.isRenderer = true;
			break;
		}
		
		if (shaftWidth > 2) for (int slice = 0; slice < flowLength; slice++) {
			for (BlockPos pos : getInteriorPlane(flowDir, slice, bladeLength + 1, bladeLength + 1, bladeLength + 1, bladeLength + 1)) {
				TileEntity tile = WORLD.getTileEntity(pos);
				if (tile instanceof TileTurbineRotorShaft) {
					((TileTurbineRotorShaft)tile).render = false;
				}
			}
		}
		
		return true;
	}
	
	/* ========================================= Modified Kurtchekov stuff! ========================================= */
	
	protected ITurbineRotorBlade getBlade(BlockPos pos) {
		TileEntity tile = WORLD.getTileEntity(pos);
		return tile instanceof ITurbineRotorBlade ? (ITurbineRotorBlade) tile : null;
	}
	
	public TurbinePartDir getShaftDir() {
		switch (flowDir.getAxis()) {
		case Y:
			return TurbinePartDir.Y;
		case Z:
			return TurbinePartDir.Z;
		case X:
			return TurbinePartDir.X;
		default:
			return TurbinePartDir.Y;
		}
	}
	
	protected TurbinePartDir getBladeDir(PlaneDir planeDir) {
		switch (flowDir.getAxis()) {
		case Y:
			switch (planeDir) {
			case U:
				return TurbinePartDir.Z;
			case V:
				return TurbinePartDir.X;
			}
		case Z:
			switch (planeDir) {
			case U:
				return TurbinePartDir.X;
			case V:
				return TurbinePartDir.Y;
			}
		case X:
			switch (planeDir) {
			case U:
				return TurbinePartDir.Y;
			case V:
				return TurbinePartDir.Z;
			}
		default:
			return TurbinePartDir.Y;
		}
	}
	
	protected enum PlaneDir {
		U, V;
	}
	
	/* ====================================== End of modified Kurtchekov stuff! ===================================== */
	
	public int getFlowLength() {
		return getInteriorLength(flowDir);
	}
	
	protected int getBladeVolume() {
		return 4*shaftWidth*bladeLength*noBladeSets;
	}
	
	public double getRotorRadius() {
		return bladeLength + shaftWidth/2D;
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
		isTurbineOn = (isRedstonePowered()|| computerActivated) && isAssembled();
		if (isTurbineOn != oldIsTurbineOn) {
			if (controller != null) controller.updateBlockState(isTurbineOn);
			sendUpdateToAllPlayers();
		}
	}
	
	protected boolean isRedstonePowered() {
		if (controller != null && controller.checkIsRedstonePowered(WORLD, controller.getPos())) return true;
		return false;
	}
	
	protected void updateTurbine() {
		if (shouldUpdate()) {
			boolean wasProcessing = isProcessing;
			refreshRecipe();
			double previousRawPower = rawPower;
			double rawMaxPower = getRawMaxProcessPower();
			if (canProcessInputs()) {
				isProcessing = true;
				produceProducts();
				rawPower = getNewRawProcessPower(previousRawPower, rawMaxPower, true);
			}
			else {
				isProcessing = false;
				rawPower = getNewRawProcessPower(previousRawPower, rawMaxPower, false);
			}
			power = rawPower*getEffectiveConductivity();
			angVel = rawMaxPower == 0D ? 0F : (float) (0.5D*rawPower/rawMaxPower);
			if (wasProcessing != isProcessing) {
				sendUpdateToAllPlayers();
			}
		}
		energyStorage.changeEnergyStored((int)power);
		
		PacketHandler.instance.sendToAll(getRenderPacket());
	}
	
	protected void refreshRecipe() {
		recipeInfo = RECIPE_HANDLER.getRecipeInfoFromInputs(new ArrayList<ItemStack>(), tanks.subList(0, 1));
	}
	
	protected boolean canProcessInputs() {
		if (!isTurbineOn || !setRecipeStats()) return false;
		return canProduceProducts();
	}
	
	protected boolean setRecipeStats() {
		if (recipeInfo == null) {
			recipeRate = 0;
			basePowerPerMB = 0D;
			idealTotalExpansionLevel = 1D;
			return false;
		}
		basePowerPerMB = recipeInfo.getRecipe().getTurbinePowerPerMB();
		idealTotalExpansionLevel = recipeInfo.getRecipe().getTurbineExpansionLevel();
		return true;
	}
	
	protected boolean canProduceProducts() {
		IFluidIngredient fluidProduct = recipeInfo.getRecipe().fluidProducts().get(0);
		if (fluidProduct.getMaxStackSize(0) <= 0 || fluidProduct.getStack() == null) return false;
		recipeRate = Math.min(tanks.get(0).getFluidAmount(), getMaxRecipeRateMultiplier()*updateTime());
		if (!tanks.get(1).isEmpty()) {
			if (!tanks.get(1).getFluid().isFluidEqual(fluidProduct.getStack())) {
				return false;
			} else if (tanks.get(1).getFluidAmount() + fluidProduct.getMaxStackSize(0)*recipeRate > tanks.get(1).getCapacity()) {
				return false;
			}
		}
		return true;
	}
	
	public void produceProducts() {
		int fluidIngredientStackSize = recipeInfo.getRecipe().fluidIngredients().get(0).getMaxStackSize(recipeInfo.getFluidIngredientNumbers().get(0))*recipeRate;
		if (fluidIngredientStackSize > 0) tanks.get(0).changeFluidAmount(-fluidIngredientStackSize);
		if (tanks.get(0).getFluidAmount() <= 0) tanks.get(0).setFluidStored(null);
		
		IFluidIngredient fluidProduct = recipeInfo.getRecipe().fluidProducts().get(0);
		if (fluidProduct.getMaxStackSize(0) <= 0) return;
		if (tanks.get(1).isEmpty()) {
			tanks.get(1).setFluidStored(fluidProduct.getNextStack(0));
			tanks.get(1).setFluidAmount(tanks.get(1).getFluidAmount()*recipeRate);
		} else if (tanks.get(1).getFluid().isFluidEqual(fluidProduct.getStack())) {
			tanks.get(1).changeFluidAmount(fluidProduct.getNextStackSize(0)*recipeRate);
		}
	}
	
	public int getMaxRecipeRateMultiplier() {
		return getBladeVolume()*NCConfig.turbine_mb_per_blade;
	}
	
	protected double getNewRawProcessPower(double previousRawPower, double maxRawPower, boolean increasing) {
		if (increasing) {
			return (inertia*previousRawPower + maxRawPower)/(inertia + 1D);
		}
		else {
			return (inertia*previousRawPower)/(inertia + 2D);
		}
	}
	
	protected double getRawMaxProcessPower() {
		if (noBladeSets == 0) return 0D;
		
		double bladeMultiplier = 0D;
		
		for (int depth = 0; depth < getFlowLength(); depth++) {
			if (rawBladeEfficiencies.get(depth) <= 0D) continue;
			bladeMultiplier += rawBladeEfficiencies.get(depth)*getExpansionIdealityMultiplier(getIdealExpansionLevel(depth), expansionLevels.get(depth));
		}
		bladeMultiplier /= noBladeSets;
		
		return bladeMultiplier*getExpansionIdealityMultiplier(idealTotalExpansionLevel, totalExpansionLevel)*(recipeRate/(double)updateTime())*basePowerPerMB;
	}
	
	protected static double getExpansionIdealityMultiplier(double ideal, double actual) {
		if (ideal <= 0 || actual <= 0) return 0D;
		return ideal < actual ? ideal/actual : actual/ideal;
	}
	
	protected double getIdealExpansionLevel(int depth) {
		return Math.pow(idealTotalExpansionLevel, (depth + 0.5D)/getFlowLength());
	}
	
	public List<Double> getIdealExpansionLevels() {
		List<Double> levels = new ArrayList<Double>();
		if (flowDir == null) {
			return levels;
		}
		for (int depth = 0; depth < getFlowLength(); depth++) {
			levels.add(getIdealExpansionLevel(depth));
		}
		return levels;
	}
	
	public double getEffectiveConductivity() {
		if (rotorBearings.size() == 0 || dynamoCoils.size() == 0) return 0;
		return dynamoCoils.size() >= rotorBearings.size() ? rawConductivity : rawConductivity*dynamoCoils.size()/rotorBearings.size();
	}
	
	public int getActualInputRate() {
		return Math.round(recipeRate/(float)updateTime());
	}
	
	protected void incrementUpdateCount() {
		updateCount++; updateCount %= updateTime();
	}
	
	protected static int updateTime() {
		return NCConfig.machine_update_rate / 4;
	}
	
	protected boolean shouldUpdate() {
		return updateCount == 0;
	}
	
	// Client
	
	@Override
	protected void updateClient() {
		if (isProcessing && !Minecraft.getMinecraft().isGamePaused()) {
			double flowSpeed = getFlowLength()/23.2D; // Particles will just reach the outlets at this speed
			double offsetX = particleSpeedOffest(), offsetY = particleSpeedOffest(), offsetZ = particleSpeedOffest();
			
			double speedX = flowDir == EnumFacing.WEST ? -flowSpeed : (flowDir == EnumFacing.EAST ? flowSpeed : offsetX);
			double speedY = flowDir == EnumFacing.DOWN ? -flowSpeed : (flowDir == EnumFacing.UP ? flowSpeed : offsetY);
			double speedZ = flowDir == EnumFacing.NORTH ? -flowSpeed : (flowDir == EnumFacing.SOUTH ? flowSpeed : offsetZ);
			
			for (Iterable<MutableBlockPos> iter : inputPlane) {
				if (iter != null) {
					for (BlockPos pos : iter) {
						if (rand.nextDouble() < 0.075D*recipeRate/(getMaxRecipeRateMultiplier()*updateTime())) {
							double[] spawnPos = particleSpawnPos(pos);
							if (spawnPos != null) WORLD.spawnParticle(EnumParticleTypes.CLOUD, false, spawnPos[0], spawnPos[1], spawnPos[2], speedX, speedY, speedZ);
						}
					}
				}
			}
		}
	}
	
	protected double particleSpeedOffest() {
		return (rand.nextDouble() - 0.5D)/getFlowLength();
	}
	
	protected double[] particleSpawnPos(BlockPos pos) {
		double offsetU = 0.5D + (rand.nextDouble() - 0.5D)/2D;
		double offsetV = 0.5D + (rand.nextDouble() - 0.5D)/2D;
		switch (flowDir) {
		case DOWN:
			return new double[] {pos.getX() + offsetV, pos.getY() + 1D, pos.getZ() + offsetU};
		case UP:
			return new double[] {pos.getX() + offsetV, pos.getY(), pos.getZ() + offsetU};
		case NORTH:
			return new double[] {pos.getX() + offsetU, pos.getY() + offsetV, pos.getZ() + 1D};
		case SOUTH:
			return new double[] {pos.getX() + offsetU, pos.getY() + offsetV, pos.getZ()};
		case WEST:
			return new double[] {pos.getX() + 1D, pos.getY() + offsetU, pos.getZ() + offsetV};
		case EAST:
			return new double[] {pos.getX(), pos.getY() + offsetU, pos.getZ() + offsetV};
		default:
			return new double[] {pos.getX(), pos.getY(), pos.getZ()};
		}
	}
	
	// NBT
	
	@Override
	protected void syncDataTo(NBTTagCompound data, SyncReason syncReason) {
		energyStorage.writeToNBT(data);
		writeTanks(data);
		data.setBoolean("isTurbineOn", isTurbineOn);
		data.setBoolean("computerActivated", computerActivated);
		data.setDouble("power", power);
		data.setDouble("rawPower", rawPower);
		data.setDouble("rawConductivity", rawConductivity);
		data.setFloat("angVel", angVel);
		data.setInteger("flowDir", flowDir == null ? -1 : flowDir.getIndex());
		data.setInteger("shaftWidth", shaftWidth);
		data.setInteger("shaftVolume", inertia);
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
		data.setBoolean("isProcessing", isProcessing);
	}
	
	@Override
	protected void syncDataFrom(NBTTagCompound data, SyncReason syncReason) {
		energyStorage.readFromNBT(data);
		readTanks(data);
		isTurbineOn = data.getBoolean("isTurbineOn");
		computerActivated = data.getBoolean("computerActivated");
		power = data.getDouble("power");
		rawPower = data.getDouble("rawPower");
		rawConductivity = data.getDouble("rawConductivity");
		angVel = data.getFloat("angVel");
		flowDir = data.getInteger("flowDir") < 0 ? null : EnumFacing.VALUES[data.getInteger("flowDir")];
		shaftWidth = data.getInteger("shaftWidth");
		inertia = data.getInteger("shaftVolume");
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
		isProcessing = data.getBoolean("isProcessing");
	}
	
	protected NBTTagCompound writeTanks(NBTTagCompound nbt) {
		if (!tanks.isEmpty()) for (int i = 0; i < tanks.size(); i++) {
			nbt.setInteger("fluidAmount" + i, tanks.get(i).getFluidAmount());
			nbt.setString("fluidName" + i, tanks.get(i).getFluidName());
		}
		return nbt;
	}
	
	protected void readTanks(NBTTagCompound nbt) {
		if (!tanks.isEmpty()) for (int i = 0; i < tanks.size(); i++) {
			if (nbt.getString("fluidName" + i).equals("nullFluid") || nbt.getInteger("fluidAmount" + i) == 0) tanks.get(i).setFluidStored(null);
			else tanks.get(i).setFluidStored(FluidRegistry.getFluid(nbt.getString("fluidName" + i)), nbt.getInteger("fluidAmount" + i));
		}
	}
	
	// Packets
	
	@Override
	protected TurbineUpdatePacket getUpdatePacket() {
		return new TurbineUpdatePacket(controller.getPos(), isTurbineOn, power, rawPower, rawConductivity, totalExpansionLevel, idealTotalExpansionLevel, shaftWidth, bladeLength, noBladeSets, energyStorage.getMaxEnergyStored(), energyStorage.getEnergyStored());
	}
	
	@Override
	public void onPacket(TurbineUpdatePacket message) {
		isTurbineOn = message.isTurbineOn;
		power = message.power;
		rawPower = message.rawPower;
		rawConductivity = message.rawConductivity;
		totalExpansionLevel = message.totalExpansionLevel;
		idealTotalExpansionLevel = message.idealTotalExpansionLevel;
		shaftWidth = message.shaftWidth;
		bladeLength = message.bladeLength;
		noBladeSets = message.noBladeSets;
		energyStorage.setStorageCapacity(message.capacity);
		energyStorage.setMaxTransfer(message.capacity);
		energyStorage.setEnergyStored(message.energy);
	}
	
	protected TurbineRenderPacket getRenderPacket() {
		return new TurbineRenderPacket(controller.getPos(), angVel, isProcessing, recipeRate);
	}
	
	public void onRenderPacket(TurbineRenderPacket message) {
		angVel = message.angVel;
		isProcessing = message.isProcessing;
		recipeRate = message.recipeRate;
	}
	
	public Container getContainer(EntityPlayer player) {
		return new ContainerTurbineController(player, controller);
	}
	
	@Override
	public void clearAllFluids() {
		for (Tank tank : tanks) tank.setFluidStored(null);
	}
	
	// Multiblock Validators
	
	@Override
	protected boolean isBlockGoodForInterior(World world, int x, int y, int z, IMultiblockValidator validatorCallback) {
		BlockPos pos = new BlockPos(x, y, z);
		if (MaterialHelper.isReplaceable(world.getBlockState(pos).getMaterial()) || world.getTileEntity(pos) instanceof TileTurbinePartBase) return true;
		else return standardLastError(x, y, z, validatorCallback);
	}
}